import com.google.gson.Gson
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.reflect
import kotlin.reflect.typeOf

class Server(private var port: Int) {
    private fun getServerSocket(): ServerSocket {
        val selectorManager = SelectorManager(Dispatchers.IO)
        val serverSocket = aSocket(selectorManager).tcp().bind("192.168.196.123", port)
        LoggerMy.log("server created", this.javaClass.name)
        return serverSocket
    }


    fun run(){
        val server = getServerSocket()
        LoggerMy.log("Server started", "main")

        runBlocking {
            val db = DatabaseController()
            MyTest().test(db)
            while (true){
                val socket = server.accept()
                MyTest().test(socket)
                launch {
                    val input = socket.openReadChannel()
                    val output = socket.openWriteChannel()
                    val postJSON = input.readUTF8Line()
                    val post = Gson().fromJson<UniversalPost>(postJSON, typeOf<UniversalPost>().javaType)
                    when (post.comand) {
                        "getTables" -> {
                            output.writeStringUtf8(db.getTables()!!)
                        }
                        "getOccupation" -> {
                            val answer = db.getOccupation(post.argument)
                            output.writeStringUtf8(Gson().toJson(answer))
                        }
                        "setOccupation" -> {
                            db.setOccupation(post.argument)
                        }

                        "login" -> {
                            db.login(post.argument)
                        }

                        "register" -> {
                            db.register(post.argument)
                        }
                        else -> {
                            LoggerMy.log("Error in request", "server")
                        }
                    }


                }
            }
        }
    }



}