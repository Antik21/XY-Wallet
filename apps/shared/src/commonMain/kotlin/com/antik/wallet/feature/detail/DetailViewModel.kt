package com.antik.wallet.feature.detail

import com.antik.wallet.data.MuseumRepository
import com.antik.wallet.dto.MuseumObject
import com.antik.wallet.screens.BaseViewModel
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
) : BaseViewModel<ViewState, SideEffect>() {

    override val container = viewModelScope.container<ViewState, SideEffect>(
        initialState = ViewState.Loading(objectId),
        onCreate = {
            loadObject()
        },
    )

    fun onBackClick() = intent {
        postSideEffect(SideEffect.Navigation.Back)
    }

    fun onAppBarAction(actionId: String) = intent {
        when (actionId) {
            else -> Unit
        }
    }

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
                postSideEffect(SideEffect.ViewEffect.ShowMessage("Object not found"))
            }
        }.onFailureCancellable { ex ->
            val message = ex.message ?: "Unknown error"
            reduce { ViewState.Error(objectId = objectId, message = message) }
            postSideEffect(SideEffect.ViewEffect.ShowMessage(message))
        }
    }
}
