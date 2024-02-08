package com.akz.cinema.data.connectivity

import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class Connectivity @Inject constructor(
    private val connectivityManager: ConnectivityManager
) {

    private var networkCallback: NetworkCallback
    private val _isConnected: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isConnected: Flow<Boolean> = _isConnected


    private fun  getNetworkCallback(): NetworkCallback {
        return object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                _isConnected.update {
                    true
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                _isConnected.update {
                    false
                }
            }
        }
    }

    fun unRegisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    init {
        networkCallback = getNetworkCallback()
        connectivityManager.registerNetworkCallback(InternetWifiCellularRequest, networkCallback)
    }

    companion object {
        private val InternetWifiCellularRequest by lazy {
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build()
        }
    }
}