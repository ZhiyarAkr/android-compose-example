package com.akz.cinema.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.akz.cinema.data.movie.Movie

@Composable
fun HomePageHeroCard(
    modifier: Modifier = Modifier,
    movie: Movie?,
    scale: () -> State<Float>,
    onClick: (Int) -> Unit
) {
    movie?.let {
        ElevatedCard(
            modifier = modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp)
                .graphicsLayer {
                    val s = scale().value
                    scaleY = s
                    scaleX = s
                },
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 10.dp
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w1280/${it.backdropPath}",
                    contentDescription = "Hero movie Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            onClick(it.id)
                        },
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100))
                            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
                            .padding(8.dp),
                        text = it.title,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    } ?: Card(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(16.dp)
            .graphicsLayer {
                val s = scale().value
                scaleY = s
                scaleX = s
            }
    ) {
        Skeleton()
    }
}