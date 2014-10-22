package com.github.fzakaria.addressme.authentication.oauth.providers

import com.github.fzakaria.addressme.authentication.oauth._
import spray.http.Uri
import com.github.fzakaria.addressme.factories.ConfigFactory
import spray.routing.ActorSystemProvider

trait GithubOAuth2Provider extends OAuth2Provider {
  me: ConfigFactory with ActorSystemProvider =>
  override def name: String = "github"

}
