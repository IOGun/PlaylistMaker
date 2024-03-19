package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.TrackDto
import com.practicum.playlistmaker.data.dto.TrackRequest
import com.practicum.playlistmaker.data.dto.TrackResponse
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.models.AppCollectionStatus
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.models.TrackSearchResult
import java.lang.Exception

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTrack(expression: String): TrackSearchResult {
        try {
            val response = networkClient.doRequest(TrackRequest(expression))
            return when(response.status) {
                AppCollectionStatus.RECEIVED -> {
                    val tracks: List<Track> = (response as TrackResponse).results.map {
                        DtoToTrack(it)
                    }
                    TrackSearchResult(tracks).apply {
                        status = AppCollectionStatus.RECEIVED
                    }
                } else -> TrackSearchResult(emptyList()).apply {
                    status = AppCollectionStatus.ERROR
                }
            }
        } catch (e: Exception) {
            return TrackSearchResult(emptyList()).apply {
                status = AppCollectionStatus.ERROR
            }
        }
    }

    private fun DtoToTrack(trackDto: TrackDto): Track {
        return Track(
            trackDto.trackName,
            trackDto.artistName,
            trackDto.trackTimeMillis,
            trackDto.artworkUrl100,
            trackDto.trackId,
            trackDto.collectionName,
            trackDto.releaseDate,
            trackDto.primaryGenreName,
            trackDto.country,
            trackDto.previewUrl
        )
    }

}

