package com.app.pokemonviewer.data.pokemon_responses.pokemon_detail

data class Move(
    val move: MoveX,
    val version_group_details: List<VersionGroupDetail>
)