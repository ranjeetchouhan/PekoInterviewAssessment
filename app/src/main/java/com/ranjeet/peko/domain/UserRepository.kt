package com.ranjeet.peko.domain

import com.ranjeet.peko.domain.entity.Repository
import com.ranjeet.peko.domain.entity.User
import com.ranjeet.peko.utils.Result
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun fetchUserList(): Flow<Result<List<User>>>
    suspend fun fetchUser(username: String): Flow<Result<User>>
    suspend fun fetchUserRepositories(username: String): Flow<Result<List<Repository>>>
}