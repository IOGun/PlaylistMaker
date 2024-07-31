package com.practicum.playlistmaker.data.search.network

import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.search.dto.Response
import com.practicum.playlistmaker.data.search.dto.TrackRequest
import com.practicum.playlistmaker.domain.search.model.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val itunesService: ITunesApi) : NetworkClient {


    override suspend fun doRequest(dto: TrackRequest): Response {

        return withContext(Dispatchers.IO) {
            try {
                val resp = itunesService.search(dto.expression)
                resp.apply {
                   status  = Status.RECEIVED
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Response().apply { status = Status.ERROR }
            }
        }
    }
}