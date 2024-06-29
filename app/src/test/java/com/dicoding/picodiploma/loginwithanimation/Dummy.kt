package com.dicoding.picodiploma.loginwithanimation

import com.dicoding.picodiploma.loginwithanimation.data.response.StoriesItem

object Dummy {

    fun generateDummyQuoteResponse(): List<StoriesItem> {
        val items: MutableList<StoriesItem> = arrayListOf()
        for (i in 0..100) {
            val story = StoriesItem(
                "photoUrl $i",
                "2021-09-14T06:51.15.000Z ",
                "name $i",
                "description $i",
                0.0,
                i.toString(),
                0.0
            )
            items.add(story)
        }
        return items
    }
}