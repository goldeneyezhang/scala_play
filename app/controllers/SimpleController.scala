package controllers
import play.api.http.HttpEntity
import javax.inject._
import play.api._
import play.api.mvc._
import akka.util.ByteString

class SimpleController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
   implicit val myCustomCharset = Codec.javaSupported("iso-8859-1")
    def index = Action {
    Result(
        header = ResponseHeader(200, Map.empty),
        body = HttpEntity.Strict(ByteString("Hello world!"), Some("text/plain"))
        )
    }
    def simpleResult = {
        val ok = Ok("Hello world!")
        val notFound = NotFound
        val pageNotFound = NotFound(<h1>Page not found</h1>)
        // val badRequest = BadRequest(views.html.form(formWithErrors))
        val oops = InternalServerError("Oops")
        val anyStatus = Status(488)("Strange response type")
    }
    def index2 = Action {
       val textResult =  Ok("Hello World!")
       val xmlResult = Ok(<message>Hello World!</message>)
       val htmlResult = Ok(<h1>Hello World!</h1>).as("text/html")
       val htmlResult2 = Ok(<h1>Hello World!</h1>).as(HTML)
       val result = Ok("Hello World!").withHeaders(
  CACHE_CONTROL -> "max-age=3600",
  ETAG -> "xx")
        val result2 = Ok("Hello world")
        .withCookies(Cookie("theme", "blue"))
        .bakeCookies()

       result2
    }
    def index3 = Action { implicit request =>
            request.session.get("connected").map {user =>
                Ok("Hello " + user)
            }.getOrElse {
                Unauthorized("Oops, you are not connected")
            }
    }
    def save3 = Action {
        Redirect("/index3").withSession(
            "connected" -> "YYYY")
    }

    def index4 = Action { implicit request =>
        Ok {
            request.flash.get("success").getOrElse("Welcome!")
            }
    }

    def save4 = Action {
        Redirect("/index4").flashing(
            "success" -> "The item has been created")
        }
}