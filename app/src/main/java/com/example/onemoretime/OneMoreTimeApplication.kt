package com.example.onemoretime

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.onemoretime.data.AppContainer
import com.example.onemoretime.data.AppDataContainer

class OneMoreTimeApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Likes"
            val descriptionText = "Notificaciones cuando a alguien le gusta tu reseña"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("like_channel", name, importance).apply {
                description = descriptionText
            }
            // Registramos el canal en el sistema
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
