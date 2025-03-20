package controller

import repository.WiseSayingRepository
import service.WiseSayingService
import java.util.*

class WiseSayingController {

    private val service: WiseSayingService
    private val sc: Scanner

    init {
        val repository = WiseSayingRepository()
        service = WiseSayingService()
        sc = Scanner(System.`in`)
    }

    fun start() {
        println("== Wise Saying App ==")

        while (true) {
            println("Command): ")
            var command: String? = sc.nextLine().trim()

            when (command) {
                "register" -> registerProverb()
                "list" ->  listProverb()
                "build" -> buildDataProverb()
                "exit" -> println("Program exited!")
                else -> {
                    if (command != null) {
                        when {
                            command.startsWith("search") -> searchProverb(command)
                            command.startsWith("update?id=") -> updateProverb(command)
                            command.startsWith("delete?id=") -> deleteProverb(command)
                            else -> println("Invalid command.")
                        }
                    }
                }
            }
        }
    }

    private fun registerProverb() {
        print("명언 : ")
        val proverb: String? = sc.nextLine()?.trim()

        print("작가 : ")
        val author:  String? = sc.nextLine()?.trim()

        service.registerProverb(proverb, author)
    }

    private fun listProverb() {
        print("페이지 번호 입력: ")
        val page: Int = sc.nextLine().trim().toInt()
        service.listProverbs(page)
    }


}