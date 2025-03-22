package controller

import global.SingletonScope
import service.WiseSayingService

class WiseSayingController {

    private val service: WiseSayingService = SingletonScope.wiseSayingService

    fun start() {
        println("== Wise Saying App ==")

        while (true) {
            println("Command): ")
            val command: String = readlnOrNull()?.trim() ?: ""

            when (command) {
                "등록" -> registerProverb()
                "목록" -> listProverb()
                "빌드" -> buildDataProverb()
                "종료" -> println("Program exited!")
                else -> {
                    when {
                        command.startsWith("검색") -> searchProverb(command)
                        command.startsWith("수정?id=") -> updateProverb(command)
                        command.startsWith("삭제?id=") -> deleteProverb(command)
                        else -> println("Invalid command.")
                    }
                }
            }
        }
    }

    private fun registerProverb() {
        print("명언 : ")
        val proverb: String = readlnOrNull()?.trim() ?: ""

        print("작가 : ")
        val author: String = readlnOrNull()?.trim() ?: ""

        service.registerProverb(proverb, author)
    }

    private fun listProverb() {
        print("페이지 번호 입력: ")
        val page: Int = readlnOrNull()?.trim()?.toInt() ?: 1
        service.listProverbs(page)
    }

    private fun buildDataProverb() {
        service.buildDataToFile()
    }

    private fun searchProverb(command: String) {
//        val parts: MutableList<String> = command.split("\\?").toMutableList()
        val parts: List<String> = command.split("\\?")

        if (parts.size < 2) {
            println("잘못된 검색입니다.")
            return
        }

        val keywords: List<String> = parts[1].split("&")
        if (keywords.size < 2) {
            println("검색 타입 또는 검색어가 누락되었습니다.")
            return
        }

        val keywordType: String = keywords[0].split("=")[1]
        val keyword: String = keywords[1].split("=")[1]
        service.searchProverb(keywordType, keyword)
    }

    private fun updateProverb(command: String) {
        val id: Int = command.split("=")[1].toInt()
        service.updateProverb(id)
    }

    private fun deleteProverb(command: String) {
        val id: Int = command.split("=")[1].toInt()
        service.deleteProverb(id)
    }
}
