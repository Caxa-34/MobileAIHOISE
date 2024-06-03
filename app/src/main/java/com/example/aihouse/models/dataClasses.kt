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
    val countLetters: Int?
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
    val isSetLike: Boolean?
)

data class Notification(
    val id: Int?,
    val idRecipient: Int?,
    val idSender: Int?,
    val idType: Int?,
    val wasRead: Boolean?
)

