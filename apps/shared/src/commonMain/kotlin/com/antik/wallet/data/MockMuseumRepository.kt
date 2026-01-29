package com.antik.wallet.data

import com.antik.wallet.dto.MuseumObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MockMuseumRepository(
    private val museumStorage: MuseumStorage,
    private val appScope: AppScope,
    private val mockData: List<MuseumObject> = defaultMockMuseumObjects(),
) : MuseumRepository {
    override fun initialize() {
        appScope.launch {
            museumStorage.saveObjects(mockData)
        }
    }

    override suspend fun refresh() {
        museumStorage.saveObjects(mockData)
    }

    override fun getObjects(): Flow<List<MuseumObject>> = museumStorage.getObjects()

    override fun getObjectById(objectId: Int): Flow<MuseumObject?> =
        museumStorage.getObjectById(objectId)
}

private fun defaultMockMuseumObjects(): List<MuseumObject> = listOf(
    MuseumObject(
        objectID = 1,
        title = "Mocked Portrait",
        artistDisplayName = "Unknown Artist",
        medium = "Oil on canvas",
        dimensions = "60 x 40 cm",
        objectURL = "https://example.com/museum/mock-1",
        objectDate = "1901",
        primaryImage = "https://example.com/museum/mock-1.jpg",
        primaryImageSmall = "https://example.com/museum/mock-1-small.jpg",
        repository = "Mock Museum",
        department = "Paintings",
        creditLine = "Mock acquisition",
    ),
    MuseumObject(
        objectID = 2,
        title = "Mocked Landscape",
        artistDisplayName = "Test Author",
        medium = "Watercolor",
        dimensions = "45 x 30 cm",
        objectURL = "https://example.com/museum/mock-2",
        objectDate = "1875",
        primaryImage = "https://example.com/museum/mock-2.jpg",
        primaryImageSmall = "https://example.com/museum/mock-2-small.jpg",
        repository = "Mock Museum",
        department = "Drawings",
        creditLine = "Gift of Test Suite",
    ),
    MuseumObject(
        objectID = 3,
        title = "Mocked Sculpture",
        artistDisplayName = "Demo Sculptor",
        medium = "Bronze",
        dimensions = "25 x 18 x 15 cm",
        objectURL = "https://example.com/museum/mock-3",
        objectDate = "1958",
        primaryImage = "https://example.com/museum/mock-3.jpg",
        primaryImageSmall = "https://example.com/museum/mock-3-small.jpg",
        repository = "Mock Museum",
        department = "Sculpture",
        creditLine = "Mock collection",
    ),
)
