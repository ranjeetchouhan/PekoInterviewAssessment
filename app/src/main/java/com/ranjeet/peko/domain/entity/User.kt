package com.ranjeet.peko.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var name: String?,
    var login: String,
    var avatar_url : String?,
    var bio : String?,
    var followers : Int?,
    var following : Int?,
    var public_repos: Int?,
    var location: String?,
    var created_at: String?
) : Parcelable