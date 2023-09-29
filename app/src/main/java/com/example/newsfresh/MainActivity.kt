package com.example.newsfresh

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException

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
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                try {
                    val newsJsonArray = response.getJSONArray("articles")
                    val newsArray = ArrayList<News>()
                    for (i in 0 until newsJsonArray.length()) {
                        val newsJsonObject = newsJsonArray.getJSONObject(i)
                        val news = News(
                            newsJsonObject.getString("title"),
                            newsJsonObject.getString("author"),
                            newsJsonObject.getString("url"),
                            newsJsonObject.getString("urlToImage")
                        )
                        newsArray.add(news)
                    }
                    mAdapter.updateNews(newsArray)
                } catch (e: JSONException) {
                    // Handle JSON parsing error
                    Log.e("Api Call", "JSON parsing error: ${e.message}")
                }
            },
            { error ->
                // Handle Volley error
                Log.e("Api Call", "Volley error: ${error.message}")
                if (error is VolleyError) {
                    if (error.networkResponse != null) {
                        val statusCode = error.networkResponse.statusCode
                        val responseData = String(error.networkResponse.data)
                        val responseHeaders = error.networkResponse.headers
                        Log.e("Api Call", "Volley network error - Status code: $statusCode")
                        Log.e("Api Call", "Response headers: $responseHeaders")
                        Log.e("Api Call", "Response data: $responseData")
                    } else {
                        Log.e("Api Call", "Volley error: ${error.message}")
                    }
                } else {
                    Log.e("Api Call", "Unknown error")
                }

            }
        )

// Access the RequestQueue through your singleton class
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)


// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {

    }

}
