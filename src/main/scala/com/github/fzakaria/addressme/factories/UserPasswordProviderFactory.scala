package com.github.fzakaria.addressme.factories

import com.github.fzakaria.addressme.authentication.{ UserPasswordProvider, UserPasswordProviderImpl }

trait UserPasswordProviderFactory {

  val userPasswordProvider: UserPasswordProvider

}

trait UserPasswordProviderFactoryImpl extends UserPasswordProviderFactory {

  val userPasswordProvider: UserPasswordProvider = new UserPasswordProviderImpl() with UserRepositoryFactoryImpl

}