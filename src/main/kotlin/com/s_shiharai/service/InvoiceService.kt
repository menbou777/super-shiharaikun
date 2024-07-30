package com.s_shiharai.service

import com.s_shiharai.database.mapper.InvoiceMapper
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Database
import java.math.BigDecimal
import java.time.LocalDate

interface InvoiceService {
    suspend fun requestInvoice(amount: BigDecimal)
    suspend fun getInvoices(): List<Invoice>
}

class InvoiceServiceImpl(database: Database) : InvoiceService {

    val invoiceMapper = InvoiceMapper(database)

    //TODO configuration もしくはDBでの設定を行う
    private var feeRate = BigDecimal(0.04)
    private val taxRate = BigDecimal(1.1)


    override suspend fun requestInvoice(payment: BigDecimal) {

        //TODO Login user の情報をcontext から取得する
        val currentCompanyId = 1
        val currentClientId = 1

        val fee = payment.multiply(feeRate)
        val tax = fee.multiply(taxRate)
        val invoiceAmount = payment + tax

        invoiceMapper.create(
            InvoiceMapper.InvoiceDto(
                companyId = currentCompanyId,
                clientId = currentClientId,
                LocalDate.now(),
                paymentAmount = payment,
                fee = fee,
                feeRate = feeRate,
                consumptionTax = tax,
                consumptionTaxRate = taxRate,
                invoiceAmount = invoiceAmount,
                paymentDueDate = LocalDate.now().plusDays(30),
                status = InvoiceStatus.Pending
            )
        )
    }

    override suspend fun getInvoices(): List<Invoice> {
        return invoiceMapper.get().map {
            Invoice(
                it.companyId,
                it.clientId,
                it.issueDate,
                it.paymentAmount,
                it.fee,
                it.feeRate,
                it.consumptionTax,
                it.consumptionTaxRate,
                it.invoiceAmount,
                it.paymentDueDate,
                it.status.name
            )
        }
    }

}

data class Invoice(
    val companyId: Int,
    val clientId: Int,
    val issueDate: LocalDate,
    val paymentAmount: BigDecimal,
    val fee: BigDecimal?,
    val feeRate: BigDecimal?,
    val consumptionTax: BigDecimal?,
    val consumptionTaxRate: BigDecimal?,
    val invoiceAmount: BigDecimal,
    val paymentDueDate: LocalDate?,
    val status: String
)
