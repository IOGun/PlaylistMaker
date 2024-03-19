package com.practicum.playlistmaker.data.dto

import com.practicum.playlistmaker.domain.models.AppCollectionStatus

open class Response() {
    var status : AppCollectionStatus = AppCollectionStatus.DEFAULT
}