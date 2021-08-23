package com.app.pokemonviewer.data.pokemon_responses.pokemon_detail

import com.google.gson.annotations.SerializedName

data class GenerationV(
    @SerializedName("black-white")
    val blackWhite: BlackWhite
)