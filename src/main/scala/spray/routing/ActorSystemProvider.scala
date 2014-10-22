package spray.routing

import akka.actor.ActorSystem

trait ActorSystemProvider {
  implicit def actorSystem: ActorSystem
}

trait ActorSystemProviderImpl extends ActorSystemProvider {
  implicit def actorSystem = ActorSystem("on-spray-can")
}