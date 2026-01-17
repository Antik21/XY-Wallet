package com.antik.wallet.server.modules.museum.application

import com.antik.wallet.server.modules.museum.application.ports.MuseumRepository
import com.antik.wallet.server.modules.museum.domain.MuseumItem

class ListMuseumObjectsUseCase(private val repository: MuseumRepository) {
    fun execute(): List<MuseumItem> = repository.all()
}
