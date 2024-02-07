package com.countrydelight.cdlogger.data.remote

import com.countrydelight.cdlogger.data.remote.event.SpaceRequestModel
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse

internal class RemoteRepository {
    private val remoteClientBuilder = RemoteClientBuilder.get()

    suspend fun sendEventToRemote(
        spaceId: String,
        spaceKey: String,
        spaceToken: String,
        spaceRequest: SpaceRequestModel
    ): HttpResponse {
        val url =
            "https://chat.googleapis.com/v1/spaces/$spaceId/messages?key=$spaceKey&token=$spaceToken"
        return remoteClientBuilder.api.post(url) {
            setBody(spaceRequest)
        }
    }
}