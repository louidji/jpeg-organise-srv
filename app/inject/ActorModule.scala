package inject

import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport
import actor._

class ActorModule extends AbstractModule with AkkaGuiceSupport {
  def configure = {
    bindActor[FileManageActor]("filemanage-actor")
  }
}