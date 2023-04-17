package com.example.jetcompose.di

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object ActivityModul {


    @Provides
    fun providePackageManager(activity: ComponentActivity): PackageManager {
        return activity.packageManager
    }
}