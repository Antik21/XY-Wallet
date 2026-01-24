import SwiftUI
import Shared

struct ListScreen: View {
    @StateObject private var viewModelStoreOwner = IosViewModelStoreOwner()

    @State private var objects: [DtoMuseumObject] = []

    let columns = [
        GridItem(.adaptive(minimum: 120), alignment: .top)
    ]

    var body: some View {
        let viewModel: ListViewModel = viewModelStoreOwner.viewModel(
            factory: ViewModelFactoriesKt.listViewModelFactory
        )

        ZStack {
            if !objects.isEmpty {
                NavigationStack {
                    ScrollView {
                        LazyVGrid(columns: columns, alignment: .leading, spacing: 20) {
                            ForEach(objects, id: \.objectID) { item in
                                NavigationLink(destination: DetailScreen(objectId: item.objectID)) {
                                    ObjectFrame(obj: item)
                                }
                                .buttonStyle(PlainButtonStyle())
                            }
                        }
                        .padding(.horizontal)
                    }
                }
            } else {
                EmptyScreenContent()
            }
        }
        .task {
            for await latestObjects in viewModel.objects {
                await MainActor.run {
                    objects = latestObjects
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
