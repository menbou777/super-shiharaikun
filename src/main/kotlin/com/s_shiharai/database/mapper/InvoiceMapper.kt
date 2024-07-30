package com.s_shiharai.database.mapper

import InvoiceStatus
import Invoices
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.bitwiseOr
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
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
        invoice: InvoiceDto
    ): InvoiceDto {
        dbQuery {
            Invoices.insert {
                it[Invoices.companyId] = invoice.companyId
                it[Invoices.clientId] = invoice.clientId
                it[Invoices.issueDate] = invoice.issueDate
                it[Invoices.paymentAmount] = invoice.paymentAmount
                it[Invoices.fee] = invoice.fee
                it[Invoices.feeRate] = invoice.feeRate
                it[Invoices.consumptionTax] = invoice.consumptionTax
                it[Invoices.consumptionTaxRate] = invoice.consumptionTaxRate
                it[Invoices.invoiceAmount] = invoice.invoiceAmount
                it[Invoices.paymentDueDate] = invoice.paymentDueDate
                it[Invoices.status] = invoice.status
            }
        }
        return invoice
    }

    suspend fun get(from: LocalDate, to: LocalDate): List<InvoiceDto> {
        return dbQuery {
            Invoices.select(Invoices.paymentDueDate.between(from, to)).map {
                InvoiceDto(
                    it[Invoices.companyId].value,
                    it[Invoices.clientId].value,
                    it[Invoices.issueDate],
                    it[Invoices.paymentAmount],
                    it[Invoices.fee],
                    it[Invoices.feeRate],
                    it[Invoices.consumptionTax],
                    it[Invoices.consumptionTaxRate],
                    it[Invoices.invoiceAmount],
                    it[Invoices.paymentDueDate],
                    it[Invoices.status]
                )
            }
        }
    }

    data class InvoiceDto(
        val companyId: Int,
        val clientId: Int,
        val issueDate: LocalDate,
        val paymentAmount: BigDecimal,
        val fee: BigDecimal,
        val feeRate: BigDecimal,
        val consumptionTax: BigDecimal,
        val consumptionTaxRate: BigDecimal,
        val invoiceAmount: BigDecimal,
        val paymentDueDate: LocalDate,
        val status: InvoiceStatus
    )
}

