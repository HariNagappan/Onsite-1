package com.example.fastapitesting

import android.R.attr.name
import android.util.Log
import android.widget.MediaController
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.fastapitesting.ui.theme.FastApiTestingTheme

@Composable
fun NavigationScreen(mainViewModel: MainViewModel= viewModel(),navController: NavHostController = rememberNavController(),modifier: Modifier=Modifier){
         FastApiTestingTheme() {
             Scaffold(
             ) { innerPadding ->
                 NavHost(
                     navController = navController,
                     startDestination = Routes.HOME.name,
                     modifier = Modifier.fillMaxSize().padding(innerPadding)
                 ) {
                     composable(Routes.HOME.name) {
                         Log.d("switchclicked", "navigated to Home Screen")
                         Home(mainViewModel = mainViewModel, navController = navController)
                     }
                 }
             }
         }

}