package com.example.aihouse.api
import com.example.aihouse.models.Publication
import com.example.aihouse.models.User

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/users/registration")
    fun registerUser(@Body request: RegisterRequest): Call<CustomResponse>

    @POST("api/users/get")
    fun getUserById(@Body request: GetUserRequest): Call<CustomResponse>

    @POST("api/users/authorization/email")
    fun loginEmailUser(@Body request: LoginRequest): Call<CustomResponse>

    @POST("api/users/authorization/name")
    fun loginNameUser(@Body request: LoginRequest): Call<CustomResponse>

    @POST("api/publications/getNotByAuthor")
    fun getFeedPublications(@Body request: GetFeedPublicaionsRequest): Call<CustomResponse>

    @POST("api/publications/getByAuthor")
    fun getMyPublications(@Body request: GetMyPublicaionsRequest): Call<CustomResponse>

    @POST("api/publications/add")
    fun createPublication(@Body request: CreatePublicationRequest): Call<CustomResponse>

    @POST("api/publications/searchByText")
    fun searchPublications(@Body request: SearchRequest): Call<CustomResponse>

    @POST("api/users/searchByText")
    fun searchAuthors(@Body request: SearchRequest): Call<CustomResponse>

    @POST("api/publications/like")
    fun likePublication(@Body request: LikePublicationRequest): Call<CustomResponse>
}

// requests

data class GetUserRequest(
    val id: Int
)

data class LikePublicationRequest(
    val idUser: Int,
    val idPublication: Int,
    val type: String
)

data class LoginRequest(
    val name: String?,
    val email: String?,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class GetFeedPublicaionsRequest(
    val idAuthor: Int
)

data class GetMyPublicaionsRequest(
    val idAuthor: Int
)

data class CreatePublicationRequest(
    val title: String,
    val text: String,
    val idAuthor: Int
)

data class SearchRequest(
    val fragment: String,
    val idAuthor: Int
)

data class CustomResponse(
    val title: String?,
    val message: String?,
    val userData: User?,
    val users: List<User>?,
    val publicationData: Publication?,
    val publications: List<Publication>?,
    val result: String?,
    val countLikes: Int?,
    val isSetLike: Boolean?
)