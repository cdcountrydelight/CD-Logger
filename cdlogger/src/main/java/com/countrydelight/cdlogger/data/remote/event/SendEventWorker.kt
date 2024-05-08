package com.countrydelight.cdlogger.data.remote.event

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.countrydelight.cdlogger.data.remote.response.ResponseStatusEnum
import com.countrydelight.cdlogger.domain.usecases.DeleteEventFromLocalUseCase
import com.countrydelight.cdlogger.domain.usecases.GetAllEventsFromLocalUseCase
import com.countrydelight.cdlogger.domain.usecases.SendEventDataToRemoteUseCase
import com.countrydelight.cdlogger.domain.utils.SharedPreferenceHelper
import com.countrydelight.cdlogger.main.utils.ConstantHelper.LOG_TAG
import kotlinx.coroutines.runBlocking


/**
 * A worker class that sends events to a remote server.
 *
 * @param context The application context.
 * @param workerParameters The worker parameters.
 */
internal class SendEventWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {


    private val getAllEventsFromLocalUseCase = GetAllEventsFromLocalUseCase(context)

    private val deleteEventFromLocalUseCase = DeleteEventFromLocalUseCase(context)

    private val sendEventDataToRemoteUseCase = SendEventDataToRemoteUseCase(context)

    private val preference = SharedPreferenceHelper.get(context)


    /**
     * Performs the work of the worker.
     *
     * @return A Result object indicating the success or failure of the work.
     */
    override fun doWork(): Result {
        runBlocking {
            val eventList = getAllEventsFromLocalUseCase()
            eventList.forEach { event ->
                val eventToSend = SendDataToRemoteEntity(
                    preference.appName,
                    preference.appUID,
                    preference.advertisingId,
                    preference.userDetails,
                    preference.deviceDetails,
                    event.toEventRemoteEntity()
                )
                val response = sendEventDataToRemoteUseCase(eventToSend, preference.spaceDetails)
                if (response.status == ResponseStatusEnum.Success) {
                    deleteEventFromLocalUseCase(event)
                    Log.i(LOG_TAG, "Success on event: ${event.toDisplayEvent()}")
                } else {
                    Log.e(
                        LOG_TAG,
                        "Exception on event: ${event.toDisplayEvent()} with error code: ${response.statusCode}, error message: ${response.message}"
                    )
                }
            }
        }
        return Result.success()
    }
}