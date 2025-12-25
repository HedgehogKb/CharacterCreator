package com.hedgehogkb.Fighter;

import java.awt.event.MouseListener;
import java.util.HashMap;

import com.hedgehogkb.Fighter.Animation.Animation;
import com.hedgehogkb.Fighter.Animation.MultiAnimation;
import com.hedgehogkb.Fighter.Animation.SingleAnimation;

public class FighterInfo {
    private String name;

    private double weight;
    private double max_grounded_time;
    private int max_jumps;
    private double standing_decel;
    private double air_decel;
    private double walking_acc;
    private double max_walking_vel;
    private double sprinting_acc;
    private double max_sprinting_vel;
    private double max_y_vel;

    private HashMap<MoveType, Animation> animations;

    public FighterInfo(String name) {
        this.name = name;
        animations = new HashMap<>();
    }

    public Animation getAnimation(MoveType moveType) {
        return animations.get(moveType);
    }

    public SingleAnimation createNewAnimation(MoveType moveType) {
        if (animations.containsKey(moveType)) throw new IllegalArgumentException("Already contains animation :(");
        SingleAnimation newAnimation = new SingleAnimation(moveType);
        animations.put(moveType, newAnimation);
        return newAnimation;
    }

    public void addAnimation(MoveType moveType, Animation animation) {
        if (animations.containsKey(moveType)) throw new IllegalArgumentException("already contains animation");
        animations.put(moveType, animation);
    }

    public boolean containsAnimation(MoveType moveType) {
        return animations.containsKey(moveType);
    }

    public boolean makeSingleAnimation(MoveType moveType) {
        Animation animation = animations.get(moveType);
        if (animation instanceof SingleAnimation) return true;
        MultiAnimation multiAinmation = (MultiAnimation) animation;
        if (multiAinmation.size() > 1) return false;

        animations.put(moveType, multiAinmation.getAnimation(0));
        return true;
    }

    public void makeMultiAnimation(MoveType moveType) {
        if (animations.containsKey(moveType)) {
            if (animations.get(moveType) instanceof MultiAnimation) throw new IllegalArgumentException("Already is a multi animation");
            animations.put(moveType, new MultiAnimation((SingleAnimation) animations.get(moveType)));
            return;
        }

        SingleAnimation startAnimation = new SingleAnimation(moveType);
        startAnimation.addFrame();

        animations.put(moveType, new MultiAnimation(startAnimation));
    }

    public void clearAnimation(MoveType moveType) {
        animations.put(moveType, new SingleAnimation(moveType));
        ((SingleAnimation) (animations.get(moveType))).addFrame();
    }

    public HashMap<MoveType, Animation> getAnimations() {
        return this.animations;
    }

    public Object[] getCurMoveTypes() {
        return animations.keySet().toArray();
    }

    //Getters and setters

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return this.weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getMaxGroundedTime() {
        return this.max_grounded_time;
    }
    public void setMaxGroundedTime(double maxGroundedTime) {
        this.max_grounded_time = maxGroundedTime;
    }

    public int getMaxJumps() {
        return this.max_jumps;
    }
    public void setMaxJumps(int maxJumps) {
        this.max_jumps = maxJumps;
    }

    public double getStandingDecel() {
        return this.standing_decel;
    }
    public void setStandingDecel(double standingDecel) {
        this.standing_decel = standingDecel;
    }

    public double getAirDecel() {
        return this.air_decel;
    }
    public void setAirDecel(double airDecel) {
        this.air_decel = airDecel;
    }

    public double getWalkingAcc() {
        return this.walking_acc;
    }
    public void setWalkingAcc(double walkingAcc) {
        this.walking_acc = walkingAcc;
    }

    public double getMaxWalkingVel() {
        return this.max_walking_vel;
    }
    public void setMaxWalkingVel(double maxWalkingVel) {
        this.max_walking_vel = maxWalkingVel;
    }

    public double getSprintingAcc() {
        return this.sprinting_acc;
    }
    public void setSprintingAcc(double sprintingAcc) {
        this.sprinting_acc = sprintingAcc;
    }

    public double getMaxSprintingVel() {
        return this.max_sprinting_vel;
    }
    public void setMaxSprintingVel(double maxSprintingVel) {
        this.max_sprinting_vel = maxSprintingVel;
    }

    public double getMaxYVel() {
        return this.max_y_vel;
    }
    public void setMaxYVel(double maxYVel) {
        this.max_y_vel = maxYVel;
    }
}
