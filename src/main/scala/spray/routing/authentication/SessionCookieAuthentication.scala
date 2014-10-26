package spray.routing.authentication

import scala.concurrent.{ ExecutionContext, Future }
import spray.routing.RequestContext
import spray.routing.Session
import spray.routing.authentication.session._
import spray.routing.Rejection
/**
 * An HttpAuthenticator is a ContextAuthenticator that uses credentials passed to the server via the
 * HTTP `Authorization` header to authenticate the user and extract a user object.
 */
class SessionAuthenticator[U](findUser: String => Option[U])(implicit val executionContext: ExecutionContext) extends UserSessionAuthenticator[U] {

  def cookieName: String = "userId"

  def apply(session: Option[Session]): Future[Authentication[Option[U]]] = {
    val userId = session.flatMap(_.get(cookieName))
    val response = userId.flatMap {
      findUser(_)
    }
    Future.successful { Right(response) }
  }

}

object SessionLoginAuth {

  def apply[T](findUser: String => Option[T])(implicit ec: ExecutionContext) = new SessionAuthenticator[T](findUser)

}