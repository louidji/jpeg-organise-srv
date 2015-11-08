package actor
import akka.actor._
import play.api.libs.json._

object WebSocketActor {
  def props(out: ActorRef, fileManageActor: ActorRef) = Props(new WebSocketActor(out, fileManageActor))
}

class WebSocketActor(out: ActorRef, fileManageActor: ActorRef) extends Actor {
  implicit val terminatFileProcessingWrites = new Writes[TerminatFileProcessing] {
    def writes(terminatFileProcessing: TerminatFileProcessing) = Json.obj(
      "uuid" -> terminatFileProcessing.uuid,
      "done" -> terminatFileProcessing.done)
  }

  def receive = {
    case msg: String =>
      out ! Json.obj("received" -> msg)
    case t: TerminatFileProcessing =>
      out ! Json.toJson(t)
  }

  override def postStop() = fileManageActor ! DisconnectWebSocketActorRef(self)

  override def preStart() = fileManageActor ! ConnectWebSocketActorRef(self)

}