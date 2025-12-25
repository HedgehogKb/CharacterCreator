package com.hedgehogkb.Hitbox;

import java.awt.geom.Point2D;

public class TubeHitbox {
    private Point2D center1;
    private Point2D center2;
    private double radius;

    public TubeHitbox(double center1X, double center1Y, double center2X, double center2Y, double radius) {
        this.center1 = new Point2D.Double(center1X, center1Y);
        this.center2 = new Point2D.Double(center2X, center2Y);
        this.radius = radius;
    }

    public double getCenter1X() {
        return center1.getX();
    }
    public void setCenter1X(double x) {
        center1 = new Point2D.Double(x, center1.getY());
    }

    public double getCenter1Y() {
        return center1.getY();
    }
    public void setCenter1Y(double y) {
        center1 = new Point2D.Double(center1.getX(), y);
    }

    public double getCenter2X() {
        return center2.getX();
    }
    public void setCenter2X(double x) {
        center2 = new Point2D.Double(x, center2.getY());
    }

    public double getCenter2Y() {
        return center2.getY();
    }
    public void setCenter2Y(double y) {
        center2 = new Point2D.Double(center2.getX(), y);
    }

    public double getRadius() {
        return radius;
    }
    public void setRadius(double radius) {
        this.radius = radius;
    }
}
