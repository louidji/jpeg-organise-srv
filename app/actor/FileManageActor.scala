package actor

import play.api.Logger
import play.api.libs.concurrent.Akka
import play.api.Play.current
import akka.actor._
import play.api.cache._
import javax.inject._
import akka.actor._
import play.api.cache._
import scala.concurrent.duration._

object FileManageActor {

  def props = Props[FileManageActor]
  
}

class FileManageActor @Inject() (cache: CacheApi)  extends Actor   {
  override def receive = {
    case t: Terminated =>      
      Logger.info(s"Image Finish Processing [${t.uuid}] : ${t.done}")
      cache.set(t.uuid, s"Terminated : ${t.done}", 5 minutes )
      if(t.file.exists()) t.file.delete()
      
    case s: Started =>
      Logger.info(s"Start processing ${s.uuid} : ${s.file}")
      cache.set(s.uuid, "Started", 5 minutes )

    case _ =>
      Logger.info("?? FileManageActor ??")

  }
}




