package com.github.fzakaria.addressme.actors

import spray.routing._
import Directives._
import akka.actor.ActorSystem
import com.github.fzakaria.addressme.factories.ApiRouterFactory
import com.github.fzakaria.addressme.factories.ApiRouterFactoryImpl
import com.github.fzakaria.addressme.factories.StaticRouterFactory
import com.github.fzakaria.addressme.factories.StaticRouterFactoryImpl
import spray.routing.directives.DebuggingDirectives._
import akka.event.Logging
import akka.actor.ActorLogging

trait ApiActor extends HttpServiceActor with ActorLogging {
  me: ApiRouterFactory with StaticRouterFactory =>
  def receive = runRoute(
    staticRouter.route
      ~
      //log the request/response when Akkas log level is debug or lower
      logRequestResponse("[API]") {
        apiRouter.route
      }
  )
}

class ApiActorImpl extends HttpServiceActor with StaticRouterFactoryImpl with ApiRouterFactoryImpl with ApiActor {

}