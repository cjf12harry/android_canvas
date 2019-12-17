package com.bennyplo.android_mooc_graphics_3d;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.Log;

public class Robot implements VisualComponents {
    final static String TAG = Robot.class.getSimpleName();
    private Arm leftArm, rightArm;
    private Torso torso;
    private Leg leftLeg, rightLeg;
    private Head head;
    private Coordinate centerInWorld; //the top-left corner
    private Coordinate OriginalCenterInWorld;

    private int bodySwingDegree = 0;
    private int bodySwingDegreeDelta = 1;

    private int leftLegRaiseDegree = 0;
    private int leftLegRaiseDegreeDelta = 5;
    private int rightLegRaiseDegree = 0;
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

    public Coordinate getOriginalCenterInWorld() {
        return OriginalCenterInWorld;
    }

    public void setOriginalCenterInWorld(Coordinate originalCenterInWorld) {
        OriginalCenterInWorld = originalCenterInWorld;
        head.setOriginalCenterInWorld(new Coordinate(originalCenterInWorld.x, originalCenterInWorld.y, originalCenterInWorld.z));
        torso.setOriginalCenterInWorld(new Coordinate(originalCenterInWorld.x, originalCenterInWorld.y + head.getHeight(), originalCenterInWorld.z));
        leftArm.setOriginalCenterInWorld(new Coordinate(originalCenterInWorld.x - torso.getWidth() / 2 - leftArm.getWidth() / 2, torso.getOriginalCenterInWorld().y + torso.getNeckHeight(), OriginalCenterInWorld.z));
        rightArm.setOriginalCenterInWorld(new Coordinate(originalCenterInWorld.x + torso.getWidth() / 2 + rightArm.getWidth() / 2, torso.getOriginalCenterInWorld().y + torso.getNeckHeight(), OriginalCenterInWorld.z));
        leftLeg.setOriginalCenterInWorld(new Coordinate(originalCenterInWorld.x - leftLeg.getWidth() / 2, torso.getOriginalCenterInWorld().y + torso.getHeight(), originalCenterInWorld.z));
        rightLeg.setOriginalCenterInWorld(new Coordinate(originalCenterInWorld.x + rightLeg.getWidth() / 2, torso.getOriginalCenterInWorld().y + torso.getHeight(), originalCenterInWorld.z));
    }

    public void reset() {
        head.reset();
        torso.reset();
        leftArm.reset();
        rightArm.reset();
        leftLeg.resetLeg();
        rightLeg.resetLeg();
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

    public void rotateRobotY(final double degree) {
        final Coordinate startingPoint = getCenterInWorld();
        final Coordinate endingPoint = new Coordinate(startingPoint.x, startingPoint.y + 1, startingPoint.z);
        head.rotate(Edge.Y_AXIS, degree);
        torso.rotate(Edge.Y_AXIS, degree);
        leftLeg.rotate(startingPoint, endingPoint, degree);
        rightLeg.rotate(startingPoint, endingPoint, degree);
        leftArm.rotate(startingPoint, endingPoint, degree);
        rightArm.rotate(startingPoint, endingPoint, -1 * degree);
    }

    public void actionSwingBody() {
        if (Math.abs(bodySwingDegree) >= 45) {
            bodySwingDegreeDelta *= -1;
        }
        Log.i(TAG, "actionSwingBody: swing degree "+bodySwingDegree + " delta:"+bodySwingDegreeDelta);
        bodySwingDegree += bodySwingDegreeDelta;
        rotateRobotY(bodySwingDegree);
    }

    private boolean startLegMovement = true;
    private boolean leftLegMoving = true;

    public void raiseLeftLeg() {
        if (leftLegRaiseDegree >= 90) {
            leftLegRaiseDegreeDelta = -5;
            startLegMovement = false;
        } else if (leftLegRaiseDegree <= 0) {
            leftLegRaiseDegreeDelta = 5;
            if (!startLegMovement && leftLegMoving){
                startLegMovement = true;
                leftLegMoving = false;
                return;
            }
        }
        leftLegRaiseDegree += leftLegRaiseDegreeDelta;
        leftLeg.raiseLeg(leftLegRaiseDegree);
    }

    public void raiseRightLeg() {
        if (rightLegRaiseDegree >= 90) {
            rightLegRaiseDegreeDelta = -5;
            startLegMovement = false;
        } else if (rightLegRaiseDegree <= 0) {
            rightLegRaiseDegreeDelta = 5;
            if (!startLegMovement && !leftLegMoving){
                startLegMovement = true;
                leftLegMoving = true;
                return;
            }
        }

        rightLegRaiseDegree += rightLegRaiseDegreeDelta;
        rightLeg.raiseLeg(rightLegRaiseDegree);
    }

    public void raiseLeftLeg(final double degree) {
        leftLeg.raiseLeg(degree);
    }

    public void makeSteps() {
        if (leftLegMoving) {
            raiseLeftLeg();
        }else {
            raiseRightLeg();
        }
    }


}
