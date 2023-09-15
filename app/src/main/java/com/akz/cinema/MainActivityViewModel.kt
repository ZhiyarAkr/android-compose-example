package com.akz.cinema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akz.cinema.data.connectivity.Connectivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val connectivity: Connectivity
): ViewModel() {

    private val _showConnectivityStatus = MutableStateFlow(false)
    val showConnectivityStatus = _showConnectivityStatus.asStateFlow()

    val isConnected = connectivity.isConnected.onEach{
        if (it) {
            _showConnectivityStatus.update {
                false
            }
        } else {
            _showConnectivityStatus.update {
                true
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        false
    )

}