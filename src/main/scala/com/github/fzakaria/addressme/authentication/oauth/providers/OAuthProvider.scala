package com.github.fzakaria.addressme.authentication.oauth.providers
import spray.http.Uri
import com.github.fzakaria.addressme.models.User
import scala.concurrent.Future

abstract class OAuthUser(login: Option[String], id: Option[String], avatar_url: Option[String], name: Option[String], company: Option[String],
  email: Option[String])

trait OAuthProvider {

  type SocialUser <: OAuthUser

  def name: String

  def login(token: String): Future[OAuthUser]

  def find(socialProfile: SocialUser): Option[User]

}