import SwiftUI
import Shared

enum AppRoute: Hashable {
    case detail(objectId: Int32)
}

struct AppNavigation: View {
    @State private var path: [AppRoute] = []

    var body: some View {
        let listNavigator = ListNavigatorImpl(openDetailHandler: { objectId in
            path.append(.detail(objectId: objectId))
        })
        let detailNavigator = DetailNavigatorImpl(backHandler: {
            if !path.isEmpty {
                path.removeLast()
            }
        })

        NavigationStack(path: $path) {
            ListScreen(navigator: listNavigator)
            .navigationDestination(for: AppRoute.self) { route in
                switch route {
                case .detail(let objectId):
                    DetailScreen(objectId: objectId, navigator: detailNavigator)
                }
            }
        }
    }
}

private final class ListNavigatorImpl: ListNavigationNavigator {
    private let openDetailHandler: (Int32) -> Void

    init(openDetailHandler: @escaping (Int32) -> Void) {
        self.openDetailHandler = openDetailHandler
    }

    func openDetail(objectId: Int32) {
        openDetailHandler(objectId)
    }
}

private final class DetailNavigatorImpl: DetailNavigationNavigator {
    private let backHandler: () -> Void

    init(backHandler: @escaping () -> Void) {
        self.backHandler = backHandler
    }

    func back() {
        backHandler()
    }
}
