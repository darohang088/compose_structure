package com.example.app.utils

import java.io.IOException

// Custom Network Exceptions
class NoInternetException(message: String = "No internet connection available") : IOException(message)
class ServerException(message: String = "Server error occurred", val code: Int = -1) : Exception(message)
class NetworkTimeoutException(message: String = "Network request timed out") : IOException(message)
class UnknownNetworkException(message: String = "An unknown network error occurred") : Exception(message)
class EmptyCacheException(message: String = "No cached data available") : Exception(message)
