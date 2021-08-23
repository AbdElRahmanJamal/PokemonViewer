package com.app.pokemonviewer.pokemon_detail_screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.pokemonviewer.R
import com.app.pokemonviewer.data.pokemon_responses.pokemon_detail.PokemonDetails
import com.app.pokemonviewer.data.pokemon_responses.pokemon_detail.Type
import com.app.pokemonviewer.util.*
import kotlin.math.roundToInt

@Composable
fun PokemonDetailScreen(
    navController: NavController,
    viewModel: PokemonDetailScreenViewModel = hiltViewModel(),
    pokemonName: String,
    dominantColor: Color
) {

    val pokemonInfoState by remember {
        viewModel.getPokemonDetails(pokemonName)
        viewModel.pokemonInfoState
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColor)
            .padding(bottom = 16.dp)
    ) {
        //arrowView
        PokemonDetailsBackArrowSection(
            navController = navController,
            modifier = pokemonDetailScreenArrowSection
                .fillMaxWidth()
                .align(Alignment.TopStart)
        )
        //details state
        PokemonDetailsStateDrawer(
            pokemonInfo = pokemonInfoState,
            modifier = detailsScreenSuccessStateModifier
                .background(MaterialTheme.colors.surface)
                .align(Alignment.BottomCenter),
            loadingModifier = Modifier
                .size(75.dp)
                .align(Alignment.Center)
        )
    }
    //pokemon image
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (pokemonInfoState is ResourceState.Success) {
            pokemonInfoState.data?.sprites?.let {

                val bitMapState = getImageFromRemoteUrlAsBitMap(
                    it.front_default,
                    LocalContext.current
                ).value

                when (bitMapState) {
                    is ResourceState.Loading -> {
                        DetailScreenImagePlaceHolder(pokemonInfoState.data?.name ?: "")
                    }
                    is ResourceState.Success -> {
                        bitMapState.data?.asImageBitmap()?.let { bitmap ->
                            Image(
                                bitmap = bitmap,
                                contentDescription = pokemonInfoState.data?.name ?: "",
                                modifier = Modifier
                                    .size(200.dp)
                                    .offset(y = 16.dp)
                            )
                        } ?: DetailScreenImagePlaceHolder(pokemonInfoState.data?.name ?: "")

                    }
                    is ResourceState.Error -> {
                        DetailScreenImagePlaceHolder(pokemonInfoState.data?.name ?: "")
                    }
                }
            }
        }
    }
}

@Composable
fun PokemonDetailsBackArrowSection(
    navController: NavController,
    modifier: Modifier
) {

    Box(
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = arrowBackModifier
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}

@Composable
fun PokemonDetailsStateDrawer(
    pokemonInfo: ResourceState<PokemonDetails>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier
) {
    when (pokemonInfo) {
        is ResourceState.Success -> {
            PokemonDetailSection(pokemonInfo.data, modifier)
        }
        is ResourceState.Error -> {
            Text(
                text = pokemonInfo.message ?: "",
                color = Color.Red,
                modifier = modifier
            )
        }
        is ResourceState.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = loadingModifier
            )
        }
    }
}


@Composable
fun PokemonDetailSection(
    pokemonInfo: PokemonDetails?,
    modifier: Modifier
) {
    pokemonInfo?.let {
        val scrollState = rememberScrollState()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .offset(y = 75.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "#${pokemonInfo.id} ${pokemonInfo.name.replaceFirstChar { it.uppercase() }}",
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onSurface
            )

            //Section type
            PokemonTypeSection(types = pokemonInfo.types)
            //Section details
            PokemonDetailDataSection(
                pokemonWeight = pokemonInfo.weight,
                pokemonHeight = pokemonInfo.height
            )
            //section BaseState
            PokemonBaseStats(pokemonInfo = pokemonInfo)

        }
    }

}

@Composable
fun PokemonTypeSection(types: List<Type>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
    ) {
        for (type in types) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .clip(CircleShape)
                    .background(parseTypeToColor(type))
                    .height(35.dp)
            ) {
                Text(
                    text = type.type.name.replaceFirstChar {
                        it.uppercase()
                    },
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun PokemonDetailDataSection(
    pokemonWeight: Int,
    pokemonHeight: Int,
    sectionHeight: Dp = 80.dp
) {
    val pokemonWeightInKg = remember {
        (pokemonWeight * 100f).roundToInt() / 1000f
    }
    val pokemonHeightInMeters = remember {
        (pokemonHeight * 100f).roundToInt() / 1000f
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        PokemonDetailDataItem(
            dataValue = pokemonWeightInKg,
            dataUnit = "kg",
            dataIcon = painterResource(id = R.drawable.ic_weight),
            modifier = Modifier.weight(1f)
        )
        Spacer(
            modifier = Modifier
                .size(1.dp, sectionHeight)
                .background(Color.LightGray)
        )
        PokemonDetailDataItem(
            dataValue = pokemonHeightInMeters,
            dataUnit = "m",
            dataIcon = painterResource(id = R.drawable.ic_height),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun PokemonDetailDataItem(
    dataValue: Float,
    dataUnit: String,
    dataIcon: Painter,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(
            painter = dataIcon,
            contentDescription = dataUnit,
            tint = MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$dataValue$dataUnit",
            color = MaterialTheme.colors.onSurface
        )
    }
}

@Composable
fun PokemonBaseStats(
    pokemonInfo: PokemonDetails,
    animDelayPerItem: Int = 100
) {
    val maxBaseStat = remember {
        pokemonInfo.stats.maxOf { it.base_stat }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = "Base stats:",
            fontSize = 20.sp,
            color = MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))

        for (i in pokemonInfo.stats.indices) {
            val stat = pokemonInfo.stats[i]
            PokemonStat(
                statName = parseStatToAbbr(stat),
                statValue = stat.base_stat,
                statMaxValue = maxBaseStat,
                statColor = parseStatToColor(stat),
                animDelay = i * animDelayPerItem
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun PokemonStat(
    statName: String,
    statValue: Int,
    statMaxValue: Int,
    statColor: Color,
    height: Dp = 28.dp,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercent = animateFloatAsState(
        targetValue = if (animationPlayed) {
            statValue / statMaxValue.toFloat()
        } else 0f,
        animationSpec = tween(
            animDuration,
            animDelay
        )
    )
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .background(
                if (isSystemInDarkTheme()) {
                    Color(0xFF505050)
                } else {
                    Color.LightGray
                }
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(curPercent.value)
                .clip(CircleShape)
                .background(statColor)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = statName,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = (curPercent.value * statMaxValue).toInt().toString(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DetailScreenImagePlaceHolder(name: String) {
    Image(
        alignment = Alignment.Center,
        painter = painterResource(R.drawable.pokemon_logo),
        contentDescription = name,
        modifier = Modifier
            .size(200.dp)
            .offset(y = 16.dp)
    )
}

