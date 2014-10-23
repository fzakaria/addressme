package com.github.fzakaria.addressme.factories

import com.github.fzakaria.addressme.routers.ApiRouter

trait ApiRouterFactory {
  def apiRouter: ApiRouter

}

trait ApiRouterFactoryImpl extends ApiRouterFactory {
  override def apiRouter: ApiRouter = new ApiRouter with LoginRouterFactoryImpl
}