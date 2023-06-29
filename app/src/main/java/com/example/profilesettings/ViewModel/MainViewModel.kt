package com.example.profilesettings.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.profilesettings.Repository.ProfileRepository
import com.example.profilesettings.data.ProfileDetails
import com.example.profilesettings.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: ProfileRepository) : ViewModel(){

    init {

        viewModelScope.launch(Dispatchers.IO) {

            repository.getProfileDetails("378")

        }
    }

    val profileDetails : LiveData<NetworkResult<ProfileDetails>> get() = repository.profileDetails

}