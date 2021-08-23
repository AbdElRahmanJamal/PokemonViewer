package com.app.pokemonviewer.pokemon_detail_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.pokemonviewer.data.pokemon_responses.pokemon_detail.PokemonDetails
import com.app.pokemonviewer.data.repository.PokemonViewerRepository
import com.app.pokemonviewer.util.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailScreenViewModel @Inject constructor(
    private val repository: PokemonViewerRepository
) : ViewModel() {

    var pokemonInfoState = mutableStateOf<ResourceState<PokemonDetails>>(ResourceState.Loading())

    fun getPokemonDetails(name: String) {
        viewModelScope.launch {

            when (val result = repository.getPokemonDetails(name = name)) {
                is ResourceState.Loading -> {
                    pokemonInfoState.value = ResourceState.Loading()
                }
                is ResourceState.Success -> {
                    pokemonInfoState.value = ResourceState.Success(result.data!!)
                }

                is ResourceState.Error -> {
                    pokemonInfoState.value = ResourceState.Error("Failed Getting Info...")
                }
            }
        }
    }
}