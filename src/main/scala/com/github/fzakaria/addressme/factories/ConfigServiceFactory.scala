package com.github.fzakaria.addressme.factories

import net.ceedubs.ficus.Ficus._
import com.typesafe.config.{ Config, ConfigFactory }

trait ConfigServiceFactory {

  def config: Config

}

trait ConfigServiceFactoryImpl extends ConfigServiceFactory {
  override def config = ConfigFactory.load()
}