package com.github.fzakaria.addressme.authentication.oauth

import spray.http.Uri
import com.github.fzakaria.addressme.factories.ConfigFactory
import collection.JavaConversions._
import spray.http._
import spray.json.DefaultJsonProtocol
import spray.httpx.encoding.{ Gzip, Deflate }
import spray.httpx.SprayJsonSupport._
import spray.client.pipelining._
import scala.concurrent._
import spray.routing.ActorSystemProvider
import scala.concurrent.ExecutionContext.Implicits.global

trait OAuth2Provider extends OAuthProvider {
  me: ConfigFactory with ActorSystemProvider =>

  private val keyPrefix: String = "addressme.oauth2"
  def name: String

  override def doesStateMatch(state: String): Boolean = state == this.state

  case class OAuth2TokenResult(accessToken: String, scopes: Seq[String], tokenType: String)
  object MyJsonProtocol extends DefaultJsonProtocol {
    implicit val orderConfirmationFormat = jsonFormat3(OAuth2TokenResult)
  }
  import MyJsonProtocol._

  val pipeline: HttpRequest => Future[OAuth2TokenResult] = (
    sendReceive ~> unmarshal[OAuth2TokenResult]
  )

  def getToken(code: String) = {
  }

  override def authorizeUrl: Uri = {
    val authUri = Uri(config.getString(s"$keyPrefix.$name.authorizeUrl"))
    authUri.withQuery(
      ("client_id", clientId), ("redirect_uri", callbackUrl.toString),
      ("scope", scopes.mkString(",")), ("state", state)
    )
  }

  val tokenUrl: String = {
    config.getString(s"$keyPrefix.$name.tokenUrl")
  }

  val state: String = {
    config.getString(s"$keyPrefix.$name.state")
  }

  val clientId: String = {
    config.getString(s"$keyPrefix.$name.clientId")
  }

  val callbackUrl: Uri = {
    Uri(config.getString(s"$keyPrefix.$name.callbackUrl"))
  }

  val scopes: Seq[String] = {
    config.getStringList(s"$keyPrefix.$name.scopes")
  }

}