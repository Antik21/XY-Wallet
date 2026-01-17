package com.antik.wallet.server.modules.museum.api

import com.antik.wallet.dto.MuseumObject
import com.antik.wallet.server.modules.museum.domain.MuseumItem

fun MuseumItem.toDto(): MuseumObject = MuseumObject(
    objectID = id,
    title = title,
    artistDisplayName = artist,
    medium = medium,
    dimensions = dimensions,
    objectURL = url,
    objectDate = objectDate,
    primaryImage = primaryImage,
    primaryImageSmall = primaryImageSmall,
    repository = repository,
    department = department,
    creditLine = creditLine,
)
