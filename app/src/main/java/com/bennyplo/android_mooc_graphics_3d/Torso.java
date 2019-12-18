package com.bennyplo.android_mooc_graphics_3d;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;

public class Torso implements VisualComponents{
    Cube neck, body, hip;
    private Coordinate centerInWorld;
    private Coordinate OriginalCenterInWorld;

    public Torso() {
        this.neck = new Cube(100, 50, 30);
        this.body = new Cube(100,300,550);
        this.hip = new Cube(100, 300, 100);
        Paint neckPaint = new Paint();
        neckPaint.setColor(Color.MAGENTA);
        neckPaint.setStyle(Paint.Style.FILL);
        this.neck.setPaint(neckPaint);

        Paint bodyPaint = new Paint();
        bodyPaint.setColor(Color.RED);
        bodyPaint.setStyle(Paint.Style.FILL);
        this.body.setPaint(bodyPaint);

        Paint hipPaint = new Paint();
        hipPaint.setColor(Color.MAGENTA);
        hipPaint.setStyle(Paint.Style.FILL);
        this.hip.setPaint(hipPaint);
    }

    public Coordinate getCenterInWorld() {
        return centerInWorld;
    }

    public void setCenterInWorld(Coordinate centerInWorld) {
        this.centerInWorld = centerInWorld;
        this.neck.setCenterInWorld(new Coordinate(centerInWorld.x, centerInWorld.y+15, centerInWorld.z));
        this.body.setCenterInWorld(CoordinateUtilities.placeBelow(neck, this.body));
        this.hip.setCenterInWorld(CoordinateUtilities.placeBelow(this.body, this.hip));
    }

    public Coordinate getOriginalCenterInWorld() {
        return OriginalCenterInWorld;
    }

    public void setOriginalCenterInWorld(Coordinate originalCenterInWorld) {
        OriginalCenterInWorld = originalCenterInWorld;
        this.neck.setOriginalCenterInWorld(new Coordinate(originalCenterInWorld.x, originalCenterInWorld.y+15, originalCenterInWorld.z));
        this.body.setOriginalCenterInWorld(CoordinateUtilities.placeBelow(neck, this.body));
        this.hip.setOriginalCenterInWorld(CoordinateUtilities.placeBelow(this.body, this.hip));
    }

    @Override
    public double getHeight(){
        return neck.getHeight()+body.getHeight()+hip.getHeight();
    }

    @Override
    public double getWidth() {
        return body.getWidth();
    }

    @Override
    public void rotate(@NonNull Edge axis, @NonNull double degree) {
        neck.rotate(axis, degree);
        body.rotate(axis, degree);
        hip.rotate(axis, degree);
    }

    @Override
    public void rotate(@NonNull Coordinate coordinate1, @NonNull Coordinate coordinate2, @NonNull double degree) {

    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        neck.draw(canvas);
        body.draw(canvas);
        hip.draw(canvas);
    }

    public double getNeckHeight(){
        return neck.getHeight();
    }

    public void reset(){
        centerInWorld = OriginalCenterInWorld.clone();
        this.neck.resetToOriginal();
        this.body.resetToOriginal();
        this.hip.resetToOriginal();
    }
}
