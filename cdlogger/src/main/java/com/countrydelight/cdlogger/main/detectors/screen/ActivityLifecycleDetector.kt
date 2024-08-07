package com.countrydelight.cdlogger.main.detectors.screen

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.countrydelight.cdlogger.main.CDLogger
import com.countrydelight.cdlogger.main.utils.ConstantHelper

/**
 * An ActivityLifecycleCallbacks implementation that logs activity openings and fragment opening events.
 *
 * @param logFragmentOpeningEvent Whether to log fragment opening events.
 */
internal class ActivityLifecycleDetector(
    private val logFragmentOpeningEvent: Boolean,
    private val logActivityOpeningEvent: Boolean
) :
    Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (logFragmentOpeningEvent) {
            if (activity is AppCompatActivity) {
                activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                    FragmentLifecycleDetector(),
                    true
                )
            } else if (activity is FragmentActivity) {
                activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                    FragmentLifecycleDetector(),
                    true
                )
            }
        }
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        if (logActivityOpeningEvent) {
            CDLogger.logEvent(
                eventName = ConstantHelper.ACTIVITY_OPENED,
                eventData = mutableMapOf(
                    ConstantHelper.SCREEN_NAME to (activity::class.simpleName ?: "")
                )
            )
        }
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}