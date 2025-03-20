package util

import java.io.IOException

class ExceptionHandler {
    companion object {
        fun handleIOException(e: IOException, message: String) {
            println("$message : ${e.message}")
        }
    }
}