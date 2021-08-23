package com.app.pokemonviewer.data.pokemon_responses.pokemon_detail

data class HeldItem(
    val item: Item,
    val version_details: List<VersionDetail>
)