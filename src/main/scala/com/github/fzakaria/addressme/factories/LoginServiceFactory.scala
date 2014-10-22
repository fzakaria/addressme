package com.github.fzakaria.addressme.factories

import com.github.fzakaria.addressme.services.LoginService

trait LoginServiceFactory {
  def loginService: LoginService
}

trait LoginServiceFactoryImpl extends LoginServiceFactory {
  def loginService: LoginService = new LoginService() with OAuth2ServiceFactoryImpl
}