package com.app.pokemonviewer.util

import androidx.navigation.NavType
import androidx.navigation.compose.navArgument

object ScreensArguments {
    val detailsScreenArguments = listOf(
        navArgument(AppScreenArgumentKeys.POKEMON_DETAIL_SCREEN_POKEMON_NAME) {
            type = NavType.StringType
        },
        navArgument(AppScreenArgumentKeys.POKEMON_DETAIL_SCREEN_DOMINANT_COLOR) {
            type = NavType.StringType
        }
    )
}