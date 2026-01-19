import Foundation
import Shared

final class ListViewModelHolder: ObservableObject {
    let viewModel: ListViewModel

    init(museumRepository: MuseumRepository) {
        self.viewModel = ListViewModel(museumRepository: museumRepository)
    }

    deinit {
        viewModel.clear()
    }
}

final class DetailViewModelHolder: ObservableObject {
    let viewModel: DetailViewModel

    init(museumRepository: MuseumRepository, objectId: Int) {
        self.viewModel = DetailViewModel(
            museumRepository: museumRepository,
            objectId: objectId
        )
    }

    deinit {
        viewModel.clear()
    }
}
