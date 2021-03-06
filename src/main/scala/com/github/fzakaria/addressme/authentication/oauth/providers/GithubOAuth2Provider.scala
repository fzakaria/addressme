package com.github.fzakaria.addressme.authentication.oauth.providers

import com.github.fzakaria.addressme.authentication.oauth._
import spray.http.Uri
import com.github.fzakaria.addressme.factories.{ ConfigServiceFactory, CryptoProviderFactory }
import com.github.fzakaria.addressme.models.User
import com.github.fzakaria.addressme.factories.UserRepositoryFactory
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
import com.github.fzakaria.addressme.models.AuthenticationMethod._

case class GithubUser(login: Option[String], id: Option[Long], avatar_url: Option[String], `type`: Option[String], name: Option[String], company: Option[String],
  blog: Option[String], location: Option[String], email: Option[String], bio: Option[String], public_repos: Option[Int], public_gists: Option[Int], followers: Option[Int],
  following: Option[Int], created_at: Option[String], updated_at: Option[String]) extends OAuthUser(login, id.map(_.toString), avatar_url, name, company,
  email)

object GithubUserProtocol extends DefaultJsonProtocol {
  implicit val GithubUserFormat = jsonFormat16(GithubUser)
}

trait GithubOAuth2Provider extends OAuth2Provider {
  me: ConfigServiceFactory with ActorSystemProvider with UserRepositoryFactory with CryptoProviderFactory =>

  type SocialUser = GithubUser
  override def name: String = "github"

  import GithubUserProtocol._

  override def find(socialProfile: GithubUser): Option[User] = {
    socialProfile.login.flatMap { userRepo.findByProviderAndUserId(name, _) }
  }

  override def create(socialProfile: GithubUser): User = {
    val nameSplit = socialProfile.name.map { name =>
      name.split("\\s+")
    }
    val firstName = nameSplit.flatMap { nameSplit =>
      nameSplit.headOption
    }
    val lastName = nameSplit.flatMap { nameSplit =>
      nameSplit.lastOption

    }
    val newUser = User(userId = socialProfile.login, email = socialProfile.email,
      providerId = name, authMethod = OAuth2, firstName = firstName,
      lastName = lastName)
    userRepo.create(newUser)
  }

  override def login(token: String): Future[GithubUser] = {
    val loginUri = Uri("https://api.github.com/user").withQuery(("access_token", token))
    val pipeline: (HttpRequest) => Future[GithubUser] = pipelineWithoutMarshal ~> unmarshal[GithubUser]
    pipeline(Get(loginUri))
  }

}

