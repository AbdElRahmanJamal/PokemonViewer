package com.app.pokemonviewer.di

import com.app.pokemonviewer.data.pokemon_responses.PokemonApi
import com.app.pokemonviewer.data.repository.PokemonViewerRepository
import com.app.pokemonviewer.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providePokemonRepository(
        api: PokemonApi
    ) = PokemonViewerRepository(api)

    @Singleton
    @Provides
    fun providePokeApi(): PokemonApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(PokemonApi::class.java)
    }
}