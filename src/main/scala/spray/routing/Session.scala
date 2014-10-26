package spray.routing

import spray.http.HttpCookie
import com.github.fzakaria.addressme.factories.CryptoProviderFactoryImpl
import com.github.fzakaria.addressme.factories.ConfigServiceFactoryImpl

/**
 * HTTP Session.
 *
 * Session data are encoded into an HTTP cookie, and can only contain simple `String` values.
 */
case class Session(data: Map[String, String] = Map.empty[String, String]) {

  /**
   * Optionally returns the session value associated with a key.
   */
  def get(key: String) = data.get(key)

  /**
   * Retruns true if the session has the given key.
   */
  def contains(key: String) = data.contains(key)

  /**
   * Returns `true` if this session is empty.
   */
  def isEmpty: Boolean = data.isEmpty

  /**
   * Adds a value to the session, and returns a new session.
   *
   * For example:
   * {{{
   * session + ("username" -> "bob")
   * }}}
   *
   * @param kv the key-value pair to add
   * @return the modified session
   */
  def +(kv: (String, String)) = copy(data + kv)

  /**
   * Removes any value from the session.
   *
   * For example:
   * {{{
   * session - "username"
   * }}}
   *
   * @param key the key to remove
   * @return the modified session
   */
  def -(key: String) = copy(data - key)

  /**
   * Retrieves the session value which is associated with the given key.
   */
  def apply(key: String) = data(key)

}

/**
 * Helper utilities to manage the Session cookie.
 */
object Session extends CookieBaker[Session] with CryptoProviderFactoryImpl with ConfigServiceFactoryImpl {

  val emptyCookie = new Session
  override val isSigned = true

  def deserialize(data: Map[String, String]) = new Session(data)

  def serialize(session: Session) = session.data
}