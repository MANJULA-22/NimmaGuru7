package com.example.nimmaguru

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.nimmaguru.navigation.NimmaGuruNavGraph
import com.example.nimmaguru.ui.theme.NimmaGuruTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NimmaGuruTheme {
                val navController = rememberNavController()
                NimmaGuruNavGraph(navController = navController)
            }
        }
    }
}