package com.example.projectmm

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.projectmm.model.Movie
import com.example.projectmm.model.MovieDetail
import kotlin.math.roundToInt

class ClientViewHolder(val view: View) : ViewHolder(view)


class MovieAdapter(val movies: List<Movie>, val context: Context, val layout: Int) : RecyclerView.Adapter<ClientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(layout, parent, false)
        return ClientViewHolder(view)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        val movie : Movie = movies[position]

        val view = holder.itemView

        val text_title = view.findViewById<TextView>(R.id.movie_title)
        val release_date = view.findViewById<TextView>(R.id.release_date)
        val overview = view.findViewById<TextView>(R.id.overview)
        release_date.text = movie.release_date
        overview.text = movie.overview
        setNoteStars(movie, view)
        text_title.text = movie?.title ?: movie?.name ?: "On a perdu le titre dans l'API"

        //DownloadImageFromInternet(view.findViewById(R.id.movie_view_imageview), context).execute("https://image.tmdb.org/t/p/original/" + movie.poster_path)
        Glide.with(context)
            .load("https://image.tmdb.org/t/p/original/" + movie.poster_path)
            .into(view.findViewById(R.id.movie_view_imageview))

        view.setOnClickListener {
            val intent = Intent(context, MovieDetailsActivity::class.java)
            intent.putExtra("movie_id", movie.id)
            context.startActivity(intent)
        }
    }
    @SuppressLint("SetTextI18n")
    private fun setNoteStars(movie: Movie?, view: View) {
        val starIcons = ArrayList<ImageView>()
        starIcons.add(view.findViewById(R.id.star_icon1))
        starIcons.add(view.findViewById(R.id.star_icon2))
        starIcons.add(view.findViewById(R.id.star_icon3))
        starIcons.add(view.findViewById(R.id.star_icon4))
        starIcons.add(view.findViewById(R.id.star_icon5))

        val noteI = (movie?.vote_average!!.roundToInt().toDouble()/2).roundToInt()
        val noteF = movie.vote_average.roundToInt().toDouble()/2

        for((cpt, star) in starIcons.withIndex()) {
            if((cpt+1) <= noteI) {
                if(noteF-cpt == 0.5) star.setImageResource(R.drawable.baseline_star_half_24)
                else star.setImageResource(R.drawable.baseline_star_24)
            }
        }

        val starTextView = view.findViewById<TextView>(R.id.text_note_count)
        starTextView.text = " (" + movie.vote_count + " votes)"
    }

}
