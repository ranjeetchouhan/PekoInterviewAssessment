package com.ranjeet.peko.domain

import android.util.Log
import com.ranjeet.peko.domain.entity.Repository
import com.ranjeet.peko.domain.entity.User
import com.ranjeet.peko.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchUserRepositoriesUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(username: String): Flow<Result<List<Repository>>> {
        return userRepository.fetchUserRepositories(username = username)
    }
}