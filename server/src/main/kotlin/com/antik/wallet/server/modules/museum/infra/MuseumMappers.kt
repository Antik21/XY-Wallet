package com.antik.wallet.server.modules.museum.infra

import com.antik.wallet.dto.MuseumObject
import com.antik.wallet.server.modules.museum.domain.MuseumItem

fun MuseumObject.toDomain(): MuseumItem = MuseumItem(
    id = objectID,
    title = title,
    artist = artistDisplayName,
    medium = medium,
    dimensions = dimensions,
    url = objectURL,
    objectDate = objectDate,
    primaryImage = primaryImage,
    primaryImageSmall = primaryImageSmall,
    repository = repository,
    department = department,
    creditLine = creditLine,
)
