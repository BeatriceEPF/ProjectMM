package com.example.projectmm

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.projectmm.model.Movie

class ClientViewHolder(val view: View) : ViewHolder(view)


class MovieAdapter(val movies: List<Movie>, val context: Context) : RecyclerView.Adapter<ClientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.movie_list_item, parent, false)
        return ClientViewHolder(view)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        val movie : Movie = movies[position]

        val view = holder.itemView

        val textView = view.findViewById<TextView>(R.id.movie_view_textview)
        textView.text = movie?.title ?: movie?.name ?: "On a perdu le titre dans l'API"

        //DownloadImageFromInternet(view.findViewById(R.id.movie_view_imageview), context).execute("https://image.tmdb.org/t/p/original/" + movie.poster_path)
        Glide.with(context)
            .load("https://image.tmdb.org/t/p/original/" + movie.poster_path)
            .into(view.findViewById(R.id.movie_view_imageview))



        //imageView.setImageResource(movie.getImage())

        val cardView = view.findViewById<CardView>(R.id.movie_list_item)
        cardView.setOnClickListener {
            val test = movie.id
            val intent = Intent(context, MovieDetailsActivity::class.java)
            intent.putExtra("movie_id", movie.id)
            context.startActivity(intent)
        }
    }

}
