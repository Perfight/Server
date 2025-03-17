import io.ktor.network.sockets.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class MyTest {
    private val name = "TEST"
    fun test(module: DatabaseController){
        transaction {
            if (SchemaUtils.listTables().isNotEmpty()){
                LoggerMy.log("Database work correctly", name)
            }
            else{
                LoggerMy.log("Database does'nt work correctly", name)
            }
        }
    }

    suspend fun test(module: Socket){
        val input = module.openReadChannel()
        try {
            input.readBoolean()
            LoggerMy.log("Connection with ${module.remoteAddress} was succesfull", name)
        }
        catch (exception: Exception){
            LoggerMy.log(exception.message!!, name)
        }
    }
}