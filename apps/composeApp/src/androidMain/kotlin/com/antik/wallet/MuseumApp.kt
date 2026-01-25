package com.antik.wallet

import android.app.Application
import com.antik.wallet.api.ApiConfig
import com.antik.wallet.di.initKoin
import com.antik.wallet.feature.detail.DetailViewModel
import com.antik.wallet.feature.list.ListViewModel
import org.koin.dsl.module

class MuseumApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(
            listOf(
                module {
                    single { ApiConfig(baseUrl = "http://192.168.100.10:8080") }
                    factory { ListViewModel(get()) }
                    factory { (objectId: Int) -> DetailViewModel(get(), objectId) }
                }
            )
        )
    }
}
