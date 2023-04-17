package com.example.jetcompose.di

import android.content.Context
import android.view.WindowManager
import com.example.jetcompose.data.SharedPreferences
import com.example.jetcompose.domin.service.OverlayService
import com.example.jetcompose.domin.service.OverlayServiceImpl
import com.example.jetcompose.domin.service.repo.SharedPreferencesINF
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOverlayService(): OverlayService {
        return OverlayServiceImpl()
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context):Context{
        return context
    }

    @Provides
    @Singleton
    fun provideWindowManager(@ApplicationContext context: Context): WindowManager {
        return context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferencesINF {
        return SharedPreferences(context)
    }

}


