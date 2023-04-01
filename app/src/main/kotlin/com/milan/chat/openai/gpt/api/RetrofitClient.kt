package com.milan.chat.openai.gpt.api

import com.elvishew.xlog.XLog
import com.milan.chat.openai.gpt.BuildConfig
import com.milan.chat.openai.gpt.ext.GsonExt.gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * User: milan
 * Time: 2023/3/30 19:55
 * Des:
 */
object RetrofitClient {
    private val logger: HttpLoggingInterceptor.Logger =
        HttpLoggingInterceptor.Logger { message ->
            XLog.w(message)
        }

    private val loggingInterceptor = HttpLoggingInterceptor(logger).apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    }

    private val headerInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${BuildConfig.OPEN_AI_CHAT_GPT_API_KEY}")
            .build()
        chain.proceed(request)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(headerInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val okHttpClient: OkHttpClient
        get() = client

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openai.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val openAiService: OpenAiService = retrofit.create(OpenAiService::class.java)
}