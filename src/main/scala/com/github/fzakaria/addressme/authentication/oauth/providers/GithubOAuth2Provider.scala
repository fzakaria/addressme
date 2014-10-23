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

case class GithubUser(login: Option[String], id: Option[Long], avatar_url: Option[String], `type`: Option[String], name: Option[String], company: Option[String],
  blog: Option[String], location: Option[String], email: Option[String], bio: Option[String], public_repos: Option[Int], public_gists: Option[Int], followers: Option[Int],
  following: Option[Int], created_at: Option[String], updated_at: Option[String]) extends OAuth2User(login, id, avatar_url, name, company,
  email)

object GithubUserProtocol extends DefaultJsonProtocol {
  implicit val GithubUserFormat = jsonFormat16(GithubUser)
}

trait GithubOAuth2Provider extends OAuth2Provider {
  me: ConfigServiceFactory with ActorSystemProvider =>
  override def name: String = "github"

  import GithubUserProtocol._
  override def login(token: String): Future[OAuth2User] = {
    val loginUri = Uri("https://api.github.com/user").withQuery(("access_token", token))
    val pipeline: (HttpRequest) => Future[GithubUser] = pipelineWithoutMarshal ~> unmarshal[GithubUser]
    pipeline(Get(loginUri))
  }

}

