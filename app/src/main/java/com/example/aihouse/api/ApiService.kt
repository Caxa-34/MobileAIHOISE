package com.example.aihouse.api
import com.example.aihouse.models.Draft
import com.example.aihouse.models.Notification
import com.example.aihouse.models.Publication
import com.example.aihouse.models.Rule
import com.example.aihouse.models.User
import com.example.aihouse.models.UserGender
import com.example.aihouse.models.UserSetting

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
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

    @POST("api/users/settings/set")
    fun setUserInfo(@Body request: SetUserInfoRequest): Call<CustomResponse>

    @POST("api/users/settings/get")
    fun getUserInfo(@Body request: UserRequest): Call<CustomResponse>

    @POST("api/publications/getNotByAuthor")
    fun getFeedPublications(@Body request: UserRequest): Call<CustomResponse>

    @POST("api/publications/getByAuthor")
    fun getMyPublications(@Body request: UserRequest): Call<CustomResponse>

    @POST("api/users/subscribtions")
    fun getSubscribtions(@Body request: UserRequest): Call<CustomResponse>

    @POST("api/publications/add")
    fun createPublication(@Body request: CreatePublicationRequest): Call<CustomResponse>

    @POST("api/publications/searchByText")
    fun searchPublications(@Body request: SearchRequest): Call<CustomResponse>

    @POST("api/users/searchByText")
    fun searchAuthors(@Body request: SearchRequest): Call<CustomResponse>

    @POST("api/publications/like")
    fun likePublication(@Body request: LikePublicationRequest): Call<CustomResponse>
    @POST("api/users/subscribe")
    fun subscribe(@Body request: SubscribeReqest): Call<CustomResponse>

    @POST("api/publications/drafts/add")
    fun addDraft(@Body request: DraftRequest): Call<CustomResponse>
    @POST("api/publications/drafts/update")
    fun updateDraft(@Body request: DraftRequest): Call<CustomResponse>
    @POST("api/publications/drafts/get")
    fun getMyDrafts(@Body request: DraftRequest): Call<CustomResponse>

    @POST("api/users/notifications/get")
    fun getMyNotifs(@Body request: UserRequest): Call<CustomResponse>
    @POST("api/users/notifications/read")
    fun readNotif(@Body request: NotificationRequest): Call<CustomResponse>
    @GET("api/rules")
    fun getRules(): Call<CustomResponse>
}

// requests

data class GetUserRequest(
    val id: Int
)

data class NotificationRequest(
    val idNotification: Int
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

data class UserRequest(
    val idUser: Int
)

data class CreatePublicationRequest(
    val title: String,
    val text: String,
    val idUser: Int,
    val idDraft: Int?
)

data class SubscribeReqest(
    val idUser: Int,
    val idAuthor: Int,
    val type: String
)

data class SearchRequest(
    val fragment: String,
    val idUser: Int
)

data class DraftRequest(
    val id: Int?,
    val title: String?,
    val text: String?,
    val idUser: Int?
)

data class SetUserInfoRequest(
    val idUser: Int,
    val idGender: Int?,
    val birthday: String?,
    val aboutMe: String?,
    val notifTechnical: Boolean,
    val notifResponse: Boolean,
    val notifReference: Boolean,
    val notifSubscribe: Boolean,
    val notifLike: Boolean,
    val notifComment: Boolean,
    val privateShowSubscriber: Boolean,
    val mobileGetPush: Boolean
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
    val isSetLike: Boolean?,
    val drafts: List<Draft>?,
    val userSettings: UserSetting?,
    val genders: List<UserGender>?,
    val rules: List<Rule>?,
    val notifications: List<Notification>?
)