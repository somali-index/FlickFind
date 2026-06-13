package com.example.flickfind.ui.Navigation

import kotlinx.serialization.Serializable


@Serializable
object AuthRoute

@Serializable
object HomeRoute

@Serializable
object ProfileRoute

@Serializable
object SavedMoviesRoute

@Serializable
object CollectionsRoute

@Serializable
data class CollectionDetailRoute(val collectionId: String, val collectionName: String)

@Serializable
object UnderDevelopmentRoute

@Serializable
data class MovieDetailRoute(val movieId: String)