package com.github.fzakaria.addressme.factories

import com.github.fzakaria.addressme.authentication.oauth.providers.GithubOAuth2Provider
import spray.routing.ActorSystemProviderImpl
import com.github.fzakaria.addressme.authentication.oauth.providers.OAuth2Provider
import com.github.fzakaria.addressme.authentication.oauth.providers.FacebookOAuth2Provider
import com.github.fzakaria.addressme.authentication.oauth.providers.OAuthUser

trait OAuth2ProviderFactory {

  def getProvider(name: String): OAuth2Provider

}

trait OAuth2ProviderFactoryImpl extends OAuth2ProviderFactory {

  override def getProvider(name: String): OAuth2Provider = {
    name match {
      case "facebook" => new FacebookOAuth2Provider with ActorSystemProviderImpl with ConfigServiceFactoryImpl with UserRepositoryFactoryImpl with CryptoProviderFactoryImpl
      case "github" => new GithubOAuth2Provider with ActorSystemProviderImpl with ConfigServiceFactoryImpl with UserRepositoryFactoryImpl with CryptoProviderFactoryImpl
      case _ => throw new IllegalArgumentException("No provider is found with that name.")
    }
  }

}