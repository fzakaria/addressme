package com.github.fzakaria.addressme.routers

import spray.routing._
import Directives._
import spray.http._
import StatusCodes._
import spray.httpx.PlayTwirlSupport._
import com.github.fzakaria.addressme.factories.OAuth2RouterFactory

trait LoginRouter extends Routable {
  me: OAuth2RouterFactory =>

  override def route: Route = {
    pathPrefix("login") {
      (pathEnd & get) {
        complete { html.login.render }
      } ~
        oauth2Router.route
    }

  }
}
