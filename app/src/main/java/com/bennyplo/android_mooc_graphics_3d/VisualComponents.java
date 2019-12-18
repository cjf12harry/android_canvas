package com.bennyplo.android_mooc_graphics_3d;

import android.graphics.Canvas;
import android.support.annotation.NonNull;

public interface VisualComponents {
    void rotate(@NonNull final Edge axis, @NonNull final double degree);

    void rotate(@NonNull final Coordinate coordinate1,
                @NonNull final Coordinate coordinate2,
                @NonNull final double degree);

    void draw(@NonNull Canvas canvas);

    double getHeight();
    double getWidth();


}
