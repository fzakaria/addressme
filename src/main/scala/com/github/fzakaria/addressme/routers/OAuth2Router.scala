package com.github.fzakaria.addressme.routers

import spray.routing.Routable
import spray.routing._
import Directives._
import spray.http._
import StatusCodes._
import com.github.fzakaria.addressme.factories.OAuth2ProviderFactory
import scala.concurrent.ExecutionContext.Implicits.global
import spray.routing.SessionDirectives._

trait OAuth2Router extends Routable {
  me: OAuth2ProviderFactory =>

  override def route(rs: RequestSession): Route = {
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
              validate(providerService.generateState == state, "The state doesn't match! Likely CSRF") {
                onSuccess(providerService.getToken(code)) { tokenResult =>
                  onSuccess(providerService.login(tokenResult.access_token)) { oauthUser =>
                    val user = providerService.findOrCreate(oauthUser)
                    setSession(("userId", user.id.get.toString)) {
                      complete { user.toString }
                    }
                  }
                }
              }
            }

        }
    }
  }

}