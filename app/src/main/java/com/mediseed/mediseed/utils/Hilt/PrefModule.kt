package com.mediseed.mediseed.utils.Hilt

import android.content.Context
import android.content.SharedPreferences
import com.mediseed.mediseed.utils.Const.Companion.PREF_MODULE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PrefModule {

    @Provides
    @Singleton
    fun provideSharedPref(@ApplicationContext context:Context) :SharedPreferences{
        return context.getSharedPreferences(PREF_MODULE,Context.MODE_PRIVATE)
    }

}