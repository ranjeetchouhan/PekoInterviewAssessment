package com.ranjeet.peko.data

import android.util.Log
import com.ranjeet.peko.domain.UserRepository
import com.ranjeet.peko.domain.entity.Repository
import com.ranjeet.peko.domain.entity.User
import com.ranjeet.peko.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource): UserRepository {
    override suspend fun fetchUserList(): Flow<Result<List<User>>> {
        return remoteDataSource.fetchUserList()
    }

    override suspend fun fetchUser(username: String): Flow<Result<User>> {
        return remoteDataSource.fetchUser(username)
    }

    override suspend fun fetchUserRepositories(username: String): Flow<Result<List<Repository>>> {
        return remoteDataSource.fetchUserRepositories(username)
    }
}