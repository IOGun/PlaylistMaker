package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.search.dto.Response
import com.practicum.playlistmaker.data.search.dto.TrackRequest

interface NetworkClient {
    suspend fun doRequest(dto: TrackRequest): Response
}