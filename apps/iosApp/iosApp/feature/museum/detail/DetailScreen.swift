import Foundation
import SwiftUI
import Shared
import UiKit

struct DetailScreen: View {
    @StateObject private var viewModelStoreOwner = IosViewModelStoreOwner()

    @State private var viewState: Shared.Skie.XY_Wallet__shared.ViewState.__Sealed?
    @State private var alertMessage: String?

    let objectId: Int32
    let navigator: DetailNavigationNavigator

    var body: some View {
        let viewModel: DetailViewModel = viewModelStoreOwner.viewModel(
            factory: ViewModelFactoriesKt.detailViewModelFactory(objectId: objectId)
        )

        content()
        .alert(
            "Message",
            isPresented: Binding(
                get: { alertMessage != nil },
                set: { if !$0 { alertMessage = nil } }
            )
        ) {
            Button("OK", role: .cancel) {}
        } message: {
            Text(alertMessage ?? "")
        }
        .task(id: objectId) {
            for await latestState in viewModel.uiState {
                let sealedState = onEnum(of: latestState)
                await MainActor.run {
                    viewState = sealedState
                }
            }
        }
        .task {
            for await effect in viewModel.sideEffects {
                switch onEnum(of: effect) {
                case .navigation(let navigation):
                    switch onEnum(of: navigation) {
                    case .back:
                        await MainActor.run {
                            navigator.back()
                        }
                    }
                case .viewEffect(let viewEffect):
                    switch onEnum(of: viewEffect) {
                    case .showMessage(let showMessage):
                        await MainActor.run {
                            alertMessage = showMessage.message
                        }
                    }
                }
            }
        }
    }

    @ViewBuilder
    private func content() -> some View {
        switch viewState {
        case .data(let data):
            ObjectDetails(obj: data.museumObject)
        case .error(let error):
            Text(error.message)
        case .loading, nil:
            ProgressView()
        case .notFound:
            UiKitEmptyState(title: "No data available")
        }
    }
}

struct ObjectDetails: View {
    var obj: DtoMuseumObject

    var body: some View {
        ScrollView {

            VStack {
                AsyncImage(url: URL(string: obj.primaryImageSmall)) { phase in
                    switch phase {
                    case .empty:
                        ProgressView()
                    case .success(let image):
                        image
                            .resizable()
                            .scaledToFill()
                            .clipped()
                    default:
                        EmptyView()
                    }
                }

                VStack(alignment: .leading, spacing: 6) {
                    Text(obj.title)
                        .font(.title)

                    LabeledInfo(label: "Artist", data: obj.artistDisplayName)
                    LabeledInfo(label: "Date", data: obj.objectDate)
                    LabeledInfo(label: "Dimensions", data: obj.dimensions)
                    LabeledInfo(label: "Medium", data: obj.medium)
                    LabeledInfo(label: "Department", data: obj.department)
                    LabeledInfo(label: "Repository", data: obj.repository)
                    LabeledInfo(label: "Credits", data: obj.creditLine)
                }
                .padding(16)
            }
        }
    }
}

struct LabeledInfo: View {
    var label: String
    var data: String

    var body: some View {
        Spacer()
        Text("**\(label):** \(data)")
    }
}
