package com.example.aihouse.api
import android.util.Log
import com.example.aihouse.models.User
import com.google.gson.Gson
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
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        return localDateTime.format(formatter)
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
    suspend fun getUserById(request: GetUserRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.getUserById(request).execute()
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

    suspend fun searchAuthors(request: SearchRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.searchAuthors(request).execute()
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
    suspend fun searchPublications(request: SearchRequest): Result<CustomResponse> {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.searchPublications(request).execute()
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

