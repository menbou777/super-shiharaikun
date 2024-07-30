import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

fun insertInitialData() {
    transaction {
        Companies.insert {
            it[corporateName] = "CompanyA"
            it[representativeName] = "John Doe"
            it[phoneNumber] = "03-1234-5678"
            it[postalCode] = "100-0001"
            it[address] = "Tokyo, Chiyoda 1-1-1"
        }
        Companies.insert {
            it[corporateName] = "CompanyB"
            it[representativeName] = "Jane Smith"
            it[phoneNumber] = "06-1234-5678"
            it[postalCode] = "530-0001"
            it[address] = "Osaka, Kita 1-1-1"
        }

        Users.insert {
            it[companyId] = 1
            it[userName] = "Alice Johnson"
            it[email] = "alice@example.com"
            it[password] = "password1"
        }
        Users.insert {
            it[companyId] = 1
            it[userName] = "Bob Brown"
            it[email] = "bob@example.com"
            it[password] = "password2"
        }
        Users.insert {
            it[companyId] = 2
            it[userName] = "Charlie Davis"
            it[email] = "charlie@example.com"
            it[password] = "password3"
        }

        Clients.insert {
            it[companyId] = 1
            it[corporateName] = "ClientA"
            it[representativeName] = "Eve Black"
            it[phoneNumber] = "03-8765-4321"
            it[postalCode] = "100-0002"
            it[address] = "Tokyo, Chiyoda 2-2-2"
        }
        Clients.insert {
            it[companyId] = 2
            it[corporateName] = "ClientB"
            it[representativeName] = "Frank White"
            it[phoneNumber] = "06-8765-4321"
            it[postalCode] = "530-0002"
            it[address] = "Osaka, Kita 2-2-2"
        }

        ClientBankAccounts.insert {
            it[clientId] = 1
            it[bankName] = "Mizuho Bank"
            it[branchName] = "Tokyo Central"
            it[accountNumber] = "1234567890"
            it[accountName] = "ClientA Account"
        }
        ClientBankAccounts.insert {
            it[clientId] = 2
            it[bankName] = "MUFG Bank"
            it[branchName] = "Osaka Central"
            it[accountNumber] = "0987654321"
            it[accountName] = "ClientB Account"
        }

        Invoices.insert {
            it[companyId] = 1
            it[clientId] = 1
            it[issueDate] = java.time.LocalDate.of(2023, 1, 1)
            it[paymentAmount] = java.math.BigDecimal("100000")
            it[fee] = java.math.BigDecimal("500")
            it[feeRate] = java.math.BigDecimal("0.5")
            it[consumptionTax] = java.math.BigDecimal("10000")
            it[consumptionTaxRate] = java.math.BigDecimal("10")
            it[invoiceAmount] = java.math.BigDecimal("110500")
            it[paymentDueDate] = java.time.LocalDate.of(2023, 1, 31)
            it[status] = InvoiceStatus.Pending
        }
        Invoices.insert {
            it[companyId] = 2
            it[clientId] = 2
            it[issueDate] = java.time.LocalDate.of(2023, 2, 1)
            it[paymentAmount] = java.math.BigDecimal("200000")
            it[fee] = java.math.BigDecimal("1000")
            it[feeRate] = java.math.BigDecimal("0.5")
            it[consumptionTax] = java.math.BigDecimal("20000")
            it[consumptionTaxRate] = java.math.BigDecimal("10")
            it[invoiceAmount] = java.math.BigDecimal("220000")
            it[paymentDueDate] = java.time.LocalDate.of(2023, 2, 28)
            it[status] = InvoiceStatus.Processing
        }
    }
}
