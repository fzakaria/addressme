package com.github.fzakaria.addressme.services

import spray.routing.Routable
import spray.routing._
import Directives._
import spray.http._
import StatusCodes._
import com.github.fzakaria.addressme.factories.OAuth2ProviderFactory

trait OAuth2Service extends Routable {
  me: OAuth2ProviderFactory =>

  override def route: Route = {
    //login/:provider
    pathPrefix("oauth2" / Segment) { provider =>
      val providerService = getProvider(provider)
      pathEnd {
        redirect(providerService.authorizeUrl.toString, spray.http.StatusCodes.TemporaryRedirect)
      } ~
        //login/:provider/callback
        path("callback") {
          parameter('error) { error =>
            complete(BadRequest -> s"We have the following error: $error")
          } ~
            parameter('code, 'state) { (code, state) =>
              validate(providerService.doesStateMatch(state), "The state doesn't match! Likely CSRF") {
                complete { provider }
              }
            }

        }
    }
  }

}