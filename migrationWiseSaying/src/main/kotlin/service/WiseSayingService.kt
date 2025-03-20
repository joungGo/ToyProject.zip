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

}