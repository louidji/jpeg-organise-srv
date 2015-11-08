package actor

import play.api.Logger
import play.api.libs.concurrent.Akka
import play.api.Play.current
import akka.actor._
import play.api.cache._
import javax.inject._
import play.api.cache._
import scala.concurrent.duration._
import scala.collection.mutable.MutableList
import scala.collection.mutable.ListBuffer

object FileManageActor {

  def props = Props[FileManageActor]

}

class FileManageActor @Inject() (cache: CacheApi) extends Actor {
  val listWebSocket = ListBuffer.empty [ActorRef]
  override def receive = {
    case t: TerminatFileProcessing =>
      Logger.debug(s"Image Finish Processing [${t.uuid}] : ${t.done}")
      cache.set(t.uuid, s"Terminated, integrated : ${t.done}", 5 minutes)
      if (t.file.exists()) t.file.delete()
      listWebSocket.foreach { ref => ref ! t }
      
    // TODO broadcast

    case s: StartedFileProcessing =>
      Logger.debug(s"Start processing ${s.uuid} : ${s.file}")
      cache.set(s.uuid, "Started", 5 minutes)

    case c: ConnectWebSocketActorRef =>
      Logger.debug(s"Connect ${c.actorRef}")
      listWebSocket.+=(c.actorRef)

    case d: DisconnectWebSocketActorRef =>
      Logger.debug(s"Disconnect ${d.actorRef}")
      listWebSocket.-=(d.actorRef)

    case _ => 
      Logger.info("?? FileManageActor ??")

  }
}




