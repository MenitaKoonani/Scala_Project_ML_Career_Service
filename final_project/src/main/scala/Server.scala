import java.io.{File, FileOutputStream}
import java.nio.file.Paths
import java.util.UUID
import java.util.logging.{Level, Logger}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives.{entity, _}
import akka.http.scaladsl.server.directives.FileInfo
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.FileIO
import akka.util.ByteString
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import sparkModel.{JobMatch, NaiveBayesClass, WordFilter}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object Server extends App {

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  val conf = new SparkConf()
  conf.setMaster("local")
  conf.setAppName("NaiveBayes Text classifier")

  val spark = SparkSession
    .builder()
    .appName("NaiveBayes Text classifier")
    .config(conf)
    .getOrCreate()
  val host = "0.0.0.0"
  val port = 9000
  val jsonString = """
  {
  "message": "Hello World"
  }
  """
  val errorMessageString = """
  {
  "errorMessage": "Error in file uploading:only PDF allowed"
  }
  """

  implicit val system: ActorSystem = ActorSystem("helloworld")
  implicit val executor: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  def tempDestination(fileInfo: FileInfo): File =
    File.createTempFile(fileInfo.fileName, ".tmp")
  // prints the helloworld message as http response
  def route = path("") {
    get {
      val response = HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, jsonString))
      complete(response)
    }
  } ~
    pathPrefix("resume_txt") {
      post {
        entity(as[String]) { resume_text =>
          var model = new NaiveBayesClass()
          var wordfilter = new WordFilter()

            var resume_cleaned = wordfilter.stringOperations(resume_text,spark)
            var predicted_role = model.predict(resume_cleaned,"src/main/scala/classifier/spark-model",spark)
            println(predicted_role)
            val result_match = new JobMatch()
            val array = result_match.getJobMatches(predicted_role,spark)
            println(array)
            val resultJsonString =
              s"""{
                 |"Predicted Role": "$predicted_role",
                 | "Available Jobs" : [$array]
                 |}""".stripMargin
          val body = s"""{"body": "${resultJsonString}"}"""
          val response = HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, body))
          complete(response)
        }
      }
    } ~
    pathPrefix("resume_txt_file") {
      post {
        fileUpload("fileUpload") {
          case (fileInfo, fileStream) =>
            var model = new NaiveBayesClass()
            var wordfilter = new WordFilter()
            val sink = FileIO.toPath(Paths.get("/tmp") resolve fileInfo.fileName)
            //// materialize the flow, getting the Sinks materialized value
            val writeResult = fileStream.runWith(sink)

            onSuccess(writeResult) { result =>
              var resume_cleaned = wordfilter.stringOperations(result.toString(),spark)
              var predicted_role = model.predict(resume_cleaned,"src/main/scala/classifier/spark-model",spark)
              println(predicted_role)
              val result_match = new JobMatch()
              val array = result_match.getJobMatches(predicted_role,spark)
              println(array)
              val resultJsonString =
                s"""{
                   |"Predicted Role": "$predicted_role",
                   | "Available Jobs" : [$array]
                   |}""".stripMargin
              result.status match {
                case Success(_) => complete(resultJsonString)
                case Failure(e) => throw e
              }
            }
        }
      }
    } ~
        pathPrefix("pdf_file") {
            (post & entity(as[Multipart.FormData])) { fileData =>
              complete {
                val fileName = UUID.randomUUID().toString+".pdf"
                val temp = System.getProperty("java.io.tmpdir")
                val filePath = temp + "/" + fileName
                println(fileName)

                processFile(filePath,fileData).map { fileSize =>
                  // takes the pdf document and strip out all of the text
                  val pdf = PDDocument.load(new File(filePath))
                  val stripper = new PDFTextStripper
                  stripper.setStartPage(1)
                  var resumeText = stripper.getText(pdf)
                  var model = new NaiveBayesClass()
                  var wordfilter = new WordFilter()
                  var resume_cleaned = wordfilter.stringOperations(resumeText,spark)
                  var predicted_role = model.predict(resume_cleaned,"src/main/scala/classifier/spark-model",spark)
                  println(predicted_role)
                  val result_match = new JobMatch()
                  val array = result_match.getJobMatches(predicted_role,spark)
                  println(array)
                  val resultJsonString =
                    s"""{
                      |"Predicted Role": "$predicted_role",
                      | "Available Jobs" : [$array]
                      |}""".stripMargin

                  HttpResponse(StatusCodes.OK, entity = HttpEntity(ContentTypes.`application/json`, resultJsonString))
                }.recover {
                  case ex: Exception => HttpResponse(StatusCodes.InternalServerError, entity = HttpEntity(ContentTypes.`application/json`, errorMessageString))
                }
              }
          }
    }
  //Method to store the uploaded PDF file in the Temp folder
  private def processFile(filePath: String, fileData: Multipart.FormData) = {
    val fileOutput = new FileOutputStream(filePath)
    fileData.parts.mapAsync(1) { bodyPart ⇒
      def writeFileOnLocal(array: Array[Byte], byteString: ByteString): Array[Byte] = {
        val byteArray: Array[Byte] = byteString.toArray
        fileOutput.write(byteArray)
        array ++ byteArray
      }
      bodyPart.entity.dataBytes.runFold(Array[Byte]())(writeFileOnLocal)
    }.runFold(0)(_ + _.length)
  }

  val bindingFuture = Http().bindAndHandle(route, host, port)

  bindingFuture.onComplete {
    case Success(serverBinding) => println(s"Listening to ${serverBinding.localAddress}")
    case Failure(error) => println(s"error: ${error.getMessage}")
  }

}
