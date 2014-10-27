package com.github.fzakaria.addressme.routers

import spray.routing._
import Directives._
import spray.http._
import StatusCodes._
import spray.httpx.PlayTwirlSupport._
import com.github.fzakaria.addressme.factories.OAuth2RouterFactory
import com.typesafe.scalalogging.LazyLogging
import spray.routing.SessionDirectives._
import com.github.fzakaria.addressme.factories.UserPasswordProviderFactory

case class LoginForm(email: String, password: String, rememberMe: Boolean)

trait LoginRouter extends Routable with LazyLogging {
  me: OAuth2RouterFactory with UserPasswordProviderFactory =>

  val incorrectLoginHandler = RejectionHandler {
    case ValidationRejection(message, cause) :: _ => {
      addFlash(("danger", message)) {
        redirect("/api/login", spray.http.StatusCodes.SeeOther)
      }
    }
  }

  override def route(rs: RequestSession): Route = {
    pathPrefix("login") {
      (pathEnd & get) {
        complete { html.login.render(rs) }
      } ~
        (pathEnd & post & formFields('email, 'password, 'rememberMe.as[Boolean] ? false).as(LoginForm)) { form =>
          val user = userPasswordProvider.login(form.email, form.password)
          handleRejections(incorrectLoginHandler) {
            validate(user.isDefined, "We cannot find a user with those credentials") {
              redirect("/api/home", spray.http.StatusCodes.TemporaryRedirect)
            }
          }
        } ~
        oauth2Router.route(rs)
    } ~
      path("logout" ~ Slash.?) {
        clearSession {
          redirect("/api/home", spray.http.StatusCodes.TemporaryRedirect)
        }
      }

  }
}
