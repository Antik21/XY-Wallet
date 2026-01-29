import SwiftUI

public struct UiKitEmptyState: View {
    public let title: String
    public var description: String?
    public var image: Image?

    public init(
        title: String,
        description: String? = nil,
        image: Image? = nil
    ) {
        self.title = title
        self.description = description
        self.image = image
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
        }
        .padding(24)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}
