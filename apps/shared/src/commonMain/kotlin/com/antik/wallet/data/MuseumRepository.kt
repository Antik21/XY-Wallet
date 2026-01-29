package com.antik.wallet.data

import com.antik.wallet.dto.MuseumObject
import kotlinx.coroutines.flow.Flow

interface MuseumRepository {
    fun initialize()

    suspend fun refresh()

    fun getObjects(): Flow<List<MuseumObject>>

    fun getObjectById(objectId: Int): Flow<MuseumObject?>
}
