package com.ranjeet.peko.data

import com.ranjeet.peko.domain.entity.Repository
import com.ranjeet.peko.domain.entity.User
import com.ranjeet.peko.utils.Result
import com.ranjeet.peko.utils.Result.*
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UserRepositoryImplTest {

    private val mockRemoteDataSource: RemoteDataSource = mockk()
    private val userRepositoryImpl = UserRepositoryImpl(mockRemoteDataSource)

    @Test
    fun `fetchUserList should return flow of user list`() = runTest {
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
        coEvery { mockRemoteDataSource.fetchUserList() } returns flowOf(Success(userList))

        // When
        val resultFlow = userRepositoryImpl.fetchUserList()

        // Then
        assertEquals(Result.Success(userList), resultFlow.single())
    }

    @Test
    fun `fetchUser should return flow of user`() = runTest{
        // Given
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
        coEvery { mockRemoteDataSource.fetchUser("TestUser") } returns flowOf(com.ranjeet.peko.utils.Result.Success(user))

        // When
        val resultFlow = userRepositoryImpl.fetchUser("TestUser")

        // Then
        assertEquals(Result.Success(user), resultFlow.single())
    }

    @Test
    fun `fetchUserRepositories should return flow of user repositories`() = runTest {
        // Given
        val repositories = listOf(
            Repository("repo1","url1","desc1",1,2,""),
            Repository("repo2","url2","desc2",1,2,"")
        )
        coEvery { mockRemoteDataSource.fetchUserRepositories("TestUser") } returns flowOf(Result.Success(repositories))

        // When
        val resultFlow = userRepositoryImpl.fetchUserRepositories("TestUser")

        // Then
        assertEquals(Result.Success(repositories), resultFlow.single())
    }
}