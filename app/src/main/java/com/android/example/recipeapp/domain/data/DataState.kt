package com.android.example.recipeapp.domain.data

data class DataState<out T>(
    val data: T? = null,
    val error: String? = null,
    val loading: Boolean = false,
) {
    companion object {
        fun <T> success(data: T): DataState<T> {
            return DataState(data = data)
        }

        fun <T> error(message: String , data: T? = null, ): DataState<T> {
            return DataState(data = data, error = message)
        }

        fun <T> loading(): DataState<T> {
            return DataState(loading = true)
        }
    }
}