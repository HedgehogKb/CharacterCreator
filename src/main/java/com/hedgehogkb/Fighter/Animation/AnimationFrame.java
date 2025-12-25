package com.hedgehogkb.Fighter.Animation;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.hedgehogkb.Hitbox.AttackHitbox;
import com.hedgehogkb.Hitbox.TubeHitbox;


public class AnimationFrame implements Animation{
    private BufferedImage sprite;
    private double duration;
    private ArrayList<TubeHitbox> hurtboxes;
    private ArrayList<AttackHitbox> attackHitboxes;
    //private List<Projectile> projectiles;

    private boolean changeXVel;
    private double xVel;
    private boolean changeXAcc;
    private double xAcc;

    private boolean changeYVel;
    private double yVel;
    private boolean changeYAcc;
    private double yAcc;

    public AnimationFrame() {
        this.sprite = null;
        this.duration = 1.0/30.0;
        this.hurtboxes = new ArrayList<>();
        this.attackHitboxes = new ArrayList<>();

        this.changeXVel = false;
        xVel = 0;
        this.changeXAcc = false;
        xAcc = 0;

        this.changeYVel = false;
        yVel = 0;
        this.changeYAcc = false;
        yAcc = 0;
    }

    public double getDuration() {
        return this.duration;
    }
    public void setDuration(double duration) {
        this.duration = duration;
    }

    public boolean getChangeXVel() {
        return changeXVel;
    }
    public void setChangeXVel(boolean changeXVel) {
        this.changeXVel = changeXVel;
    }

    public double getXVel() {
        return xVel;
    }
    public void setXVel(double xVel) {
        this.xVel = xVel;
    }

    public boolean getChangeXAcc() {
        return changeXAcc;
    }
    public void setChangeXAcc(boolean changeXAcc) {
        this.changeXAcc = changeXAcc;
    }

    public double getXAcc() {
        return xAcc;
    }
    public void setXAcc(double xAcc) {
        this.xAcc = xAcc;
    }

    public boolean getChangeYVel() {
        return changeYVel;
    }
    public void setChangeYVel(boolean changeYVel) {
        this.changeYVel = changeYVel;
    }

    public double getYVel() {
        return yVel;
    }
    public void setYVel(double yVel) {
        this.yVel = yVel;
    }

    public boolean getChangeYAcc() {
        return changeYAcc;
    }
    public void setChangeYAcc(boolean changeYAcc) {
        this.changeYAcc = changeYAcc;
    }

    public double getYAcc() {
        return yAcc;
    }
    public void setYAcc(double yAcc) {
        this.yAcc = yAcc;
    }
   
    public void addHurtbox(TubeHitbox hitbox) {
        hurtboxes.add(hitbox);
    }
    public boolean removeHurtbox(TubeHitbox hitbox) {
        return hurtboxes.remove(hitbox);
    }
    public ArrayList<TubeHitbox> getHurtboxes() {
        return this.hurtboxes;
    }

    public void addAttackHitbox(AttackHitbox hitbox) {
        attackHitboxes.add(hitbox);
    }
    public boolean removeAttackHitbox(AttackHitbox hitbox) {
        return attackHitboxes.remove(hitbox);
    }
    public ArrayList<AttackHitbox> getAttackHitboxs() {
        return this.attackHitboxes;
    }

    public BufferedImage getSprite() {
        return this.sprite;
    }
    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }
}
