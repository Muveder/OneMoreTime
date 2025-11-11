package com.example.onemoretime

import android.app.Application
import com.example.onemoretime.data.AppContainer
import com.example.onemoretime.data.AppDataContainer

class OneMoreTimeApplication : Application() {
    /**
     * El contenedor de dependencias de la aplicación.
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
