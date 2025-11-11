package com.example.onemoretime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.onemoretime.navigation.AppNavigation
import com.example.onemoretime.ui.theme.OneMoreTimeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OneMoreTimeTheme {
                AppNavigation()
            }
        }
    }
}
