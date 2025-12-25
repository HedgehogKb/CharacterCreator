package com.hedgehogkb.Fighter.Animation;

import java.util.ArrayList;

import com.hedgehogkb.Fighter.MoveType;

public class MultiAnimation implements Animation{
    private MoveType moveType;
    private ArrayList<SingleAnimation> animations;

    public MultiAnimation(MoveType moveType) {
        this.moveType = moveType;
        animations = new ArrayList<>();
    }
    
    public MultiAnimation(SingleAnimation animation) {
        this.moveType = animation.getMoveType();
        animations = new ArrayList<>();
        animations.add(animation);
    }

    public void addAnimation() {
        animations.add(new SingleAnimation(moveType));
        animations.get(animations.size() -1).addFrame();
    }

    public void addAnimation(SingleAnimation animation) {
        if (animation.getMoveType() != moveType) throw new IllegalArgumentException("wrong move type of animation");
        animations.add(animation);
    }

    public void removeAnimation(int index) {
        animations.remove(index);
    }

    public SingleAnimation getAnimation(int index) {
        return animations.get(index);
    }

    public int size() {
        return animations.size();
    }
}
