package actor

import java.io.File

import akka.actor.{Actor, Props}
import fr.louidji.tools.Organize
import play.api.Logger

case class Terminated(uuid: String, done: Boolean, file: File)
case class Work(uuid: String, file: File)

object FileProcessActor {
  val baseDestDir = new File("/home/louis/photo/imported/")

  def props = Props[FileProcessActor]

}

class FileProcessActor extends Actor  {
  override def receive = {
    case w: Work =>
      Logger.info(s"Image Processing [${w.uuid}] : ${w.file} => ${FileProcessActor.baseDestDir.getPath}")

      val result = Organize.organize(w.file, FileProcessActor.baseDestDir, Organize.BASE_DIR_PATTERN_FORMAT, Organize.PHOTO_NAME_LONG_FORMAT)


      sender() ! Terminated(w.uuid, result, w.file)

    case _ =>
      Logger.info("?? FileProcessActor ??")

  }
}


