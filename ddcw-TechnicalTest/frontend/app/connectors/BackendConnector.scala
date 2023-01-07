package connectors

import javax.inject.{Inject, Singleton}

import play.api.Configuration
import play.api.libs.json.Json
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BackendConnector @Inject()(ws: WSClient,
                                 config: Configuration) {

  private val serviceName = "backend"
  private lazy val env = config.get[String]("run.mode")
  private lazy val host = config.get[String](s"$env.services.$serviceName.host")

  private lazy val baseUrl = env match {
    case "Prod" => s"http://$host/$serviceName"
    case _ =>
      val port = config.get[Int](s"$env.services.$serviceName.port")
      s"http://$host:$port/$serviceName"
  }

  private def buildUrl(endpoint: String) = s"$baseUrl$endpoint"

  def sendMessage(message: String)(implicit ec: ExecutionContext): Future[String] = {
    ws.url(buildUrl("/print")).post(Json.obj("message" -> s"$message")) map {
      response =>
        response.status match {
          case 200 => response.body
          case _ => "ERROR - Non 200 response"
        }
    }
  }.recover { case e => s"ERROR - ${e.toString}" }
}