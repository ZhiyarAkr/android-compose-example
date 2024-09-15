package com.akz.cinema.ui.screen.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.akz.cinema.data.movie.Movie
import com.akz.cinema.util.RemoteImageSize

@Composable
fun HeroCard(
    movie: Movie,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .shadow(16.dp)
            .clickable {
                onClick(movie.id)
            }
    ) {
        AsyncImage(
            model = movie.getImageUrl(RemoteImageSize.ImageSizeW780),
            contentDescription = movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(224.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = movie.title,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = movie.overview,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}