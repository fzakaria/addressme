package com.github.fzakaria.addressme.actors

import spray.routing._
import Directives._
import akka.actor.ActorSystem
import com.github.fzakaria.addressme.factories.ApiRouterFactory
import com.github.fzakaria.addressme.factories.ApiRouterFactoryImpl
import com.github.fzakaria.addressme.factories.StaticRouterFactory
import com.github.fzakaria.addressme.factories.StaticRouterFactoryImpl

trait ApiActor extends HttpServiceActor {
  me: ApiRouterFactory with StaticRouterFactory =>
  def receive = runRoute(staticRouter.route ~ apiRouter.route)
}

class ApiActorImpl extends HttpServiceActor with StaticRouterFactoryImpl with ApiRouterFactoryImpl with ApiActor {

}