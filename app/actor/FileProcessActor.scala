package actor

import java.io.File
import akka.actor.{ Actor, Props }
import fr.louidji.tools.Organize
import play.api.libs.concurrent.Akka
import play.api.Play.current
import javax.inject._
import play.api.{Configuration, Logger}

object FileProcessActor {

  def props = Props[FileProcessActor]

}

class FileProcessActor @Inject() (configuration: Configuration) extends Actor {
  val imagesDestDir = configuration.getString("images.directory.dest") match {
    case Some(s: String) => new File(s)
    case _               => throw new Exception("Configuration error, please set images.directory.dest")
  }

  override def receive = {
    case w: Work =>
      sender() ! StartedFileProcessing(w.uuid, w.file)
      Logger.debug(s"Image Processing [${w.uuid}] : ${w.file} => ${imagesDestDir.getPath}")
      val result = Organize.organize(w.file, imagesDestDir, Organize.BASE_DIR_PATTERN_FORMAT, Organize.PHOTO_NAME_LONG_FORMAT)
      sender() ! TerminatFileProcessing(w.uuid, w.clientId, result, w.file)

    case _ =>
      Logger.info("?? FileProcessActor ??")

  }
}

