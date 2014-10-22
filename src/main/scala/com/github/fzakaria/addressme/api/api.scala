package com.github.fzakaria.addressme.api

import spray.routing._
import Directives._
import akka.actor.ActorSystem

trait ApiFactory {
  def api: Api
}

trait ApiFactoryImpl extends ApiFactory {
  override def api: Api = new Api with ApiServiceFactoryImpl
}

trait Api extends Routable {
  me: ApiServiceFactory =>
  override def route: Route = {
    apiService.route
  }
}

trait ApiServiceFactory {
  def apiService: ApiService

}

trait ApiServiceFactoryImpl extends ApiServiceFactory {
  override def apiService: ApiService = new ApiService with LoginServiceFactoryImpl
}

trait ApiService extends Routable {
  me: LoginServiceFactory =>

  override def route: Route = {
    pathPrefix("api") {
      loginService.route
    }
  }

}

class ApiActor extends HttpServiceActor with ApiFactoryImpl {
  def receive = runRoute(api.route)
}