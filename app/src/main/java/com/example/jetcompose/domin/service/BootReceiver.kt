package com.example.jetcompose.domin.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.jetcompose.MainActivity
import com.example.jetcompose.data.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/*
Bu sınıf bir yayın alıcısıdır ve cihaz yeniden başlatıldığında belirli bir aktiviteyi çalıştırmak için kullanılır.
(EN) This class is a broadcast receiver and is used to run a specific activity when the device is restarted.
*/
class BootReceiver : BroadcastReceiver() {
  @Inject
  lateinit var sharedPreferences: SharedPreferences
  @Inject
  @ApplicationContext
  lateinit var appContext: Context
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            val activityIntent = Intent(context, MainActivity::class.java)
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context!!.startActivity(activityIntent)
        }
    }
}