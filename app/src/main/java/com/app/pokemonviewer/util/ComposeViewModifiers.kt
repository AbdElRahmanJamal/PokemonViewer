package com.app.pokemonviewer.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


val pokemonItemInRowModifier = Modifier
    .shadow(5.dp, RoundedCornerShape(10.dp))
    .clip(RoundedCornerShape(10.dp))
    .aspectRatio(1f)


val imageModifier = Modifier
    .size(120.dp)

val modifierFillMaxSize = Modifier
    .fillMaxSize()

val modifierFillMaxWidth = Modifier
    .fillMaxWidth()

val modifierWith16DPHeight = Modifier
    .height(16.dp)

val modifierWith16DPWidth = Modifier
    .width(16.dp)

val modifierWithPadding = Modifier
    .padding(horizontal = 16.dp, vertical = 16.dp)

val pokemonDetailScreenArrowSection = Modifier
    .fillMaxWidth()
    .fillMaxHeight(0.2f)
    .background(
        Brush.verticalGradient(
            listOf(
                Color.Black,
                Color.Transparent
            )
        )
    )
val arrowBackModifier = Modifier
    .size(36.dp)
    .offset(16.dp, 30.dp)

val detailsScreenSuccessStateModifier = Modifier
    .fillMaxSize()
    .padding(
        top = 16.dp + 200.dp / 2f,
        start = 16.dp,
        end = 16.dp,
        bottom = 16.dp
    )
    .shadow(10.dp, RoundedCornerShape(10.dp))
    .clip(RoundedCornerShape(10.dp))

