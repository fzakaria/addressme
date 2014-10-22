package com.github.fzakaria.addressme.api

import spray.routing._
import Directives._
import spray.http._
import StatusCodes._
import spray.httpx.PlayTwirlSupport._
import com.github.fzakaria.addressme.authentication.oauth.{ OAuthProviderFactoryImpl, OAuthProviderFactory }

trait LoginServiceFactory {
  def loginService: LoginService
}

trait LoginService extends Routable {
  me: OAuthProviderFactory =>

  override def route: Route = {
    pathPrefix("login") {
      (pathEnd & get) {
        complete { html.login.render }
      } ~
        //login/:provider
        pathPrefix(Segment) { provider =>
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
}

trait LoginServiceFactoryImpl extends LoginServiceFactory {
  def loginService: LoginService = new LoginService with OAuthProviderFactoryImpl
}