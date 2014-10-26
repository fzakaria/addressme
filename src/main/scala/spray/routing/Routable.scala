package spray.routing

trait Routable {
  def route(rs: RequestSession): Route
}