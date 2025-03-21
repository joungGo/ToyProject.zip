package app

import controller.WiseSayingController

class App {
    private val controller: WiseSayingController = WiseSayingController()

    fun run() {
        controller.start()
    }
}