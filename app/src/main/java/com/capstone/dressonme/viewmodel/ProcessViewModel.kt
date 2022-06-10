package com.capstone.dressonme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstone.dressonme.model.remote.response.ProcessDetail
import com.capstone.dressonme.model.remote.response.ProcessItem
import com.capstone.dressonme.model.repo.ProcessRepository
import com.capstone.dressonme.helper.ApiCallbackString
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProcessViewModel @Inject constructor (
    private val processRepository: ProcessRepository
) : ViewModel() {

    val userPhotos : LiveData<ArrayList<ProcessItem>> = processRepository.userPhotos
    val userProcess : LiveData<ProcessDetail> = processRepository.userProcess

    fun getUserPhotos(token : String, userId: String, callback: ApiCallbackString) {
        processRepository.getUserPhoto(token, userId, callback)
    }

    fun startProcess(token: String, userId: String, imgFile: File, callback: ApiCallbackString) {
        processRepository.startProcess(token, userId, imgFile, callback)
    }

    fun getUserProcess(token: String, id: String, callback: ApiCallbackString) {
        processRepository.getUserProcess(token, id, callback)
    }

    fun updateResult(token : String, id : String, imgFile : File, callback: ApiCallbackString) {
        processRepository.updateResult(token, id, imgFile, callback)
    }

    fun deleteProcess(token : String, userId: String, callback: ApiCallbackString) {
        processRepository.deleteProcess(token, userId, callback)
    }
}
