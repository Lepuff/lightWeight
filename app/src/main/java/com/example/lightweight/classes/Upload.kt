package com.example.lightweight.classes

class Upload {
    private lateinit var mName: String
    private lateinit var mImageUrl: String

    fun Upload(){
        //empty constructor needed
    }

    fun Upload(name: String ,imageUrl: String){
        mName = name
        mImageUrl = imageUrl
    }

    fun getName(): String{
        return mName
    }

    fun setName(name: String){
        mName = name
    }

    fun getImageUrl(): String{
        return mImageUrl
    }

    fun setImageUrl(imageUrl: String){
        mImageUrl = imageUrl
    }
}
