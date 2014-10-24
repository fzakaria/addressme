package com.github.fzakaria.addressme.factories

import spray.routing.ActorSystemProviderImpl
import com.github.fzakaria.addressme.authentication.oauth.providers.OAuthProvider
import com.github.fzakaria.addressme.authentication.oauth.providers.OAuth1Provider
import com.github.fzakaria.addressme.authentication.oauth.providers.OAuthUser

trait OAuth1ProviderFactory {

  def getProvider(name: String): OAuth1Provider[OAuthUser]

}

trait OAuth1ProviderFactoryImpl extends OAuth1ProviderFactory {

  override def getProvider(name: String): OAuth1Provider[OAuthUser] = {
    name match {
      case _ => throw new IllegalArgumentException("No provider is found with that name.")
    }
  }

}