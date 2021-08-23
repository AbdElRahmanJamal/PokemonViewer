package com.app.pokemonviewer.pokemon_list_screen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.pokemonviewer.data.repository.PokemonViewerRepository
import com.app.pokemonviewer.pokemon_list_screen.ui_model.PokemonItemUIModel
import com.app.pokemonviewer.util.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListScreenViewModel @Inject constructor(
    private val repository: PokemonViewerRepository
) : ViewModel() {

    private var curPage = 0
    private val numberOfPokemonPerApiCall = 20
    var pokemonList = mutableStateOf<List<PokemonItemUIModel>>(listOf())
    var isError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var isLoadAllPokemon = mutableStateOf(false)


    fun getPokemonList() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getPokemonList(
                numberOfPokemonPerApiCall,
                curPage * numberOfPokemonPerApiCall
            )

            when (result) {
                is ResourceState.Loading -> {
                    isLoading.value = true
                }
                is ResourceState.Success -> {
                    isLoadAllPokemon.value =
                        curPage * numberOfPokemonPerApiCall >= result.data?.count ?: 0
                    val pokemonItemUIModel = result.data?.results?.mapIndexed { index, entry ->
                        val number = if (entry.url.endsWith("/")) {
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        } else {
                            entry.url.takeLastWhile { it.isDigit() }
                        }
                        val url =
                            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                        PokemonItemUIModel(entry.name.lowercase(), url, number.toInt())
                    } ?: emptyList()
                    curPage++

                    isError.value = ""
                    isLoading.value = false
                    pokemonList.value += pokemonItemUIModel
                }

                is ResourceState.Error -> {
                    isError.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }
}
