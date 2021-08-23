package com.app.pokemonviewer.app_navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.pokemonviewer.pokemon_detail_screen.PokemonDetailScreen
import com.app.pokemonviewer.pokemon_list_screen.PokemonListScreenView
import com.app.pokemonviewer.util.AppScreenArgumentKeys
import com.app.pokemonviewer.util.ScreensArguments

@Composable
fun SetUpAppNavigation(context: Context) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.PokemonListScreen.route) {
        composable(AppScreens.PokemonListScreen.route) {
            //see list fragment
            PokemonListScreenView(navController = navController, context = context)
        }
        composable(
            AppScreens.PokemonDetailScreen.route,
            arguments = ScreensArguments.detailsScreenArguments
        ) {
            val pokemonName = remember {
                it.arguments?.getString(AppScreenArgumentKeys.POKEMON_DETAIL_SCREEN_POKEMON_NAME)
                    ?.replace("{","")
                    ?.replace("}","")
            } ?: ""
            var pokemonColor = Color.White
            it.arguments?.getString(AppScreenArgumentKeys.POKEMON_DETAIL_SCREEN_DOMINANT_COLOR)
                ?.let {
                    runCatching {
                        it.replace("{", "").replace("}", "").toInt()
                    }.onSuccess { intColor ->
                        pokemonColor = Color(intColor)
                    }
                } ?: Color.White

            PokemonDetailScreen(
                navController = navController,
                pokemonName = pokemonName,
                dominantColor = pokemonColor
            )
        }
    }
}