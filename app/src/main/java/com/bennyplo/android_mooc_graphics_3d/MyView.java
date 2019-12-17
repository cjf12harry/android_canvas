package com.bennyplo.android_mooc_graphics_3d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class MyView extends View {
    private Paint redPaint; //paint object for drawing the lines
    private Cube cube;
    private Robot robot;

    public MyView(Context context) {
        super(context, null);
        final MyView thisview = this;
        //create the paint object
        redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        redPaint.setStyle(Paint.Style.STROKE);//Stroke
        redPaint.setColor(Color.RED);
        redPaint.setStrokeWidth(2);
        //create a 3D cube
        cube = new Cube(200, 200, 200);
        cube.setCenterInWorld(new Coordinate(200, 200, 0, 1));

        robot = new Robot();
        robot.setCenterInWorld(new Coordinate(500, 100, 0));
        robot.setOriginalCenterInWorld(new Coordinate(500, 100, 0));

        thisview.invalidate();//update the view

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                cube.rotate(new Coordinate(0, 0, 0, 1), new Coordinate(1, 1, 1, 1), 1);
                robot.reset();
                robot.makeSteps();
                //robot.rotateRobotY(90);
                robot.actionSwingBody();

               // robot.makeSteps();
               // robot.actionSwingBody();
                thisview.invalidate();
            }
        };
        timer.scheduleAtFixedRate(task, 100, 100);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //draw objects on the screen
        super.onDraw(canvas);
        cube.draw(canvas, redPaint);//draw a cube onto the screen
        robot.draw(canvas, redPaint);
    }
}