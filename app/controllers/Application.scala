package controllers

import java.io.File
import javax.inject._
import actor._
import akka.actor._
import play.api.Logger
import play.api.mvc._
import java.io.File
import javax.inject._
import play.api.cache.CacheApi
import play.api.libs.json._

class Application @Inject() (managerActor: Manager, cache :CacheApi) extends Controller {

  case class FileStatus(uuid:String, status:String)
  implicit val fileStatusWrites = new Writes[FileStatus] {
    def writes(fileStatus: FileStatus) = Json.obj(
      "uuid" -> fileStatus.uuid,
      "uuid" -> fileStatus.status
    )
  }


  val fileProcessActor = managerActor.fileProcessActor
  val fileManageActor = managerActor.fileManageActor

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def upload = Action(parse.multipartFormData) { request =>
    request.body.file("data").map { picture =>
      val filename = picture.filename

      val uuid = java.util.UUID.randomUUID.toString

      val file = new File(s"/tmp/upload/$uuid-$filename")

      picture.ref.moveTo(file)

      Logger.info(s"UUID : $uuid -- File : ${file.getPath}")
      fileProcessActor.tell(Work(uuid, file), fileManageActor)

      Ok(s"File uploaded, id $uuid")
    }.getOrElse {
      Redirect(routes.Application.index).flashing(
        "error" -> "Missing file")
    }
  }

  def jsonUpload = Action(parse.temporaryFile) { request =>
    val uuid = java.util.UUID.randomUUID.toString

    val file = new File(s"/tmp/upload/$uuid-jsonfile.tmp")
    request.body.moveTo(file)
    Logger.info(s"UUID : $uuid -- File : ${file.getPath}")
    fileProcessActor.tell(Work(uuid, file), fileManageActor)

    Ok(s"File uploaded, id $uuid")
  }
  
  def info(uuid:String) = Action {
    val cachedValue = cache.getOrElse(uuid)("Not Found")
    
    Ok(Json.toJson(FileStatus(uuid, cachedValue)))
  }

  // TODO websocket pour avoir le flux de retour
  //https://www.playframework.com/documentation/2.4.x/ScalaWebSockets
}


