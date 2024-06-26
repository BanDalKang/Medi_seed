package com.mediseed.mediseed.ui.sprout.di

import android.content.Context
import com.mediseed.mediseed.ui.sprout.SproutRepository
import com.mediseed.mediseed.ui.sprout.SproutViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) //Application Context Inject
object RepositoryModule {
    @Provides
    @Singleton
    fun provideSproutRepository(@ApplicationContext context: Context): SproutRepository {
        return SproutRepository(context)
    }
}

