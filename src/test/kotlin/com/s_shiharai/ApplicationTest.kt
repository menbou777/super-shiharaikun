package com.s_shiharai

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.s_shiharai.plugins.*
import com.s_shiharai.service.Invoice
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.math.exp
import kotlin.test.*


class ApplicationTest {

    @Test
    fun testGetInvoice() = testApplication {
        connectToDatabase()
        testGetInsertInitialData()

        application {
            configureRouting()
            install(ContentNegotiation) {
                jackson {
                    registerModules(JavaTimeModule())
                }
            }
        }
        client.get("/invoice?from=2024-06-01&to=2024-07-30").apply {
            val mapper = jacksonObjectMapper()
            mapper.registerModules(JavaTimeModule())
            val expected = mapper.readValue<List<Invoice>>(bodyAsText())
            assertEquals(HttpStatusCode.OK, status)
            assertTrue { expected.get(0).paymentDueDate.isAfter(LocalDate.of(2024, 6, 1)) }

        }
    }

    @Test
    fun testPostInvoice() = testApplication {
        connectToDatabase()
        testPostInsertInitialData()

        application {
            configureRouting()
            install(ContentNegotiation) {
                jackson {
                    registerModules(JavaTimeModule())
                }
            }
        }

        var amount = BigDecimal(9999)
        client.post("/invoice?amount=$amount").apply {
            val mapper = jacksonObjectMapper()
            mapper.registerModules(JavaTimeModule())
            val repsonse = mapper.readValue<Invoice>(bodyAsText())
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(repsonse.paymentAmount, amount)
        }
    }

    private fun testGetInsertInitialData() {
        transaction {

            Companies.insert {
                it[id] = 999
                it[corporateName] = "CompanyA"
                it[representativeName] = "John Doe"
                it[phoneNumber] = "03-1234-5678"
                it[postalCode] = "100-0001"
                it[address] = "Tokyo, Chiyoda 1-1-1"
            }

            Clients.insert {
                it[id] = 123
                it[companyId] = 999
                it[corporateName] = "ClientA"
                it[representativeName] = "Eve Black"
                it[phoneNumber] = "03-8765-4321"
                it[postalCode] = "100-0002"
                it[address] = "Tokyo, Chiyoda 2-2-2"
            }


            Invoices.insert {
                it[companyId] = 999
                it[clientId] = 123
                it[issueDate] = java.time.LocalDate.of(2023, 1, 1)
                it[paymentAmount] = java.math.BigDecimal("100000")
                it[fee] = java.math.BigDecimal("500")
                it[feeRate] = java.math.BigDecimal("0.5")
                it[consumptionTax] = java.math.BigDecimal("10000")
                it[consumptionTaxRate] = java.math.BigDecimal("10")
                it[invoiceAmount] = java.math.BigDecimal("110500")
                it[paymentDueDate] = java.time.LocalDate.of(2024, 6, 15)
                it[status] = InvoiceStatus.Pending
            }
        }
    }

    private fun testPostInsertInitialData() {
        transaction {

            Companies.insert {
                it[id] = 1
                it[corporateName] = "CompanyA"
                it[representativeName] = "John Doe"
                it[phoneNumber] = "03-1234-5678"
                it[postalCode] = "100-0001"
                it[address] = "Tokyo, Chiyoda 1-1-1"
            }

            Clients.insert {
                it[id] = 1
                it[companyId] = 1
                it[corporateName] = "ClientA"
                it[representativeName] = "Eve Black"
                it[phoneNumber] = "03-8765-4321"
                it[postalCode] = "100-0002"
                it[address] = "Tokyo, Chiyoda 2-2-2"
            }
        }
    }
}

