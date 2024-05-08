package com.countrydelight.cdlogger.data.remote

import android.util.Log
import com.countrydelight.cdlogger.domain.utils.DomainConstantHelper.LOG_TAG
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.gson.gson

internal class RemoteClientBuilder {
    companion object {
        @Volatile
        private var instance: RemoteClientBuilder? = null

        fun get(): RemoteClientBuilder {
            return instance ?: synchronized(this) {
                instance
                    ?: RemoteClientBuilder().also { instance = it }
            }
        }
    }

    val api: HttpClient = HttpClient(Android) {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.i(LOG_TAG, message)
                }
            }
            level = LogLevel.ALL
        }

        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
                disableHtmlEscaping()
            }
        }

        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }

        install(ResponseObserver) {
            onResponse { response ->
                Log.i(LOG_TAG, "Response:- ${response.status.value},${response.status.description}")
            }
        }


        // Timeout
        install(HttpTimeout) {
            requestTimeoutMillis = 300000L
            connectTimeoutMillis = 300000L
            socketTimeoutMillis = 300000L
        }
    }
}