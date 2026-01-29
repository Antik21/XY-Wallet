package com.antik.wallet.feature.start

object StartNavigation {
    interface Navigator {
        fun openListFlow()

        fun openWebFlow(url: String)
    }
}
