package com.github.fzakaria.addressme.actors

import spray.routing._
import Directives._
import akka.actor.ActorSystem
import com.github.fzakaria.addressme.factories.ApiRouterFactory
import com.github.fzakaria.addressme.factories.ApiRouterFactoryImpl

trait ApiActor extends HttpServiceActor {
  me: ApiRouterFactory =>
  def receive = runRoute(apiRouter.route)
}

class ApiActorImpl extends HttpServiceActor with ApiRouterFactoryImpl with ApiActor {

}