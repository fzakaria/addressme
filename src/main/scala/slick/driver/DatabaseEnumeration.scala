package slick.driver

/**
 * *
 * Helper class that any enumeration can extend to now be mappable in Slick
 */
trait DatabaseEnumeration extends Enumeration {
  me: DriverComponent =>

  import driver.simple._
  import driver.MappedJdbcType

  implicit val enumMapper = MappedJdbcType.base[Value, Int](_.id, this.apply)
}