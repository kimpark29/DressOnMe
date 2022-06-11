package com.capstone.dressonme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.dressonme.model.remote.response.ProcessDetail
import com.capstone.dressonme.model.remote.response.ProcessItem
import com.capstone.dressonme.model.repo.ProcessRepository
import com.capstone.dressonme.helper.ApiCallbackString
import com.capstone.dressonme.model.Process
import com.capstone.dressonme.model.ProcessPreference
import com.capstone.dressonme.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProcessViewModel @Inject constructor (
    private val processRepository: ProcessRepository,
    private val pref: ProcessPreference
) : ViewModel() {

    val userPhotos : LiveData<ArrayList<ProcessItem>> = processRepository.userPhotos
    val userProcess : LiveData<Process> = processRepository.userProcess

    fun getUserPhotos(token : String, userId: String, callback: ApiCallbackString) {
        processRepository.getUserPhoto(token, userId, callback)
    }

    fun startProcess(token: String, userId: String, imgFile: File, callback: ApiCallbackString) {
        processRepository.startProcess(token, userId, imgFile, callback)
    }

    fun getUserProcess(token: String, id: String, callback: ApiCallbackString) {
        processRepository.getUserProcess(token, id, callback)
    }

    fun triggerCloudRun(token : String, callback: ApiCallbackString) {
        processRepository.triggerCloudRun(token, callback)
    }

    fun updateResult(token : String, id : String, imgFile : File, callback: ApiCallbackString) {
        processRepository.updateResult(token, id, imgFile, callback)
    }

    fun deleteProcess(token : String, userId: String, callback: ApiCallbackString) {
        processRepository.deleteProcess(token, userId, callback)
    }

    fun getProcess(): LiveData<Process> {
        return pref.getProcess().asLiveData()
    }

    fun saveProcess(process: Process) {
        viewModelScope.launch {
            pref.saveProcess(process)
        }
    }

    fun delete() {
        viewModelScope.launch {
            pref.deleteProses()
        }
    }
}
