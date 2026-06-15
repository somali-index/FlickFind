package com.flickfind.app.data.model

import com.google.gson.annotations.SerializedName

// ─── Movie ───────────────────────────────────────────────
data class Movie(
    val id: Int = 0,
    val title: String = "",
    @SerializedName("original_title") val originalTitle: String = "",
    val overview: String = "",
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("backdrop_path") val backdropPath: String? = null,
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("vote_count") val voteCount: Int = 0,
    @SerializedName("release_date") val releaseDate: String = "",
    @SerializedName("genre_ids") val genreIds: List<Int> = emptyList(),
    val genres: List<Genre> = emptyList(),
    val runtime: Int = 0,
    @SerializedName("original_language") val originalLanguage: String = "",
    val status: String = "",
    val popularity: Double = 0.0,
    val adult: Boolean = false
) {
    val posterUrl: String get() = if (!posterPath.isNullOrBlank()) "https://image.tmdb.org/t/p/w500${if (posterPath.startsWith("/")) "" else "/"}$posterPath" else ""
    val backdropUrl: String get() = if (!backdropPath.isNullOrBlank()) "https://image.tmdb.org/t/p/w780${if (backdropPath.startsWith("/")) "" else "/"}$backdropPath" else ""
    val ratingFormatted: String get() = "%.1f".format(voteAverage)
    val releaseYear: String get() = if (releaseDate.length >= 4) releaseDate.substring(0, 4) else releaseDate
    val runtimeFormatted: String get() {
        if (runtime == 0) return "N/A"
        val h = runtime / 60; val m = runtime % 60
        return if (h > 0) "${h}g ${m}p" else "${m}p"
    }
}

// ─── Responses ───────────────────────────────────────────
data class MovieListResponse(
    val page: Int = 1,
    val results: List<Movie> = emptyList(),
    @SerializedName("total_pages") val totalPages: Int = 0,
    @SerializedName("total_results") val totalResults: Int = 0
)

// ─── Genre ───────────────────────────────────────────────
data class Genre(
    val id: Int = 0,
    val name: String = ""
)

data class GenreListResponse(
    val genres: List<Genre> = emptyList()
)

// ─── Credits / Cast ──────────────────────────────────────
data class CreditsResponse(
    val id: Int = 0,
    val cast: List<CastMember> = emptyList(),
    val crew: List<CrewMember> = emptyList()
)

data class CastMember(
    val id: Int = 0,
    val name: String = "",
    val character: String = "",
    @SerializedName("profile_path") val profilePath: String? = null,
    val order: Int = 0
) {
    val profileUrl: String get() = if (!profilePath.isNullOrBlank()) "https://image.tmdb.org/t/p/w185${if (profilePath.startsWith("/")) "" else "/"}$profilePath" else ""
}

data class CrewMember(
    val id: Int = 0,
    val name: String = "",
    val job: String = "",
    val department: String = ""
)

// ─── Videos ──────────────────────────────────────────────
data class VideoResponse(
    val id: Int = 0,
    val results: List<Video> = emptyList()
)

data class Video(
    val id: String = "",
    val key: String = "",
    val name: String = "",
    val site: String = "",
    val type: String = ""
) {
    val isYouTubeTrailer: Boolean get() = site == "YouTube" && type == "Trailer"
    val youTubeUrl: String get() = "https://www.youtube.com/watch?v=$key"
}

// ─── UI State ─────────────────────────────────────────────
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
