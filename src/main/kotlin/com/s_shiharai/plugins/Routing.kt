package com.s_shiharai.plugins

import com.s_shiharai.service.InvoiceServiceImpl
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database

fun Application.configureRouting() {

    val database = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "user",
        driver = "org.h2.Driver",
        password = "password"
    )

    val invoiceService = InvoiceServiceImpl(database)
    routing {
        get("/invoice") {
            invoiceService.getInvoices()
            call.respond("OK")
        }
        post("/invoice") {
            invoiceService.requestInvoice(
                call.parameters["amount"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            )
            call.respond(HttpStatusCode.OK)
        }
    }
}
