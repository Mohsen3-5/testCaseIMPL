package com.example.jetcompose.presentation.homePage

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.example.jetcompose.R
import com.example.jetcompose.data.repo.SharedPreferences
import com.example.jetcompose.domin.service.services.OverlayServiceImpl
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


// The MainActivity class is the entry point to the app and extends ComponentActivity, which is an activity class that provides integration with AndroidX Lifecycle and Hilt.
@RequiresApi(Build.VERSION_CODES.M)
@AndroidEntryPoint
class MainActivity
@Inject constructor(
): ComponentActivity() {
    @Inject
     lateinit var overlayServiceImpl: OverlayServiceImpl
     @Inject
     lateinit var sharedPreferences: SharedPreferences
    val permissions = listOf(
        Manifest.permission.PACKAGE_USAGE_STATS,
        Manifest.permission.SYSTEM_ALERT_WINDOW
    )
    private val viewModel by viewModels<HomePageViewModel>()

    @Inject
    @ApplicationContext
    lateinit var context:Context

    // This button is used to start or stop the overlay service.
    private lateinit var controllerBtn : Button

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.home_page)
        // If the overlay service is running, start it.
        if(sharedPreferences.getServiceStatus()){
         viewModel.startService(context)
        }

        controllerBtn = findViewById(R.id.oval_button)
        controllerBtn.text=if(sharedPreferences.getServiceStatus())"STOP" else "START"

        // When the button is clicked, it starts or stops the overlay service and updates the button text accordingly.
        controllerBtn.setOnClickListener {
            run {
                println(sharedPreferences.getServiceStatus())
                if (sharedPreferences.getServiceStatus()) {
                    viewModel.stopService(context)
                    controllerBtn.text = "START"
                } else {
                    viewModel.askPermissionAndGo(context, this)
                    controllerBtn.text = "STOP"
                }
            }
        }

    }

}