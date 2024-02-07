package com.countrydelight.cdlogger.domain.repository

import com.countrydelight.cdlogger.data.local.event.EventEntity
import com.countrydelight.cdlogger.data.remote.event.SendDataToRemoteEntity
import com.countrydelight.cdlogger.data.remote.response.ResponseState
import com.countrydelight.cdlogger.domain.models.SpaceDetails

/**
 * An interface for interacting with event entities.
 */
internal interface IEventRepository {
    /**
     * Inserts an event entity into the repository.
     *
     * @param eventEntity The event entity to insert.
     * @return The ID of the inserted event entity.
     */
    suspend fun insertEventToLocal(eventEntity: EventEntity): Long

    /**
     * Deletes event from the repository.
     *
     * @param event The event to delete.
     * @return The number of rows affected.
     */
    fun deleteEventFromLocal(event: EventEntity): Int


    /**
     * Retrieves all events from the database.
     *
     * @return A list of all events.
     */
    fun getAllEvents(): List<EventEntity>


    /**
     * Adds an event to the remote entity.
     *
     * @param sendDataToRemoteEntity The object used to send data to the remote entity.
     * @param spaceDetails The details of the space where the event will be added.
     * @return A ResponseState object indicating the success or failure of the operation.
     */
    suspend fun addEventToRemote(
        sendDataToRemoteEntity: SendDataToRemoteEntity,
        spaceDetails: SpaceDetails,
    ): ResponseState<Nothing>

}