package com.jetbrains.kmpapp.screens

import com.jetbrains.kmpapp.data.MuseumObject
import com.jetbrains.kmpapp.data.MuseumRepository
import com.jetbrains.kmpapp.screens.DetailViewModel.ViewState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.stateIn
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class DetailViewModel(
    private val objectId: Int,
    private val museumRepository: MuseumRepository
) : BaseViewModel<ViewState, Unit>() {

    override val container = container<ViewState, Unit>(
        initialState = ViewState.Init,
        onCreate = {
            repeatOnSubscription {
                loadMuseumObject(objectId)
            }
        }
    )

    private fun loadMuseumObject(objectId: Int) = intent {
        val obj = museumRepository.getObjectById(objectId).first()
        if (obj != null) {
            reduce { ViewState.Data(obj) }
        }
    }

    sealed class ViewState {
        data object Init : ViewState()
        data class Data(val museumObject: MuseumObject) : ViewState()
    }
}
