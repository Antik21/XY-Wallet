package com.antik.wallet.server.modules.museum.application

import com.antik.wallet.server.modules.museum.application.ports.MuseumRepository
import com.antik.wallet.server.modules.museum.domain.MuseumItem

class GetMuseumObjectUseCase(private val repository: MuseumRepository) {
    fun execute(id: Int): MuseumItem? = repository.byId(id)
}
