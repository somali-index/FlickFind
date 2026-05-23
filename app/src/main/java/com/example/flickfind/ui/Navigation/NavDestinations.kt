package com.example.flickfind.ui.Navigation

import kotlinx.serialization.Serializable


@Serializable
object AuthRoute

@Serializable
object HomeRoute

@Serializable
data class MovieDetailRoute(val movieId: String)