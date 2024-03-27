package com.ranjeet.peko.domain

import android.util.Log
import com.ranjeet.peko.domain.entity.User
import com.ranjeet.peko.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchUserListUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Flow<Result<List<User>>> {
        return userRepository.fetchUserList()
    }
}