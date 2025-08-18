package ru.mygames.vk.navigation

import android.net.Uri
import com.google.gson.Gson
import ru.mygames.vk.domain.entity.FeedPost

sealed class Screen (
    val route: String
){
    object NewsFeet: Screen(ROUTE_NEWS_FEED)
    object Favorite: Screen(ROUTE_FAVORITE)
    object Profile: Screen(ROUTE_PROFILE)
    object Home: Screen(ROUTE_HOME)
    object Comments: Screen(ROUTE_COMMENTS){

        private const val ROUTE_FOR_ARGS = "comments"

        fun getRouteWithArgs(feedPost: FeedPost): String {
            val feedPostGson = Gson().toJson(feedPost)
            return "$ROUTE_FOR_ARGS/${feedPostGson.encode()}"
        }
    }

    companion object{
        const val KEY_FEED_POST = "feed_post"
        const val ROUTE_HOME = "home"
        const val ROUTE_COMMENTS = "comments/{$KEY_FEED_POST}"
        const val ROUTE_NEWS_FEED = "news_feed"
        const val ROUTE_FAVORITE = "favorite"
        const val ROUTE_PROFILE = "profile"
    }
}

fun String.encode(): String {
    return Uri.encode(this)
}
