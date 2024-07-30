package com.s_shiharai.plugins

import com.s_shiharai.service.InvoiceServiceImpl
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.logging.Logger
import org.jetbrains.exposed.sql.Database

fun Application.configureRouting() {

    val database = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;",
        driver = "org.h2.Driver",
    )

    val invoiceService = InvoiceServiceImpl(database)
    routing {
        get("/invoice") {
            val message = invoiceService.getInvoices()
            print(message)
            call.respond(HttpStatusCode.OK, message.toList().toString())

        }
        post("/invoice") {
            invoiceService.requestInvoice(
                call.parameters["amount"]?.toBigDecimal() ?: throw IllegalArgumentException("Invalid Amount")
            )
            call.respond(HttpStatusCode.OK)
        }
    }
}
