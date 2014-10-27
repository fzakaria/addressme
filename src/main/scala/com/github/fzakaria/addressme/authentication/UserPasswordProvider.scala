package com.github.fzakaria.addressme.authentication

import com.github.fzakaria.addressme.models.User
import scala.concurrent.Future
import com.github.fzakaria.addressme.factories.UserRepositoryFactory
import com.roundeights.hasher.Implicits._

trait UserPasswordProvider {

  def name: String = "addressme"

  def login(username: String, hashpwd: String): Option[User]

}

trait UserPasswordProviderImpl extends UserPasswordProvider {
  me: UserRepositoryFactory =>

  override def login(username: String, pwd: String): Option[User] = {
    userRepo.findByProviderAndUserId(name, username).filter(_.password.map(p => pwd.bcrypt hash = p) getOrElse false)
  }

}