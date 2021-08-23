package com.app.pokemonviewer.data.repository

import com.app.pokemonviewer.data.pokemon_responses.PokemonApi
import com.app.pokemonviewer.data.pokemon_responses.pokemon_detail.PokemonDetails
import com.app.pokemonviewer.data.pokemon_responses.pokemon_list.PokemonList
import com.app.pokemonviewer.util.ResourceState
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PokemonViewerRepository @Inject constructor(
    private val pokemonApi: PokemonApi
) {
    suspend fun getPokemonList(
        limit: Int,
        offset: Int
    ): ResourceState<PokemonList> {
        val response = try {
            pokemonApi.getPokemonList(limit, offset)
        } catch (e: Exception) {
            return ResourceState.Error("Something didn't went as planned.")
        }
        return ResourceState.Success(response)
    }

    suspend fun getPokemonDetails(
        name: String
    ): ResourceState<PokemonDetails> {
        val response = try {
            pokemonApi.getPokemonDetails(name)
        } catch (e: Exception) {
            return ResourceState.Error("Something didn't went as planned.")
        }
        return ResourceState.Success(response)
    }
}