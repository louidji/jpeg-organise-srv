package controllers

import java.io.File
import javax.inject._

import actor._
import akka.actor._
import play.api.Logger
import play.api.mvc._
import java.io.File

@Singleton
class Application @Inject() (system: ActorSystem)  extends Controller {


  val fileProcessActor = system.actorOf(FileProcessActor.props)
  val fileManageActor = system.actorOf(FileManageActor.props)

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

}
