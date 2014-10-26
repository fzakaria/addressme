package com.github.fzakaria.addressme.routers

import spray.routing.Routable
import com.github.fzakaria.addressme.factories.LoginRouterFactory
import spray.routing._
import Directives._

trait ApiRouter extends Routable {
  me: LoginRouterFactory =>

  override def route(rs: RequestSession): Route = {
    pathPrefix("api") {
      loginRouter.route(rs)
    }
  }

}