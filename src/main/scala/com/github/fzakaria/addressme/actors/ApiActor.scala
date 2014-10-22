package com.github.fzakaria.addressme.actors

import spray.routing._
import Directives._
import akka.actor.ActorSystem
import com.github.fzakaria.addressme.factories.ApiServiceFactory
import com.github.fzakaria.addressme.factories.ApiServiceFactoryImpl

trait ApiActor extends HttpServiceActor {
  me: ApiServiceFactory =>
  def receive = runRoute(apiService.route)
}

class ApiActorImpl extends HttpServiceActor with ApiServiceFactoryImpl with ApiActor {

}