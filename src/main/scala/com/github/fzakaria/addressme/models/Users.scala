package com.github.fzakaria.addressme.models

import slick.driver.{ DriverComponent, DatabaseEnumeration, DriverComponentFromConfig }

/**
 * Authentication methods used by the identity providers
 */
object AuthenticationMethod extends DatabaseEnumeration with DriverComponentFromConfig {
  type AuthenticationMethod = Value
  val OAuth1 = Value("oauth1")
  val OAuth2 = Value("oauth2")
  val OpenId = Value("openId")
  val UserPassword = Value("userPassword")
}
import AuthenticationMethod._

/**
 * The OAuth 1 details
 *
 * @param token the token
 * @param secret the secret
 */
case class OAuth1Info(token: Option[String] = None, secret: Option[String] = None)

/**
 * The Oauth2 details
 *
 * @param accessToken the access token
 * @param tokenType the token type
 * @param expiresIn the number of seconds before the token expires
 * @param refreshToken the refresh token
 */
case class OAuth2Info(accessToken: Option[String] = None, tokenType: Option[String] = None,
  expiresIn: Option[Int] = None, refreshToken: Option[String] = None)

case class User(
  id: Option[Long] = None,
  userId: Option[String] = None,
  providerId: String,
  firstName: Option[String],
  lastName: Option[String],
  email: Option[String] = None,
  avatarUrl: Option[String] = None,
  authMethod: AuthenticationMethod,
  //OAuth1
  oAuth1Info: OAuth1Info = OAuth1Info(),
  //OAuth2
  oAuth2Info: OAuth2Info = OAuth2Info(),
  //PasswordInfo
  password: Option[String] = None)

trait UsersComponent {
  me: DriverComponent =>
  import driver.simple._

  class Users(tag: Tag) extends Table[User](tag, "users") {
    def id = column[Long]("id", O.AutoInc, O.PrimaryKey)

    def userId = column[Option[String]]("userId")

    def providerId = column[String]("providerId")

    def firstName = column[Option[String]]("firstName")

    def lastName = column[Option[String]]("lastName")

    def email = column[Option[String]]("email")

    def avatarUrl = column[Option[String]]("avatarUrl")

    def authMethod = column[AuthenticationMethod]("authMethod")

    // oAuth 1
    def token = column[Option[String]]("token")
    def secret = column[Option[String]]("secret")

    def oAuth1Info = (token, secret) <> (OAuth1Info.tupled, OAuth1Info.unapply)

    // oAuth 2
    def accessToken = column[Option[String]]("accessToken")
    def tokenType = column[Option[String]]("tokenType")
    def expiresIn = column[Option[Int]]("expiresIn")
    def refreshToken = column[Option[String]]("refreshToken")

    def oAuth2Info = (accessToken, tokenType, expiresIn, refreshToken) <> (OAuth2Info.tupled, OAuth2Info.unapply)

    // password login
    def password = column[Option[String]]("password")

    def * = (
      id.?,
      userId,
      providerId,
      firstName,
      lastName,
      email,
      avatarUrl,
      authMethod,
      oAuth1Info,
      oAuth2Info,
      password
    ) <> (User.tupled, User.unapply)
  }
  val users = TableQuery[Users]

}