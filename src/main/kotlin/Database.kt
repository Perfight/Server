import com.google.gson.Gson
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Date

class Entities{
    object Tables: IntIdTable("tables"){
        val sits = integer("sits")
    }

    object Occupations: IntIdTable("occupation"){
        val date = date("occupation_date")
        val orders = integer("orders")
        val tableID = integer("tableid")
    }

    object Accounts: IntIdTable("accounts"){
        val login = varchar("login", 100)
        val password = varchar("password", 100)
        val numberPhone = varchar("numberPhone", 100)
        val isAdministrator = bool("isAdministrator").default(false)
    }
}

class ResponseData{
    data class Table(val id: Int, val sits: Int, )
    data class Occupation(val date: String, val orders: Int, val tableID: Int)
}


data class OccupationArguments(val date: String, val tableID: Int)
data class RegisterArguments(val login: String, val password: String, val numberPhone: String)

class DatabaseController {



    fun connect(): Database {
        val password = "ak4usudavirog".reversed()
        val database = Database.connect(
            url = "jdbc:postgresql://localhost:5432/maxim_bar",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = password
        )
        LoggerMy.log("connect to database", this.javaClass.name)
        return database
    }

    fun register(arguments: String){
        val account = Gson().fromJson<RegisterArguments>(arguments, RegisterArguments::class.java)
        transaction {
            Entities.Accounts.insertAndGetId {
                it[login] = account.login
                it[numberPhone] = account.numberPhone
                it[password] = account.password
            }
        }
    }

    fun login(arguments: String){
        val account = Gson().fromJson<RegisterArguments>(arguments, RegisterArguments::class.java)
        transaction {
            Entities.Accounts.insertAndGetId {
                it[login] = account.login
                it[numberPhone] = account.numberPhone
                it[password] = account.password
            }
        }
    }


    fun getOccupation(date: String): List<ResponseData.Occupation> {
        return transaction {
            val response = listOf<ResponseData.Occupation>()
            val query = Entities.Occupations.selectAll().filter {
                date == it[Entities.Occupations.date].toString()
            }
            return@transaction response

        }
    }

    fun setOccupation(arguments: String){
        val arguments = Gson().fromJson<OccupationArguments>(arguments, OccupationArguments::class.java)
        transaction {
            Entities.Occupations.insertAndGetId {
                it[orders] = 5
                it[tableID] = arguments.tableID
                it[date] = LocalDate.parse(arguments.date)
            }
        }
    }

    fun getTables(): String? {
        return transaction {
            val query = Entities.Tables.selectAll()
            val returnData = emptyList<ResponseData.Table>().toMutableList()
            query.forEach {
                val id = it[Entities.Tables.id]
                val sits = it[Entities.Tables.sits]
                val result = "table number $id have $sits sits"
                LoggerMy.log(result, this.javaClass.name)
                returnData += ResponseData.Table(id.value, sits)
            }

            return@transaction Gson().toJson(returnData)
        }
    }

}