package com.ranjeet.peko.di

import com.ranjeet.peko.domain.FetchUserListUseCase
import com.ranjeet.peko.domain.FetchUserRepositoriesUseCase
import com.ranjeet.peko.domain.FetchUserUseCase
import com.ranjeet.peko.domain.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    @ViewModelScoped
    fun provideFetchUserListUseCase(userRepository: UserRepository): FetchUserListUseCase {
        return FetchUserListUseCase(userRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideFetchUserUseCase(userRepository: UserRepository): FetchUserUseCase {
        return FetchUserUseCase(userRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideFetchUserRepositoriesUseCase(userRepository: UserRepository): FetchUserRepositoriesUseCase {
        return FetchUserRepositoriesUseCase(userRepository)
    }
}