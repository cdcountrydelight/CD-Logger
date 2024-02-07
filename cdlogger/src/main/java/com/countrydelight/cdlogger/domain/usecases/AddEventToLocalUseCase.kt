package com.countrydelight.cdlogger.domain.usecases

import android.content.Context
import com.countrydelight.cdlogger.data.local.event.EventEntity
import com.countrydelight.cdlogger.data.repository.IEventRepositoryImpl
import com.countrydelight.cdlogger.domain.repository.IEventRepository

/**
 * A use case that adds an event to the local repository.
 *
 */
internal class AddEventToLocalUseCase(context: Context) {

    private val repository: IEventRepository = IEventRepositoryImpl(context)

    /**
     * Inserts an event into the repository.
     *
     * @param event The event to insert.
     */
    suspend  operator fun invoke(event: EventEntity) {
            repository.insertEventToLocal(event)
    }
}