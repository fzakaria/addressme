package com.github.fzakaria.addressme.services

import spray.routing._
import Directives._
import spray.http._
import StatusCodes._
import spray.httpx.PlayTwirlSupport._
import com.github.fzakaria.addressme.factories.OAuth2ServiceFactory

trait LoginService extends Routable {
  me: OAuth2ServiceFactory =>

  override def route: Route = {
    pathPrefix("login") {
      (pathEnd & get) {
        complete { html.login.render }
      } ~
        oauth2Service.route
    }

  }
}
