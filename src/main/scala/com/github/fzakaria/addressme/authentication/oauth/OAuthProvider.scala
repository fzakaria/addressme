package com.github.fzakaria.addressme.authentication.oauth
import spray.http.Uri

trait OAuthProvider {

  def authorizeUrl: Uri

  def doesStateMatch(code: String): Boolean
}