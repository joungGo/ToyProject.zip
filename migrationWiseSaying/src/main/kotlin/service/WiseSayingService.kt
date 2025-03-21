package service

import proverb.Proverb
import repository.WiseSayingRepository
import kotlin.math.ceil
import kotlin.math.min

class WiseSayingService {
    private val repository = WiseSayingRepository()
    private val proverbList: MutableList<Proverb> = mutableListOf()

    companion object {
        private val LIST_PER_PAGE: Int = 5
    }

    fun registerProverb(proverb: String?, author: String?) {
        val id: Int = repository.readLastId()
        val proverbObject: Proverb = Proverb(id, proverb, author)
        proverbList.add(proverbObject)
        repository.saveProverb(proverbObject)
        repository.saveLastId(id + 1)
        println("${id}번 명언이 등록되었습니다.")
    }

    fun listProverbs(page: Int) {
        val itemsPerPage: Int = LIST_PER_PAGE
        val totalItems: Int = proverbList.size
        val totalPages: Int = ceil(totalItems.toDouble() / itemsPerPage).toInt()

        if (page < 1 || page > totalPages) {
            println("존재하지 않는 페이지입니다.")
            return
        }

        println("번호 / 작가 / 명언")
        println("------------------")

        val startIndex: Int = (page - 1) * itemsPerPage
        val endIndex: Int = min(startIndex + itemsPerPage, totalItems)

        for (i in startIndex until endIndex) {
            println(proverbList[i].toString())
        }

        println("페이지: [")
        for (i in 1..totalPages) {
            print(if (i == page) " $i " else " $i ")
        }
        println("]")
    }

    fun buildDataToFile() {
        repository.saveDataToFile(proverbList)
    }

    fun searchProverb(keywordType: String, keyword: String) {
        // .forEach vs .filter : 아래의 경우 조건에 맞는 결과를 반환하는 것이므로 filter 를 사용한다.
        val results: List<Proverb> = proverbList.filter {
            when (keywordType) {
//                "id" -> it.id.toString() == keyword
                "author" -> it.author == keyword
                "proverb" -> it.proverb == keyword
                else -> false
            }
        }

        if (results.isEmpty()) {
            println("결과가 없습니다.")
            return
        }

        println("번호 / 작가 / 명언")
        println("--------------------")
        results.forEach {
            println(it.toString())
        }
    }

    fun updateProverb(id: Int) {
        /*for (proverb in proverbList) {
            if (proverb.id == id) {
                print("수정할 명언 : ")
                val newProverb: String? = readlnOrNull()?.trim() ?: ""

                print("수정할 작가 : ")
                val newAuthor: String? = readlnOrNull()?.trim() ?: ""

                proverb.proverb = newProverb
                proverb.author = newAuthor

                repository.saveProverb(proverb)
                println("${id}번 명언이 수정되었습니다.")
                return
            }
        }*/
        val proverb = proverbList.find { it.id == id }
        if (proverb != null) {
            println("명언(기존) : ${proverb.proverb}")
            println("작가(기존) : ${proverb.author}")

            print("명언(수정) : ")
            val newProverb = readlnOrNull()?.trim() ?: ""
            proverb.proverb = newProverb

            print("작가(수정) : ")
            val newAuthor = readLine()?.trim() ?: ""
            proverb.author = newAuthor

            repository.saveProverb(proverb)
        } else {
            println("찾으시는 ${id}의 명언이 없습니다.")
        }
    }

    fun deleteProverb(id: Int) {
        var check: Boolean = false

        proverbList.find { it.id == id }?.let {
            proverbList.remove(it)
            repository.deleteProverbFile(id)
            println("${id}번 명언이 삭제되었습니다.")
            check = true
        }

    }
}