package com.github.fzakaria.addressme.routers

import spray.routing._
import Directives._
import spray.http._
import StatusCodes._
import spray.routing.directives.CachingDirectives._

trait StaticRouter extends Routable {
  me: ActorSystemProvider =>

  override def route(rs: RequestSession): Route = {
    (pathPrefix("static") & alwaysCache(routeCache())) {
      path(RestPath) { path =>
        getFromResource(s"public/$path")
      }
    }
  }

}