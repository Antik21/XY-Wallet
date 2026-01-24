package com.antik.wallet.screens

import com.antik.wallet.data.MuseumRepository
import com.antik.wallet.dto.MuseumObject
import com.antik.wallet.utils.onFailureCancellable
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.orbitmvi.orbit.container

class DetailViewModel(
    private val museumRepository: MuseumRepository,
    private val objectId: Int,
) : BaseViewModel<DetailViewModel.ViewState, DetailViewModel.SideEffect>() {

    override val container = viewModelScope.container<ViewState, SideEffect>(
        initialState = ViewState.Loading(objectId),
        onCreate = {
            loadObject()
        },
    )

    val museumObject: StateFlow<MuseumObject?> =
        uiState
            .map { state -> (state as? ViewState.Data)?.museumObject }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private fun loadObject() = intent {
        runCatching {
            val obj = museumRepository.getObjectById(objectId).first()
            if (obj != null) {
                reduce { ViewState.Data(objectId = objectId, museumObject = obj) }
            } else {
                reduce { ViewState.NotFound(objectId) }
            }
        }.onFailureCancellable { ex ->
            reduce { ViewState.Error(objectId = objectId, message = ex.message ?: "Unknown error") }
        }
    }

    sealed interface SideEffect

    sealed interface ViewState {
        data class Loading(val objectId: Int) : ViewState
        data class Data(val objectId: Int, val museumObject: MuseumObject) : ViewState
        data class NotFound(val objectId: Int) : ViewState
        data class Error(val objectId: Int, val message: String) : ViewState
    }
}
