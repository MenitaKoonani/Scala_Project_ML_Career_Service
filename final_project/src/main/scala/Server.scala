import java.io.{File, FileOutputStream}
import java.nio.file.Paths
import java.util.UUID

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse, Multipart, StatusCodes}
import akka.http.scaladsl.server.Directives.{entity, _}
import akka.http.scaladsl.server.directives.FileInfo
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.FileIO
import akka.util.ByteString
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.codehaus.jettison.json.JSONObject
import sparkModel.{JobMatch, NaiveBayesClass, WordFilter}

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
  implicit val system: ActorSystem = ActorSystem("helloworld")
  implicit val executor: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  def tempDestination(fileInfo: FileInfo): File =
    File.createTempFile(fileInfo.fileName, ".tmp")
  def route = path("") {
    get {
      complete(jsonString)
    }
  } ~
    pathPrefix("resume_txt") {
      post {
        entity(as[String]) { resume_text =>
          complete(resume_text)
        }
      }
    } ~
    pathPrefix("resume_txt_file") {
      post {
        fileUpload("fileUpload") {
          case (fileInfo, fileStream) =>
            val sink = FileIO.toPath(Paths.get("/tmp") resolve fileInfo.fileName)
            val writeResult = fileStream.runWith(sink)
            onSuccess(writeResult) { result =>
              result.status match {
                case Success(_) => complete(s"Successfully written ${result.count} bytes")
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
                  val jsonString =
                    s"""{
                      |"Predicted Role": "$predicted_role",
                      | "Available Jobs" : [$array]
                      |}""".stripMargin

                  HttpResponse(StatusCodes.OK, entity = jsonString)
                }.recover {
                  case ex: Exception => HttpResponse(StatusCodes.InternalServerError, entity = "Error in file uploading:only PDF allowed")
                }
              }
          }
    }
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

  Http().bindAndHandle(route, host, port)

}
