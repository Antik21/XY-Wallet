import SwiftUI
import Shared

struct StartScreen: View {
    @StateObject private var viewModelStoreOwner = IosViewModelStoreOwner()

    @State private var appBarTitle: String = "Start"

    let navigator: StartNavigationNavigator

    var body: some View {
        let viewModel: StartViewModel = viewModelStoreOwner.viewModel(
            factory: ViewModelFactoriesKt.startViewModelFactory
        )

        VStack(spacing: 16) {
            Text("Choose a flow")
                .font(.headline)
            Button("Open list flow") {
                viewModel.onListFlowClick()
            }
            Button("Open web flow") {
                viewModel.onWebFlowClick()
            }
        }
        .padding(24)
        .navigationTitle(appBarTitle)
        .task {
            for await latestState in viewModel.uiState {
                await MainActor.run {
                    appBarTitle = latestState.appBar.title
                }
            }
        }
        .task {
            for await effect in viewModel.sideEffects {
                switch onEnum(of: effect) {
                case .navigation(let navigation):
                    switch onEnum(of: navigation) {
                    case .openListFlow:
                        await MainActor.run {
                            navigator.openListFlow()
                        }
                    case .openWebFlow(let openWebFlow):
                        await MainActor.run {
                            navigator.openWebFlow(url: openWebFlow.url)
                        }
                    }
                }
            }
        }
    }
}
