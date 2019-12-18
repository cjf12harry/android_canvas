package com.bennyplo.android_mooc_graphics_3d;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;

public class Head implements VisualComponents{
    private Cube head;
    private Coordinate centerInWorld; //the top-left corner
    private Coordinate OriginalCenterInWorld;

    public Head() {
        this.head = new Cube(100,200,200);
        Paint headPaint = new Paint();
        headPaint.setColor(Color.BLUE);
        headPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.head.setPaint(headPaint);
    }

    public Coordinate getCenterInWorld() {
        return centerInWorld;
    }

    public void setCenterInWorld(Coordinate centerInWorld) {
        this.centerInWorld = centerInWorld;
        this.head.setCenterInWorld(new Coordinate(centerInWorld.x, centerInWorld.y+100, centerInWorld.z));
    }

    public Coordinate getOriginalCenterInWorld() {
        return OriginalCenterInWorld;
    }

    public void setOriginalCenterInWorld(Coordinate originalCenterInWorld) {
        OriginalCenterInWorld = originalCenterInWorld;
        this.head.setOriginalCenterInWorld(new Coordinate(OriginalCenterInWorld.x, OriginalCenterInWorld.y+100, OriginalCenterInWorld.z));
    }

    public void reset(){
        centerInWorld = OriginalCenterInWorld.clone();
        head.resetToOriginal();
    }

    @Override
    public void rotate(@NonNull Edge axis, @NonNull double degree) {
        this.head.rotate(axis, degree);

    }

    @Override
    public void rotate(@NonNull Coordinate coordinate1, @NonNull Coordinate coordinate2, @NonNull double degree) {
        this.head.rotate(coordinate1, coordinate2, degree);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        head.draw(canvas);
    }

    @Override
    public double getHeight() {
        return head.getHeight();
    }

    @Override
    public double getWidth() {
        return head.getWidth();
    }
}
