package com.example.repartio.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.repartio.ui.screens.GroupDetailScreen
import com.example.repartio.ui.screens.GroupListScreen

// Defino rutas navegacion
sealed class Screen(val route: String) {
    object GroupList : Screen("group_list")
    object GroupDetail : Screen("group_detail/{groupId}") {
        fun createRoute(groupId: Long) = "group_detail/$groupId"
    }
}

@Composable
fun RepartioNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.GroupList.route
    ) {
        composable(Screen.GroupList.route) {
            GroupListScreen(
                onGroupClick = { groupId ->
                    navController.navigate(Screen.GroupDetail.createRoute(groupId))
                }
            )
        }
        composable(
            route = Screen.GroupDetail.route,
            arguments = listOf(navArgument("groupId") { type = NavType.LongType })
        ) {
            GroupDetailScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}