package com.capstone.dressonme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.dressonme.model.User
import com.capstone.dressonme.model.UserPreference
import com.capstone.dressonme.model.repo.UserRepository
import com.capstone.dressonme.helper.ApiCallbackString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor (
    private val userRepository: UserRepository,
    private val pref: UserPreference
    ) : ViewModel()  {

    val loginData : LiveData<User> = userRepository.loginData

    fun login(email: String, pass: String, callback: ApiCallbackString) {
        userRepository.login(email, pass, callback)
    }

    fun register(name: String, email: String, pass: String, callback: ApiCallbackString) {
        userRepository.register(name, email, pass, callback)
    }

    fun getUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }

    fun saveUser(user: User) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}