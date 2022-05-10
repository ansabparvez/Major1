package com.devansab.begnn.utils

class ApiResult<T>(val type: Int, val data: T? = null, val error: String? = null) {

    companion object {
        const val PROGRESS = 0
        const val SUCCESS = 1
        const val ERROR = 2
    }

}