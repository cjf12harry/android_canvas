package com.bennyplo.android_mooc_graphics_3d;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;

import org.apache.commons.math3.complex.Quaternion;

public class Cube implements VisualComponents {
    private Coordinate[] cube_vertices;//the vertices of a 3D cube
    private Coordinate[] display_vertices;
    private Coordinate centerInWorld;
    private Coordinate originalCenterInWorld;
    public final double length;
    public final double width;
    public final double height;
    private Paint paint;
    private Paint blackOutline;


    public Cube(final double length,
                final double width,
                final double height) {
        final double l = length / 2;
        final double w = width / 2;
        final double h = height / 2;
        this.length = length;
        this.width = width;
        this.height = height;
        this.cube_vertices = new Coordinate[8];
        this.cube_vertices[0] = new Coordinate(-1 * w, -1 * h, -1 * l, 1);
        this.cube_vertices[1] = new Coordinate(-1 * w, -1 * h, 1 * l, 1);
        this.cube_vertices[2] = new Coordinate(-1 * w, 1 * h, -1 * l, 1);
        this.cube_vertices[3] = new Coordinate(-1 * w, 1 * h, 1 * l, 1);
        this.cube_vertices[4] = new Coordinate(1 * w, -1 * h, -1 * l, 1);
        this.cube_vertices[5] = new Coordinate(1 * w, -1 * h, 1 * l, 1);
        this.cube_vertices[6] = new Coordinate(1 * w, 1 * h, -1 * l, 1);
        this.cube_vertices[7] = new Coordinate(1 * w, 1 * h, 1 * l, 1);
        this.display_vertices = translate(this.cube_vertices, 0, 0, 0);
        this.blackOutline = new Paint(Paint.ANTI_ALIAS_FLAG);
        blackOutline.setStyle(Paint.Style.STROKE);
        blackOutline.setColor(Color.BLACK);
        blackOutline.setStrokeWidth(2);
    }

    public Coordinate getOriginalCenterInWorld() {
        return originalCenterInWorld;
    }

    public void setOriginalCenterInWorld(Coordinate originalCenterInWorld) {
        this.originalCenterInWorld = originalCenterInWorld;
    }

    public void setCenterInWorld(Coordinate centerInWorld) {
        this.centerInWorld = centerInWorld;
    }

