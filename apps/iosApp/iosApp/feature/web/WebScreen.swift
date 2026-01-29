import SwiftUI
import WebKit
import Shared

struct WebScreen: View {
    @StateObject private var viewModelStoreOwner = IosViewModelStoreOwner()

    @State private var appBarTitle: String = "Web"
    @State private var currentUrl: String = ""

    let url: String

    var body: some View {
        let viewModel: WebViewModel = viewModelStoreOwner.viewModel(
            factory: ViewModelFactoriesKt.webViewModelFactory(url: url)
        )

        WebView(url: currentUrl)
            .navigationTitle(appBarTitle)
            .task {
                for await latestState in viewModel.uiState {
                    await MainActor.run {
                        appBarTitle = latestState.appBar.title
                        currentUrl = latestState.url
                    }
                }
            }
    }
}

private struct WebView: UIViewRepresentable {
    let url: String

    func makeUIView(context: Context) -> WKWebView {
        WKWebView()
    }

    func updateUIView(_ uiView: WKWebView, context: Context) {
        guard let targetUrl = URL(string: url), uiView.url?.absoluteString != url else {
            return
        }
        uiView.load(URLRequest(url: targetUrl))
    }
}
