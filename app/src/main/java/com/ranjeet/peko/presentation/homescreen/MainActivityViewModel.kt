package com.ranjeet.peko.presentation.homescreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranjeet.peko.domain.FetchUserListUseCase
import com.ranjeet.peko.domain.FetchUserUseCase
import com.ranjeet.peko.domain.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.ranjeet.peko.utils.Result
import retrofit2.HttpException

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val fetchUserListUseCase: FetchUserListUseCase,
    private val fetchUserUseCase: FetchUserUseCase,
) :
    ViewModel() {

    private val _userList = MutableStateFlow<List<User>?>(null)
    val userList: StateFlow<List<User>?> = _userList

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun fetchUserList() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null // Reset error message
            fetchUserListUseCase().collect { result ->
                when (result) {
                    is Result.Success -> _userList.value = result.data
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

    fun searchUser(username: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null // Reset error message
            fetchUserUseCase(username).collect { result ->
                when (result) {
                    is Result.Success -> _user.value = result.data
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