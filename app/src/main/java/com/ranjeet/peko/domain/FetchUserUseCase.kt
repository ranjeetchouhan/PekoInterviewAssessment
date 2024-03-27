package com.ranjeet.peko.domain

import android.util.Log
import com.ranjeet.peko.domain.entity.User
import com.ranjeet.peko.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(username: String): Flow<Result<User>> {
        return userRepository.fetchUser(username = username)
    }
}