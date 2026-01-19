import Foundation
import SwiftUI
import Shared

struct DetailView: View {
    @StateObject private var holder: DetailViewModelHolder

    @State private var museumObject: DtoMuseumObject?

    let objectId: Int32

    init(objectId: Int32) {
        self.objectId = objectId
        _holder = StateObject(
            wrappedValue: DetailViewModelHolder(
                museumRepository: KoinDependencies().museumRepository,
                objectId: objectId
            )
        )
    }

    var body: some View {
        VStack {
            if let obj = museumObject {
                ObjectDetails(obj: obj)
            } else {
                ProgressView()
            }
        }
        .task(id: objectId) {
            for await latestObject in holder.viewModel.museumObject {
                await MainActor.run {
                    museumObject = latestObject
                }
            }
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
