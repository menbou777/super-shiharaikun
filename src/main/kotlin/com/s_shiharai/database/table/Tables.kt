import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date

object Companies : IntIdTable() {
    val corporateName = varchar("corporateName", 255)
    val representativeName = varchar("representativeName", 255)
    val phoneNumber = varchar("phoneNumber", 20).nullable()
    val postalCode = varchar("postalCode", 10).nullable()
    val address = varchar("address", 255).nullable()
}

object Users : IntIdTable() {
    val companyId = reference("companyId", Companies)
    val userName = varchar("userName", 255)
    val email = varchar("email", 255).uniqueIndex()
    val password = varchar("password", 255)
}

object Clients : IntIdTable() {
    val companyId = reference("companyId", Companies)
    val corporateName = varchar("corporateName", 255)
    val representativeName = varchar("representativeName", 255)
    val phoneNumber = varchar("phoneNumber", 20).nullable()
    val postalCode = varchar("postalCode", 10).nullable()
    val address = varchar("address", 255).nullable()
}

object ClientBankAccounts : IntIdTable() {
    val clientId = reference("clientId", Clients)
    val bankName = varchar("bankName", 255)
    val branchName = varchar("branchName", 255)
    val accountNumber = varchar("accountNumber", 20)
    val accountName = varchar("accountName", 255)
}

object Invoices : IntIdTable() {
    val companyId = reference("companyId", Companies)
    val clientId = reference("clientId", Clients)
    val issueDate = date("issueDate")
    val paymentAmount = decimal("paymentAmount", 10, 2)
    val fee = decimal("fee", 10, 2).nullable()
    val feeRate = decimal("feeRate", 5, 2).nullable()
    val consumptionTax = decimal("consumptionTax", 10, 2).nullable()
    val consumptionTaxRate = decimal("consumptionTaxRate", 5, 2).nullable()
    val invoiceAmount = decimal("invoiceAmount", 10, 2)
    val paymentDueDate = date("paymentDueDate").nullable()
    val status = enumerationByName("status", 10, InvoiceStatus::class)
}

enum class InvoiceStatus {
    Pending, Processing, Paid, Error
}
