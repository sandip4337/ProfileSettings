package com.example.profilesettings

import android.app.Application
import com.example.profilesettings.RemoteDataSource.ProfileApi
import com.example.profilesettings.RemoteDataSource.RetrofitBuilder
import com.example.profilesettings.Repository.ProfileRepository

class ProfileApplication: Application() {
    lateinit var repository: ProfileRepository

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize(){

        val quoteService = RetrofitBuilder.getInstance().create(ProfileApi::class.java)

        repository = ProfileRepository(quoteService, applicationContext)

    }
}