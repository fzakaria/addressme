package com.github.fzakaria.addressme.factories

import com.github.fzakaria.addressme.services.OAuth2Service

trait OAuth2ServiceFactory {

  def oauth2Service: OAuth2Service
}

trait OAuth2ServiceFactoryImpl extends OAuth2ServiceFactory {

  override def oauth2Service: OAuth2Service = new OAuth2Service() with OAuth2ProviderFactoryImpl
}