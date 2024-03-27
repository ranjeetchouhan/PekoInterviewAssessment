package com.ranjeet.peko.data

import android.util.Log
import com.ranjeet.peko.domain.entity.Repository
import com.ranjeet.peko.domain.entity.User
import com.ranjeet.peko.utils.Result
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
class GithubRemoteDataSourceTest {

    private lateinit var githubApiService: GitHubApiService
    private lateinit var remoteDataSource: GithubRemoteDataSource


    @Before
    fun setup() {
        githubApiService = mockk()
        remoteDataSource = GithubRemoteDataSource(githubApiService)
    }

    @Test
    fun `test fetchUserList success`() = runTest {
        // Given
        val userList = listOf(
            User(
                "Ranjeet Chouhan",
                "ranjeetchouhan",
                "https://avatars.githubusercontent.com/u/29429548?v=4",
                "Android Developer, Optimistic, Reader, Traveller, Listener",
                1,
                2,
                3,
                "Bengaluru",
                "2017-06-14T11:48:14Z"
            )
        )
        coEvery { githubApiService.fetchUserList(100) } returns userList

        // When
        val result = remoteDataSource.fetchUserList().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(Result.Success(userList), result[0])
    }

    @Test
    fun `test fetchUserList failure`() = runTest {
        // Given
        val responseBody: ResponseBody = mockk()
        every { responseBody.contentType() } returns "application/json".toMediaType()
        every { responseBody.contentLength() } returns 0 // Set the content length as needed
        val exception = HttpException(Response.error<User>(404, responseBody))
        coEvery { githubApiService.fetchUserList(100) } throws exception

        // When
        var result = remoteDataSource.fetchUserList().toList()

        // Then
        assertEquals(Result.Error(exception),result[0])
    }

    @Test
    fun `test fetchUser success`() = runTest {
        // Given
        val username = "ranjeetchouhan"
        val user = User(
            "Ranjeet Chouhan",
            "ranjeetchouhan",
            "https://avatars.githubusercontent.com/u/29429548?v=4",
            "Android Developer, Optimistic, Reader, Traveller, Listener",
            1,
            2,
            3,
            "Bengaluru",
            "2017-06-14T11:48:14Z"
        )
        coEvery { githubApiService.fetchUser(username) } returns user

        // When
        val result = remoteDataSource.fetchUser(username).toList()

        // Then
        assertEquals(Result.Success(user), result[0])
    }

    @Test
    fun `test fetchUserRepositories success`() = runTest{
        // Given
        val username = "test_user"
        val repositories = listOf(
            Repository("repo1","url1","desc1",1,2,""),
            Repository("repo2","url2","desc2",1,2,"")
        )
        coEvery {githubApiService.fetchUserRepositories(username)  } returns repositories

        // When
        val result = remoteDataSource.fetchUserRepositories(username).toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(Result.Success(repositories), result[0])
    }
}