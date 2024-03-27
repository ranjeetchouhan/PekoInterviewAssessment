package com.ranjeet.peko.di

import com.ranjeet.peko.data.GitHubApiService
import com.ranjeet.peko.data.GithubRemoteDataSource
import com.ranjeet.peko.data.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(retrofit: Retrofit): RemoteDataSource {
        return GithubRemoteDataSource(retrofit.create(GitHubApiService::class.java))
    }
}