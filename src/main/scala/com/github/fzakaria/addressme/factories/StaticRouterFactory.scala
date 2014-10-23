package com.github.fzakaria.addressme.factories

import com.github.fzakaria.addressme.routers.StaticRouter
import spray.routing.ActorSystemProviderImpl

trait StaticRouterFactory {

  def staticRouter: StaticRouter

}

trait StaticRouterFactoryImpl extends StaticRouterFactory {

  override def staticRouter: StaticRouter = new StaticRouter() with ActorSystemProviderImpl

}