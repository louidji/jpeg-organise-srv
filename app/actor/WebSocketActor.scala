package actor
import akka.actor._
import play.Logger
import play.api.libs.json._
import play.api.libs.functional.syntax._

object WebSocketActor {
  def props(out: ActorRef, fileManageActor: ActorRef) = Props(new WebSocketActor(out, fileManageActor))
}

class WebSocketActor(out: ActorRef, fileManageActor: ActorRef) extends Actor {
//  implicit val terminatFileProcessingWrites = new Writes[TerminatFileProcessing] {
//    def writes(terminatFileProcessing: TerminatFileProcessing) = Json.obj(
//      "uuid" -> terminatFileProcessing.uuid,   
//      "done" -> terminatFileProcessing.done)
//  }
  
 

  def receive = {
    case msg: String =>
      Logger.debug(msg)
      out ! Json.obj("received" -> msg)
      val json = Json.parse(msg)
      // client identification
      val maybeId = (json \ "id").asOpt[String]
      if (maybeId.isDefined) fileManageActor ! IdClient(maybeId.get, self)
      
    case t: TerminatFileProcessing =>
      //out ! Json.toJson(t)
      out ! JsObject(Seq(
        "uuid" -> JsString(t.uuid),
        "done" -> JsBoolean(t.done)
        ))
    case _ => Logger.error("?? WebSocketActor ??")        
  }

  override def postStop() = fileManageActor ! DisconnectWebSocketActorRef(self)

  override def preStart() = fileManageActor ! ConnectWebSocketActorRef(self)
  
  

}