package com.s_shiharai

import com.s_shiharai.plugins.*
import com.s_shiharai.service.Invoice
import com.s_shiharai.service.InvoiceService
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.mockk
import java.math.BigDecimal
import kotlin.test.*


class ApplicationTest {

    @Test
    fun testGetInvoice() = testApplication {


        application {
            configureRouting()
        }
        client.get("/invoice").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun testPostInvoice() = testApplication {


        var amount = BigDecimal(9999)

        application {
            configureRouting()
        }


        client.post("/invoice?amount=$amount").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }
}
