package com.rcappstudios.qualityeducation.fragments.whiteboard.model;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class Segment {


    private int color;
    private float strokeWidth;
    private final List<Point> points = new ArrayList<>();
    private int width;
    public int height;

    public Segment(){}

    public Segment(int color, float strokeWidth, int width, int height) {
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public void addPoints(int xPos, int yPos) {
        Point point = new Point(xPos, yPos);
        points.add(point);
    }

    public List<Point> getPoints() {
        return points;
    }
}
