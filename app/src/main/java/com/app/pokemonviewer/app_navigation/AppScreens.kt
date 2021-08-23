package com.app.pokemonviewer.app_navigation

import com.app.pokemonviewer.util.AppScreenRoute


sealed class AppScreens(val route: String) {
    object PokemonListScreen : AppScreens(AppScreenRoute.POKEMON_LIST_SCREEN)
    object PokemonDetailScreen : AppScreens(AppScreenRoute.POKEMON_DETAIL_SCREEN)
}
