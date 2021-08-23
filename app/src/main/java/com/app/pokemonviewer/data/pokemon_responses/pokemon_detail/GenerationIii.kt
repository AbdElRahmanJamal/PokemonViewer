package com.app.pokemonviewer.data.pokemon_responses.pokemon_detail

import com.google.gson.annotations.SerializedName

data class GenerationIii(
    val emerald: Emerald,
    @SerializedName("firered-leafgreen")
    val fireredLeafGreen: FireredLeafgreen,
    @SerializedName("ruby-sapphire")
    val rubySapphire: RubySapphire
)