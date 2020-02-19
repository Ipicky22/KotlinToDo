package com.maxadri.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxadri.network.UserInfo
import com.maxadri.network.UserRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody


class UserInfoViewModel : ViewModel() {

    private val userRepository = UserRepository()

    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo> = _userInfo

    fun getInfos() {
        viewModelScope.launch {
            userRepository.getInfo()?.let {
                _userInfo.value = it
            }
        }
    }

    fun updateAvatar(avatar: MultipartBody.Part) {
        viewModelScope.launch {
            userRepository.updateAvatar(avatar)?.let {
                _userInfo.value = it
            }
        }
    }

    fun update (user: UserInfo) {
        viewModelScope.launch {
            userRepository.update(user)?.let {
                _userInfo.value = it
            }
        }
    }
}
