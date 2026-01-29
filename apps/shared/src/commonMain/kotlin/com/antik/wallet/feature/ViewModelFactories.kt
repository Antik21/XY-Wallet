package com.antik.wallet.feature

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.antik.wallet.feature.museum.detail.DetailViewModel
import com.antik.wallet.feature.museum.list.ListViewModel
import com.antik.wallet.feature.start.StartViewModel
import com.antik.wallet.feature.web.WebViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

private object ViewModelDependencies : KoinComponent

val listViewModelFactory: ViewModelProvider.Factory = viewModelFactory {
    initializer {
        ListViewModel(ViewModelDependencies.get())
    }
}

val startViewModelFactory: ViewModelProvider.Factory = viewModelFactory {
    initializer {
        StartViewModel()
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

fun webViewModelFactory(url: String): ViewModelProvider.Factory = viewModelFactory {
    initializer {
        WebViewModel(url = url)
    }
}
