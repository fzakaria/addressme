package spray.routing

import shapeless._
import spray.routing._
import scala.Some
import shapeless.::

/**
 * Taken from: https://github.com/azeem/spray-cookiebaker
 */

/**
 * Rejection created when a given session value is not found
 */
case class MissingSessionRejection(valueName: String) extends Rejection

trait SessionDirectives {
  import directives.BasicDirectives._
  import spray.routing.directives._
  import CookieDirectives._
  import RouteDirectives._

  /**
   * Extracts and passes a baked cookie object from the cookie
   * @param baker CookieBaker that can decode the cookie
   */
  def bakedCookie[T <: AnyRef](baker: CookieBaker[T]): Directive[T :: HNil] = cookie(baker.cookieName).hmap {
    case c :: HNil => baker.decodeFromCookie(Some(c))
  }

  /**
   * Extracts and passes an Some of a baked cookie object from
   * the cookie if it exists, otherwise None
   * @param baker CookieBaker that can decode the baked cookie
   */
  def optionalBakedCookie[T <: AnyRef](baker: CookieBaker[T]): Directive[Option[T] :: HNil] =
    bakedCookie(baker).hmap(_.map(shapeless.option)) | provide(None)

  /**
   * Encodes a Baked cookie and sets it in the response header
   * @param baker CookieBaker that can encode the baked cookie
   * @param data the baked cookie to be set
   */
  def setBakedCookie[T <: AnyRef](baker: CookieBaker[T], data: T): Directive0 = setCookie(baker.encodeAsCookie(data))

  /**
   * Clears a baked cookie in the response. By sending a discard cookie in
   * the response
   * @param baker CookieBaker that contains the discard cookie
   */
  def clearBakedCookie[T <: AnyRef](baker: CookieBaker[T]): Directive0 = setCookie(baker.discard)

  /**
   * Same as bakedCookie, using Session CookieBaker
   */
  def session: Directive[Session :: HNil] = bakedCookie(Session)

  /**
   * Extracts a Session value with the given name. Rejects MissingSessionRejection
   * if the session value was not found
   * @param name name of the session value
   */
  def sessionVal(name: String): Directive[String :: HNil] = session.hflatMap {
    case s :: HNil if s.contains(name) => provide(s(name))
    case _ => reject(MissingSessionRejection(name))
  }

  /**
   * Same as optionalBakedCookie, suing Session cookiebaker
   * @return
   */
  def optionalSession: Directive[Option[Session] :: HNil] = optionalBakedCookie(Session)

  /**
   * Extracts and passes Some of session value with the given name.
   * None if the session or the session value with given name was
   * not found
   * @param name name of the session value
   */
  def optionalSessionVal(name: String): Directive[Option[String] :: HNil] = optionalSession.hmap {
    case Some(s) :: HNil => s.get(name)
    case _ => None
  }

  /**
   * Same as setBakedCookie with Session as the CookieBaker
   * @param sessionData the Session objecompct to be set
   */
  def setSession(sessionData: Session): Directive0 = setBakedCookie(Session, sessionData)

  /**
   * Creates a session object from set of key value pairs and
   * sets ut
   * @param mapData set of (String, String) to create the session from
   */
  def setSession(mapData: (String, String)*): Directive0 = setSession(Session(Map(mapData: _*)))

  /**
   * Same as clearBakedCookie, using Session as the CookieBaker
   */
  def clearSession: Directive0 = clearBakedCookie(Session)
}

object SessionDirectives extends SessionDirectives