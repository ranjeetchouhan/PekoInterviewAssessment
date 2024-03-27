package com.ranjeet.peko.presentation.userprofile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranjeet.peko.domain.FetchUserRepositoriesUseCase
import com.ranjeet.peko.domain.FetchUserUseCase
import com.ranjeet.peko.domain.entity.Repository
import com.ranjeet.peko.domain.entity.User
import com.ranjeet.peko.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val fetchUserRepositoriesUseCase: FetchUserRepositoriesUseCase,
    private val fetchUserUseCase: FetchUserUseCase
) : ViewModel() {

    private val _userRepository = MutableStateFlow<List<Repository>?>(null)
    val userRepository: StateFlow<List<Repository>?> = _userRepository

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun fetchUserRepositories(username: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null // Reset error message
            fetchUserRepositoriesUseCase(username).collect { result ->
                when (result) {
                    is Result.Success -> _userRepository.value = result.data
                    is Result.Error -> {
                        val errorMessage = when (val exception = result.exception) {
                            is HttpException -> {
                                when (exception.code()) {
                                    403 -> "API limit is exhausted. Try again after 60 minutes."
                                    else -> exception.message ?: "An error occurred"
                                }
                            }
                            else -> exception.message ?: "An error occurred"
                        }
                        _errorMessage.value = errorMessage
                    }
                }
                _isLoading.value = false
            }
        }
    }
}