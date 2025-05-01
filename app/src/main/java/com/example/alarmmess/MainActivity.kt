package com.example.alarmmess

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.alarmmess.Route.App
import com.example.alarmmess.ui.theme.AlarmMessTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val navController = rememberNavController()
//
//        intent?.data?.let { uri ->
//            val alarmId = uri.lastPathSegment?.toLongOrNull()
//            if(alarmId != null) {
//                navController.navigate("alarm/$alarmId")
//            }
//        }

        enableEdgeToEdge()
        setContent {
            AlarmMessTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                ) { innerPadding ->
                    App(Modifier.padding(innerPadding))
                }
            }
        }
    }
}
