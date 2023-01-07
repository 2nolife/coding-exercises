package views

import forms.Forms._
import forms.VehicleData
import org.scalatestplus.play.PlaySpec
import play.api.test.Helpers._

class TemplatesSpec extends PlaySpec {

  "templates" should {

    "render index template" in {
      val html = views.html.index(title = "ABC")
      contentAsString(html) must include ("<title>ABC</title>")
      contentAsString(html) must include ("<h1>Get vehicle information from DVLA</h1>")
    }

    "render custom 404 template" in {
      val html = views.html.error404(title = "ABC")
      contentAsString(html) must include ("<title>ABC</title>")
      contentAsString(html) must include ("<h1>Page not found</h1>")
    }

    "render enquity template" in {
      val html = views.html.enquiry(title = "ABC", form = vehicleForm.fill(VehicleData("AE64 FOO", "AUDI", Some(""))))
      contentAsString(html) must include ("<title>ABC</title>")
      contentAsString(html) must include ("""<input name="registration" type="text" maxlength="8" id="registration" class="input-large input-upper" value='AE64 FOO'/>""")
      contentAsString(html) must include ("""<input name="make" type="text" maxlength="100" id="make" class="input-large input-upper" value='AUDI'/>""")
    }

    //todo rest of the templates go here

  }
}
