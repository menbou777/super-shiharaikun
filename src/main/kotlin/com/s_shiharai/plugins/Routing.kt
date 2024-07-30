package com.s_shiharai.plugins

import com.s_shiharai.service.InvoiceServiceImpl
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database
import java.time.LocalDate


fun Application.configureRouting() {

    val database = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;",
        driver = "org.h2.Driver",
    )

    val invoiceService = InvoiceServiceImpl(database)
    routing {
        get("/invoice") {
            val from = LocalDate.parse(call.parameters["from"])
            val to = LocalDate.parse(call.parameters["to"])

            val message = invoiceService.getInvoices(from, to)
            call.respond(HttpStatusCode.OK, message.toList())

        }
        post("/invoice") {

            val invoice = invoiceService.requestInvoice(
                call.parameters["amount"]?.toBigDecimal() ?: throw IllegalArgumentException("Invalid Amount")
            )
            call.respond(HttpStatusCode.OK, invoice)
        }
    }
}
