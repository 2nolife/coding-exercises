package services

import support.UnitSpec

class MessageServiceSpec extends UnitSpec {

  private val messageService = new MessageService

  "MessageService" - {

    "return a response that indicates message received was printed" in {

      val response = messageService.printToTerminal("Test1")

      response shouldBe s"Printed 'Test1' to terminal"
    }

  }
}
