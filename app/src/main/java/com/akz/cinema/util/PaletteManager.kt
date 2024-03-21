package com.akz.cinema.util


import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext

class PaletteManager {

    private var palette: Palette? = null
    private val _dominantSwatchChanel = Channel<Color?>()
    val dominantSwatch: Flow<Color?> = _dominantSwatchChanel.receiveAsFlow()

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
            _dominantSwatchChanel.trySend(
                Color.hsl(
                    ca[0],
                    ca[1],
                    lighting
                )
            )
        }
    }
}