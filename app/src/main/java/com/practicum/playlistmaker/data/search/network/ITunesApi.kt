package com.practicum.playlistmaker.data.search.network

import com.practicum.playlistmaker.data.search.dto.TrackResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    suspend fun search(@Query("term") text: String): TrackResponse
}