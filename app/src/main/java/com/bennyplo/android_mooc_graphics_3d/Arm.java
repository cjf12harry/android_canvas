package com.bennyplo.android_mooc_graphics_3d;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;

public class Arm implements VisualComponents {
    Cube palm, foreArm, backArm;
    private Coordinate centerInWorld; //the top-left corner
    private Coordinate OriginalCenterInWorld;


    public Arm() {
        backArm = new Cube(100, 100, 200);
        foreArm = new Cube(100, 100, 150);
        palm = new Cube(200, 100, 80);

        Paint backArmPaint = new Paint();
        backArmPaint.setColor(Color.BLUE);
        backArmPaint.setStyle(Paint.Style.FILL);
        this.backArm.setPaint(backArmPaint);

        Paint foreArmPaint = new Paint();
        foreArmPaint.setColor(Color.GREEN);
        foreArmPaint.setStyle(Paint.Style.FILL);
        this.foreArm.setPaint(foreArmPaint);

        Paint palmPaint = new Paint();
        palmPaint.setColor(Color.CYAN);
        palmPaint.setStyle(Paint.Style.FILL);
        this.palm.setPaint(palmPaint);

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
    public void draw(@NonNull Canvas canvas) {
        backArm.draw(canvas);
        foreArm.draw(canvas);
        palm.draw(canvas);
    }

    @Override
    public double getHeight() {
       return palm.getHeight()+foreArm.getHeight()+backArm.getHeight();
    }

    @Override
    public double getWidth() {
        return backArm.getWidth();
    }

    public void raiseArmForward(final double degree){
        final Coordinate[] foreArmAxis = foreArm.getDisplayedTopFrontAxis();
        foreArm.rotate(foreArmAxis[0], foreArmAxis[1], degree);
        palm.rotate(foreArmAxis[0], foreArmAxis[1], degree);

        final Coordinate[] shoulderAxis = backArm.getDisplayedTopBackAxis();
        backArm.rotate(shoulderAxis[0], shoulderAxis[1],degree);
        foreArm.rotate(shoulderAxis[0], shoulderAxis[1], degree);
        palm.rotate(shoulderAxis[0], shoulderAxis[1], degree);
    }

    public void raiseArmToLeft(final double degree){
        final Coordinate[] foreArmAxis = foreArm.getDisplayedTopRightAxis();
        foreArm.rotate(foreArmAxis[0], foreArmAxis[1], degree);
        palm.rotate(foreArmAxis[0], foreArmAxis[1], degree);

        final Coordinate[] shoulderAxis = backArm.getDisplayedTopRightAxis();
        backArm.rotate(shoulderAxis[0], shoulderAxis[1],degree);
        foreArm.rotate(shoulderAxis[0], shoulderAxis[1], degree);
        palm.rotate(shoulderAxis[0], shoulderAxis[1], degree);
    }

    public void raiseArmToRight(final double degree){
        final Coordinate[] foreArmAxis = foreArm.getDisplayedTopLeftAxis();
        foreArm.rotate(foreArmAxis[1], foreArmAxis[0], degree);
        palm.rotate(foreArmAxis[1], foreArmAxis[0], degree);

        final Coordinate[] shoulderAxis = backArm.getDisplayedTopLeftAxis();
        backArm.rotate(shoulderAxis[1], shoulderAxis[0],degree);
        foreArm.rotate(shoulderAxis[1], shoulderAxis[0], degree);
        palm.rotate(shoulderAxis[1], shoulderAxis[0], degree);
    }

    public void swingArmToLeft(final double degree){
        final Coordinate[] shoulderAxis = backArm.getDisplayedDiagonalTopRightToBottomLeftAxis();
        backArm.rotate(shoulderAxis[0], shoulderAxis[1],degree);
        foreArm.rotate(shoulderAxis[0], shoulderAxis[1], degree);
        palm.rotate(shoulderAxis[0], shoulderAxis[1], degree);
    }

    public void swingArmToRight(final double degree){
        final Coordinate[] shoulderAxis = backArm.getDisplayedDiagonalTopLeftToBottomRightAxis();
        backArm.rotate(shoulderAxis[0], shoulderAxis[1],degree);
        foreArm.rotate(shoulderAxis[0], shoulderAxis[1], degree);
        palm.rotate(shoulderAxis[0], shoulderAxis[1], degree);
    }
}
