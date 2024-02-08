package com.akz.cinema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akz.cinema.data.connectivity.Connectivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val connectivity: Connectivity
): ViewModel() {

    val isConnected = connectivity.isConnected.map {
        if (it) {
            true
        } else {
            delay(100)
            false
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        true
    )

    override fun onCleared() {
        connectivity.unRegisterNetworkCallback()
        super.onCleared()
    }
}