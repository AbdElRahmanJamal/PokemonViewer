package com.app.pokemonviewer.data.pokemon_responses.pokemon_detail

import com.google.gson.annotations.SerializedName

data class Other(
    val dream_world: DreamWorld,
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtwork
)