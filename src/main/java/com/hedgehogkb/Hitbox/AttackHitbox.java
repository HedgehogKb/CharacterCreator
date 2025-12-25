package com.hedgehogkb.Hitbox;

public class AttackHitbox extends TubeHitbox {
    double damage;
    double stunDuration;
    double knockbackAmount;
    double knockbackAngle;

    public AttackHitbox(double center1X, double center1Y, double center2X, double center2Y, double radius) {
        super(center1X, center1Y, center2X, center2Y, radius);
        damage = 0;
        stunDuration = 0;
        knockbackAmount = 0;
        knockbackAngle = 0;
    }

    public double getDamage() {
        return damage;
    }
    public void setDamage(double damage) {
        this.damage = damage;
    }

    public double getStunDuration() {
        return stunDuration;
    }
    public void setStunDuration(double stunDuration) {
        this.stunDuration = stunDuration;
    }

    public double getKnockbackAmount() {
        return knockbackAmount;
    }
    public void setKnockbackAmount(double knockbackAmount) {
        this.knockbackAmount = knockbackAmount;
    }

    public double getKnockbackAngle() {
        return knockbackAngle;
    }
    public void setKnockbackAngle(double knockbackAngle) {
        this.knockbackAngle = knockbackAngle;
    }

}
