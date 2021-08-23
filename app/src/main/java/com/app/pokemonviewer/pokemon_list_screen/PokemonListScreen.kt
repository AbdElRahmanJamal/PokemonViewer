package com.app.pokemonviewer.pokemon_list_screen

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.pokemonviewer.R
import com.app.pokemonviewer.pokemon_list_screen.ui_model.PokemonItemUIModel
import com.app.pokemonviewer.ui.theme.RobotoCondensed
import com.app.pokemonviewer.util.*

@Composable
fun PokemonListScreenView(navController: NavController, context: Context) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = modifierFillMaxSize
    ) {
        Column(
            modifier = modifierWithPadding
        ) {
            Spacer(modifier = modifierWith16DPHeight)
            //create Pokemon Logo
            PokemonLogo()
            //show spacer
            Spacer(modifier = modifierWith16DPHeight)
            //show spacer
            Spacer(modifier = modifierWith16DPHeight)
            //show list
            PokemonList(
                navController = navController,
                context = context
            )
        }
    }
}

@Composable
fun PokemonLogo() {
    Image(
        painter = painterResource(id = R.drawable.pokemon_logo),
        contentDescription = "Pokemon Logo",
        modifier = modifierFillMaxWidth,
        alignment = Alignment.Center,

        )
}

@Composable
fun PokemonSearchBar(
    modifier: Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    var textFieldValue by remember {
        mutableStateOf("")
    }
    var isHintShown by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier) {
        BasicTextField(
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, CircleShape, false)
                .background(Color.White, CircleShape)
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .onFocusChanged {
                    isHintShown = !it.isFocused
                }
        )
        if (isHintShown) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = modifierWithPadding
            )
        }
    }

}

@Composable
fun PokemonList(
    navController: NavController,
    context: Context,
    viewModel: PokemonListScreenViewModel = hiltViewModel()
) {
    val pokemonList by remember {
        viewModel.getPokemonList()
        viewModel.pokemonList
    }
    val isLoadAllPokemon by remember { viewModel.isLoadAllPokemon }
    val isError by remember { viewModel.isError }
    val isLoading by remember { viewModel.isLoading }

    LazyColumn {
        val itemCount = if (pokemonList.size % 2 == 0) {
            pokemonList.size / 2
        } else {
            pokemonList.size / 2 + 1
        }
        items(itemCount) {
            if (it >= itemCount - 1 && !isLoadAllPokemon && !isLoading) {
                viewModel.getPokemonList()
            }
            PokemonListItemRow(
                rowIndex = it,
                pokemonRowList = pokemonList,
                navController = navController,
                context = context
            )
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if (isError.isNotEmpty()) {
            RetrySection(error = isError) {
                viewModel.getPokemonList()
            }
        }
    }

}

@Composable
fun PokemonListItemRow(
    rowIndex: Int,
    pokemonRowList: List<PokemonItemUIModel>,
    context: Context,
    navController: NavController
) {
    Column {
        Row {
            PokemonListItem(
                pokemonItemUIModel = pokemonRowList[rowIndex * 2],
                modifier = pokemonItemInRowModifier.weight(1f),
                context = context,
                navController = navController
            )
            Spacer(modifier = modifierWith16DPWidth)

            if (pokemonRowList.size >= rowIndex * 2 + 2) {
                PokemonListItem(
                    pokemonItemUIModel = pokemonRowList[rowIndex * 2 + 1],
                    modifier = pokemonItemInRowModifier.weight(1f),
                    context = context,
                    navController = navController
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = modifierWith16DPHeight)
    }
}

@Composable
fun PokemonListItem(
    pokemonItemUIModel: PokemonItemUIModel,
    modifier: Modifier,
    context: Context,
    navController: NavController
) {
    val defaultDominantColor = MaterialTheme.colors.surface

    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Box(modifier = modifier
        .background(
            Brush.verticalGradient(
                listOf(
                    dominantColor,
                    defaultDominantColor
                )
            )
        )
        .clickable {
            navController.navigate(
                "pokemon_detail_screen/{${pokemonItemUIModel.pokemonName}}/{${dominantColor.toArgb()}}"
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val bitmapState by remember {
                mutableStateOf(
                    getImageFromRemoteUrlAsBitMap(
                        pokemonItemUIModel.pokemonImageURL, context
                    )
                )
            }

            when (bitmapState.value) {
                is ResourceState.Loading -> {
                    ShowLoadingIndicator()
                }
                is ResourceState.Success -> {
                    bitmapState.value.data?.asImageBitmap()?.let {
                        ShowSuccessImage(it, pokemonItemUIModel)
                        calcDominantColor(bitmapState.value.data as Bitmap) { color ->
                            dominantColor = color
                        }

                    } ?: ShowErrorImage(pokemonItemUIModel)
                }
                is ResourceState.Error -> {
                    ShowErrorImage(pokemonItemUIModel)
                }
            }
            Text(
                text = pokemonItemUIModel.pokemonName,
                fontFamily = RobotoCondensed,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
            )
        }
    }


}

@Composable
private fun ShowLoadingIndicator() {
    CircularProgressIndicator(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.scale(0.5f)
    )
}

@Composable
private fun ShowSuccessImage(
    it: ImageBitmap,
    pokemonItemUIModel: PokemonItemUIModel
) {
    Image(
        bitmap = it,
        contentDescription = pokemonItemUIModel.pokemonName,
        modifier = imageModifier

    )
}

@Composable
private fun ShowErrorImage(pokemonItemUIModel: PokemonItemUIModel) {
    Image(
        alignment = Alignment.Center,
        painter = painterResource(R.drawable.pokemon_logo),
        contentDescription = pokemonItemUIModel.pokemonName,
        modifier = imageModifier
    )
}

@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }
}