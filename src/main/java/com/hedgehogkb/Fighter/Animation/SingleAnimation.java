package com.hedgehogkb.Fighter.Animation;

import java.util.ArrayList;

import com.hedgehogkb.Fighter.MoveType;

public class SingleAnimation implements Animation{
    private ArrayList<AnimationFrame> frames;
    private double duration;
    private MoveType moveType;

    public SingleAnimation(MoveType moveType) {
        frames  = new ArrayList<>();
        this.duration = 0;
        this.moveType = moveType;
    }

    public AnimationFrame getFrame(int index) {
        return frames.get(index);
    }

    public void addFrame() {
        frames.add(new AnimationFrame());
        updateDuration();
    }
    public void addFrame(AnimationFrame frame) {
        frames.add(frame);
        updateDuration();
    }

    public void insertFrame(int index) {
        frames.add(index, new AnimationFrame());
        updateDuration();
    }

    public void removeFrame(int index) {
        frames.remove(index);
    }

    public void removeFrame(AnimationFrame frame) {
        frames.remove(frame);
    }

    public void clearAnimation() {
        frames.clear();
    }

    public void updateDuration() {
        duration = 0;
        for (AnimationFrame frame: frames) {
            duration += frame.getDuration();
        }
    }

    public int frameCount() {
        return this.frames.size();
    }

    public MoveType getMoveType() {
        return this.moveType;
    }
}