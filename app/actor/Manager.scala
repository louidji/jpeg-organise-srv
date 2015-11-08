package actor

import java.io.File
import javax.inject._
import akka.actor._


case class Started(uuid: String,  file: File)
case class Terminated(uuid: String, done: Boolean, file: File)
case class Work(uuid: String, file: File)

@Singleton
class Manager @Inject() (system: ActorSystem, @Named("filemanage-actor")  aFileManageActor : ActorRef) {
  val fileManageActor = aFileManageActor
  val fileProcessActor = system.actorOf(FileProcessActor.props)
}