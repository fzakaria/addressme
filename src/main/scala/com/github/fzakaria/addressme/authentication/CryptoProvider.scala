package com.github.fzakaria.addressme.authentication
import com.roundeights.hasher.Implicits._
import com.github.fzakaria.addressme.factories.ConfigServiceFactory
import com.typesafe.config.PimpedConfig._

case class CryptoException(val message: String) extends Throwable

trait CryptoProvider {

  def sign(message: String, key: Array[Byte]): String

  def sign(message: String): String

}

trait CryptoProviderImpl extends CryptoProvider {
  me: ConfigServiceFactory =>

  private lazy val secret: String = config.applicationSecret

  override def sign(message: String, key: Array[Byte]): String = {
    message.hmac(key).sha1
  }

  override def sign(message: String): String = {
    sign(message, secret.getBytes("utf-8"))
  }

}