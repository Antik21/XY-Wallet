import SwiftUI

public struct UiKitButton: View {
    public let title: String
    public let action: () -> Void
    public var isEnabled: Bool

    public init(
        title: String,
        action: @escaping () -> Void,
        isEnabled: Bool = true
    ) {
        self.title = title
        self.action = action
        self.isEnabled = isEnabled
    }

    public var body: some View {
        Button(title, action: action)
            .buttonStyle(.borderedProminent)
            .disabled(!isEnabled)
    }
}
