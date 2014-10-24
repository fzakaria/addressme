package com.github.fzakaria.addressme.factories

import com.typesafe.config.PimpedConfig._
import slick.driver.DriverComponent
import slick.driver.DriverComponentFromConfig

trait DatabaseServiceFactory {
  me: DriverComponent =>

  import driver.backend.DatabaseDef

  def database: DatabaseDef

}

trait DatabaseServiceFactoryImpl extends DatabaseServiceFactory with ConfigServiceFactoryImpl with DriverComponentFromConfig {
  import driver.backend.DatabaseDef
  import driver.backend.Database

  override def database: DatabaseDef = {
    val driverName = config.driverName()
    val databaseUrl = config.dbUrl()
    Database.forURL(databaseUrl, driver = driverName)
  }

}