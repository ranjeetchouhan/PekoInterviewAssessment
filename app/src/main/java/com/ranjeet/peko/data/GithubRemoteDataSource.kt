package com.ranjeet.peko.data

import android.util.Log
import com.ranjeet.peko.domain.entity.Repository
import com.ranjeet.peko.domain.entity.User
import com.ranjeet.peko.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class GithubRemoteDataSource(private var githubApiService: GitHubApiService) : RemoteDataSource {

    override suspend fun fetchUserList(): Flow<Result<List<User>>>  = flow {
        try {
            val userList = githubApiService.fetchUserList(DEFAULT_PER_PAGE)
            emit(Result.Success(userList))
        } catch (e: HttpException) {
            emit(Result.Error(e))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun fetchUser(username: String): Flow<Result<User>> = flow {
        try {
            val user = githubApiService.fetchUser(username)
            emit(Result.Success(user))
        } catch (e: HttpException) {
            emit(Result.Error(e))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun fetchUserRepositories(username: String): Flow<Result<List<Repository>>> = flow {
        try {
            val repository = githubApiService.fetchUserRepositories(username)
            emit(Result.Success(repository))
        } catch (e: HttpException) {
            emit(Result.Error(e))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
    companion object {
        private const val DEFAULT_PER_PAGE = 100 // Default value for per_page parameter
    }
}

interface GitHubApiService {
    @GET("users")
    suspend fun fetchUserList(@Query("per_page") per_page: Int): List<User>

    @GET("users/{username}")
    suspend fun fetchUser(@Path("username") username: String): User

    @GET("users/{username}/repos")
    suspend fun fetchUserRepositories(@Path("username") username: String): List<Repository>
}