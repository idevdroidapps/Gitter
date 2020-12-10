package com.idevdroidapps.gitter.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.idevdroidapps.gitter.Injection
import com.idevdroidapps.gitter.R
import com.idevdroidapps.gitter.databinding.ActivityMainBinding
import com.idevdroidapps.gitter.ui.viewmodels.SharedViewModel

/**
 * Launcher activity class that handles user interaction with toolbar components
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        actionBar?.hide()

        ViewModelProvider(this@MainActivity, Injection.provideMainActViewModelFactory()).get(
            SharedViewModel::class.java
        )

        binding.lifecycleOwner = this
    }


}