    public Coordinate getCenterInWorld() {
        return centerInWorld;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public void resetToOriginal() {
        this.centerInWorld = originalCenterInWorld.clone();
        this.display_vertices = translate(this.cube_vertices, 0, 0, 0);
    }


    @Override
    public void rotate(@NonNull final Edge axis,
                       @NonNull final double degree) {
        final double rad = (degree / 180 * Math.PI) / 2;
        final Quaternion p, np;
        Coordinate translationVector = new Coordinate(0, 0, 0, 1);
        Coordinate translationRecoveryVector = new Coordinate(0, 0, 0, 1);
        switch (axis) {
            case X_AXIS:
                p = new Quaternion(Math.cos(rad), new double[]{Math.sin(rad), 0, 0});
                break;
            case Y_AXIS:
                p = new Quaternion(Math.cos(rad), new double[]{0, Math.sin(rad), 0});
                break;
            case Z_AXIS:
                p = new Quaternion(Math.cos(rad), new double[]{0, 0, Math.sin(rad)});
                break;
            case UPPER_FRONT:
                p = new Quaternion(Math.cos(rad), new double[]{Math.sin(rad), 0, 0});
                translationVector = new Coordinate(0, -1 * cube_vertices[0].y, -1 * cube_vertices[0].z, 1);
                translationRecoveryVector = new Coordinate(0, cube_vertices[0].y, cube_vertices[0].z, 1);
                break;
            default:
                throw new IllegalArgumentException("Edge not supported for rotation");
        }
        np = p.getInverse();
        display_vertices = translate(display_vertices, translationVector.x, translationVector.y, translationVector.z);
        display_vertices = rotate(display_vertices, p, np);
        display_vertices = translate(display_vertices, translationRecoveryVector.x, translationRecoveryVector.y, translationRecoveryVector.z);
    }

    @Override
    public void rotate(@NonNull Coordinate coordinate1, @NonNull Coordinate coordinate2, @NonNull double degree) {
        final double rad = (degree / 180 * Math.PI) / 2;
        final Quaternion p, np;
        Coordinate translationVector = new Coordinate(
                -1 * (coordinate1.x + coordinate2.x) / 2,
                -1 * (coordinate1.y + coordinate2.y) / 2,
                -1 * (coordinate1.z + coordinate2.z) / 2, 1);

        Coordinate rotationAxis = new Coordinate(
                (coordinate2.x - coordinate1.x),
                (coordinate2.y - coordinate1.y),
                (coordinate2.z - coordinate1.z), 1);

        Coordinate localTranslationVector = new Coordinate(
                translationVector.x + getCenterInWorld().x,
                translationVector.y + getCenterInWorld().y,
                translationVector.z + getCenterInWorld().z);

        rotationAxis.toUnitVector();

        final double sinRad = Math.sin(rad);
        p = new Quaternion(Math.cos(rad), new double[]{sinRad * rotationAxis.x, sinRad * rotationAxis.y, sinRad * rotationAxis.z});
        np = p.getInverse();
        Coordinate localTranslationVectorRecover = new Coordinate(
                -1 * localTranslationVector.x,
                -1 * localTranslationVector.y,
                -1 * localTranslationVector.z);
        display_vertices = translate(display_vertices, localTranslationVector.x, localTranslationVector.y, localTranslationVector.z);
        display_vertices = rotate(display_vertices, p, np);
        display_vertices = translate(display_vertices, localTranslationVectorRecover.x, localTranslationVectorRecover.y, localTranslationVectorRecover.z);
    }

    private Coordinate rotate(@NonNull final Coordinate input,
                              @NonNull final Quaternion p,
                              @NonNull final Quaternion np) {
        final Quaternion inputPoint = new Quaternion(0, new double[]{input.x, input.y, input.z});
        final Quaternion outputPoint = p.multiply(inputPoint).multiply(np);
        return new Coordinate(outputPoint.getQ1(),
                outputPoint.getQ2(),
                outputPoint.getQ3(),
                1);
    }

    private Coordinate[] rotate(@NonNull final Coordinate[] inputs,
                                @NonNull final Quaternion p,
                                @NonNull final Quaternion np) {
        Coordinate[] outputs = new Coordinate[8];
        for (int index = 0; index < inputs.length; index++) {
            outputs[index] = rotate(inputs[index], p, np);
        }
        return outputs;
    }

    private Coordinate Transformation(Coordinate vertex, double[] matrix) {//affine transformation with homogeneous coordinates
        //i.e. a vector (vertex) multiply with the transformation matrix
        // vertex - vector in 3D
        // matrix - transformation matrix
        Coordinate result = new Coordinate();
        result.x = matrix[0] * vertex.x + matrix[1] * vertex.y + matrix[2] * vertex.z + matrix[3];
        result.y = matrix[4] * vertex.x + matrix[5] * vertex.y + matrix[6] * vertex.z + matrix[7];
        result.z = matrix[8] * vertex.x + matrix[9] * vertex.y + matrix[10] * vertex.z + matrix[11];
        result.w = matrix[12] * vertex.x + matrix[13] * vertex.y + matrix[14] * vertex.z + matrix[15];
        return result;
    }

    private Coordinate[] Transformation(Coordinate[] vertices, double[] matrix) {   //Affine transform a 3D object with vertices
        // vertices - vertices of the 3D object.
        // matrix - transformation matrix
        Coordinate[] result = new Coordinate[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            result[i] = Transformation(vertices[i], matrix);
            result[i].Normalise();
        }
        return result;
    }

    //Affine transformation
    public Coordinate[] translate(Coordinate[] vertices, double tx, double ty, double tz) {
        double[] matrix = GetIdentityMatrix();
        matrix[3] = tx;
        matrix[7] = ty;
        matrix[11] = tz;
        return Transformation(vertices, matrix);
    }

    //matrix and transformation functions
    private double[] GetIdentityMatrix() {//return an 4x4 identity matrix
        double[] matrix = new double[16];
        matrix[0] = 1;
        matrix[1] = 0;
        matrix[2] = 0;
        matrix[3] = 0;
        matrix[4] = 0;
        matrix[5] = 1;
        matrix[6] = 0;
        matrix[7] = 0;
        matrix[8] = 0;
        matrix[9] = 0;
        matrix[10] = 1;
        matrix[11] = 0;
        matrix[12] = 0;
        matrix[13] = 0;
        matrix[14] = 0;
        matrix[15] = 1;
        return matrix;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        final Coordinate[] finalDisplayVertices = translate(display_vertices, centerInWorld.x, centerInWorld.y, centerInWorld.z);
        DrawRectFace(canvas, finalDisplayVertices, new int[]{0,1,3,2}, paint);
        DrawRectFace(canvas, finalDisplayVertices, new int[]{0,4,6,2}, paint);
        DrawRectFace(canvas, finalDisplayVertices, new int[]{4,5,7,6}, paint);
        DrawRectFace(canvas, finalDisplayVertices, new int[]{1,5,7,3}, paint);
        DrawRectFace(canvas, finalDisplayVertices, new int[]{0,1,4,5}, paint);
        DrawRectFace(canvas, finalDisplayVertices, new int[]{2,3,7,6}, paint);


        DrawLinePairs(canvas, finalDisplayVertices, 0, 1, blackOutline);
        DrawLinePairs(canvas, finalDisplayVertices, 1, 3, blackOutline);
        DrawLinePairs(canvas, finalDisplayVertices, 3, 2, blackOutline);
        DrawLinePairs(canvas, finalDisplayVertices, 2, 0, blackOutline);
        DrawLinePairs(canvas, finalDisplayVertices, 4, 5, blackOutline);
        DrawLinePairs(canvas, finalDisplayVertices, 5, 7, blackOutline);
        DrawLinePairs(canvas, finalDisplayVertices, 7, 6, blackOutline);
        DrawLinePairs(canvas, finalDisplayVertices, 6, 4, blackOutline);
        DrawLinePairs(canvas, finalDisplayVertices, 0, 4, blackOutline);
        DrawLinePairs(canvas, finalDisplayVertices, 1, 5, blackOutline);
        DrawLinePairs(canvas, finalDisplayVertices, 2, 6, blackOutline);
        DrawLinePairs(canvas, finalDisplayVertices, 3, 7, blackOutline);

    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public double getWidth() {
        return width;
    }

    public double getLength() {
        return length;
    }

    private void DrawLinePairs(Canvas canvas, Coordinate[] vertices, int start, int end, Paint paint) {//draw a line connecting 2 points
        //canvas - canvas of the view
        //points - array of points
        //start - index of the starting point
        //end - index of the ending point
        //paint - the paint of the line
        canvas.drawLine((int) vertices[start].x, (int) vertices[start].y, (int) vertices[end].x, (int) vertices[end].y, paint);
    }

    private void DrawRectFace(Canvas canvas, Coordinate[] vertices, int[] index, Paint paint) {
        Path path = new Path();
        path.moveTo((float) vertices[index[0]].x, (float) vertices[index[0]].y);
        path.lineTo((float) vertices[index[1]].x, (float) vertices[index[1]].y);
        path.lineTo((float) vertices[index[2]].x, (float) vertices[index[2]].y);
        path.lineTo((float) vertices[index[3]].x, (float) vertices[index[3]].y);
        path.close();
        canvas.drawPath(path, paint);
    }


    public Coordinate getDisplayedBottomCenter() {
        final Coordinate[] finalDisplayVertices = translate(display_vertices, centerInWorld.x, centerInWorld.y, centerInWorld.z);
        final Coordinate bottomCenter = new Coordinate(
                (finalDisplayVertices[2].x + finalDisplayVertices[7].x) / 2,
                (finalDisplayVertices[2].y + finalDisplayVertices[7].y) / 2,
                (finalDisplayVertices[2].z + finalDisplayVertices[7].z) / 2
        );
        return bottomCenter;
    }

    public Coordinate[] getDisplayedTopFrontAxis() {
        final Coordinate[] finalDisplayVertices = translate(display_vertices, centerInWorld.x, centerInWorld.y, centerInWorld.z);
        final Coordinate startPoint = finalDisplayVertices[0];
        final Coordinate endPoint = finalDisplayVertices[4];
        return new Coordinate[]{startPoint, endPoint};
    }

    public Coordinate[] getDisplayedTopBackAxis() {
        final Coordinate[] finalDisplayVertices = translate(display_vertices, centerInWorld.x, centerInWorld.y, centerInWorld.z);
        final Coordinate startPoint = finalDisplayVertices[1];
        final Coordinate endPoint = finalDisplayVertices[5];
        return new Coordinate[]{startPoint, endPoint};
    }

    public Coordinate[] getDisplayedBottomBackAxis() {
        final Coordinate[] finalDisplayVertices = translate(display_vertices, centerInWorld.x, centerInWorld.y, centerInWorld.z);
        final Coordinate startPoint = finalDisplayVertices[2];
        final Coordinate endPoint = finalDisplayVertices[6];
        return new Coordinate[]{startPoint, endPoint};
    }

    public Coordinate[] getDisplayedTopRightAxis() {
        final Coordinate[] finalDisplayVertices = translate(display_vertices, centerInWorld.x, centerInWorld.y, centerInWorld.z);
        final Coordinate startPoint = finalDisplayVertices[4];
        final Coordinate endPoint = finalDisplayVertices[5];
        return new Coordinate[]{startPoint, endPoint};
    }

    public Coordinate[] getDisplayedTopLeftAxis() {
        final Coordinate[] finalDisplayVertices = translate(display_vertices, centerInWorld.x, centerInWorld.y, centerInWorld.z);
        final Coordinate startPoint = finalDisplayVertices[0];
        final Coordinate endPoint = finalDisplayVertices[1];
        return new Coordinate[]{startPoint, endPoint};
    }

    public Coordinate[] getDisplayedBottomRightAxis() {
        final Coordinate[] finalDisplayVertices = translate(display_vertices, centerInWorld.x, centerInWorld.y, centerInWorld.z);
        final Coordinate startPoint = finalDisplayVertices[6];
        final Coordinate endPoint = finalDisplayVertices[7];
        return new Coordinate[]{startPoint, endPoint};
    }

    public Coordinate[] getDisplayedDiagonalTopRightToBottomLeftAxis() {
        final Coordinate[] finalDisplayVertices = translate(display_vertices, centerInWorld.x, centerInWorld.y, centerInWorld.z);
        final Coordinate startPoint = finalDisplayVertices[5];
        final Coordinate endPoint = finalDisplayVertices[2];
        return new Coordinate[]{startPoint, endPoint};
    }

    public Coordinate[] getDisplayedDiagonalTopLeftToBottomRightAxis() {
        final Coordinate[] finalDisplayVertices = translate(display_vertices, centerInWorld.x, centerInWorld.y, centerInWorld.z);
        final Coordinate startPoint = finalDisplayVertices[0];
        final Coordinate endPoint = finalDisplayVertices[7];
        return new Coordinate[]{startPoint, endPoint};
    }

}
