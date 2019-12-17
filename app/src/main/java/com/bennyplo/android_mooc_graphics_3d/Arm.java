package com.bennyplo.android_mooc_graphics_3d;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

public class Arm implements VisualComponents {
    Cube palm, foreArm, backArm;
    private Coordinate centerInWorld; //the top-left corner
    private Coordinate OriginalCenterInWorld;


    public Arm() {
        backArm = new Cube(100, 100, 200);
        foreArm = new Cube(100, 100, 300);
        palm = new Cube(200, 100, 80);
    }



    @Override
    public void rotate(@NonNull Edge axis, @NonNull double degree) {

    }

    @Override
    public void rotate(@NonNull Coordinate coordinate1, @NonNull Coordinate coordinate2, @NonNull double degree) {
        backArm.rotate(coordinate1, coordinate2, degree);
        foreArm.rotate(coordinate1, coordinate2, degree);
        palm.rotate(coordinate1, coordinate2, degree);
    }

    public Coordinate getCenterInWorld() {
        return centerInWorld;
    }

    public void setCenterInWorld(Coordinate centerInWorld) {
        this.centerInWorld = centerInWorld;
        backArm.setCenterInWorld(new Coordinate(centerInWorld.x, centerInWorld.y+100, centerInWorld.z));
        foreArm.setCenterInWorld(CoordinateUtilities.placeBelow(backArm, foreArm));
        palm.setCenterInWorld(CoordinateUtilities.placeBelow(foreArm, palm));
    }

    public Coordinate getOriginalCenterInWorld() {
        return OriginalCenterInWorld;
    }

    public void setOriginalCenterInWorld(Coordinate originalCenterInWorld) {
        OriginalCenterInWorld = originalCenterInWorld;
        backArm.setOriginalCenterInWorld(new Coordinate(originalCenterInWorld.x, originalCenterInWorld.y+100, originalCenterInWorld.z));
        foreArm.setOriginalCenterInWorld(CoordinateUtilities.placeBelow(backArm, foreArm));
        palm.setOriginalCenterInWorld(CoordinateUtilities.placeBelow(foreArm, palm));
    }

    public void reset(){
        setCenterInWorld(OriginalCenterInWorld.clone());
        backArm.resetToOriginal();
        foreArm.resetToOriginal();
        palm.resetToOriginal();
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull Paint paint) {
        backArm.draw(canvas, paint);
        foreArm.draw(canvas, paint);
        palm.draw(canvas, paint);
    }

    @Override
    public double getHeight() {
       return palm.getHeight()+foreArm.getHeight()+backArm.getHeight();
    }

    @Override
    public double getWidth() {
        return backArm.getWidth();
    }
}
