package creditsuisse_loan.rest
package test

import org.apache.http.client.HttpClient
import org.apache.http.client.methods.{HttpPatch, _}
import org.apache.http.entity.{ContentType, StringEntity}
import org.apache.http.impl.client.HttpClientBuilder
import spray.json._

import scala.io.Source

object RestClient {
  trait HttpCall
  case class HttpCallSuccessful(url: String, code: Int, json: JsObject, rh: Seq[(String,String)]) extends HttpCall
  case class HttpCallFailed(url: String, error: Throwable) extends HttpCall
}

class RestClient {
  import RestClient._

  private val client = HttpClientBuilder.create.build

  private def doGetOrPost(url: String, client: HttpClient, method: HttpRequestBase): HttpCall = {
    try {
      val response = client.execute(method)
      val content = Source.fromInputStream(response.getEntity.getContent).mkString
      val json = (if (content.startsWith("{")) content else "{}").parseJson.asJsObject
      HttpCallSuccessful(url, response.getStatusLine.getStatusCode, json,
        response.getAllHeaders.map(x => (x.getName, x.getValue)))
    } catch {
      case e : Throwable => HttpCallFailed(url, e)
    } finally {
      method.releaseConnection()
    }
  }

  /** HTTP {method} as "application/json", overwrite with "rh" as "Content-type", "text/xml; charset=UTF-8" */
  private def postWithEntity(method: HttpEntityEnclosingRequestBase, url: String, json: JsObject, rh: String*): HttpCall = {
    method.setEntity(new StringEntity(json.toString, ContentType.create("application/json", "UTF-8")))
    for (Seq(a, b) <- rh.grouped(2)) method.setHeader(a, b)
    doGetOrPost(url, client, method)
  }

  def get(url: String, rh: String*): HttpCall = {
    val method = new HttpGet(url)
    for (Seq(a, b) <- rh.grouped(2)) method.setHeader(a, b)
    doGetOrPost(url, client, method)
  }

  def post(url: String, json: JsObject, rh: String*): HttpCall =
    postWithEntity(new HttpPost(url), url, json, rh: _*)

  def put(url: String, json: JsObject, rh: String*): HttpCall =
    postWithEntity(new HttpPut(url), url, json, rh: _*)

  def patch(url: String, json: JsObject, rh: String*): HttpCall =
    postWithEntity(new HttpPatch(url), url, json, rh: _*)

}
