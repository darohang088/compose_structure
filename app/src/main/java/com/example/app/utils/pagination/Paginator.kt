package com.example.app.utils.pagination

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}
