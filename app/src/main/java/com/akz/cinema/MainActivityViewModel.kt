package com.akz.cinema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akz.cinema.data.connectivity.Connectivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    connectivity: Connectivity
): ViewModel() {
    val isConnected = connectivity.isOnline.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        true
    )
}