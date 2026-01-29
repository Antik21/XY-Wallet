package com.antik.wallet.data

import com.antik.wallet.dto.MuseumObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NetworkMuseumRepository(
    private val museumApi: MuseumApi,
    private val museumStorage: MuseumStorage,
    private val appScope: AppScope,
) : MuseumRepository {
    override fun initialize() {
        appScope.launch {
            refresh()
        }
    }

    override suspend fun refresh() {
        museumStorage.saveObjects(museumApi.getData())
    }

    override fun getObjects(): Flow<List<MuseumObject>> = museumStorage.getObjects()

    override fun getObjectById(objectId: Int): Flow<MuseumObject?> =
        museumStorage.getObjectById(objectId)
}
