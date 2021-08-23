package com.app.pokemonviewer.util

import com.app.pokemonviewer.util.AppScreenArgumentKeys.POKEMON_DETAIL_SCREEN_DOMINANT_COLOR
import com.app.pokemonviewer.util.AppScreenArgumentKeys.POKEMON_DETAIL_SCREEN_POKEMON_NAME

object AppScreenRoute {
    const val POKEMON_LIST_SCREEN = "pokemon_list_screen"
    const val POKEMON_DETAIL_SCREEN =
        "pokemon_detail_screen/{$POKEMON_DETAIL_SCREEN_POKEMON_NAME}/{$POKEMON_DETAIL_SCREEN_DOMINANT_COLOR}"

}

object AppScreenArgumentKeys {
    const val POKEMON_DETAIL_SCREEN_POKEMON_NAME = "pokemon_detail_screen_pokemon_name"
    const val POKEMON_DETAIL_SCREEN_DOMINANT_COLOR = "pokemon_detail_screen_dominant_color"
}