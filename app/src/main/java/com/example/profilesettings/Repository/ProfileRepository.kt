package com.example.profilesettings.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.profilesettings.RemoteDataSource.ProfileApi
import com.example.profilesettings.data.ProfileDetails
import com.example.profilesettings.utils.NetworkResult
import com.example.profilesettings.utils.NetworkUtil
import retrofit2.Response

class ProfileRepository(private val profileApi: ProfileApi,
                        private val applicationContext: Context
) {
    private val profileLivedata = MutableLiveData<NetworkResult<ProfileDetails>>()

    val profileDetails: LiveData<NetworkResult<ProfileDetails>> get() = profileLivedata

    suspend fun getProfileDetails(id : String) {

        if (NetworkUtil.checkForInternet(applicationContext)) {

            try {

                val result = profileApi.getProfileDetails(id)
                profileLivedata.postValue(handleProfileDetailsResponse(result))

            } catch (e: Exception) {
                profileLivedata.postValue(NetworkResult.Error(e.message.toString()))
            }
        } else {
            profileLivedata.postValue(NetworkResult.Error("No Network Connection"))
        }

    }

    private fun handleProfileDetailsResponse(response: Response<ProfileDetails>): NetworkResult<ProfileDetails>? {
        when {
            response.message().toString().contains("timeout")->{
                return NetworkResult.Error("Requested Time Out")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API key Limited")
            }
            response.body()!!.data.app_list.isEmpty()->{
                return NetworkResult.Error("Profiles Not Found")
            }
            response.isSuccessful->{
                val profileData = response.body()

                return NetworkResult.Success(profileData!!)
            }
            else->{
                return NetworkResult.Error(response.message())
            }
        }
    }
}