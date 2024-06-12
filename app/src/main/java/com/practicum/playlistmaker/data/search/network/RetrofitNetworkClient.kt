package com.practicum.playlistmaker.data.search.network

import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.search.dto.Response
import com.practicum.playlistmaker.data.search.dto.TrackRequest
import com.practicum.playlistmaker.domain.search.model.Status

class RetrofitNetworkClient(private val itunesService: ITunesApi) : NetworkClient {


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