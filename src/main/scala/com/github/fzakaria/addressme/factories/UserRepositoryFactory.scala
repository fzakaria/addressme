package com.github.fzakaria.addressme.factories

import com.github.fzakaria.addressme.repositories.UserRepository
import com.github.fzakaria.addressme.repositories.SlickUserRepository
import slick.driver.DriverComponentFromConfig

trait UserRepositoryFactory {
  val userRepo: UserRepository
}

trait UserRepositoryFactoryImpl extends UserRepositoryFactory {

  val userRepo: UserRepository = new SlickUserRepository() with DatabaseServiceFactoryImpl with DriverComponentFromConfig
}