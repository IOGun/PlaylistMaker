package com.practicum.playlistmaker.data.search.network.Impl

import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.search.dto.TrackDto
import com.practicum.playlistmaker.data.search.dto.TrackRequest
import com.practicum.playlistmaker.data.search.dto.TrackResponse
import com.practicum.playlistmaker.domain.search.TrackRepository
import com.practicum.playlistmaker.domain.search.model.Status
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.domain.search.model.TrackSearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override suspend fun searchTrack(expression: String): Flow<TrackSearchResult> = flow {
            val response = networkClient.doRequest(TrackRequest(expression))

            when (response.status) {
                Status.RECEIVED -> {
                    val tracks: List<Track> = (response as TrackResponse).results.map {
                        DtoToTrack(it)
                    }
                    emit(
                        TrackSearchResult(tracks).apply {
                            if (tracks.isEmpty()) {
                                status = Status.NOT_FOUND
                            } else {
                                status = Status.RECEIVED
                            }
                        }
                    )

                }

                else -> emit(
                    TrackSearchResult(emptyList()).apply {
                        status = Status.ERROR
                    }
                )
            }
        }.catch { e ->
            e.printStackTrace()
            emit(
                TrackSearchResult(emptyList()).apply {
                    status = Status.ERROR
                }
            )
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

