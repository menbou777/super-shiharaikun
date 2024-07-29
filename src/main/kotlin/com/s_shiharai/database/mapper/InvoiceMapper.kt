package com.s_shiharai.database.mapper

import InvoiceStatus
import Invoices
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import java.math.BigDecimal
import java.time.LocalDate

class InvoiceMapper(private val database: Database) {

    init {
        transaction(database) {
            SchemaUtils.create(Invoices)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(
        companyId: Int,
        clientId: Int,
        issueDate: LocalDate,
        paymentAmount: BigDecimal,
        fee: BigDecimal?,
        feeRate: BigDecimal?,
        consumptionTax: BigDecimal?,
        consumptionTaxRate: BigDecimal?,
        invoiceAmount: BigDecimal,
        paymentDueDate: LocalDate?,
        status: InvoiceStatus
    ) {
        dbQuery {
            Invoices.insert {
                it[Invoices.company] = companyId
                it[Invoices.client] = clientId
                it[Invoices.issueDate] = issueDate
                it[Invoices.paymentAmount] = paymentAmount
                it[Invoices.fee] = fee
                it[Invoices.feeRate] = feeRate
                it[Invoices.consumptionTax] = consumptionTax
                it[Invoices.consumptionTaxRate] = consumptionTaxRate
                it[Invoices.invoiceAmount] = invoiceAmount
                it[Invoices.paymentDueDate] = paymentDueDate
                it[Invoices.status] = status
            }
        }
    }

}

