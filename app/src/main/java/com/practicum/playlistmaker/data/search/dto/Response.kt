package com.practicum.playlistmaker.data.search.dto

import com.practicum.playlistmaker.domain.search.model.Status

open class Response() {
    var status : Status = Status.DEFAULT
}