package com.github.fzakaria.addressme.models

import slick.driver.DriverComponent

case class User()


trait UsersComponent {
  me: DriverComponent =>
	import driver.simple._
	
    class Users(tag: Tag) extends Table[User](tag, "users") {
		def id = column[Long]("id", O.AutoInc, O.PrimaryKey)
    }
	
	val users = TableQuery[Users]
}