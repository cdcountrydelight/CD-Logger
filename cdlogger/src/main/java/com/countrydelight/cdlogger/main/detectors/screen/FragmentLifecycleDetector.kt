package com.countrydelight.cdlogger.main.detectors.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.countrydelight.cdlogger.main.CDLogger
import com.countrydelight.cdlogger.main.utils.ConstantHelper


/**
 * A FragmentManager.FragmentLifecycleCallbacks implementation that logs fragment opening events.
 */
internal class FragmentLifecycleDetector : FragmentManager.FragmentLifecycleCallbacks() {
    override fun onFragmentCreated(
        fm: FragmentManager,
        fragment: Fragment,
        savedInstanceState: Bundle?
    ) {
        super.onFragmentCreated(fm, fragment, savedInstanceState)
        val fragmentName = fragment::class.simpleName
        if (fragmentName != "NavHostFragment") {
            CDLogger.logEvent(
                eventName = ConstantHelper.SCREEN_OPENED,
                eventData = mutableMapOf(ConstantHelper.SCREEN_NAME to (fragmentName ?: ""))
            )
        }

    }
}