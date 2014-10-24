package com.github.fzakaria.addressme.actors

import com.github.fzakaria.addressme.models.UsersComponent
import akka.actor._
import slick.driver.DriverComponentFromConfig
import slick.driver.DriverComponent
import scala.slick.jdbc.meta.MTable
import com.github.fzakaria.addressme.factories.DatabaseServiceFactory
import com.github.fzakaria.addressme.factories.DatabaseServiceFactoryImpl
import com.typesafe.scalalogging.LazyLogging

sealed trait ActorMessage
case class CreateDatabaseMessage() extends ActorMessage

trait DatabaseCreatorActor extends Actor with LazyLogging with UsersComponent {
  me: DriverComponent with DatabaseServiceFactory =>

  import driver.simple._

  def receive = {
    case CreateDatabaseMessage => {
      database withSession { implicit session =>
        logger.debug("Creating tables...")
        createIfNotExists(users)
        logger.debug("Finshed creating tables!")
      }
    }

  }

  def createIfNotExists(tables: TableQuery[_ <: Table[_]]*)(implicit session: Session) {
    tables foreach { table =>
      if (MTable.getTables(table.baseTableRow.tableName).list.isEmpty) {
        logger.debug(s"Creating ${table.baseTableRow.tableName} table...")
        table.ddl.create
      }
    }
  }

}

class DatabaseCreatorActorImpl extends DatabaseCreatorActor with DatabaseServiceFactoryImpl with UsersComponent with DriverComponentFromConfig {

}