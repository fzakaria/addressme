package slick.driver

/**
 * *
 * Helper class that any enumeration can extend to now be mappable in Slick
 */
trait DatabaseEnumeration extends Enumeration {
  me: DriverComponent =>

  import driver.simple._

  implicit def databaseEnumerationTypeMapper = MappedColumnType.base[Value, String](
    enum => enum.toString,
    string => this.withName(string))

}