package com.example.namufinder

import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

object CrollingTask {
    private lateinit var document: Document
    private lateinit var elements: Elements
    fun searchTask(url : String) : Elements? {
        try {
            Log.i("URL", url)
            document = Jsoup.connect(url).get()
            elements = document.select("div.search-item h4")
            return elements
        } catch(e : Exception) { e.printStackTrace() }

        return null
    }
}