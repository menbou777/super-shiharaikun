package com.s_shiharai.database.mapper

import Users
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class UserMapper(private val database: Database) {
    init {
        transaction(database) {
            SchemaUtils.create(Users)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun get(email: String, password: String): UserDto {
        return dbQuery {
            Users.select((Users.email eq email) and (Users.password eq password))
                .map {
                    UserDto(
                        it[Users.companyId].value,
                        it[Users.userName],
                        it[Users.email]
                    )

                }.first()
        }
    }

    data class UserDto(
        val companyId: Int,
        val userName: String,
        val email: String,
    )

}
