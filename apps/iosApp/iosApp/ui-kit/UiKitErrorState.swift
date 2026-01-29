import SwiftUI

public struct UiKitErrorState: View {
    public let title: String
    public let onRetry: () -> Void
    public var description: String?
    public var image: Image?
    public var retryTitle: String
    public var isRetryEnabled: Bool

    public init(
        title: String,
        onRetry: @escaping () -> Void,
        description: String? = nil,
        image: Image? = nil,
        retryTitle: String = "Повторить",
        isRetryEnabled: Bool = true
    ) {
        self.title = title
        self.onRetry = onRetry
        self.description = description
        self.image = image
        self.retryTitle = retryTitle
        self.isRetryEnabled = isRetryEnabled
    }

    public var body: some View {
        VStack(spacing: 12) {
            if let image {
                image
                    .resizable()
                    .scaledToFit()
                    .frame(width: 120, height: 120)
            }

            Text(title)
                .font(.headline)
                .multilineTextAlignment(.center)

            if let description, !description.isEmpty {
                Text(description)
                    .font(.subheadline)
                    .foregroundStyle(.secondary)
                    .multilineTextAlignment(.center)
            }

            UiKitButton(
                title: retryTitle,
                action: onRetry,
                isEnabled: isRetryEnabled
            )
            .padding(.top, 8)
        }
        .padding(24)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}
