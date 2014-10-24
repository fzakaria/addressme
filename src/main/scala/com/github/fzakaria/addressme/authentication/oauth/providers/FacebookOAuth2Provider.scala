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
import com.github.fzakaria.addressme.models.User
import com.github.fzakaria.addressme.factories.UserRepositoryFactory
import com.github.fzakaria.addressme.models.AuthenticationMethod._

case class FacebookUser(id: Option[String], name: Option[String], email: Option[String], first_name: Option[String], last_name: Option[String])
  extends OAuthUser(id = id, name = name, email = email, company = None, login = None, avatar_url = None)

object FacebookUserProtocol extends DefaultJsonProtocol {
  implicit val FacebookUserFormat = jsonFormat5(FacebookUser)
}

trait FacebookOAuth2Provider extends OAuth2Provider {
  me: ConfigServiceFactory with ActorSystemProvider with UserRepositoryFactory =>

  type SocialUser = FacebookUser
  override def name: String = "facebook"

  import FacebookUserProtocol._

  override def find(socialProfile: FacebookUser): Option[User] = {
    socialProfile.id.flatMap { userRepo.findByProviderAndUserId(name, _) }
  }

  override def create(socialProfile: FacebookUser): User = {
    val newUser = User(userId = socialProfile.id, email = socialProfile.email,
      providerId = name, authMethod = OAuth2, firstName = socialProfile.first_name,
      lastName = socialProfile.last_name)
    userRepo.create(newUser)
  }

  override def login(token: String): Future[FacebookUser] = {
    val loginUri = Uri("https://graph.facebook.com/me").withQuery(("access_token", token))
    val pipeline: (HttpRequest) => Future[FacebookUser] = pipelineWithoutMarshal ~> unmarshal[FacebookUser]
    pipeline(Get(loginUri))
  }

}