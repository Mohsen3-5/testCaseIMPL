package com.example.jetcompose.presentation.homePage

import android.app.AlertDialog
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Process.myUid
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.jetcompose.domin.service.services.OverlayService
import com.example.jetcompose.domin.service.services.OverlayServiceImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject


// This annotation indicates that this ViewModel should be injected by Hilt
@HiltViewModel
class HomePageViewModel @Inject constructor(
) : ViewModel() {


    @Inject
    lateinit var overlayService: OverlayService


    private val _state = mutableStateOf(HomePageState())
    val state: State<HomePageState> = _state

    // This function stops the overlay service
    fun stopService(context:Context) {
            val serviceIntent = Intent(context, OverlayServiceImpl::class.java)
            context.stopService(serviceIntent)
            _state.value.runningStatus=false
        }

    // This function starts the overlay service
    fun startService(context:Context){
           val serviceIntent = Intent(context, OverlayServiceImpl::class.java)
            context.startService(serviceIntent)
           _state.value.runningStatus=true
       }

    // This function asks for the overlay permission and starts the service if it's granted
    @RequiresApi(Build.VERSION_CODES.Q)
        fun askPermissionAndGo(context: Context, activity: ComponentActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(activity)) {
            // Permission is not granted, ask the user for permission to go to settings
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            val uri = Uri.fromParts("package", activity.packageName, null)
            intent.data = uri
           requesrPermisson(intent,"OVERLAY PERMISSION",activity).apply {
                    if (!checkUsageStatsPermission(context)) {
                       requesrPermisson(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),"USAGE ACCESS",activity)
                    }
                }
        } else{
            startService(context)
        }
        }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun checkUsageStatsPermission(context: Context): Boolean {
        val appOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode =
            appOpsManager.checkOp(AppOpsManager.OPSTR_GET_USAGE_STATS, myUid(), context.packageName)
        return mode == AppOpsManager.MODE_ALLOWED
    }


    private fun requesrPermisson(intent: Intent, permission: String, activity: ComponentActivity){
        AlertDialog.Builder(activity)
            .setTitle("Permission Required")
            .setMessage("Please go to settings to enable $permission permission")
            .setPositiveButton("Go to Settings") { _, _ ->
                // Launch the settings activity
                activity.startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onCleared() {
        super.onCleared()
    }
    }





