package com.countrydelight.cdlogger.main.detectors.screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.countrydelight.cdlogger.main.CDLogger
import com.countrydelight.cdlogger.main.utils.ConstantHelper


/**
 * A FragmentManager.FragmentLifecycleCallbacks implementation that logs fragment opening events.
 */
internal class FragmentLifecycleDetector : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentResumed(fm: FragmentManager, fragment: Fragment) {
        super.onFragmentResumed(fm, fragment)
        val fragmentName = fragment::class.simpleName
        if (fragmentName != "NavHostFragment") {
            CDLogger.logEvent(
                eventName = ConstantHelper.FRAGMENT_OPENED,
                eventData = mutableMapOf(ConstantHelper.SCREEN_NAME to (fragmentName ?: ""))
            )
        }
    }
}