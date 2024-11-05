package com.example.epic_choice.fragments.next

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.epic_choice.R

class MapFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize imgclose and set its click listener
        val imgclose = view.findViewById<ImageView>(R.id.imgclose)
        imgclose.setOnClickListener {
            findNavController().navigateUp()
        }

        // Initialize the WebView and load the map.html file
        val mapWebView = view.findViewById<WebView>(R.id.mapWebView)
        mapWebView.webViewClient = WebViewClient()
        mapWebView.settings.javaScriptEnabled = true
        mapWebView.loadUrl("file:///android_asset/map.html")

        // Set up search bar listener
        val searchBar = view.findViewById<EditText>(R.id.searchBar)
        searchBar.setOnEditorActionListener { _, _, _ ->
            val query = searchBar.text.toString().trim()
            if (query.isNotEmpty()) {
                mapWebView.evaluateJavascript("searchLocation('$query')", null)
            }
            true
        }
    }
}
