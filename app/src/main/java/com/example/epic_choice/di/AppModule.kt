package com.example.epic_choice.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.example.epic_choice.util.Constant.INTRODUCTION_SP
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class )
object AppModule  {
    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestoreDatabase() = Firebase.firestore

    @Provides
    fun provideFirstPageSP(
        application: Application
    ) =application.getSharedPreferences(INTRODUCTION_SP,MODE_PRIVATE)
}