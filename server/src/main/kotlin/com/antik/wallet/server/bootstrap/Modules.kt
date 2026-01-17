package com.antik.wallet.server.bootstrap

import com.antik.wallet.server.modules.museum.application.ports.MuseumRepository
import com.antik.wallet.server.modules.museum.infra.JsonMuseumRepository

data class ModuleDependencies(
    val museumRepository: MuseumRepository = JsonMuseumRepository(),
)
