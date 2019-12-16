package com.bennyplo.android_mooc_graphics_3d;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

public class Robot implements VisualComponents {
    private Arm leftArm, rightArm;
    private Torso torso;
    private Leg leftLeg, rightLeg;
    private Head head;
    private Coordinate centerInWorld; //the top-left corner
    private int bodySwingDegree=0;
    private int bodySwingDegreeDelta = 1;

    private int leftLegRaiseDegree=0;
    private int leftLegRaiseDegreeDelta = 5;
    private int rightLegRaiseDegree=0;
    private int rightLegRaiseDegreeDelta = 5;
    private boolean movingLeftLeg = true;

    public Robot() {
        head = new Head();
        torso = new Torso();
        leftArm = new Arm();
        rightArm = new Arm();
        leftLeg = new Leg();
        rightLeg = new Leg();
    }

    public Coordinate getCenterInWorld() {
        return centerInWorld;
    }

    public void setCenterInWorld(Coordinate centerInWorld) {
        this.centerInWorld = centerInWorld;
        head.setCenterInWorld(new Coordinate(centerInWorld.x, centerInWorld.y, centerInWorld.z));
        torso.setCenterInWorld(new Coordinate(centerInWorld.x, centerInWorld.y + head.getHeight(), centerInWorld.z));
        leftArm.setCenterInWorld(new Coordinate(centerInWorld.x - torso.getWidth() / 2 - leftArm.getWidth() / 2, torso.getCenterInWorld().y + torso.getNeckHeight(), centerInWorld.z));
        rightArm.setCenterInWorld(new Coordinate(centerInWorld.x + torso.getWidth() / 2 + rightArm.getWidth() / 2, torso.getCenterInWorld().y + torso.getNeckHeight(), centerInWorld.z));
        leftLeg.setCenterInWorld(new Coordinate(centerInWorld.x - leftLeg.getWidth() / 2, torso.getCenterInWorld().y + torso.getHeight(), centerInWorld.z));
        rightLeg.setCenterInWorld(new Coordinate(centerInWorld.x + rightLeg.getWidth() / 2, torso.getCenterInWorld().y + torso.getHeight(), centerInWorld.z));
    }

    @Override
    public void rotate(@NonNull Edge axis, @NonNull double degree) {

    }

    @Override
    public void rotate(@NonNull Coordinate coordinate1, @NonNull Coordinate coordinate2, @NonNull double degree) {

    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull Paint paint) {
        head.draw(canvas, paint);
        torso.draw(canvas, paint);
        leftArm.draw(canvas, paint);
        rightArm.draw(canvas, paint);
        leftLeg.draw(canvas, paint);
        rightLeg.draw(canvas, paint);
    }

    @Override
    public double getHeight() {
        return head.getWidth() + torso.getHeight() + leftLeg.getHeight();
    }

    @Override
    public double getWidth() {
        return leftArm.getWidth() + torso.getWidth() + rightArm.getWidth();
    }

    public void rotateRobotY(final double degree){
        final Coordinate startingPoint = getCenterInWorld();
        final Coordinate endingPoint = new Coordinate(startingPoint.x, startingPoint.y+1, startingPoint.z);
        head.rotate(Edge.Y_AXIS, degree);
        torso.rotate(Edge.Y_AXIS, degree);
        leftLeg.rotate(startingPoint, endingPoint, degree);
        rightLeg.rotate(startingPoint, endingPoint, -1*degree);
        leftArm.rotate(startingPoint, endingPoint, degree);
        rightArm.rotate(startingPoint, endingPoint, -1*degree);
    }

    public void actionSwingBody(){
        if (Math.abs(bodySwingDegree)>=45){
            bodySwingDegreeDelta *= -1;
        }
        bodySwingDegree+=bodySwingDegreeDelta;
        rotateRobotY(bodySwingDegreeDelta);
    }

    public void raiseLeftLeg(){
        if (leftLegRaiseDegree>=90 || leftLegRaiseDegree<0){
            leftLegRaiseDegreeDelta *= -1;
        }
        leftLegRaiseDegree+=leftLegRaiseDegreeDelta;
        leftLeg.raiseLeg(leftLegRaiseDegreeDelta);
    }

    public void raiseRightLeg(){
        if (rightLegRaiseDegree>=90 || rightLegRaiseDegree<0){
            rightLegRaiseDegreeDelta *= -1;
        }
        rightLegRaiseDegree+=rightLegRaiseDegreeDelta;
        rightLeg.raiseLeg(rightLegRaiseDegreeDelta);
    }

    public void makeSteps(){
         raiseLeftLeg();
    }


}
