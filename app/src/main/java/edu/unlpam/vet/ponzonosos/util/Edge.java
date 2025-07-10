package edu.unlpam.vet.ponzonosos.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class Edge {
    public static void applyDynamicEdgeAppBar(
            Context context,
            View decorView,
            View rootContainer,
            View headerContent,
            float baseHeightDp
    ) {
        ViewCompat.setOnApplyWindowInsetsListener(decorView, (view, insets) -> {
            int statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;

            // Setear nueva altura del contenedor
            ViewGroup.LayoutParams layoutParams = rootContainer.getLayoutParams();
            layoutParams.height = (int)(Measures.dpToPx(context, baseHeightDp) + statusBarHeight);
            rootContainer.setLayoutParams(layoutParams);

            // Setear padding superior en el header
            headerContent.setPadding(
                    headerContent.getPaddingLeft(),
                    statusBarHeight,
                    headerContent.getPaddingRight(),
                    headerContent.getPaddingBottom()
            );

            return insets;
        });
    }

}
