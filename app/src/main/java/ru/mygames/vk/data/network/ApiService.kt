package ru.mygames.vk.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.mygames.vk.data.model.CommentsResponseDto
import ru.mygames.vk.data.model.FavoriteResponseDto
import ru.mygames.vk.data.model.LikesCountResponseDto
import ru.mygames.vk.data.model.NewsFeedResponseDto

interface ApiService {
    @GET("newsfeed.getRecommended?v=5.131")
    suspend fun loadRecommendation(
        @Query("access_token") token: String
    ): NewsFeedResponseDto

    @GET("newsfeed.getRecommended?v=5.131")
    suspend fun loadRecommendation(
        @Query("access_token") token: String,
        @Query("start_from") startFrom: String
    ): NewsFeedResponseDto

    @GET("newsfeed.ignoreItem?v=5.131&type=wall")
    suspend fun ignoreItem(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") postId: Long,
    )

    @GET("likes.add?v=5.131&type=post")
    suspend fun addLike(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") postId: Long,
    ): LikesCountResponseDto
    @GET("likes.delete?v=5.131&type=post")
    suspend fun deleteLike(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") postId: Long,
    ): LikesCountResponseDto
    @GET("wall.getComments?v=5.131&extended=1&fields=photo_100")
    suspend fun getComments(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("post_id") postId: Long,
    ):CommentsResponseDto
    @GET("fave.addPost?v=5.131")
    suspend fun addFavorites(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("id") postId: Long,
    )
    @GET("fave.removePost?v=5.131")
    suspend fun removeFavorites(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("id") postId: Long,
    )
    @GET("fave.get?v=5.131&item_type=post")
    suspend fun loadFavorite(
        @Query("access_token") token: String,
        @Query("count") count: Int = 50,
        @Query("extended") extended: Int = 1
    ): FavoriteResponseDto
}