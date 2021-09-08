package com.example.whatsappclone.Call

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel






class CallViewModel : ViewModel() {
    private val calls:MutableLiveData<List<Call>> by lazy {
        MutableLiveData<List<Call>>().also {
            load()
        }
    }
    init {
        calls.value = dummyCalls
    }

    fun getCalls():LiveData<List<Call>>{
        return calls
    }

    private fun load(){

    }
}