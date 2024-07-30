package com.s_shiharai

import ClientBankAccounts
import Clients
import Companies
import Invoices
import Users
import com.s_shiharai.plugins.*
import insertInitialData
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


fun main() {
    connectToDatabase()
    insertInitialData()
    embeddedServer(Netty, port = 8081, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}


fun Application.module() {
    configureRouting()
}

fun connectToDatabase() {
    // データベース接続の設定
    Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
    transaction {
        SchemaUtils.create(Companies, Users, Clients, ClientBankAccounts, Invoices)
    }
}
