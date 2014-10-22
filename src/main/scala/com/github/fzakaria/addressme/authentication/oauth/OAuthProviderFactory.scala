package com.github.fzakaria.addressme.authentication.oauth

import com.github.fzakaria.addressme.authentication.oauth.providers.GithubOAuth2Provider
import spray.routing.ActorSystemProviderImpl
import com.github.fzakaria.addressme.factories.ConfigFactoryImpl

trait OAuthProviderFactory {

  def getProvider(name: String): OAuthProvider

}

trait OAuthProviderFactoryImpl extends OAuthProviderFactory {

  override def getProvider(name: String): OAuthProvider = {
    name match {
      case "github" => new GithubOAuth2Provider with ActorSystemProviderImpl with ConfigFactoryImpl
      case _ => throw new IllegalArgumentException("No provider is found with that name.")
    }
  }

}