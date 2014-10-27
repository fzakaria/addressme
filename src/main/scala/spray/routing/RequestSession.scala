package spray.routing

import com.github.fzakaria.addressme.models.User

case class RequestSession(user: Option[User] = None, flash: Flash = Flash.emptyCookie) {

  def withUser(user: Option[User]): RequestSession = this.copy(user = user)

}