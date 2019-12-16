package com.bennyplo.android_mooc_graphics_3d;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

public class Leg implements VisualComponents {
    private Cube thigh, calf, foot;
    private Coordinate centerInWorld; //the top-left corner

    public Leg() {
        this.thigh = new Cube(100, 150, 200);
        this.calf = new Cube(100, 150, 200);
        this.foot = new Cube(100, 150, 80);
    }

    @Override
    public void rotate(@NonNull Edge axis, @NonNull double degree) {

    }

    @Override
    public void rotate(@NonNull Coordinate coordinate1, @NonNull Coordinate coordinate2, @NonNull double degree) {
        this.thigh.rotate(coordinate1, coordinate2, degree);
        this.calf.rotate(coordinate1, coordinate2, degree);
        this.foot.rotate(coordinate1, coordinate2, degree);
    }

    public Coordinate getCenterInWorld() {
        return centerInWorld;
    }

    public void setCenterInWorld(Coordinate centerInWorld) {
        this.centerInWorld = centerInWorld;
        this.thigh.setCenterInWorld(new Coordinate(centerInWorld.x, centerInWorld.y + 100, centerInWorld.z));
        this.calf.setCenterInWorld(CoordinateUtilities.placeBelow(thigh, this.calf));
        this.foot.setCenterInWorld(CoordinateUtilities.placeBelow(this.calf, this.foot));
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull Paint paint) {
        thigh.draw(canvas, paint);
        calf.draw(canvas, paint);
        foot.draw(canvas, paint);

    }

    @Override
    public double getHeight() {
        return thigh.getHeight() + calf.getHeight() + foot.getHeight();
    }

    @Override
    public double getWidth() {
        return thigh.getWidth();
    }

    public void raiseLeg(final double degree) {
        final Coordinate[] rotationAxis = this.thigh.getDisplayedTopFrontAxis();
        this.thigh.rotate(rotationAxis[0], rotationAxis[1], degree);
        this.thigh.rotate();
        final Coordinate kneePoint = thigh.getDisplayedBottomCenter();
        this.calf.setCenterInWorld(getCalfCenterInWorld());
        this.foot.setCenterInWorld(CoordinateUtilities.placeBelow(calf, foot));
    }

    Coordinate getCalfCenterInWorld(){
        final Coordinate[] rotationAxis = this.thigh.getDisplayedBottomBackAxis();
        final Coordinate centerInWorld = new Coordinate(
                (rotationAxis[0].x+rotationAxis[1].x)/2,
                (rotationAxis[0].y+rotationAxis[1].y)/2+this.thigh.getHeight()/2,
                (rotationAxis[0].z+rotationAxis[1].z)/2+this.thigh.getLength()/2);
        return centerInWorld;

    }
}
