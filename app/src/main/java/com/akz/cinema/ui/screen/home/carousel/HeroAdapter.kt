package com.akz.cinema.ui.screen.home.carousel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.akz.cinema.R
import com.akz.cinema.data.movie.Movie
import com.akz.cinema.util.RemoteImageSize
import com.akz.cinema.util.getUriForRemoteImage
import com.google.android.material.carousel.MaskableFrameLayout
import com.google.android.material.math.MathUtils.lerp

class HeroAdapter(
    @ColorInt private val color: Int,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<HeroAdapter.MViewHolder>() {

    private var movies: List<Movie> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.carousel_item, parent, false)
        return MViewHolder(view)
    }

    override fun onBindViewHolder(holder: MViewHolder, position: Int) {
        holder.maskableFrameLayout.setBackgroundColor(color)
        holder.maskableFrameLayout.setOnClickListener {
            onClick(movies[position].id)
        }
        holder.maskableFrameLayout.setOnMaskChangedListener {
                maskRect ->
            // Any custom motion to run when mask size changes
            holder.imageTitle.translationX = maskRect.left
            holder.imageTitle.setAlpha(lerp(1F, 0F, (maskRect.left - 0F) / 80F))
        }
        holder.imageTitle.text = movies[position].title
        holder.imageView.load(
            getUriForRemoteImage(
                movies[position].backdropPath,
                RemoteImageSize.ImageSizeW780
            )
        )
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun setMovies(m: List<Movie>) {
        if (m != movies) {
            movies = m
            notifyDataSetChanged()
        }
    }

    inner class MViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.carousel_image_view)
        val imageTitle: TextView = view.findViewById(R.id.imageTitle)
        val maskableFrameLayout: MaskableFrameLayout =
            view.findViewById(R.id.carousel_item_container)

    }
}