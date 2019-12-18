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
    public void draw(@NonNull Canvas canvas) {
        head.draw(canvas);
        torso.draw(canvas);
        leftArm.draw(canvas);
        rightArm.draw(canvas);
        leftLeg.draw(canvas);
        rightLeg.draw(canvas);
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
        rightArm.rotate(startingPoint, endingPoint, degree);
    }

    public void actionSwingBody() {
        if (Math.abs(bodySwingDegree) >= 45) {
            bodySwingDegreeDelta *= -1;
        }
        Log.i(TAG, "actionSwingBody: swing degree " + bodySwingDegree + " delta:" + bodySwingDegreeDelta);
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
            if (!startLegMovement && leftLegMoving) {
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
            if (!startLegMovement && !leftLegMoving) {
                startLegMovement = true;
                leftLegMoving = true;
                return;
            }
        }

        rightLegRaiseDegree += rightLegRaiseDegreeDelta;
        rightLeg.raiseLeg(rightLegRaiseDegree);
    }

    //72 repetition
    public void makeSteps() {
        if (leftLegMoving) {
            raiseLeftLeg();
        } else {
            raiseRightLeg();
        }
    }

    boolean startArmBackForthMovement = true;
    boolean leftArmForthMovement = true;
    double leftArmForwardDegree = 0;
    double rightArmForwardDegree = 0;
    double swingArmForthDelta = 2;

    //22 repetitions
    void swingLeftArmBackAndForth() {
        if (leftArmForwardDegree >= 44) {
            swingArmForthDelta = -2;
            startArmBackForthMovement = false;
        } else if (leftArmForwardDegree <= 0) {
            swingArmForthDelta = 2;
            if (!startArmBackForthMovement && leftArmForthMovement) {
                startArmBackForthMovement = true;
                leftArmForthMovement = false;
                return;
            }
        }

        leftArmForwardDegree += swingArmForthDelta;
        leftArm.raiseArmForward(leftArmForwardDegree);
    }

    //22 repetitions
    void swingRightArmBackAndForth() {
        if (rightArmForwardDegree >= 44) {
            swingArmForthDelta = -2;
            startArmBackForthMovement = false;
        } else if (rightArmForwardDegree <= 0) {
            swingArmForthDelta = 2;
            if (!startArmBackForthMovement && !leftArmForthMovement) {
                startArmBackForthMovement = true;
                leftArmForthMovement = true;
                return;
            }
        }

        rightArmForwardDegree += swingArmForthDelta;
        rightArm.raiseArmForward(rightArmForwardDegree);
    }

    //44 repetition
    public void FlingArmBackAndForth() {
        if (leftArmForthMovement) {
            swingLeftArmBackAndForth();
        } else {
            swingRightArmBackAndForth();
        }
    }

    boolean startArmSideMovement = true;
    boolean leftArmSideMovement = true;
    double leftArmSideDegree = 0;
    double rightArmSideDegree = 0;
    double swingArmSideDelta = 2;

    //44 repetition
    public void flingRightArmToSide() {
        if (rightArmSideDegree >= 44) {
            swingArmSideDelta = -2;
            startArmSideMovement = false;
        } else if (rightArmSideDegree <= 0) {
            swingArmSideDelta = 2;
            if (!startArmSideMovement && !leftArmSideMovement) {
                startArmSideMovement = true;
                leftArmSideMovement = true;
                return;
            }
        }
        rightArmSideDegree += swingArmSideDelta;
        rightArm.raiseArmToRight(rightArmSideDegree);
    }

    //44 repetition
    public void flingLeftArmToSide() {
        if (leftArmSideDegree >= 44) { //22
            swingArmSideDelta = -2;
            startArmSideMovement = false;
        } else if (leftArmSideDegree <= 0) { //22
            swingArmSideDelta = 2;
            if (!startArmSideMovement && leftArmSideMovement) {
                startArmSideMovement = true;
                leftArmSideMovement = false;
                return;
            }
        }
        leftArmSideDegree += swingArmSideDelta;
        leftArm.raiseArmToLeft(leftArmSideDegree);
    }

    public void FlingArmToOppositeSides() {
        if (leftArmSideMovement) {
            flingLeftArmToSide();
        } else {
            flingRightArmToSide();
        }
    }

    double degree = 0;
    double armsSideSwingDegree = 0;
    double armsSideSwingDegreeDelta = 2;
    //44 repetition
    public void swingArmToOneSide() {
        leftArm.raiseArmForward(30);
        rightArm.raiseArmForward(30);

        if (Math.abs(armsSideSwingDegree)>=44){
            armsSideSwingDegreeDelta*=-1;
            if (armsSideSwingDegree == 0){
                return;
            }
        }
        armsSideSwingDegree+=armsSideSwingDegreeDelta;
        leftArm.swingArmToLeft(armsSideSwingDegree);
        rightArm.swingArmToLeft(armsSideSwingDegree);
    }

    public void dance(final int stepNumber){
        //88 - back and forth     88 - oppo side      88 - same side
        int handMovement = stepNumber%264;
        if (handMovement>=0 && handMovement<=88){
            FlingArmBackAndForth();
        } else if (handMovement>88 && handMovement<=176){
            FlingArmToOppositeSides();
        } else {
            swingArmToOneSide();
        }

        //72 make steps
        int footMovement = stepNumber%264;
        if (footMovement>=0 && handMovement<=144){
            makeSteps();
        }

        actionSwingBody();
    }


}
