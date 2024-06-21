package com.countrydelight.cdlogger.data.remote.event

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.countrydelight.cdlogger.base.utils.BaseConstantHelper
import com.countrydelight.cdlogger.base.utils.BaseConstantHelper.LOG_TAG
import com.countrydelight.cdlogger.base.utils.BaseFunctionHelper.appendMap
import com.countrydelight.cdlogger.base.utils.SharedPreferenceHelper
import com.countrydelight.cdlogger.data.remote.response.ResponseStatusEnum
import com.countrydelight.cdlogger.data.utils.DataFunctionHelper
import com.countrydelight.cdlogger.domain.usecases.DeleteEventFromLocalUseCase
import com.countrydelight.cdlogger.domain.usecases.GetAllEventsFromLocalUseCase
import com.countrydelight.cdlogger.domain.usecases.SendEventDataToRemoteUseCase
import com.countrydelight.cdlogger.main.InternalLogger
import kotlinx.coroutines.runBlocking


/**
 * A worker class that sends events to a remote server.
 *
 * @param context The application context.
 * @param workerParameters The worker parameters.
 */
internal class SendEventWorker(private val context: Context, workerParameters: WorkerParameters) :
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
        var workerResult: Result
        runBlocking {
            try {
                if (DataFunctionHelper.isNetworkAvailable(context)) {
                    val eventList = getAllEventsFromLocalUseCase()
                    eventList.forEach { event ->
                        val userDetails = mutableMapOf<String, Any>()
                        userDetails[BaseConstantHelper.UUID] = preference.appUID
                        userDetails[BaseConstantHelper.ADVERTISING_ID] =
                            preference.advertisingId.toString()
                        userDetails.appendMap(preference.userDetails)
                        userDetails.appendMap(preference.deviceDetails)
                        val eventToSend = SendDataToRemoteEntity(
                            preference.appName.ifBlank { "N/A" },
                            event.appVersionName ?: "N/A",
                            event.appVersionCode,
                            userDetails,
                            event.toEventMap()
                        )
                        val response =
                            sendEventDataToRemoteUseCase(eventToSend, preference.spaceDetails)
                        if (response.status == ResponseStatusEnum.Success) {
                            deleteEventFromLocalUseCase(event)
                            Log.i(LOG_TAG, "Success on event: ${event.toDisplayEvent()}")
                        } else {
                            val exceptionMessage =
                                "Sending Event : ${event.toDisplayEvent()} To Remote Failed With Message : ${response.message}"
                            InternalLogger.loggerFailureCallback?.onLoggerFailure(
                                "Remote Sync",
                                Exception(exceptionMessage)
                            )
                        }
                    }
                    workerResult = Result.success()
                } else {
                    workerResult = Result.retry()
                }
            } catch (exception: Exception) {
                InternalLogger.loggerFailureCallback?.onLoggerFailure("Remote Sync", exception)
                workerResult = Result.failure()
            }
        }
        return workerResult
    }
}