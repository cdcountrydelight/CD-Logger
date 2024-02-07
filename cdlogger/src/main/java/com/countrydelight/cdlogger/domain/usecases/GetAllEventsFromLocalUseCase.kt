package com.countrydelight.cdlogger.domain.usecases

import android.content.Context
import com.countrydelight.cdlogger.data.local.event.EventEntity
import com.countrydelight.cdlogger.data.repository.IEventRepositoryImpl
import com.countrydelight.cdlogger.domain.repository.IEventRepository


/**
 * A use case that retrieves all events from the local repository.
 */
internal class GetAllEventsFromLocalUseCase(context: Context) {
    private val repository: IEventRepository = IEventRepositoryImpl(context)

    /**
     * Retrieves all events from the local repository.
     *
     * @return A list of [EventEntity] objects.
     */
     operator fun invoke(): List<EventEntity> {
        return repository.getAllEvents()
    }
}