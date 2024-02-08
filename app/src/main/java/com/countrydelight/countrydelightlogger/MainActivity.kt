package com.countrydelight.countrydelightlogger

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.countrydelight.countrydelightlogger.FunctionHelper.genericOnClick
import com.countrydelight.countrydelightlogger.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textView.genericOnClick {
            Toast.makeText(this, "TextView Click", Toast.LENGTH_LONG).show()
        }
        binding.button.genericOnClick {
            Toast.makeText(this, "Button Click", Toast.LENGTH_LONG).show()
        }
    }
}