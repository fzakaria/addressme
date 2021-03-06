package com.github.fzakaria.addressme.actors

import spray.routing._
import Directives._
import akka.actor.ActorSystem
import com.github.fzakaria.addressme.factories.ApiRouterFactory
import com.github.fzakaria.addressme.factories.ApiRouterFactoryImpl
import com.github.fzakaria.addressme.factories.StaticRouterFactory
import com.github.fzakaria.addressme.factories.StaticRouterFactoryImpl
import spray.routing.directives.DebuggingDirectives._
import akka.event.Logging
import akka.actor.ActorLogging
import spray.routing.authentication.SessionLoginAuth
import com.github.fzakaria.addressme.models.User
import spray.routing.authentication.session._
import com.github.fzakaria.addressme.factories.{ UserRepositoryFactory, UserRepositoryFactoryImpl }
import spray.util.LoggingContext
import spray.http.StatusCodes._
import spray.httpx.PlayTwirlSupport._
import spray.routing.SessionDirectives._

trait ApiActor extends HttpServiceActor with ActorLogging {
  me: ApiRouterFactory with StaticRouterFactory with UserRepositoryFactory =>

  import context.dispatcher

  def receive = runRoute(
    staticRouter.route(RequestSession())
      ~
      //log the request/response when Akkas log level is debug or lower
      logRequestResponse("[API]") {
        authenticate(SessionLoginAuth(findUser)) { potentialUser =>
          getAndClearFlash { flash =>
            apiRouter.route(RequestSession(potentialUser, flash))
          }
        }
      }
  )

  def findUser(s: String): Option[User] = {
    userRepo.findById(s.toLong)
  }

  implicit val myExceptionHandler =
    RejectionHandler {
      case Nil =>
        complete(NotFound -> html.notfound.render)
    }
}

class ApiActorImpl extends HttpServiceActor with StaticRouterFactoryImpl with ApiRouterFactoryImpl with UserRepositoryFactoryImpl with ApiActor {

}