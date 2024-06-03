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
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
            .optionalStart()
            .appendFraction(ChronoField.MILLI_OF_SECOND, 0, 3, true)
            .optionalEnd()
            .appendPattern("'Z'")
            .toFormatter()

        return LocalDateTime.parse(dateTimeString, formatter)
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
    suspend fun getFeedPublications(request: GetFeedPublicaionsRequest): Result<CustomResponse> {
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
}

