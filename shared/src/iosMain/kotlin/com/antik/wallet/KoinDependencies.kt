package com.antik.wallet

import com.antik.wallet.data.MuseumRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class KoinDependencies : KoinComponent {
    val museumRepository: MuseumRepository by inject()
}
