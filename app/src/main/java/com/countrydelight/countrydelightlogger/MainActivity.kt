package com.countrydelight.countrydelightlogger

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.countrydelight.cdlogger.main.CDLogger
import com.countrydelight.countrydelightlogger.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CDLogger.addUserDetails(mutableMapOf("user_name" to "I am Grute", "user_id" to "12345"))
        binding.sendHashMapDataToServerBtn.setOnClickListener {
            CDLogger.logEvent("View Click", mutableMapOf(Pair("view_name", (it as Button).text)))
        }
        binding.sendStringDataToServer.setOnClickListener {
            CDLogger.logEvent("View Click", (it as Button).text.toString())
        }
        binding.createExceptionOnMainThreadBtn.setOnClickListener {
            throw RuntimeException("Testing Exception On Main Thread")
        }

        binding.createExceptionOnBackgroundThreadBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                throw RuntimeException("Testing Exception On Background Thread")
            }
        }
    }
}