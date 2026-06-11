package com.buildstack.promptly.di

import android.content.Context
import androidx.room.Room
import com.buildstack.promptly.data.local.PromptlyDatabase
import com.buildstack.promptly.data.remote.GroqApi
import com.buildstack.promptly.data.repository.ChatRepositoryImpl
import com.buildstack.promptly.data.repository.GroqRepositoryImpl
import com.buildstack.promptly.domain.repository.ChatRepository
import com.buildstack.promptly.domain.repository.GroqRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

interface AppContainer {
    val chatRepository: ChatRepository
    val groqRepository: GroqRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val json = Json { ignoreUnknownKeys = true }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(GroqApi.BASE_URL)
        .client(okHttpClient)
        .build()

    private val groqApi: GroqApi by lazy {
        retrofit.create(GroqApi::class.java)
    }

    private val promptlyDatabase: PromptlyDatabase by lazy {
        Room.databaseBuilder(context, PromptlyDatabase::class.java, "promptly_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    override val chatRepository: ChatRepository by lazy {
        ChatRepositoryImpl(promptlyDatabase.chatDao)
    }

    override val groqRepository: GroqRepository by lazy {
        GroqRepositoryImpl(groqApi)
    }
}
