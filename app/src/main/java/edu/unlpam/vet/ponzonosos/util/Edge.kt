package edu.unlpam.vet.ponzonosos.util

import android.content.Context
import android.view.View
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

object Edge {

    @JvmStatic
    fun applyDynamicEdgeAppBar(
        context: Context,
        decorView: View,
        rootContainer: View,
        headerContent: View,
        baseHeightDp: Float
    ) {
        ViewCompat.setOnApplyWindowInsetsListener(decorView) { _, insets ->
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
