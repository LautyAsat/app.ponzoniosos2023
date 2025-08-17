package edu.unlpam.vet.ponzonosos.util

import android.content.Context
import android.view.View
import android.view.Window
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

object Edge {

    @JvmStatic
    fun applyDynamicEdgeAppBar(
        context: Context,
        window: Window,
        rootContainer: View,
        headerContent: View,
        baseHeightDp: Float
    ) {
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            // Cambia altura del contenedor
            val layoutParams = rootContainer.layoutParams
            layoutParams.height = (Measures.dpToPx(context, baseHeightDp) + statusBarHeight).toInt()
            rootContainer.layoutParams = layoutParams

            // Cambia padding del header interno
            headerContent.setPadding(
                headerContent.paddingLeft,
                statusBarHeight,
                headerContent.paddingRight,
                headerContent.paddingBottom
            )

            insets
        }
    }
}
