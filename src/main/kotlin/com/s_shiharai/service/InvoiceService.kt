package com.s_shiharai.service

import com.s_shiharai.database.mapper.InvoiceMapper
import org.jetbrains.exposed.sql.Database

interface InvoiceService {
    fun requestInvoice(amount: Int)
    fun getInvoices(): List<Invoice>
}

class InvoiceServiceImpl(database: Database) : InvoiceService {

    val invoiceMapper = InvoiceMapper(database)

    override fun requestInvoice(amount: Int) {

    }

    override fun getInvoices(): List<Invoice> {
        TODO("Not yet implemented")
    }

}

data object Invoice {

}
