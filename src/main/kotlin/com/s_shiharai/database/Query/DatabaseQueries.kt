package com.s_shiharai.database.Query

import ClientBankAccounts
import Clients
import Companies
import InvoiceStatus
import Invoices
import Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal
import java.time.LocalDate

fun createCompany(
    corporateName: String,
    representativeName: String,
    phoneNumber: String?,
    postalCode: String?,
    address: String?
) {
    transaction {
        Companies.insert {
            it[Companies.corporateName] = corporateName
            it[Companies.representativeName] = representativeName
            it[Companies.phoneNumber] = phoneNumber
            it[Companies.postalCode] = postalCode
            it[Companies.address] = address
        }
    }
}

fun getCompanies(): List<ResultRow> {
    return transaction {
        Companies.selectAll().toList()
    }
}

fun createUser(companyId: Int, userName: String, email: String, password: String) {
    transaction {
        Users.insert {
            it[Users.companyId] = companyId
            it[Users.userName] = userName
            it[Users.email] = email
            it[Users.password] = password
        }
    }
}

fun getUsers(): List<ResultRow> {
    return transaction {
        Users.selectAll().toList()
    }
}

fun createClient(
    companyId: Int,
    corporateName: String,
    representativeName: String,
    phoneNumber: String?,
    postalCode: String?,
    address: String?
) {
    transaction {
        Clients.insert {
            it[Clients.companyId] = companyId
            it[Clients.corporateName] = corporateName
            it[Clients.representativeName] = representativeName
            it[Clients.phoneNumber] = phoneNumber
            it[Clients.postalCode] = postalCode
            it[Clients.address] = address
        }
    }
}

fun getClients(): List<ResultRow> {
    return transaction {
        Clients.selectAll().toList()
    }
}

fun createClientBankAccount(
    clientId: Int,
    bankName: String,
    branchName: String,
    accountNumber: String,
    accountName: String
) {
    transaction {
        ClientBankAccounts.insert {
            it[ClientBankAccounts.clientId] = clientId
            it[ClientBankAccounts.bankName] = bankName
            it[ClientBankAccounts.branchName] = branchName
            it[ClientBankAccounts.accountNumber] = accountNumber
            it[ClientBankAccounts.accountName] = accountName
        }
    }
}

fun getClientBankAccounts(): List<ResultRow> {
    return transaction {
        ClientBankAccounts.selectAll().toList()
    }
}

fun createInvoice(
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
    transaction {
        Invoices.insert {
            it[Invoices.companyId] = companyId
            it[Invoices.clientId] = clientId
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

fun getInvoices(): List<ResultRow> {
    return transaction {
        Invoices.selectAll().toList()
    }
}

fun updateInvoiceStatus(invoiceId: Int, newStatus: InvoiceStatus) {
    transaction {
        Invoices.update({ Invoices.id eq invoiceId }) {
            it[status] = newStatus
        }
    }
}

