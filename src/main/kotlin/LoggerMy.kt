import io.ktor.util.date.*
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class LoggerMy {
    companion object{
        fun log(text: String, className: String){
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            val instant = Instant.ofEpochMilli(getTimeMillis())
            val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

            val time = formatter.format(date)

            val outputText = """
                $time: from $className $text
            """.trimIndent()

            if(!File("log.txt").exists()){
                File("log.txt").createNewFile()
            }
            File("log.txt").appendText(outputText)
            println("$time: from $className $text")
        }
    }
}