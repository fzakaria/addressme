package com.github.fzakaria.addressme.factories

import com.github.fzakaria.addressme.routers.OAuth2Router

trait OAuth2RouterFactory {

  def oauth2Router: OAuth2Router
}

trait OAuth2RouterFactoryImpl extends OAuth2RouterFactory {

  override def oauth2Router: OAuth2Router = new OAuth2Router() with OAuth2ProviderFactoryImpl
}