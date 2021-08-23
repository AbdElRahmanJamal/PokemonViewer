package com.app.pokemonviewer.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition


@SuppressLint("UnrememberedMutableState")
fun getImageFromRemoteUrlAsBitMap(
    url: String,
    context: Context
): MutableState<ResourceState<Bitmap>> {

    val bitmapState: MutableState<ResourceState<Bitmap>> = mutableStateOf(ResourceState.Loading())

    // get network image
    Glide.with(context)
        .asBitmap()
        .load(url)
        .into(object : CustomTarget<Bitmap>() {
            override fun onLoadStarted(placeholder: Drawable?) {
                bitmapState.value = ResourceState.Loading()
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                bitmapState.value =
                    ResourceState.Error("Failed while downloading image")
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
            override fun onResourceReady(
                resource: Bitmap,
                transition: Transition<in Bitmap>?
            ) {
                bitmapState.value = ResourceState.Success(resource)
            }
        })
    return bitmapState
}

fun calcDominantColor(bitmap: Bitmap, onFinish: (Color) -> Unit) {
    val bmp = bitmap.copy(Bitmap.Config.ARGB_8888, true)

    Palette.from(bmp).generate { palette ->
        palette?.dominantSwatch?.rgb?.let { colorValue ->
            onFinish(Color(colorValue))
        }
    }
}