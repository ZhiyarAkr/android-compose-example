package com.akz.cinema.util


import android.content.Context
import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.imageLoader
import coil.request.ImageRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject


class PaletteManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val mutex = Mutex()
    private var palette: Palette? = null
    private val _dominantSwatchChanel = Channel<Color?>()
    private val _colors = mutableMapOf<String, Color>()
    val dominantSwatch: Flow<Color?> = _dominantSwatchChanel.receiveAsFlow()

    suspend fun makePaletteFromUri(uri: String, lighting: Float) {
        if (_colors.containsKey(uri)) {
            _dominantSwatchChanel.send(_colors[uri])
            return
        }
        mutex.withLock {
            withContext(Dispatchers.IO) {
                val bitmap = getBitmap(uri)
                bitmap?.let { bitmap1 ->
                    palette = Palette.from(bitmap1).generate()
                    val color = getDominantSwatch(lighting)
                    color?.let {
                        _colors[uri] = it
                        _dominantSwatchChanel.send(it)
                    }
                }
            }
        }
    }

    private suspend fun getBitmap(imageUrl: String): Bitmap? {
        val request = ImageRequest.Builder(context)
            .allowHardware(false)
            .allowConversionToBitmap(true)
            .data(imageUrl)
            .build()

        val image = context.imageLoader.execute(request).drawable ?: return null
        return image.toBitmap()
    }

    private fun getDominantSwatch(lighting: Float): Color? {
        val out = palette?.dominantSwatch?.hsl
        return out?.let { ca ->
            Color.hsl(
                ca[0],
                ca[1],
                lighting
            )
        }
    }
}