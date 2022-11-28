package com.tharishaperera.movieapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    lateinit var popularRecyclerView: RecyclerView
    lateinit var allRecyclerView: RecyclerView

    var popularDataArray = JSONArray()
    var allDataArray = JSONArray()

    val apiKey = "945343a9152cdca2cc82852f09ac9c22"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        allRecyclerView = findViewById(R.id.lst_all)
        allRecyclerView.adapter = MovieAdapter()
        allRecyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        popularRecyclerView = findViewById(R.id.lst_popular)
        popularRecyclerView.adapter = MovieAdapter()
        popularRecyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)

        loadAllData()
//        loadPopularData()
    }

    private fun loadAllData() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.themoviedb.org/3/movie/upcoming?api_key=${apiKey}"

        val request = JsonObjectRequest(url, JSONObject(), { response ->
//            Log.e("Result: ", response.toString() )
            try{
                allDataArray = response.getJSONArray("results")
                allRecyclerView.adapter?.notifyDataSetChanged()
                popularRecyclerView.adapter?.notifyDataSetChanged()
            } catch (error : Exception){

            }
        }, {error ->
            Log.e("Error: ", error.toString())
        })
        queue.add(request)
    }

    private fun loadPopularData() {
        TODO("Not yet implemented")
    }

    private inner class MovieAdapter : RecyclerView.Adapter<MovieViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_row, parent, false)
            val tile = LayoutInflater.from(parent.context).inflate(R.layout.movie_tile, parent, false)
            return MovieViewHolder(view, tile)
        }

        override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
            try {
                holder.allTitle.text = allDataArray.getJSONObject(position).getString("title")
                holder.allReleaseDate.text = allDataArray.getJSONObject(position).getString("release_date")

                Glide.with(applicationContext)
                    .load("https://image.tmdb.org/t/p/w500" + allDataArray.getJSONObject(position).getString("backdrop_path"))
                    .centerCrop()
                    .into(holder.allThumb)

                Glide.with(applicationContext)
                    .load("https://image.tmdb.org/t/p/w500" + allDataArray.getJSONObject(position).getString("backdrop_path"))
                    .centerCrop()
                    .into(holder.popularThumb)
            } catch (e : Exception) {

            }
        }

        override fun getItemCount(): Int {
            return allDataArray.length()
        }

    }

    private inner class MovieViewHolder(itemView: View, tileView: View) : RecyclerView.ViewHolder(itemView) {
        var allTitle : TextView = itemView.findViewById(R.id.txt_title)
        var allReleaseDate : TextView = itemView.findViewById(R.id.txt_release_date)
        var allThumb : ImageView = itemView.findViewById(R.id.img_movie_icon)
        var popularThumb : ImageView = tileView.findViewById(R.id.img_tile)
    }
}