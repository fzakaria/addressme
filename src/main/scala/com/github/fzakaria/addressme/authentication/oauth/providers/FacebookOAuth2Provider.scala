package com.github.fzakaria.addressme.authentication.oauth.providers

import com.github.fzakaria.addressme.authentication.oauth._
import spray.http.Uri
import com.github.fzakaria.addressme.factories.ConfigServiceFactory
import spray.routing.ActorSystemProvider
import scala.concurrent.ExecutionContext.Implicits.global
import spray.http.HttpHeaders.Accept
import spray.http.MediaTypes.`application/json`
import spray.httpx.SprayJsonSupport._
import spray.httpx.unmarshalling._
import spray.http._
import spray.client.pipelining._
import scala.concurrent._
import spray.json.DefaultJsonProtocol
import spray.httpx.SprayJsonSupport._

case class FacebookUser(id: Option[String], name: Option[String], email: Option[String])
  extends OAuth2User(id = id, name = name, email = email, company = None, login = None, avatar_url = None)

object FacebookUserProtocol extends DefaultJsonProtocol {
  implicit val FacebookUserFormat = jsonFormat3(FacebookUser)
}

trait FacebookOAuth2Provider extends OAuth2Provider {
  me: ConfigServiceFactory with ActorSystemProvider =>

  override def name: String = "facebook"

  import FacebookUserProtocol._
  override def login(token: String): Future[OAuth2User] = {
    val loginUri = Uri("https://graph.facebook.com/me").withQuery(("access_token", token))
    val pipeline: (HttpRequest) => Future[FacebookUser] = pipelineWithoutMarshal ~> unmarshal[FacebookUser]
    pipeline(Get(loginUri))
  }

}