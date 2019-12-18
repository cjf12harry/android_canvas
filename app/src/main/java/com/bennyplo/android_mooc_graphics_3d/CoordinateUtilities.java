package com.bennyplo.android_mooc_graphics_3d;

import android.support.annotation.NonNull;

public class CoordinateUtilities {
    public static Coordinate placeBelow(@NonNull Cube above, @NonNull Cube below) {
        Coordinate aboveCor = above.getCenterInWorld();
        Coordinate expectedBelowCor = new Coordinate(
                aboveCor.x,
                aboveCor.y + above.height / 2 + below.height / 2,
                aboveCor.z);
        return expectedBelowCor;
    }
}
