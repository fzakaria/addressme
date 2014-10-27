package spray.routing

import com.github.fzakaria.addressme.factories.{ CryptoProviderFactoryImpl, ConfigServiceFactoryImpl }
/**
 * HTTP Flash scope.
 *
 * Flash data are encoded into an HTTP cookie, and can only contain simple `String` values.
 */
case class Flash(data: Map[String, String] = Map.empty[String, String]) {

  /**
   * Optionally returns the flash value associated with a key.
   */
  def get(key: String) = data.get(key)

  /**
   * Returns `true` if this flash scope is empty.
   */
  def isEmpty: Boolean = data.isEmpty

  /**
   * Adds a value to the flash scope, and returns a new flash scope.
   *
   * For example:
   * {{{
   * flash + ("success" -> "Done!")
   * }}}
   *
   * @param kv the key-value pair to add
   * @return the modified flash scope
   */
  def +(kv: (String, String)) = {
    require(kv._2 != null, "Cookie values cannot be null")
    copy(data + kv)
  }

  /**
   * Removes a value from the flash scope.
   *
   * For example:
   * {{{
   * flash - "success"
   * }}}
   *
   * @param key the key to remove
   * @return the modified flash scope
   */
  def -(key: String) = copy(data - key)

  /**
   * Retrieves the flash value that is associated with the given key.
   */
  def apply(key: String) = data(key)
}

/**
 * Helper utilities to manage the Session cookie.
 */
object Flash extends CookieBaker[Flash] with CryptoProviderFactoryImpl with ConfigServiceFactoryImpl {

  val emptyCookie = new Flash
  override val isSigned = false

  override def cookieName: String = "flash"

  def deserialize(data: Map[String, String]) = new Flash(data)

  def serialize(session: Flash) = session.data
}