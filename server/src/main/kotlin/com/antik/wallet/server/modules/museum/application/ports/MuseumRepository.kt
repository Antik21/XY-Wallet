package com.antik.wallet.server.modules.museum.application.ports

import com.antik.wallet.server.modules.museum.domain.MuseumItem

interface MuseumRepository {
    fun all(): List<MuseumItem>
    fun byId(id: Int): MuseumItem?
}
