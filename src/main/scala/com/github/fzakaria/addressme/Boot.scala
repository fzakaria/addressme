package com.github.fzakaria.addressme

import akka.actor.{ ActorSystem, Props }
import akka.io.IO
import spray.can.Http
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import com.github.fzakaria.addressme.actors.ApiActorImpl
import spray.routing.ActorSystemProviderImpl

object Boot extends App with ActorSystemProviderImpl {

  // create and start our service actor
  val service = actorSystem.actorOf(Props[ApiActorImpl], "addressme-service")

  implicit val timeout = Timeout(5.seconds)
  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ? Http.Bind(service, interface = "localhost", port = 8080)
}
