package com.example.namufinder

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

object CrollingTask {
    lateinit var document: Document
    lateinit var elements: Elements
    fun searchTask(url : String) : Elements {
        System.out.println("url >> ${url}")
        try {
            document = Jsoup.connect(url).get()
            elements = document.select("div.search-item h4")
            return elements
        } catch(e : Exception) { e.printStackTrace() }

        return elements
    }
}