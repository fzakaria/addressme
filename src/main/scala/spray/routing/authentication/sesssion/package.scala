package spray.routing.authentication

import spray.routing.Session
import scala.concurrent.{ ExecutionContext, Future }
import spray.routing.directives.AuthMagnet
import shapeless.HNil
import spray.routing.authentication._
import spray.routing.directives._
import BasicDirectives._
import FutureDirectives._
import MiscDirectives._
import RouteDirectives._

package object session {
  type UserSessionAuthenticator[T] = Option[Session] ⇒ Future[Authentication[Option[T]]]

  import spray.routing.SessionDirectives._

  implicit def fromSessionCookieAuthenticator[T](auth: UserSessionAuthenticator[T])(implicit executor: ExecutionContext): AuthMagnet[Option[T]] = {
    new AuthMagnet(optionalSession.map(auth(_)).flatMap(onSuccess(_)))
  }

}