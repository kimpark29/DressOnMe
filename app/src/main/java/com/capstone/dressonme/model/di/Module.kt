package com.capstone.dressonme.model.di

import com.capstone.dressonme.BuildConfig
import com.capstone.dressonme.model.remote.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object Module {

    private var BASE_URL_API = "https://dressonmeapi-clh64ny43a-et.a.run.app/"

    @Provides
    fun provideRetrofit(): Retrofit {
        val interceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}