package com.github.fzakaria.addressme.authentication.oauth.providers

import spray.http.Uri
import com.github.fzakaria.addressme.factories.ConfigServiceFactory
import collection.JavaConversions._
import spray.http._
import spray.client.pipelining._
import scala.concurrent._
import spray.routing.ActorSystemProvider
import scala.concurrent.ExecutionContext.Implicits.global
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import com.roundeights.hasher.Implicits._
import spray.httpx.encoding.{ Gzip, Deflate }
import spray.http.HttpHeaders.Accept
import spray.http.MediaRanges.`*/*`
import spray.http.MediaTypes.`application/json`
import spray.httpx.SprayJsonSupport._
import spray.httpx.unmarshalling.{ Unmarshaller, FromResponseUnmarshaller }

case class OAuth2Config(clientId: String, clientSecret: String, tokenUrl: String, authorizeUrl: String, scopes: Seq[String], callbackUrl: String, key: String)

trait OAuth2Provider extends OAuthProvider {
  me: ConfigServiceFactory with ActorSystemProvider =>

  private val KEY_PREFIX: String = s"oauth2.$name"

  lazy val oauth2Config: OAuth2Config = config.as[OAuth2Config](KEY_PREFIX)

  def setContentType(mediaType: MediaType)(r: HttpResponse): HttpResponse = {
    r.withEntity(HttpEntity(ContentType(mediaType), r.entity.data))
  }
  val SimpleOAuth2TokenResultUnmarshaller =
    Unmarshaller.delegate[String, OAuth2TokenResult](`*/*`) { string =>
      val query = Uri.Query(string)
      val access_token = query.getOrElse("access_token", throw new IllegalStateException("Shoudl have had access_token"))
      val scope = query.getOrElse("scope", "")
      val token_type = query.getOrElse("token_type", "bearer")
      val expires_in = query.getOrElse("expires_in", Int.MaxValue.toString)
      OAuth2TokenResult(access_token, scope, token_type, Some(expires_in.toInt))
    }

  implicit val unmarshaller = Unmarshaller.oneOf[OAuth2TokenResult](OAuth2TokenResultProtocol.OAuth2TokenFormat, SimpleOAuth2TokenResultUnmarshaller)

  def pipelineWithoutMarshal = (
    encode(Gzip)
    ~> addHeader(Accept(`application/json`))
    ~> sendReceive
    ~> setContentType(MediaTypes.`application/json`)
    ~> decode(Deflate)
  )

  def getToken(code: String): Future[OAuth2TokenResult] = {
    val accessTokenUri = Uri(oauth2Config.tokenUrl).withQuery(
      ("code", code), ("redirect_uri", oauth2Config.callbackUrl),
      ("client_id", oauth2Config.clientId), ("client_secret", oauth2Config.clientSecret)
    )
    val pipeline: (HttpRequest) => Future[OAuth2TokenResult] = pipelineWithoutMarshal ~> unmarshal[OAuth2TokenResult]
    pipeline(Post(accessTokenUri))
  }

  def authorizeUrl: Uri = {
    Uri(oauth2Config.authorizeUrl).withQuery(
      ("client_id", oauth2Config.clientId), ("redirect_uri", oauth2Config.callbackUrl),
      ("scope", oauth2Config.scopes.mkString(",")), ("state", generateState)
    )
  }

  /**
   * *
   * The state is then SHA1 encoded using the private key in the github config.
   * You can save this state in the cookie but don't forget to make it HttpOnly
   */
  def generateState: String = {
    oauth2Config.authorizeUrl.toString.hmac(oauth2Config.key).sha1
  }

}