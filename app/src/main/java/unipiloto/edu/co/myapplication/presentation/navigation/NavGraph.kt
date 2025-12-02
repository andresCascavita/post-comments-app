package com.unipiloto.myapplication.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.unipiloto.myapplication.presentation.postdetail.PostDetailScreen
import com.unipiloto.myapplication.presentation.postlist.PostListScreen

fun NavGraphBuilder.setupNavGraph(
    navController: NavController
) {
    composable(NavRoutes.POST_LIST) {
        PostListScreen(navController = navController)
    }

    composable(
        route = NavRoutes.POST_DETAIL,
        arguments = listOf(navArgument("postId") { type = androidx.navigation.NavType.IntType })
    ) { backStackEntry ->
        val postId = backStackEntry.arguments?.getInt("postId") ?: 0
        PostDetailScreen(
            navController = navController,
            postId = postId
        )
    }
}