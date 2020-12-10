package com.idevdroidapps.gitter.data.models

import com.google.gson.annotations.SerializedName

/**
 * Immutable model class for a Github repo that holds information about the repo owner.
 * Objects of this type are received from the Github API, therefore all the fields are annotated
 * with the serialized name.
 */
data class Owner(
    @field:SerializedName("login") val login: String,
    @field:SerializedName("avatar_url") val avatarUrl: String
)