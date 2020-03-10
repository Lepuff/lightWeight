package com.example.lightweight.classes

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlin.properties.Delegates
import kotlin.properties.ObservableProperty


data class User(

    @Expose
    @SerializedName("isFacebookUser")
    var isFacebookUser: Boolean = true,

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