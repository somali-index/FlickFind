package com.flickfind.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.flickfind.app.R
import com.flickfind.app.ui.about.AboutScreen
import com.flickfind.app.ui.detail.MovieDetailScreen
import com.flickfind.app.ui.home.FavoritesScreen
import com.flickfind.app.ui.home.HomeScreen
import com.flickfind.app.ui.home.ProfileScreen
import com.flickfind.app.ui.login.LoginScreen
import com.flickfind.app.ui.login.RegisterScreen
import com.flickfind.app.ui.search.SearchScreen
import com.flickfind.app.ui.splash.SplashScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Search : Screen("search")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
    object About : Screen("about")
    object Detail : Screen("detail/{movieId}/{movieTitle}") {
        fun createRoute(movieId: Int, title: String) = "detail/$movieId/$title"
    }
}

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun FlickFindApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            if (currentRoute in listOf(
                    Screen.Home.route,
                    Screen.Search.route,
                    Screen.Favorites.route,
                    Screen.Profile.route
                )
            ) {
                BottomNavigationBar(navController = navController, currentRoute = currentRoute)
            }
        }
    ) { innerPadding ->
        FlickFindNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, currentRoute: String?) {
    val items = listOf(
        BottomNavItem("Trang chủ", Icons.Default.Home, Screen.Home.route),
        BottomNavItem("Tìm kiếm", Icons.Default.Search, Screen.Search.route),
        BottomNavItem("Yêu thích", Icons.Default.Favorite, Screen.Favorites.route),
        BottomNavItem("Cá nhân", Icons.Default.Person, Screen.Profile.route)
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun FlickFindNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onNavigateToHome = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }, onNavigateToLogin = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToDetail = { movieId, title ->
                    navController.navigate(Screen.Detail.createRoute(movieId, title))
                }
            )
        }
        composable(Screen.Search.route) {
            SearchScreen(
                onNavigateToDetail = { movieId, title ->
                    navController.navigate(Screen.Detail.createRoute(movieId, title))
                }
            )
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onNavigateToDetail = { movieId, title ->
                    navController.navigate(Screen.Detail.createRoute(movieId, title))
                }
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true } // Clear backstack completely
                    }
                },
                onNavigateToAbout = {
                    navController.navigate(Screen.About.route)
                }
            )
        }
        composable(Screen.About.route) {
            AboutScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.Detail.route) { backStackEntry ->
            val movieIdStr = backStackEntry.arguments?.getString("movieId")
            val title = backStackEntry.arguments?.getString("movieTitle") ?: ""
            val movieId = movieIdStr?.toIntOrNull() ?: 0
            MovieDetailScreen(
                movieId = movieId,
                movieTitle = title,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
