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
    var isSetSubscribe: Boolean?,
    var imagePath: String?,
    var gender: String?
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
    val author: User?,
    var countLikes: Int?,
    var isSetLike: Boolean?
    )

data class Publication(
    val id: Int?,
    val title: String?,
    val text: String?,
    val idAuthor: Int?,
    val dateCreate: String?,
    var countLikes: Int?,
    val countComments: Int?,
    val countComplaints: Int?,
    val author: User?,
    var isSetLike: Boolean?,
    var isSetSubscribe: Boolean?,
    var isSetComplaint: Boolean?,
    var comments: List<PublicationComment>?,
    val isRead: Boolean?
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

data class Violation(
    var id: Int,
    var text: String
)

data class Complaint(
    val id: Int,
    val text: String,
    val count: Int
)

data class DiscussionMessage(
    var id: Int,
    var text: String,
    var idDiscussion: Int,
    var idAuthor: Int,
    var dateCreate: String,
    var isAnswer: Boolean,
    var author: User
)

data class Discussion(
    var id: Int,
    var title: String,
    var textQuestion: String,
    var icon: String,
    var dateCreate: String,
    var dateUpdate: String,
    var idAuthor: Int,
    var countParticipants: Int,
    var isAnswered: Boolean,
    var messages: List<DiscussionMessage>?
)