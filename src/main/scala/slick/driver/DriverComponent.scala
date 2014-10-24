package slick.driver

import scala.slick.driver.JdbcProfile
import com.github.fzakaria.addressme.factories.ConfigServiceFactoryImpl
import com.typesafe.config.PimpedConfig._

trait DriverComponent {

  val driver: JdbcProfile

}

trait DriverComponentFromConfig extends DriverComponent with ConfigServiceFactoryImpl {

  override val driver: JdbcProfile = config.driver()

}