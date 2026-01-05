package com.jetbrains.kmpapp.screens

import com.jetbrains.kmpapp.data.MuseumObject
import com.jetbrains.kmpapp.data.MuseumRepository
import com.rickclephas.kmp.observableviewmodel.stateIn
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.coroutines.cancellation.CancellationException
import org.orbitmvi.orbit.viewmodel.container

class DetailViewModel(
    private val museumRepository: MuseumRepository
) : BaseViewModel<DetailViewModel.ViewState, DetailViewModel.SideEffect>() {

    override val container = container<ViewState, SideEffect>(
        initialState = ViewState.Idle,
    )

    @NativeCoroutinesState
    val museumObject: StateFlow<MuseumObject?> =
        uiState
            .map { state -> (state as? ViewState.Data)?.museumObject }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun setId(objectId: Int) {
        intent {
            val currentId = when (val s = state) {
                is ViewState.Loading -> s.objectId
                is ViewState.Data -> s.objectId
                is ViewState.NotFound -> s.objectId
                is ViewState.Error -> s.objectId
                ViewState.Idle -> null
            }
            if (currentId == objectId) return@intent

            reduce { ViewState.Loading(objectId) }

            try {
                val obj = museumRepository.getObjectById(objectId).first()
                if (obj != null) {
                    reduce { ViewState.Data(objectId = objectId, museumObject = obj) }
                } else {
                    reduce { ViewState.NotFound(objectId) }
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                reduce { ViewState.Error(objectId = objectId, message = e.message ?: "Unknown error") }
            }
        }
    }

    sealed interface SideEffect

    sealed interface ViewState {
        data object Idle : ViewState
        data class Loading(val objectId: Int) : ViewState
        data class Data(val objectId: Int, val museumObject: MuseumObject) : ViewState
        data class NotFound(val objectId: Int) : ViewState
        data class Error(val objectId: Int, val message: String) : ViewState
    }
}