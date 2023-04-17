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
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

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

    // Service oluşturulduğunda, bildirim başlatılır ve örtü görünümü oluşturulur.
    // Bu metot, onStartCommand() ile birlikte çağrılır.
    // ! (EN) This method is called when the service is created
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate() {
        super.onCreate()

        // Bildirim başlatma fonksiyounu
        // ! (EN) Call the startNotification method to display the notification to the user
        startNotification()

        // Inflate the overlay view
        overlayView = LayoutInflater.from(context).inflate(R.layout.text_widget, null)

        val textView = overlayView?.findViewById<TextView>(R.id.tvWidget) as TextView

        textView.text = context.packageName.substringAfter("e.")

        // görünümü oluşturma aşaması
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        // görünümünü görüntülemek için kullanılır.
        // Gerekli olan tüm ayarlar yapılır.
        // ! (EN) Set layout parameters for the overlay view.
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            x = 0
            y = -700
        }

        // görünümü, WindowManager'a ekleme
        // ! (EN) Add the overlay view to the WindowManager
        windowManager.addView(overlayView, params)

        // Service çalışma durumu kaydedilir
        // ! (EN) Update the SharedPreferences to indicate that the service is running.
        sharedPreferences.editServiceStatus(true)
    }

    // Bildirim başlatılır. Bu, hizmetin arka planda çalışmasını sağlar.
    // ! (EN) This method starts the foreground service notification

    override fun startNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val CHANNEL_ID = "my_channel_01"
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                channel
            )
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("")
                .setContentText("").build()
            startForeground(1, notification)
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
