import java.io.File
import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.http.scaladsl.Http

import scala.util.{Failure, Success}
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.FileInfo
import akka.stream.scaladsl.FileIO

import scala.concurrent.ExecutionContext

object Server extends App {
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
    }

  Http().bindAndHandle(route, host, port)
}
