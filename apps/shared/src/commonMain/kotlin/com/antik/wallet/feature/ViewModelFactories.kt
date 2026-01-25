package com.antik.wallet.feature

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.antik.wallet.feature.detail.DetailViewModel
import com.antik.wallet.feature.list.ListViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

private object ViewModelDependencies : KoinComponent

val listViewModelFactory: ViewModelProvider.Factory = viewModelFactory {
    initializer {
        ListViewModel(ViewModelDependencies.get())
    }
}

fun detailViewModelFactory(objectId: Int): ViewModelProvider.Factory = viewModelFactory {
    initializer {
        DetailViewModel(
            museumRepository = ViewModelDependencies.get(),
            objectId = objectId,
        )
    }
}
