package com.github.fzakaria.addressme.factories

import com.typesafe.config.Config

trait ConfigFactory {

  def config: Config

}

trait ConfigFactoryImpl extends ConfigFactory {
  override def config = com.typesafe.config.ConfigFactory.load()
}