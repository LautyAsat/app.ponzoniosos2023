package edu.unlpam.vet.ponzonosos.util;

import android.content.Context;

public class Measures {
    public static float dpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
