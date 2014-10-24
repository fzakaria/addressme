package com.typesafe.config

import scala.slick.driver._
import net.ceedubs.ficus.Ficus._
import com.github.fzakaria.addressme.ApplicationMode
import ApplicationMode._

object PimpedConfig {

  implicit def pimpConfigConverter(config: Config) = new PimpedConfig(config)
}

class PimpedConfig(config: Config) {

  private val DEFAULT_NAME = "default"

  def mode: ApplicationMode = {
    config.as[String]("addressme.mode") match {
      case "dev" => Debug
      case "test" => Test
      case "prod" => Prod
    }
  }

  def dbUrl(name: String = DEFAULT_NAME): String = {
    val databaseUrl = config.as[Option[String]](s"db.$name.url").getOrElse {
      throw new IllegalArgumentException(s"Slick error : database url not defined in application.conf for db.$name.url key")
    }
    databaseUrl
  }

  def driver(name: String = DEFAULT_NAME): JdbcDriver = {
    val dN = driverName(name)
    driverByName(dN).getOrElse {
      throw new IllegalArgumentException(s"Slick error : jdbc driver not known in application.conf for $dN")
    }
  }

  def driverName(name: String = DEFAULT_NAME): String = {
    val key = s"db.$name.driver"
    val driverName = config.as[Option[String]](key).getOrElse {
      throw new IllegalArgumentException(s"Slick error : jdbc driver not defined in application.conf for $key key")
    }
    driverName
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

