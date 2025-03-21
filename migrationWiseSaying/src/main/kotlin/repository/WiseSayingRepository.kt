package repository

import com.google.gson.Gson
import proverb.Proverb
import util.ExceptionHandler
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

class WiseSayingRepository {

    // static
    companion object {
        private val DB = "src/main/kotlin/db"
        private val LAST_ID_FILE = DB + "lastId.txt"
        private val DATA_PROVERB_FILE = DB + "data.json"
    }

    fun readLastId(): Int {
        val file = File(LAST_ID_FILE)
        if (!file.exists()) {
            try {
                when {
                    file.createNewFile() -> println("파일이 생성되었습니다: ${file.absolutePath}")
                    else -> println("파일 생성에 실패했습니다.")
                }
                return 1
            } catch (e: IOException) {
                ExceptionHandler.handleIOException(e, "lastId.txt 파일 생성 중 오류 발생")
            }
        } else {
            try {
                BufferedReader(FileReader(file)).use { br -> // use 는 Closeable 리소스를 자동으로 닫아준다.,  Closeable 리소스란 입출력 스트림이나 소켓 등을 말한다.
                    return br.readLine().toInt() // .toInt() 는 문자열을 정수로 변환한다. = Integer.parseInt (in Java)
                }
            } catch (e: IOException) {
                ExceptionHandler.handleIOException(e, "lastId.txt 파일 읽기 중 오류 발생")
            }
        }
        return 1
    }

    fun saveProverb(proverbObject: Proverb) {
        val gson: Gson = Gson()
        var json: String = gson.toJson(proverbObject)

        try {
            /*val writer: FileWriter = FileWriter(DB + proverbObject.id +  ".json")
            writer.write(json)*/
            FileWriter(DB + "${proverbObject.id}.json").use { writer ->
                writer.write(json)
            }
        } catch (e: IOException) {
            ExceptionHandler.handleIOException(e, ".json 파일 저장 중 오류 발생")
        }
    }

    fun saveLastId(id: Int) {
        try {
            BufferedWriter(FileWriter(LAST_ID_FILE)).use {
                it.write(id.toString())
            }
        } catch (e: IOException) {
            ExceptionHandler.handleIOException(e, "lastId.txt  파일 쓰기 중 오류 발생")
        }
    }

    fun saveDataToFile(proverbList: MutableList<Proverb>) {
        val gson: Gson = Gson()
        try {
            BufferedWriter(FileWriter(DATA_PROVERB_FILE)).use { bw ->
                gson.toJson(proverbList, bw)
                println("빌드 성공")
            }
        } catch (e: IOException) {
            ExceptionHandler.handleIOException(e, "data.json 파일 쓰기 중 오류 발생")
        }
    }

    fun deleteProverbFile(id: Int) {
        val file = File("$DB$id.json")
        when {
            !file.exists() -> println("$id.json 파일이 존재하지 않습니다.")
            file.delete() -> println("$id.json 파일이 성공적으로 삭제되었습니다.")
            else -> println("$id.json 파일 삭제에 실패하였습니다.")
        }
    }
}