package com.github.fzakaria.addressme.authentication.oauth.providers

import spray.json.DefaultJsonProtocol
import spray.httpx.SprayJsonSupport._

case class OAuth2TokenResult(access_token: String, scope: String, token_type: String)
object OAuth2TokenResultProtocol extends DefaultJsonProtocol {
  implicit val OAuth2TokenFormat = jsonFormat3(OAuth2TokenResult)
}

abstract class OAuth2User(login: Option[String], id: Option[Long], avatar_url: Option[String], name: Option[String], company: Option[String],
  email: Option[String])
