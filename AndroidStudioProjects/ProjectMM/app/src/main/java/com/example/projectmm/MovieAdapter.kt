package com.example.projectmm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.projectmm.model.Movie

class ClientViewHolder(val view: View) : ViewHolder(view)

//class ClientViewHolder extends ViewHolder {
//
//    private View view;
//    public ClientViewHolder(View view){
//        super(view);
//        this.view = view;
//    }
//}


class MovieAdapter(val movies: List<Movie>, val context: ListMoviesActivity) : RecyclerView.Adapter<ClientViewHolder>() {

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
        textView.text = movie.title
        if (movie.title == null) textView.text = movie.name

 //       textView.text = "${client.firstName} ${client.lastName}"
        DownloadImageFromInternet(view.findViewById(R.id.movie_view_imageview), context).execute("https://image.tmdb.org/t/p/original/" + movie.poster_path)


//        val imageView = view.findViewById<ImageView>(R.id.client_view_imageview)
//    /*    imageView.setImageResource(
//            when(client.gender){
//                Gender.MAN -> R.drawable.man
//                Gender.WOMAN -> R.drawable.woman
//            }
//        )  */
//        imageView.setImageResource(movie.getImage())
//
//        val cardView = view.findViewById<CardView>(R.id.client_view_cardview)
//        cardView.setOnClickListener {
//            val intent = Intent(context, DetailsClientActivity::class.java)
//            intent.putExtra("client_id", position)
//            intent.putExtra("client", movie)
//            context.startActivity(intent)
//        }
    }

}
