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

/**
 * The password details
 *
 * @param hasher the id of the hasher used to hash this password
 * @param password the hashed password
 * @param salt the optional salt used when hashing
 */
case class PasswordInfo(hasher: Option[String] = None, password: Option[String] = None, salt: Option[String] = None)

case class User(
  id: Option[Long] = None,
  userId: String,
  providerId: String,
  firstName: String,
  lastName: String,
  email: String,
  avatarUrl: Option[String],
  authMethod: AuthenticationMethod,
  //OAuth1
  oAuth1Info: OAuth1Info,
  //OAuth2
  oAuth2Info: OAuth2Info,
  //PasswordInfo
  passwordInfo: PasswordInfo)

trait UsersComponent {
  me: DriverComponent =>
  import driver.simple._

  class Users(tag: Tag) extends Table[User](tag, "users") {
    def id = column[Long]("id", O.AutoInc, O.PrimaryKey)

    def userId = column[String]("userId")

    def providerId = column[String]("providerId")

    def firstName = column[String]("firstName")

    def lastName = column[String]("lastName")

    def email = column[String]("email")

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
    def hasher = column[Option[String]]("hasher")
    def password = column[Option[String]]("password")
    def salt = column[Option[String]]("salt")

    def passwordInfo = (hasher, password, salt) <> (PasswordInfo.tupled, PasswordInfo.unapply)

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
      passwordInfo
    ) <> (User.tupled, User.unapply)
  }
  val users = TableQuery[Users]

}