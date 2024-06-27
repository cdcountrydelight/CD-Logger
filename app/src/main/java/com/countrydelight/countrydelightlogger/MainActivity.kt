package com.countrydelight.countrydelightlogger

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.countrydelight.cdlogger.main.CDLogger
import com.countrydelight.countrydelightlogger.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var messageStreamJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        CDLogger.addUserDetails(mutableMapOf("user_name" to "I am Grute", "user_id" to "12345"))
        with(binding) {
            sendHashMapDataToServerBtn.setOnClickListener {
                CDLogger.logEvent(
                    "View Click",
                    mutableMapOf(Pair("view_name", (it as Button).text), "view_type" to "Button")
                )
            }
            sendStringDataToServer.setOnClickListener {
                CDLogger.logEvent("View Click", (it as Button).text.toString())
            }

            sendStringDataWithoutTagNameToServer.setOnClickListener {
                CDLogger.logEvent("Testing String Message Button Clicked")
            }
            createExceptionOnMainThreadBtn.setOnClickListener {
                throw RuntimeException("Testing Exception On Main Thread")
            }
            triggerMessageStream.setOnClickListener {
                messageStreamJob?.cancel()
                messageStreamJob = lifecycleScope.launch {
                    repeat(300) {
                        CDLogger.logEvent("Triggering Message Stream with index $it")
                    }
                }
            }

            createExceptionOnBackgroundThreadBtn.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    throw RuntimeException("Testing Exception On Background Thread")
                }
            }
            navigateToSecondActivityBtn.setOnClickListener {
                startActivity(Intent(this@MainActivity, SecondActivity::class.java))
            }
        }

    }
}