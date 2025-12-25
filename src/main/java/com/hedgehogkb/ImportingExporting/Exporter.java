package com.hedgehogkb.ImportingExporting;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hedgehogkb.Fighter.FighterInfo;
import com.hedgehogkb.Fighter.MoveType;
import com.hedgehogkb.Fighter.Animation.Animation;
import com.hedgehogkb.Fighter.Animation.AnimationFrame;
import com.hedgehogkb.Fighter.Animation.MultiAnimation;
import com.hedgehogkb.Fighter.Animation.SingleAnimation;
import com.hedgehogkb.Hitbox.AttackHitbox;
import com.hedgehogkb.Hitbox.TubeHitbox;

public class Exporter {
    private static final int RESOLUTION = 64;

    /**
     * Export a very small JSON representation of FighterInfo. This is a basic stub
     * to be extended to match desired datapack format.
     */
    public static boolean export(FighterInfo info, File outFile) throws IOException {
        File packFolder = new File(outFile,info.getName());
        if (!packFolder.exists())  {
            if (!packFolder.mkdir()) return false;
        }


        File definitions = new File(packFolder, "Definitions");
        if (!definitions.exists()) {
            if (!definitions.mkdir()) return false;
        }

        File characterFile = new File(definitions, "Character.json");
        if (!characterFile.exists()) {
            if (!characterFile.createNewFile()) return false;
        }
        
        JSONObject characterJson = createJson(info);
        
        try {
            FileWriter writer = new FileWriter(characterFile);
            writer.write(characterJson.toString(4));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        File spriteSheets = new File(packFolder, "Sprite_Sheets");
        if (!spriteSheets.exists()) spriteSheets.mkdir();

        File spriteSheetFile = new File(spriteSheets, info.getName() + ".png");
        if (!spriteSheetFile.exists()) spriteSheetFile.createNewFile();

        try {
            // retrieve image
            BufferedImage bi = getSpriteSheet(info);
            ImageIO.write(bi, "png", spriteSheetFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static JSONObject createJson(FighterInfo info) {
        JSONObject characterJson = new JSONObject();
        characterJson.put("name", info.getName());
        characterJson.put("type", "character");
        characterJson.put("spritesheet", info.getName());

        JSONObject statsJson = new JSONObject();
        statsJson.put("weight", info.getWeight());
        statsJson.put("max grounded time", info.getMaxGroundedTime());
        statsJson.put("max jumps", info.getMaxJumps());
        statsJson.put("walk speed", info.getMaxWalkingVel());
        statsJson.put("sprint speed", info.getMaxSprintingVel());
        statsJson.put("ground deceleration", info.getStandingDecel());
        statsJson.put("air deceleration", info.getAirDecel());
        statsJson.put("max y velocity", info.getMaxYVel());

        characterJson.put("stats", statsJson);

        JSONObject moves = new JSONObject();
        int startY = 0;
        for (MoveType moveType : info.getAnimations().keySet()) {
            JSONObject moveJson = new JSONObject();
            if (info.getAnimation(moveType) instanceof SingleAnimation sAnim) {
                moveJson.put("variation", false);

                String type = (isAttack(moveType))? "attack" : "move";
                moveJson.put("type", type);

                JSONArray frameArrayJson = new JSONArray();
                int startx = RESOLUTION;
                for (int i = 0; i < sAnim.frameCount(); i++) {
                    frameArrayJson.put(getFrameJson(sAnim.getFrame(i), startx, startY));
                    startx += RESOLUTION;
                }
                moveJson.put("frames", frameArrayJson);
                startY += RESOLUTION;
            } else if (info.getAnimation(moveType) instanceof MultiAnimation mAnim) {
                moveJson.put("variation", true);
                String type = (isAttack(moveType))? "attack" : "move";
                moveJson.put("type", type);

                JSONArray animationArray = new JSONArray();
                for (int i = 0; i < mAnim.size(); i++) {
                    SingleAnimation sAnim = mAnim.getAnimation(i);

                    JSONObject animationJson = new JSONObject();
                    animationJson.put("index", i);
                    
                    JSONArray frameArrayJson = new JSONArray();
                    int startX = RESOLUTION;
                    for (int v = 0; v < sAnim.frameCount(); v++) {
                        frameArrayJson.put(getFrameJson(sAnim.getFrame(v), startX, startY));
                        startX += RESOLUTION;
                    }
                    animationJson.put("frames", frameArrayJson);

                    animationArray.put(animationJson);
                    startY += RESOLUTION;
                }
                moveJson.put("animations", animationArray);
            }
            moves.put(moveType.name(), moveJson);
        }
        
        characterJson.put("moves", moves);
        return characterJson;
    }

    public static JSONObject getFrameJson(AnimationFrame frame, int startX, int startY) {
        JSONObject frameJson = new JSONObject();

        frameJson.put("duration", frame.getDuration());

        if (frame.getChangeXVel()) frameJson.put("x velocity", frame.getXVel());
        if (frame.getChangeXAcc()) frameJson.put("x acceleration", frame.getXAcc());
        if (frame.getChangeYVel()) frameJson.put("y velocity", frame.getYVel());
        if (frame.getChangeYAcc()) frameJson.put("y acceleration", frame.getYAcc());
       
        JSONObject spriteInfoJson = new JSONObject();
        spriteInfoJson.put("start x", startX);
        spriteInfoJson.put("start y", startY);
        spriteInfoJson.put("width", RESOLUTION);
        spriteInfoJson.put("height", RESOLUTION);
        frameJson.put("sprite", spriteInfoJson);

        JSONArray hurtboxJson = new JSONArray();
        for (TubeHitbox hitbox : frame.getHurtboxes()) {
            JSONObject curHurtboxJson = new JSONObject();
            JSONArray c1Json = new JSONArray();
            c1Json.put(hitbox.getCenter1X());
            c1Json.put(hitbox.getCenter1Y());

            JSONArray c2Json = new JSONArray();
            c2Json.put(hitbox.getCenter2X());
            c2Json.put(hitbox.getCenter2Y());

            curHurtboxJson.put("center 1", c1Json);
            curHurtboxJson.put("center 2", c2Json);
            curHurtboxJson.put("radius", hitbox.getRadius());

            hurtboxJson.put(curHurtboxJson);
        }
        frameJson.put("hurtboxes", hurtboxJson);

        JSONArray hitboxJson = new JSONArray();
        for (AttackHitbox hitbox : frame.getAttackHitboxs()) {
            JSONObject curHitboxJson = new JSONObject();
            JSONArray c1Json = new JSONArray();
            c1Json.put(hitbox.getCenter1X());
            c1Json.put(hitbox.getCenter1Y());

            JSONArray c2Json = new JSONArray();
            c2Json.put(hitbox.getCenter2X());
            c2Json.put(hitbox.getCenter2Y());

            curHitboxJson.put("center 1", c1Json);
            curHitboxJson.put("center 2", c2Json);
            curHitboxJson.put("radius", hitbox.getRadius());
            curHitboxJson.put("damage", hitbox.getDamage());
            curHitboxJson.put("stun duration", hitbox.getStunDuration());
            curHitboxJson.put("knockback amount", hitbox.getKnockbackAmount());
            curHitboxJson.put("knockback angle", hitbox.getKnockbackAngle());

            hitboxJson.put(curHitboxJson);
        }
        frameJson.put("hitboxes", hitboxJson);

        return frameJson;
    }

    public static boolean isAttack(MoveType moveType) {
        switch (moveType) {
            case MoveType.STANDING: case MoveType.FALLING: case MoveType.IDLE: case MoveType.STUNNED: case MoveType.DOWNED: case MoveType.CROUCHING: case MoveType.SHIELDING:
                case MoveType.CROUCH_WALKING: case MoveType.WALKING: case MoveType.SPRINTING: case MoveType.JUMPING:
                return false;
            default:
                return false;
        }
    }

    /**
     * Create a sprite sheet assuming frames are 64x64 tiles arranged in row-major
     * order. `frames` must be non-null and each image should be 64x64.
     */
    public static BufferedImage getSpriteSheet(FighterInfo info) {
        HashMap<MoveType, Animation> moves = info.getAnimations();
        if (moves == null || moves.keySet().size() == 0) return null;
        

        ArrayList<SingleAnimation> animations = new ArrayList<>();
        animations = getAllAnimations(moves);

        int longestAnimation = largestAnimationLength(animations);
       
        
        BufferedImage spriteSheet = new BufferedImage(
                (longestAnimation + 1) * RESOLUTION,         // width
                animations.size() * RESOLUTION,              // height
                BufferedImage.TYPE_INT_ARGB                  
        );

        Graphics2D g2d = (Graphics2D) spriteSheet.getGraphics();
        
        //fill with blank white
        g2d.setColor(Color.white);
        g2d.fillRect(0,0,(longestAnimation + 1)*RESOLUTION, animations.size()*RESOLUTION);
       
        //draw frames
        int startY = 0;
        g2d.setColor(Color.black);
        for (int r = 0; r < animations.size(); r++) {
            int startX = RESOLUTION;
            SingleAnimation curAnimation = animations.get(r);
            drawName(g2d, startY, curAnimation.getMoveType());
            for (int c = 0; c < curAnimation.frameCount(); c++) {
                BufferedImage sprite = curAnimation.getFrame(c).getSprite();
                g2d.drawImage(sprite, startX, startY, null);
                startX += RESOLUTION;
            }
            startY += RESOLUTION;
        }

        return spriteSheet;
    }

    private static void drawName(Graphics2D g2d, int starty, MoveType moveType) {
        String[] text = moveType.name().split("_");
        for (int i = 0; i < text.length; i++) {
            starty += 20;
            g2d.drawString(text[i],0,starty);
        }
    }

    private static int largestAnimationLength(ArrayList<SingleAnimation> animations) {
        int largestSize = 0;
        for (SingleAnimation animation : animations) {
            largestSize = Math.max(animation.frameCount(), largestSize);
        }

        return largestSize;
    }

    private static ArrayList<SingleAnimation> getAllAnimations(HashMap<MoveType, Animation> moves) {
        ArrayList<SingleAnimation> animations = new ArrayList<>();
        for (MoveType moveType : moves.keySet()) {
            Animation curAnimation = moves.get(moveType);
            if (curAnimation instanceof SingleAnimation sAnim) {
                animations.add(sAnim);
            } else if (curAnimation instanceof MultiAnimation mAnim) {
                for (int v = 0; v < mAnim.size(); v++) {
                    animations.add(mAnim.getAnimation(v));
                }
            }
        }
        return animations;
    }
}
