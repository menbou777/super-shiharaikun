package com.s_shiharai

import ClientBankAccounts
import Clients
import Companies
import Invoices
import Users
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.s_shiharai.plugins.*
import insertInitialData
import io.ktor.serialization.jackson.jackson
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
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
    install(ContentNegotiation) {
        jackson {
            registerModules(JavaTimeModule())
        }
    }
    install(Authentication) {
        jwt("auth-jwt") {
            realm = "ktor sample app"
            verifier(
                JWT
                    .require(Algorithm.HMAC256("secret")) // Replace "secret" with your secret key
                    .withAudience("my-audience")
                    .withIssuer("my-issuer")
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains("my-audience")) JWTPrincipal(credential.payload) else null
            }
        }
    }

}

fun connectToDatabase() {
    // データベース接続の設定
    Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
    transaction {
        SchemaUtils.create(Companies, Users, Clients, ClientBankAccounts, Invoices)
    }
}
