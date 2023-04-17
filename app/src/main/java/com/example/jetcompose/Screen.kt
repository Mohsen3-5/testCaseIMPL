package com.example.jetcompose

sealed class Screen(val route: String) {
    object HomeActivity: Screen("/home")
}
