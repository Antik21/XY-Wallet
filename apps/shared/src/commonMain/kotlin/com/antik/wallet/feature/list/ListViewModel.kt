package com.antik.wallet.feature.list

import com.antik.wallet.data.MuseumRepository
import com.antik.wallet.screens.BaseViewModel
import kotlinx.coroutines.flow.collect
import org.orbitmvi.orbit.container

class ListViewModel(
    private val museumRepository: MuseumRepository,
) : BaseViewModel<ViewState, SideEffect>() {

    override val container = viewModelScope.container<ViewState, SideEffect>(
        initialState = ViewState(isLoading = true),
        onCreate = {
            observeObjects()
        },
    )

    fun onObjectClick(objectId: Int) = intent {
        postSideEffect(SideEffect.Navigation.OpenDetail(objectId))
    }

    fun onAppBarAction(actionId: String) = intent {
        when (actionId) {
            ACTION_REFRESH -> Unit
            else -> Unit
        }
    }

    private fun observeObjects() = intent {
        museumRepository.getObjects().collect { objects ->
            reduce {
                state.copy(
                    objects = objects,
                    isLoading = false,
                )
            }
        }
    }
}
