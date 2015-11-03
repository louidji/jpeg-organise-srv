package actor

import akka.actor.{Actor, Props}
import play.api.Logger


object FileManageActor {

  def props = Props[FileManageActor]
}

class FileManageActor extends Actor   {
  override def receive = {
    case t: Terminated =>
      Logger.info(s"Image Finish Processing [${t.uuid}] : ${t.done}")
      if(t.file.exists()) t.file.delete()

    case _ =>
      Logger.info("?? FileManageActor ??")

  }
}


