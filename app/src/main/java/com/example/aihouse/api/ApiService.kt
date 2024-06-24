package com.example.aihouse.api
import com.example.aihouse.models.Complaint
import com.example.aihouse.models.Discussion
import com.example.aihouse.models.Draft
import com.example.aihouse.models.Notification
import com.example.aihouse.models.Publication
import com.example.aihouse.models.Rule
import com.example.aihouse.models.User
import com.example.aihouse.models.UserGender
import com.example.aihouse.models.UserSetting
import com.example.aihouse.models.Violation

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("api/users/registration")
    fun registerUser(@Body request: RegisterRequest): Call<CustomResponse>
    @POST("api/users/getCode")
    fun getCode(@Body request: RegisterRequest): Call<CustomResponse>
    @POST("api/users/get")
    fun getUsers(@Body request: UserRequest): Call<CustomResponse>

    @POST("api/users/getfull")
    fun getUserFull(@Body request: UserFullRequest): Call<CustomResponse>

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

    @POST("api/publications/get")
    fun getPublication(@Body request: PublicationRequest): Call<CustomResponse>

    @POST("api/publications/getByAuthor")
    fun getMyPublications(@Body request: UserRequest): Call<CustomResponse>

    @POST("api/users/subscribtions")
    fun getSubscribtions(@Body request: UserRequest): Call<CustomResponse>

    @POST("api/publications/add")
    fun createPublication(@Body request: CreatePublicationRequest): Call<CustomResponse>

    @POST("api/publications/like")
    fun likePublication(@Body request: LikePublicationRequest): Call<CustomResponse>

    @POST("api/comments/like")
    fun likeComment(@Body request: LikeCommentRequest): Call<CustomResponse>
    @POST("api/comments/add")
    fun sendComment(@Body request: SendCommentRequest): Call<CustomResponse>

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

    @GET("api/complaints")
    fun getViolations(): Call<CustomResponse>
    @POST("api/complaints/add")
    fun addComplaint(@Body request: ComplaintRequest): Call<CustomResponse>
    @POST("api/complaints/get")
    fun getComplaints(@Body request: ComplaintRequest): Call<CustomResponse>
    @POST("api/complaints/ban")
    fun banUser(@Body request: BanRequest): Call<CustomResponse>

    @GET("api/discussions")
    fun getDiscussions(): Call<CustomResponse>
    @POST("api/discussions/add")
    fun addDiscussion(@Body request: CreateDiscussionRequest): Call<CustomResponse>
    @POST("api/discussions/messages/add")
    fun addDiscussionMessage(@Body request: SendMessageRequest): Call<CustomResponse>
    @POST("api/discussions/get")
    fun getDiscussion(@Body request: GetDiscussionRequest): Call<CustomResponse>
    @POST("api/discussions/answered")
    fun setAnswered(@Body request: SetAnsweredRequest): Call<CustomResponse>
}

// requests

data class NotificationRequest(
    val idNotification: Int
)

data class LikePublicationRequest(
    val idUser: Int,
    val idPublication: Int,
    val type: String
)

data class LikeCommentRequest(
    val idUser: Int,
    val idComment: Int,
    val type: String
)

data class SendCommentRequest(
    val idUser: Int,
    val idPublication: Int,
    val text: String
)

data class SendMessageRequest(
    val idUser: Int,
    val idDiscussion: Int,
    val text: String
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

data class UserFullRequest(
    val idUser: Int,
    val idAuthor: Int
)

data class PublicationRequest(
    val idUser: Int,
    val id: Int
)

data class CreatePublicationRequest(
    val title: String,
    val text: String,
    val idUser: Int,
    val idDraft: Int?
)

data class CreateDiscussionRequest(
    val title: String,
    val question: String,
    val idUser: Int,
)

data class GetDiscussionRequest(
    val idDiscussion: Int
)

data class SetAnsweredRequest(
    val idDiscussion: Int,
    val idMessage: Int
)

data class SubscribeReqest(
    val idUser: Int,
    val idAuthor: Int,
    val type: String
)

data class ComplaintRequest(
    val idUser: Int?,
    val idPublication: Int,
    val idViolation: Int?
)

data class BanRequest(
    var idUser: Int?,
    val idPublication: Int?,
    var idViolation: Int?,
    var idAdmin: Int?,
    var days: Int?
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
    val publication: Publication?,
    val publications: List<Publication>?,
    val result: String?,
    val countLikes: Int?,
    val isSetLike: Boolean?,
    val drafts: List<Draft>?,
    val userSettings: UserSetting?,
    val genders: List<UserGender>?,
    val rules: List<Rule>?,
    val notifications: List<Notification>?,
    val verificationCode: Int?,
    val violations: List<Violation>?,
    val complaints: List<Complaint>?,
    val discussion: Discussion?,
    val discussions: List<Discussion>?
)