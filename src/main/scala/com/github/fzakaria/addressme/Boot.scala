package com.github.fzakaria.addressme

import akka.actor.{ ActorSystem, Props }
import akka.io.IO
import spray.can.Http
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import com.github.fzakaria.addressme.actors.ApiActorImpl
import spray.routing.ActorSystemProviderImpl
import com.github.fzakaria.addressme.actors.DatabaseCreatorActorImpl
import com.github.fzakaria.addressme.factories.ConfigServiceFactoryImpl
import net.ceedubs.ficus.Ficus._
import com.typesafe.config.PimpedConfig._
import com.github.fzakaria.addressme.actors.CreateDatabaseMessage

object ApplicationMode extends Enumeration {
  type ApplicationMode = Value
  val Debug = Value("dev")
  val Test = Value("test")
  val Prod = Value("prod")
}
import ApplicationMode._

object Boot extends App with ActorSystemProviderImpl with ConfigServiceFactoryImpl {

  // create and start our service actor
  val service = actorSystem.actorOf(Props[ApiActorImpl], "addressme-service")

  val databaseCreator = actorSystem.actorOf(Props[DatabaseCreatorActorImpl], "addressme-service")

  //lets create the database if we aren't in production (otherwise we shouldnt!)
  if (config.mode == Debug) {
    databaseCreator ! CreateDatabaseMessage
  }

  implicit val timeout = Timeout(5.seconds)
  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ? Http.Bind(service, interface = "localhost", port = 8080)
}
