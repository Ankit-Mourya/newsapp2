package com.example.newsfresh

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NewsItemClicked {
    private lateinit var mAdapter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fetchData()
//        progressBar.visibility = View.VISIBLE
         mAdapter = NewsListAdapter(this)
             recyclerView.adapter = mAdapter

    }
    private fun fetchData() {
        Log.d("Api Call","1")
//        progressBar.visibility = View.VISIBLE
          val url = "https://newsapi.org/v2/top-headlines?country=us&apiKey=ff4bbaf7879c4e648465b17b432574ef"

//        https://newsapi.org/v2/everything?q=tesla&from=2023-07-05&sortBy=publishedAt&apiKey=ff4bbaf7879c4e648465b17b432574ef
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, {
              val newsJsonArray = it.getJSONArray("articles")
              val newsArray = ArrayList<News>()
            Log.d("Api Call","2")
            for (i in 0 until newsJsonArray.length()){
                Log.d("Api Call","3")
                val newsJsonObject = newsJsonArray.getJSONObject(i)
                Log.d("akm", "start ")
                val news = News(
                   newsJsonObject.getString("title"),
                   newsJsonObject.getString("author"),
                    newsJsonObject.getString("url"),
                    newsJsonObject.getString("urlToImage")
                )
                Log.d("Api Call","4")
                newsArray.add(news)
            }

               mAdapter.updateNews(newsArray)
            Log.d(  "times","succeed" )
        },
            {
              }
        )

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {

    }

}
