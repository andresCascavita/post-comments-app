package com.unipiloto.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.unipiloto.myapplication.presentation.navigation.NavRoutes
import com.unipiloto.myapplication.presentation.navigation.setupNavGraph
import com.unipiloto.myapplication.presentation.theme.PostCommentsAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PostCommentsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    LaunchedEffect(Unit) {
                    }

                    NavHost(
                        navController = navController,
                        startDestination = NavRoutes.POST_LIST
                    ) {
                        setupNavGraph(navController)
                    }
                }
            }
        }
    }
}