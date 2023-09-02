package com.akz.cinema.data.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class Connectivity @Inject constructor(
    private val networkRequest: NetworkRequest,
) {
    companion object {
        private val _isConnected: MutableStateFlow<Boolean> = MutableStateFlow(false)
        val isConnected = _isConnected.asStateFlow()
    }

    fun init(context: Context) {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                _isConnected.update {
                    true
                }
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                val unMetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
                _isConnected.update {
                    unMetered
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                _isConnected.update {
                    false
                }
            }
        }

        val connectivityManager = getSystemService(
            context,
            ConnectivityManager::class.java
        ) as ConnectivityManager

        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }
}