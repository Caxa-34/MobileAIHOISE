package com.example.aihouse.api
import android.content.Context
import android.util.Log
import android.widget.ImageView
import com.example.aihouse.models.User
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

object ApiHelper {
    fun parseDateTime(dateTimeString: String): LocalDateTime {
        val formatter = DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm:ss")
            .optionalStart()
            .appendFraction(ChronoField.MILLI_OF_SECOND, 0, 3, true)
            .optionalEnd()
            .toFormatter()

        val localDateTime = LocalDateTime.parse(dateTimeString, formatter)
        // Добавляем один день
        return localDateTime
    }

    fun formatDateTime(localDateTime: LocalDateTime): String {
        val currentDateTime = LocalDateTime.now()

        // Форматтер для вывода времени HH:mm
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        // Форматтер для вывода даты и времени dd.MM.yyyy HH:mm
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

        // Проверяем, если дата и время сегодня
        if (localDateTime.toLocalDate() == currentDateTime.toLocalDate()) {
            return localDateTime.format(timeFormatter) // Время сегодня
        }

        // Проверяем, если дата и время вчера
        val yesterdayDateTime = currentDateTime.minusDays(1)
        if (localDateTime.toLocalDate() == yesterdayDateTime.toLocalDate()) {
            return "вчера " + localDateTime.format(timeFormatter) // Время вчера
        }

        // Проверяем, если дата и время больше вчера и до полугода назад
        val sixMonthsAgoDateTime = currentDateTime.minusMonths(6)
        if (localDateTime.isAfter(sixMonthsAgoDateTime)) {
            return localDateTime.format(DateTimeFormatter.ofPattern("dd.MM. HH:mm")) // Дата и время до полугода назад
        }

        // Все остальные случаи (больше полугода назад)
        return localDateTime.format(dateTimeFormatter) // Дата и время больше полугода назад
    }

    suspend fun registerUser(request: RegisterRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.registerUser(request).execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getCode(request: RegisterRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.getCode(request).execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    fun loadImage(context: Context, imageUrl: String, imageView: ImageView) {
        Picasso.get()
            .load("http://94.228.126.25:81/" + imageUrl)
            .into(imageView)
    }

    suspend fun getUsers(request: UserRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.getUsers(request).execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getFullUser(request: UserFullRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.getUserFull(request).execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun loginEmailUser(request: LoginRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.loginEmailUser(request).execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun loginNameUser(request: LoginRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.loginNameUser(request).execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun setUserInfo(request: SetUserInfoRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.setUserInfo(request).execute()
            }
            if (response.isSuccessful) {
                //Log.e("FEED BODY JSON", response.body().toString())
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserInfo(request: UserRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.getUserInfo(request).execute()
            }
            if (response.isSuccessful) {
                //Log.e("FEED BODY JSON", response.body().toString())
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFeedPublications(request: UserRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.getFeedPublications(request).execute()
            }
            if (response.isSuccessful) {
                //Log.e("FEED BODY JSON", response.body().toString())
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getPublication(request: PublicationRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.getPublication(request).execute()
            }
            if (response.isSuccessful) {
                //Log.e("FEED BODY JSON", response.body().toString())
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun createPublications(request: CreatePublicationRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.createPublication(request).execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMyPublications(request: UserRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.getMyPublications(request).execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMySubsctiptions(request: UserRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.getSubscribtions(request).execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun likePublication(request: LikePublicationRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.likePublication(request).execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun likeComment(request: LikeCommentRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.likeComment(request).execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun sendComment(request: SendCommentRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.sendComment(request).execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun subscribe(request: SubscribeReqest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.subscribe(request).execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun addDraft(request: DraftRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.addDraft(request).execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun updateDraft(request: DraftRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.updateDraft(request).execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getMyDrafts(request: DraftRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.getMyDrafts(request).execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRules(): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.getRules().execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getViolations(): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.getViolations().execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDiscussions(): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.getDiscussions().execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createDiscussion(request: CreateDiscussionRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.addDiscussion(request).execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDiscussion(request: GetDiscussionRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.getDiscussion(request).execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendMessage(request: SendMessageRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.addDiscussionMessage(request).execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun setAnswer(request: SetAnsweredRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.setAnswered(request).execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getComplaints(request: ComplaintRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.getComplaints(request).execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addComplaint(request: ComplaintRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.addComplaint(request).execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun banUser(request: BanRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.banUser(request).execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMyNotifs(request: UserRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.getMyNotifs(request).execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun readNotif(request: NotificationRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.readNotif(request).execute()
            }
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, CustomResponse::class.java)
                Result.success(errorResponse)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}

