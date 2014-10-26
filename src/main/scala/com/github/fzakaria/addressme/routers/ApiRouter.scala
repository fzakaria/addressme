package com.github.fzakaria.addressme.routers

import spray.routing.Routable
import com.github.fzakaria.addressme.factories.LoginRouterFactory
import spray.routing._
import Directives._
import spray.httpx.PlayTwirlSupport._

trait ApiRouter extends Routable {
  me: LoginRouterFactory =>

  override def route(rs: RequestSession): Route = {
    pathPrefix("api") {
      (pathEndOrSingleSlash | path("home" ~ Slash.?)) {
        complete(html.home.render(rs))
      } ~
        loginRouter.route(rs)
    }
  }

}