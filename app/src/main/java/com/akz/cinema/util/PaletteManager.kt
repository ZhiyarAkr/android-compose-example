package com.akz.cinema.util


import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
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
import javax.inject.Singleton

@Singleton
class PaletteManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val mutex = Mutex()
    private var palette: Palette? = null
    private val _dominantSwatchChanel = Channel<Color?>()
    val dominantSwatch: Flow<Color?> = _dominantSwatchChanel.receiveAsFlow()

    suspend fun makePaletteFromDrawable(drawable: Drawable, lighting: Float) {
        mutex.withLock {
            withContext(Dispatchers.Default) {
                val bitmap = drawable.toBitmap()
                palette = Palette.from(bitmap).generate()
                refreshDominantSwatch(lighting)
            }
        }
    }

    suspend fun makePaletteFromUri(uri: String, lighting: Float) {
        mutex.withLock {
            withContext(Dispatchers.Default) {
                val bitmap = getBitmap(uri)
                bitmap?.let {
                    palette = Palette.from(it).generate()
                    refreshDominantSwatch(lighting)
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

    private suspend fun refreshDominantSwatch(lighting: Float) {
        val out = palette?.dominantSwatch?.hsl
        out?.let { ca ->
            _dominantSwatchChanel.send(
                Color.hsl(
                    ca[0],
                    ca[1],
                    lighting
                )
            )
        }
    }
}