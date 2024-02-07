package com.countrydelight.cdlogger.domain.usecases

import android.content.Context
import com.countrydelight.cdlogger.data.local.event.EventEntity
import com.countrydelight.cdlogger.data.repository.IEventRepositoryImpl
import com.countrydelight.cdlogger.domain.repository.IEventRepository

/**
 * A class that removes the event  from the local repository.
 *
 */
internal class DeleteEventFromLocalUseCase(context: Context) {
    private val repository: IEventRepository = IEventRepositoryImpl(context)

    /**
     * Removes event  from the local repository.
     *
     * @param event The event  to remove.
     * @return The number of rows affected.
     */
    operator fun invoke(event: EventEntity): Int {
        return repository.deleteEventFromLocal(event)
    }
}