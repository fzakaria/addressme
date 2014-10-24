package slick.driver

import scala.slick.driver.JdbcProfile

trait DriverComponent {

  val driver: JdbcProfile

}