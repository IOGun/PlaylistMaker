package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.Response
import com.practicum.playlistmaker.data.dto.TrackRequest

interface NetworkClient {
    fun doRequest(dto: TrackRequest): Response
}