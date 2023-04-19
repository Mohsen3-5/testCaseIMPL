package com.example.jetcompose.domin.service.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.jetcompose.R
import com.example.jetcompose.data.repo.SharedPreferences
import com.example.jetcompose.domin.service.helpers.UsageStatsHelper
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject
import android.os.Handler
import android.os.Looper

/*
 Bu sınıf, uygulamanın diğer bileşenlerinden ayrı olarak çalışan bir hizmettir.
 Görevi, diğer uygulamaların üzerine bindirilen bir görünümü oluşturmak ve yönetmektir.
*/

@AndroidEntryPoint
class OverlayServiceImpl @Inject constructor(
) : OverlayService,Service(){

    @Inject
    lateinit var windowManager: WindowManager
    private  var overlayView: View? = null

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    @ApplicationContext
    lateinit var context:Context
    val CHANNEL_ID = "my_channel_01"
    lateinit var currentAppPackageName:String
    private val handler = Handler(Looper.getMainLooper())


    private lateinit var notificationBuilder: NotificationCompat.Builder


    // Service oluşturulduğunda, bildirim başlatılır ve örtü görünümü oluşturulur.
    // Bu metot, onStartCommand() ile birlikte çağrılır.
    // ! (EN) This method is called when the service is created
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate() {
        super.onCreate()
        // Call the getCurrentAppPackageName function to get the package name of the current app
        currentAppPackageName = UsageStatsHelper.getCurrentAppPackageName(context).toString()
        // ! (disabled) createNotificationChannel()

        // ! (disabled) startNotification()
        // Inflate the overlay view
        overlayView = LayoutInflater.from(context).inflate(R.layout.text_widget, null)

        val textView = overlayView?.findViewById<TextView>(R.id.tvWidget) as TextView

        textView.text = currentAppPackageName.substringAfterLast(".")

        // görünümü oluşturma aşaması
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        // görünümünü görüntülemek için kullanılır.
        // Gerekli olan tüm ayarlar yapılır.
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            x = 0
            y = -700
        }
        // görünümü, WindowManager'a ekleme
        windowManager.addView(overlayView, params)

        // Service çalışma durumu kaydedilir
        sharedPreferences.editServiceStatus(true)


        // This is the runnable that will update the current app package name in the overlay view
        val updateCurrentAppPackageNameRunnable = object : Runnable {
            override fun run() {
                currentAppPackageName = UsageStatsHelper.getCurrentAppPackageName(context)?.substringAfterLast(".").toString()
                textView.text = currentAppPackageName
                handler.postDelayed(this, 1000) // Run this task every 1 second
            }
        }

        // Run the updateCurrentAppPackageNameRunnable task
        handler.post(updateCurrentAppPackageNameRunnable)


    }

    // Bildirim başlatılır. Bu, hizmetin arka planda çalışmasını sağlar.
    // ! (EN) This method starts the foreground service notification

    override fun startNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("My notification $currentAppPackageName")
                .setSmallIcon(R.drawable.oval_button_bg)
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

            startForeground(1, notificationBuilder.build())

        }
    }


    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Channel human readable title"
            val descriptionText ="hello"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    // ! (EN) This method is called when the service is destroyed
    override fun onDestroy() {
        super.onDestroy()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if(overlayView!=null){
            // Remove the overlay view from the WindowManager if it exists
            overlayView.let {
                windowManager.removeView(it)
            }
            sharedPreferences.editServiceStatus(false)
            // Set the overlayView to null
            overlayView = null
        }
    }




}

