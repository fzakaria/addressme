package com.github.fzakaria.addressme.authentication.oauth.providers

import spray.json.DefaultJsonProtocol
import spray.httpx.SprayJsonSupport._

case class OAuth2TokenResult(access_token: String, scope: String, token_type: String, expires_in: Option[Int])
object OAuth2TokenResultProtocol extends DefaultJsonProtocol {
  implicit val OAuth2TokenFormat = jsonFormat4(OAuth2TokenResult)
}
