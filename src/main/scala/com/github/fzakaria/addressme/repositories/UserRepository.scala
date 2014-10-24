package com.github.fzakaria.addressme.repositories

import slick.driver.DriverComponent
import com.github.fzakaria.addressme.models.UsersComponent
import com.github.fzakaria.addressme.models.User
import com.github.fzakaria.addressme.factories.DatabaseServiceFactory

trait UserRepository {

  def create(newUser: User): User

  def findByProviderAndUserId(provider: String, userId: String): Option[User]

}

/**
 * *
 * Implementation of the User repository using Slick
 */
trait SlickUserRepository extends UserRepository with UsersComponent {
  me: DatabaseServiceFactory with DriverComponent =>

  import driver.simple._

  override def create(newUser: User): User = {
    database withSession { implicit session =>
      val userWithId =
        (users returning users.map(_.id)
          into ((user, id) => user.copy(id = Some(id)))
        ) += newUser
      userWithId
    }
  }

  override def findByProviderAndUserId(provider: String, userId: String): Option[User] = {
    database withSession { implicit session =>
      users.filter(u => u.providerId === provider && u.userId === userId).firstOption
    }
  }

}