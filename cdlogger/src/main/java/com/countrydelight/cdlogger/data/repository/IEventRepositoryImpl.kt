package com.countrydelight.cdlogger.data.repository

import android.content.Context
import com.countrydelight.cdlogger.data.local.CDLoggerDatabase
import com.countrydelight.cdlogger.data.local.event.EventEntity
import com.countrydelight.cdlogger.data.remote.RemoteRepository
import com.countrydelight.cdlogger.data.remote.event.SendDataToRemoteEntity
import com.countrydelight.cdlogger.data.remote.response.ResponseState
import com.countrydelight.cdlogger.domain.models.SpaceDetails
import com.countrydelight.cdlogger.domain.repository.IEventRepository
import com.countrydelight.cdlogger.data.remote.event.SpaceRequestModel
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser


internal class IEventRepositoryImpl(context: Context) : IEventRepository {
    private val eventDao = CDLoggerDatabase.get(context).getEventDao()

    override suspend fun insertEventToLocal(eventEntity: EventEntity): Long {
        // Insert the event entity into the database using the eventDao.
        return eventDao.addEvent(eventEntity)
    }

    override fun deleteEventFromLocal(event: EventEntity): Int {
        // Call the deleteEventList() method on the eventDao object to delete the event list.
        return eventDao.deleteEvent(event)
    }

    override fun getAllEvents(): List<EventEntity> {
        // retrieves all the EventEntity objects from the database.
        return eventDao.getAllEvents()
    }


    private suspend fun makeAPICall(
        spaceRequest: SendDataToRemoteEntity, // The request object to be sent to the API.
        spaceDetails: SpaceDetails // Details about the space to which the request is being sent.
    ): ResponseState<Nothing> { // The return type of the function, indicating the success or failure of the API call.
        try {
            // Create a Gson object for converting the request object to a JSON string.
            val gson = GsonBuilder().setPrettyPrinting().create()

            // Convert the request object to a JSON string.
            val spaceRequestString = gson.toJson(spaceRequest)

            // Parse the JSON string into a JsonElement object.
            val jsonElement = JsonParser.parseString(spaceRequestString)
            // Create a call object for the API request.
            RemoteRepository().sendEventToRemote(
                spaceDetails.spaceId, // The ID of the space.
                spaceDetails.spaceKey, // The key of the space.
                spaceDetails.spaceToken, // The token for the space.
                SpaceRequestModel(gson.toJson(jsonElement)) // The request body containing the JSON data.
            )// Execute the API call.
            return ResponseState.success(null)
        } catch (exception: Exception) { // Catch any exceptions that may occur during the API call.
            return ResponseState.error(null, exception.localizedMessage ?: "")
        }
    }

    override suspend fun addEventToRemote(
        sendDataToRemoteEntity: SendDataToRemoteEntity,
        spaceDetails: SpaceDetails
    ): ResponseState<Nothing> {
        // Make an API call to add the event to the remote entity.
        return makeAPICall(
            spaceRequest = sendDataToRemoteEntity,
            spaceDetails = spaceDetails,
        )
    }
}