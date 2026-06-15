package com.flickfind.app.data.repository

import com.flickfind.app.BuildConfig
import com.flickfind.app.data.model.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkClient {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val api: TmdbApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.TMDB_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TmdbApiService::class.java)
    }
}

class MovieRepository {
    private val api = NetworkClient.api
    private val apiKey = BuildConfig.TMDB_API_KEY

    suspend fun getTrending(): Result<List<Movie>> = try {
        val response = api.getTrending(apiKey)
        Result.success(response.results)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getPopular(): Result<List<Movie>> = try {
        val response = api.getPopular(apiKey)
        Result.success(response.results)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getTopRated(): Result<List<Movie>> = try {
        val response = api.getTopRated(apiKey)
        Result.success(response.results)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getUpcoming(): Result<List<Movie>> = try {
        val response = api.getUpcoming(apiKey)
        Result.success(response.results)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getMovieDetail(movieId: Int): Result<Movie> = try {
        val response = api.getMovieDetail(movieId, apiKey)
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getMovieCredits(movieId: Int): Result<CreditsResponse> = try {
        val response = api.getMovieCredits(movieId, apiKey)
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getMovieVideos(movieId: Int): Result<VideoResponse> = try {
        val response = api.getMovieVideos(movieId, apiKey)
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getSimilarMovies(movieId: Int): Result<List<Movie>> = try {
        val response = api.getSimilarMovies(movieId, apiKey)
        Result.success(response.results)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun searchMovies(query: String): Result<List<Movie>> = try {
        val response = api.searchMovies(apiKey, query)
        Result.success(response.results)
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    suspend fun getGenres(): Result<List<Genre>> = try {
        val response = api.getGenres(apiKey)
        Result.success(response.genres)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun discoverMovies(genreId: Int?, year: Int?): Result<List<Movie>> = try {
        val response = api.discoverMovies(apiKey = apiKey, genreId = genreId, year = year)
        Result.success(response.results)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
