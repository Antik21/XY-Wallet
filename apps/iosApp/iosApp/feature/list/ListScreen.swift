import SwiftUI
import Shared

struct ListScreen: View {
    @StateObject private var viewModelStoreOwner = IosViewModelStoreOwner()

    @State private var objects: [DtoMuseumObject] = []
    @State private var isLoading: Bool = false
    @State private var alertMessage: String?

    let navigator: ListNavigationNavigator

    let columns = [
        GridItem(.adaptive(minimum: 120), alignment: .top)
    ]

    var body: some View {
        let viewModel: ListViewModel = viewModelStoreOwner.viewModel(
            factory: ViewModelFactoriesKt.listViewModelFactory
        )

        ZStack {
            if !objects.isEmpty {
                ScrollView {
                    LazyVGrid(columns: columns, alignment: .leading, spacing: 20) {
                        ForEach(objects, id: \.objectID) { item in
                            Button {
                                viewModel.onObjectClick(objectId: item.objectID)
                            } label: {
                                ObjectFrame(obj: item)
                            }
                        }
                    }
                    .buttonStyle(.plain)
                    .padding(.horizontal)
                }
            } else if isLoading {
                ProgressView()
            } else {
                EmptyScreenContent()
            }
        }
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
        .task {
            for await latestState in viewModel.uiState {
                await MainActor.run {
                    objects = latestState.objects
                    isLoading = latestState.isLoading
                }
            }
        }
        .task {
            for await effect in viewModel.sideEffects {
                switch onEnum(of: effect) {
                case .navigation(let navigation):
                    switch onEnum(of: navigation) {
                    case .openDetail(let openDetail):
                        await MainActor.run {
                            navigator.openDetail(objectId: openDetail.objectId)
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
}

struct ObjectFrame: View {
    let obj: DtoMuseumObject

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            GeometryReader { geometry in
                AsyncImage(url: URL(string: obj.primaryImageSmall)) { phase in
                    switch phase {
                    case .empty:
                        ProgressView()
                            .frame(width: geometry.size.width, height: geometry.size.width)
                    case .success(let image):
                        image
                            .resizable()
                            .scaledToFill()
                            .frame(width: geometry.size.width, height: geometry.size.width)
                            .clipped()
                            .aspectRatio(1, contentMode: .fill)
                    default:
                        EmptyView()
                            .frame(width: geometry.size.width, height: geometry.size.width)
                    }
                }
            }
            .aspectRatio(1, contentMode: .fit)

            Text(obj.title)
                .font(.headline)

            Text(obj.artistDisplayName)
                .font(.subheadline)

            Text(obj.objectDate)
                .font(.caption)
        }
    }
}
