package com.ctw.newsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ctw.newsapp.model.ArticleResponse
import com.ctw.newsapp.ui.ArticleDetailScreen
import com.ctw.newsapp.ui.NewsScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "news_list") {

        composable("news_list") {
            NewsScreen(
                onArticleClick = { article ->
                    val json = Json.encodeToString(article)
                    val encodedJson = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                    navController.navigate("news_detail/$encodedJson")
                }
            )
        }

        composable(
            route = "news_detail/{articleJson}",
            arguments = listOf(
                navArgument("articleJson") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val encodedJson = backStackEntry.arguments?.getString("articleJson") ?: ""
            val json = URLDecoder.decode(encodedJson, StandardCharsets.UTF_8.toString())
            val article = try {
                Json.decodeFromString<ArticleResponse>(json)
            } catch (e: Exception) {
                null
            }

            article?.let {
                ArticleDetailScreen(
                    article = it,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

