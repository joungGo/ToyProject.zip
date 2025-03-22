package global

import repository.WiseSayingRepository
import service.WiseSayingService

object SingletonScope {
    val wiseSayingRepository: WiseSayingRepository = WiseSayingRepository()
    val wiseSayingService = WiseSayingService()
}