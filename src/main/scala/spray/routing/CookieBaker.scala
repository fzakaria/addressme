package spray.routing

import spray.http.HttpCookie
import com.typesafe.config.ConfigFactory
import com.github.fzakaria.addressme.factories.CryptoProviderFactory
import com.github.fzakaria.addressme.factories.ConfigServiceFactory
import com.typesafe.config.PimpedConfig._

/**
 * Trait that should be extended by the Cookie helpers.
 */
trait CookieBaker[T <: AnyRef] {

  me: CryptoProviderFactory with ConfigServiceFactory =>

  /**
   * The cookie name.
   */
  def cookieName: String = config.cookieName

  /**
   * Default cookie, returned in case of error or if missing in the HTTP headers.
   */
  def emptyCookie: T

  /**
   * `true` if the Cookie is signed. Defaults to false.
   */
  def isSigned: Boolean = false

  /**
   * `true` if the Cookie should have the httpOnly flag, disabling access from Javascript. Defaults to true.
   */
  def httpOnly = true

  /**
   * The cookie expiration date in seconds, `None` for a transient cookie
   */
  def maxAge: Option[Long] = None

  /**
   * The cookie domain. Defaults to None.
   */
  def domain: Option[String] = None

  /**
   * `true` if the Cookie should have the secure flag, restricting usage to https. Defaults to false.
   */
  def secure = false

  /**
   *  The cookie path.
   */
  def path = "/"

  /**
   * Encodes the data as a `String`.
   */
  def encode(data: Map[String, String]): String = {
    val encoded = java.net.URLEncoder.encode(data.filterNot(_._1.contains(":")).map(d => d._1 + ":" + d._2).mkString("\u0000"), "UTF-8")
    if (isSigned)
      cryptoProvider.sign(encoded) + "-" + encoded
    else
      encoded
  }

  /**
   * Decodes from an encoded `String`.
   */
  def decode(data: String): Map[String, String] = {

    def urldecode(data: String) = java.net.URLDecoder.decode(data, "UTF-8").split("\u0000").map(_.split(":")).map(p => p(0) -> p.drop(1).mkString(":")).toMap

    // Do not change this unless you understand the security issues behind timing attacks.
    // This method intentionally runs in constant time if the two strings have the same length.
    // If it didn't, it would be vulnerable to a timing attack.
    def safeEquals(a: String, b: String) = {
      if (a.length != b.length) {
        false
      } else {
        var equal = 0
        for (i <- Array.range(0, a.length)) {
          equal |= a(i) ^ b(i)
        }
        equal == 0
      }
    }

    try {
      if (isSigned) {
        val splitted = data.split("-")
        val message = splitted.tail.mkString("-")
        if (safeEquals(splitted(0), cryptoProvider.sign(message))) {
          urldecode(message)
        } else {
          Map.empty[String, String]
        }
      } else urldecode(data)
    } catch {
      // fail gracefully is the session cookie is corrupted
      case _: Exception => Map.empty[String, String]
    }
  }

  /**
   * Encodes the data as a `Cookie`.
   */
  def encodeAsCookie(data: T): HttpCookie = {
    val cookie = encode(serialize(data))
    //Cookie(cookieName, cookie, maxAge, path, domain, secure, httpOnly)
    HttpCookie(name = cookieName, content = cookie,
      maxAge = maxAge, path = Some(path),
      domain = domain, secure = secure, httpOnly = httpOnly)
  }

  /**
   * Decodes the data from a `Cookie`.
   */
  def decodeFromCookie(cookie: Option[HttpCookie]): T = {
    cookie.filter(_.name == cookieName).map(c => deserialize(decode(c.content))).getOrElse(emptyCookie)
  }

  def discard = HttpCookie(name = cookieName, content = "",
    maxAge = Some(-1), path = Some(path),
    domain = domain, secure = secure)

  /**
   * Builds the cookie object from the given data map.
   *
   * @param data the data map to build the cookie object
   * @return a new cookie object
   */
  protected def deserialize(data: Map[String, String]): T

  /**
   * Converts the given cookie object into a data map.
   *
   * @param cookie the cookie object to serialize into a map
   * @return a new `Map` storing the key-value pairs for the given cookie
   */
  protected def serialize(cookie: T): Map[String, String]

}