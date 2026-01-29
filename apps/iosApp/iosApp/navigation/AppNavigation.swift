import SwiftUI
import Shared

enum AppRoute: Hashable {
    case list
    case detail(objectId: Int32)
    case web(url: String)
}

struct AppNavigation: View {
    @State private var path: [AppRoute] = []

    var body: some View {
        let startNavigator = StartNavigatorImpl(
            openListFlowHandler: {
                path.append(.list)
            },
            openWebFlowHandler: { url in
                path.append(.web(url: url))
            }
        )
        let listNavigator = ListNavigatorImpl(openDetailHandler: { objectId in
            path.append(.detail(objectId: objectId))
        })
        let detailNavigator = DetailNavigatorImpl(backHandler: {
            if !path.isEmpty {
                path.removeLast()
            }
        })

        NavigationStack(path: $path) {
            StartScreen(navigator: startNavigator)
                .navigationDestination(for: AppRoute.self) { route in
                    switch route {
                    case .list:
                        ListScreen(navigator: listNavigator)
                    case .detail(let objectId):
                        DetailScreen(objectId: objectId, navigator: detailNavigator)
                    case .web(let url):
                        WebScreen(url: url)
                    }
                }
        }
    }
}

private final class StartNavigatorImpl: StartNavigationNavigator {
    private let openListFlowHandler: () -> Void
    private let openWebFlowHandler: (String) -> Void

    init(
        openListFlowHandler: @escaping () -> Void,
        openWebFlowHandler: @escaping (String) -> Void
    ) {
        self.openListFlowHandler = openListFlowHandler
        self.openWebFlowHandler = openWebFlowHandler
    }

    func openListFlow() {
        openListFlowHandler()
    }

    func openWebFlow(url: String) {
        openWebFlowHandler(url)
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
