package com.github.fzakaria.addressme.services

import spray.routing.Routable
import com.github.fzakaria.addressme.factories.LoginServiceFactory
import spray.routing._
import Directives._

trait ApiService extends Routable {
  me: LoginServiceFactory =>

  override def route: Route = {
    pathPrefix("api") {
      loginService.route
    }
  }

}