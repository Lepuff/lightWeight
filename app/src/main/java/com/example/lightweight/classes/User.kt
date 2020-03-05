package com.example.lightweight.classes

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class User(

    @Expose
    @SerializedName("isFacebookUser")
    var isFacebookUser: Boolean = false,

    @Expose
    @SerializedName("email")
    var email: String? = null,

    @Expose
    @SerializedName("username")
    var firstName: String? = null,

    @Expose
    @SerializedName("image")
    var lastName: String? = null
)