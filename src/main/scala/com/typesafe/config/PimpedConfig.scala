package com.typesafe.config

import scala.slick.driver._

import net.ceedubs.ficus.Ficus._

object PimpedConfig {
  
  implicit def pimpConfigConverter(config: Config) = new PimpedConfig(config)
}

class PimpedConfig(config: Config) {

  private val DEFAULT_NAME = "default"

  def driver(name: String = DEFAULT_NAME): JdbcDriver = {
    val key = s"db.$name.driver"
    val driverName = config.as[Option[String]](key).getOrElse {
      throw new IllegalArgumentException(s"Slick error : jdbc driver not defined in application.conf for $key key")
    }
    driverByName(driverName).getOrElse {
      throw new IllegalArgumentException(s"Slick error : jdbc driver not known in application.conf for $driverName")
    }
  }
    
  /** Extend this to add driver or change driver mapping */
  protected def driverByName: String => Option[JdbcDriver] = Map(
    "org.apache.derby.jdbc.EmbeddedDriver" -> DerbyDriver,
    "org.h2.Driver" -> H2Driver,
    "org.hsqldb.jdbcDriver" -> HsqldbDriver,
    "org.hsqldb.jdbc.JDBCDriver" -> HsqldbDriver,
    "com.mysql.jdbc.Driver" -> MySQLDriver,
    "org.postgresql.Driver" -> PostgresDriver,
    "org.sqlite.JDBC" -> SQLiteDriver).get(_)
}


