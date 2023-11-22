package com.dicoding.storyku.utils

import com.dicoding.storyku.data.response.ListStoryResponse

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryResponse> {
        val items: MutableList<ListStoryResponse> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryResponse(
                i.toString(),
                "photoUrl + $i",
                "name $i",
                "description $i",
                "id $i"
            )
            items.add(story)
        }
        return items
    }
}