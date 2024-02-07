package com.countrydelight.cdlogger.main.screen_detectors

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.countrydelight.cdlogger.domain.models.Event
import com.countrydelight.cdlogger.main.CDLogger
import com.countrydelight.cdlogger.main.utils.ConstantHelper


/**
 * A FragmentManager.FragmentLifecycleCallbacks implementation that logs fragment opening events.
 */
internal class FragmentLifecycleDetector : FragmentManager.FragmentLifecycleCallbacks() {
    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        super.onFragmentCreated(fm, f, savedInstanceState)
        val fragmentName = f::class.simpleName
        if (fragmentName != "NavHostFragment") {
            CDLogger.logEvent(
                Event(
                    eventName = ConstantHelper.SCREEN_OPENED,
                    eventData = mutableMapOf(
                        Pair(
                            ConstantHelper.SCREEN_NAME,
                            f::class.simpleName ?: ""
                        )
                    )
                )
            )
        }

    }
}