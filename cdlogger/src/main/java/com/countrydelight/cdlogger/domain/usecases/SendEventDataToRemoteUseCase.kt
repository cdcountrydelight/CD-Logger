package com.countrydelight.cdlogger.domain.usecases

import android.content.Context
import com.countrydelight.cdlogger.data.remote.event.SendDataToRemoteEntity
import com.countrydelight.cdlogger.data.remote.response.ResponseState
import com.countrydelight.cdlogger.data.repository.IEventRepositoryImpl
import com.countrydelight.cdlogger.domain.models.SpaceDetails
import com.countrydelight.cdlogger.domain.repository.IEventRepository


/**
 * A use case for sending event data to a remote repository.
 */
internal class SendEventDataToRemoteUseCase(context: Context) {
    private val repository: IEventRepository = IEventRepositoryImpl(context)

    /**
     * Sends event data to the remote repository.
     *
     * @param sendDataToRemoteEntity The event data to send.
     * @param spaceDetails The space details.
     * @return A ResponseState indicating the success or failure of the operation.
     */
    suspend operator fun invoke(
        sendDataToRemoteEntity: SendDataToRemoteEntity,
        spaceDetails: SpaceDetails
    ): ResponseState<Nothing> {
        return repository.addEventToRemote(
            sendDataToRemoteEntity,
            spaceDetails
        )
    }
}