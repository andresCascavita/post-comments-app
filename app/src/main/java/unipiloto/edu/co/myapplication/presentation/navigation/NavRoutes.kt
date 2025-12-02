package com.unipiloto.myapplication.presentation.navigation

object NavRoutes {
    const val POST_LIST = "post_list"
    const val POST_DETAIL = "post_detail/{postId}"

    fun getPostDetailRoute(postId: Int): String {
        return "post_detail/$postId"
    }
}