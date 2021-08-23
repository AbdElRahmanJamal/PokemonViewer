package com.app.pokemonviewer.data.pokemon_responses

import com.app.pokemonviewer.data.pokemon_responses.pokemon_detail.PokemonDetails
import com.app.pokemonviewer.data.pokemon_responses.pokemon_list.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface PokemonApi {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonList

    @GET("pokemon/{name}")
    suspend fun getPokemonDetails(
        @Path("name") name: String
    ): PokemonDetails
}