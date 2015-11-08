package actor

import java.io.File

import akka.actor.{Actor, Props}
import fr.louidji.tools.Organize
import play.api.Logger
import play.api.libs.concurrent.Akka
import play.api.Play.current



object FileProcessActor {
  val baseDestDir = new File("/home/louis/photo/imported/")

  def props = Props[FileProcessActor]

  

}

class FileProcessActor extends Actor  {
  override def receive = {
    case w: Work =>
      sender() ! Started(w.uuid, w.file)
      Logger.info(s"Image Processing [${w.uuid}] : ${w.file} => ${FileProcessActor.baseDestDir.getPath}")

      val result = Organize.organize(w.file, FileProcessActor.baseDestDir, Organize.BASE_DIR_PATTERN_FORMAT, Organize.PHOTO_NAME_LONG_FORMAT)


      sender() ! Terminated(w.uuid, result, w.file)

    case _ =>
      Logger.info("?? FileProcessActor ??")

  }
}


