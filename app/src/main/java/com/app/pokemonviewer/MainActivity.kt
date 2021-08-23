package com.app.pokemonviewer


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import com.app.pokemonviewer.app_navigation.SetUpAppNavigation
import com.app.pokemonviewer.ui.theme.PokemonViewerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokemonViewerTheme {
                SetUpAppNavigation(LocalContext.current)

            }
        }
    }
}
