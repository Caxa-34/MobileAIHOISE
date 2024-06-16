package com.example.aihouse.models

import java.time.LocalDate
import java.time.LocalDateTime

data class User (
    val id: Int,
    val name: String,
    val email: String,
    val dateRegistration: String?,
    val idAvatarImage: Int,
    val idRole: Int,
    val idGender: Int?,
    val birthday: String?,
    val age: Int?,
    val aboutMe: String?,
    val idState: Int?,
    val countLetters: Int?,
    var isSetSubscribe: Boolean?
)

data class Draft (
    val id: Int?,
    val title: String?,
    val text: String?,
    val idAuthor: Int?,
    val dateUpdate: String?
)

data class Rule (
    val id: Int,
    val text: String
)
data class Image (
    val id: Int,
    val name: String,
    val idTypeOwner: Int,
    val idTypeImage: Int,
    val dateDownload: String
)

data class UserGender (
    val id: Int,
    val title: String,
)

data class UserSetting (
    val idGender: Int?,
    val birthday: String?,
    val aboutMe: String?,
    val age: Int?,
    val notifTechnical: Boolean?,
    val notifResponse: Boolean?,
    val notifReference: Boolean?,
    val notifSubscribe: Boolean?,
    val notifLike: Boolean?,
    val notifComment: Boolean?,
    val privateShowSubscriber: Boolean?,
    val mobileGetPush: Boolean?
)

data class PublicationComment(
    val id: Int?,
    val text: String?,
    val idPublication: Int?,
    val idAuthor: Int?,
    val dateCreate: String?,
    val countLikes: Int?
)

data class Publication(
    val id: Int?,
    val title: String?,
    val text: String?,
    val idAuthor: Int?,
    val dateCreate: String?,
    val countLikes: Int?,
    val countComments: Int?,
    val countComplaints: Int?,
    val countLetters: Int?,
    val author: User?,
    val isSetLike: Boolean?,
    var isSetSubscribe: Boolean?
)

data class Notification(
    val id: Int?,
    val idRecipient: Int?,
    val idSender: Int?,
    val idPublication: Int?,
    val idDiscussion: Int?,
    val idType: Int?,
    val wasRead: Boolean?,
    val title: String?,
    val text: String?,
    val dateCreate: String?
)

