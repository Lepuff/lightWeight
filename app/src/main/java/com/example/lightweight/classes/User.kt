package com.example.lightweight.classes

import android.net.Uri
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.InputStream
import java.net.URI

data class User(

    @Expose
    @SerializedName("id")
    var id: String? = null,

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
    var lastName: String? = null,

    var profilePicture : String? = null
)