package com.example.myapplication

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("meta") val meta: Meta,
    @SerializedName("response") val response: SearchResponseData
)

data class Meta(
    @SerializedName("status") val status: Int
)

data class SearchResponseData(
    @SerializedName("hits") val hits: List<SearchHit>
)

data class SearchHit(
    @SerializedName("result") val result: SearchResult
)

data class SearchResult(
    @SerializedName("release_date_for_display") val releaseDateForDisplay: String,
    @SerializedName("title_with_featured") val titleWithFeatured: String,
    @SerializedName("song_art_image_url") val songArtImageUrl: String
)
