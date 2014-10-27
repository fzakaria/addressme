package com.github.fzakaria.addressme.factories

import com.github.fzakaria.addressme.routers.LoginRouter

trait LoginRouterFactory {
  def loginRouter: LoginRouter
}

trait LoginRouterFactoryImpl extends LoginRouterFactory {
  override def loginRouter: LoginRouter = new LoginRouter() with OAuth2RouterFactoryImpl with UserPasswordProviderFactoryImpl
}