package com.practicum.playlistmaker.data.search.network

import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.search.dto.Response
import com.practicum.playlistmaker.data.search.dto.TrackRequest
import com.practicum.playlistmaker.domain.player.model.AppCollectionStatus
import com.practicum.playlistmaker.domain.search.model.Status
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ITunesApi::class.java)

    override fun doRequest(dto: TrackRequest): Response {
        if (dto is TrackRequest) {
            val resp = itunesService.search(dto.expression).execute()
            val body = resp.body()?: Response()
            return body.apply { status = Status.RECEIVED }
        } else {
            return Response().apply { status = Status.ERROR }
        }
    }


}