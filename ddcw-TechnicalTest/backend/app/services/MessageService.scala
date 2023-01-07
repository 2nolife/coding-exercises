package services

class MessageService {
  def printToTerminal(message: String): String = {
    println(message)
    s"Printed '$message' to terminal"
  }
}
