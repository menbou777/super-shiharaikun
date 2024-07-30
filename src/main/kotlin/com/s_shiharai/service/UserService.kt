package com.s_shiharai.service

import com.s_shiharai.database.mapper.UserMapper
import org.jetbrains.exposed.sql.Database


interface UserService {
    suspend fun login(username: String, password: String): Boolean
}

class UserServiceImple(database: Database) : UserService {
    val userMapper = UserMapper(database)

    override suspend fun login(username: String, password: String): Boolean {

    }
}
