package com.akz.cinema.util


import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class PaletteManager {

    private var palette: Palette? = null
    private val _dominantSwatch = MutableStateFlow<Color?>(null)
    val dominantSwatch: Flow<Color?> = _dominantSwatch

    suspend fun makePaletteFromDrawable(drawable: Drawable, lighting: Float) {
        withContext(Dispatchers.Default) {
            val bitmap = drawable.toBitmap()
            palette = Palette.from(bitmap).generate()
            refreshDominantSwatch(lighting)
        }
    }

    private fun refreshDominantSwatch(lighting: Float) {
        val out = palette?.dominantSwatch?.hsl
        out?.let { ca ->
            _dominantSwatch.update {
                Color.hsl(
                    ca[0],
                    ca[1],
                    lighting
                )
            }
        }
    }

    suspend fun getDarkMutedSwatch(lighting: Float): Color? {
        if (lighting < 0 || lighting > 1) {
            return null
        }
        return suspendCancellableCoroutine {
            val res = palette?.darkMutedSwatch?.hsl
            val result = res?.let { arr ->
                Color.hsl(
                    arr[0],
                    arr[1],
                    lighting
                )
            }
            it.resume(result)
        }
    }

    suspend fun getDominantSwatch(lighting: Float): Color? {
        if (lighting < 0 || lighting > 1) {
            return null
        }
        return suspendCancellableCoroutine {
            val res = palette?.dominantSwatch?.hsl
            val result = res?.let { arr ->
                Color.hsl(
                    arr[0],
                    arr[1],
                    lighting
                )
            }
            it.resume(result)
        }
    }

    suspend fun getLightMutedSwatch(lighting: Float): Color? {
        if (lighting < 0 || lighting > 1) {
            return null
        }
        return suspendCancellableCoroutine {
            val res = palette?.lightMutedSwatch?.hsl
            val result = res?.let { arr ->
                Color.hsl(
                    arr[0],
                    arr[1],
                    lighting
                )
            }
            it.resume(result)
        }
    }
}