package com.countrydelight.cdlogger.domain.usecases

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.countrydelight.cdlogger.base.utils.BaseConstantHelper
import com.countrydelight.cdlogger.data.remote.event.SendEventWorker
import com.countrydelight.cdlogger.main.utils.ConstantHelper
import java.util.concurrent.TimeUnit


/**
 * A use case that starts a worker to log events.
 */

internal object StartLogEventWorkerUseCase {

    /**
     * Starts the worker.
     *
     * @param context The application context.
     */
    fun invoke(context: Context) {
        // Create a OneTimeWorkRequest for the SendEventWorker.
        val uploadWorkRequest =
            OneTimeWorkRequestBuilder<SendEventWorker>()
                // Set the constraints for the work request.
                .setConstraints(
                    Constraints.Builder()
                        // Require the device to be connected to a network.
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                // sets the backoff criteria for the worker
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    ConstantHelper.MIN_RETRY_INTERVAL,
                    TimeUnit.MINUTES
                )
                // Add a tag to the work request.
                .addTag(BaseConstantHelper.WORKER_TAG)
                .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            // Enqueue the work request with a unique tag.
            BaseConstantHelper.WORKER_TAG,
            // Keep the existing work if there is a conflict.
            ExistingWorkPolicy.APPEND,
            uploadWorkRequest
        )
    }
}