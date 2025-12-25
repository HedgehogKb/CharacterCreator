package com.hedgehogkb.ImportingExporting;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hedgehogkb.Fighter.FighterInfo;
import com.hedgehogkb.Fighter.MoveType;
import com.hedgehogkb.Fighter.Animation.Animation;
import com.hedgehogkb.Fighter.Animation.AnimationFrame;
import com.hedgehogkb.Fighter.Animation.MultiAnimation;
import com.hedgehogkb.Fighter.Animation.SingleAnimation;
import com.hedgehogkb.Hitbox.AttackHitbox;
import com.hedgehogkb.Hitbox.TubeHitbox;

public class Importer {
    public static FighterInfo importFighterInfo(File inputFile) throws IncorrectProjectException, IOException, JSONException{
        if (!inputFile.isDirectory()) throw new IncorrectProjectException("Input file is not directory.");
        String projectName = inputFile.getName();
        
        File definitions = new File(inputFile, "Definitions");
        if (!definitions.exists()) throw new IncorrectProjectException("Can't find 'Definitions' folder.");

        File spriteSheets = new File(inputFile, "Sprite_Sheets");
        if (!spriteSheets.exists()) throw new IncorrectProjectException("Can't find 'Sprite_Sheets' folder.");

        File character = new File(definitions, "Character.json");
        if (!character.exists()) throw new IncorrectProjectException("Can't find 'Character.json file in Definitions folder.");

        File spriteSheetFile = new File(spriteSheets, projectName+".png");
        BufferedImage spriteSheet = ImageIO.read(spriteSheetFile);

        FighterInfo fighterInfo = new FighterInfo(projectName);

        readJsonFile(fighterInfo, character, spriteSheet);

        return fighterInfo;
    }

    private static void readJsonFile(FighterInfo fighterInfo, File characterFile, BufferedImage spriteSheet) throws IOException, JSONException{
        String content = new String(Files.readAllBytes(Paths.get(characterFile.toURI())));
        JSONObject characterJson = new JSONObject(content);
        
        if (characterJson.has("stats")) {
            JSONObject stats =characterJson.getJSONObject("stats");

            if (stats.has("max jumps")) 
                fighterInfo.setMaxJumps(stats.getInt("max jumps"));

            if (stats.has("max grounded time")) 
                fighterInfo.setMaxGroundedTime(stats.getDouble("max grounded time"));

            if (stats.has("air deceleration")) 
                fighterInfo.setAirDecel(stats.getDouble("air deceleration"));

            if (stats.has("sprint speed")) 
                fighterInfo.setMaxSprintingVel(stats.getDouble("sprint speed"));

            if (stats.has("ground deceleration")) 
                fighterInfo.setStandingDecel(stats.getDouble("ground deceleration"));

            if (stats.has("weight")) 
                fighterInfo.setWeight(stats.getDouble("weight"));

            if (stats.has("max y velocity")) 
                fighterInfo.setMaxYVel(stats.getDouble("max y velocity"));

            if (stats.has("walk speed")) 
                fighterInfo.setMaxWalkingVel(stats.getDouble("walk speed"));
        }
    
        if (characterJson.has("moves")) {
            JSONObject moves = characterJson.getJSONObject("moves");
            for (String moveName : moves.keySet()) {
                MoveType moveType;
                try {
                    moveType = MoveType.valueOf(moveName);
                } catch (IllegalArgumentException e) {continue;}
                
                JSONObject moveJson = moves.getJSONObject(moveName);
                Animation animation = readMove(moveType, moveJson, spriteSheet);
                fighterInfo.addAnimation(moveType, animation);
            }
        }
    }

    private static Animation readMove(MoveType moveType, JSONObject moveJson, BufferedImage spriteSheet) {
        boolean variation = false;
        if (moveJson.has("variation")) variation = moveJson.getBoolean("variation");

        if (!variation) {
            if (moveJson.has("frames")) {
                JSONArray frameJsonArray = moveJson.getJSONArray("frames");
                return readFrames(moveType, frameJsonArray, spriteSheet);
            } else {
                return new SingleAnimation(moveType);
            }
        } else {
            JSONArray animationsJsonArray;
            if (!moveJson.has("animations")) return new MultiAnimation(new SingleAnimation(moveType));
            animationsJsonArray = moveJson.getJSONArray("animations");
            Iterator<Object> animationIterator = animationsJsonArray.iterator();
            MultiAnimation mAnim = new MultiAnimation(moveType);
            while (animationIterator.hasNext()) {
                JSONObject animationJson = (JSONObject) animationIterator.next();
                if (!animationJson.has("frames")) {
                    mAnim.addAnimation();
                    continue;
                }
                JSONArray frameJsonArray = animationJson.getJSONArray("frames");
                mAnim.addAnimation(readFrames(moveType, frameJsonArray, spriteSheet));
            }
            return mAnim;
        }
    }

    private static SingleAnimation readFrames(MoveType moveType, JSONArray framesJson, BufferedImage spriteSheet) {
        SingleAnimation animation = new SingleAnimation(moveType);
        for (int i = 0; i < framesJson.length(); i++) {
            JSONObject frameJson = (JSONObject) framesJson.getJSONObject(i);
            animation.addFrame(readAnimationFrame(frameJson, spriteSheet));
        }
        return animation;
    }

    private static AnimationFrame readAnimationFrame(JSONObject frameJson, BufferedImage spriteSheet) {
        AnimationFrame frame = new AnimationFrame();
        if (frameJson.has("duration")) frame.setDuration(frameJson.getDouble("duration"));
        
        if (frameJson.has("x velocity")) {
            frame.setChangeXVel(true);
            frame.setXVel(frameJson.getDouble("x velocity"));
        }

        if (frameJson.has("y velocity")) {
            frame.setChangeYVel(true);
            frame.setXVel(frameJson.getDouble("y velocity"));
        }

        if (frameJson.has("x acceleration")) {
            frame.setChangeXAcc(true);
            frame.setXVel(frameJson.getDouble("x acceleration"));
        }

        if (frameJson.has("y acceleration")) {
            frame.setChangeYAcc(true);
            frame.setXVel(frameJson.getDouble("y acceleration"));
        }

        if (frameJson.has("hurtboxes")) {
            for (int i = 0; i < frameJson.getJSONArray("hurtboxes").length(); i++) {
                TubeHitbox hurtbox = readHurtbox(frameJson.getJSONArray("hurtboxes").getJSONObject(i));
                if (hurtbox != null) frame.addHurtbox(hurtbox);
            }
        }

        if (frameJson.has("hitboxes")) {
            for (int i = 0; i < frameJson.getJSONArray("hitboxes").length(); i++) {
                AttackHitbox hitbox = readAttackHitbox(frameJson.getJSONArray("hitboxes").getJSONObject(i));
                if (hitbox != null) frame.addAttackHitbox(hitbox);
            }
        }

        if (frameJson.has("sprite")) {
            try {
                JSONObject spriteJson = frameJson.getJSONObject("sprite");
                int startX = spriteJson.getInt("start x");
                int startY = spriteJson.getInt("start y");
                int width = spriteJson.getInt("width");
                int height = spriteJson.getInt("height");

                BufferedImage sprite = spriteSheet.getSubimage(startX, startY, width, height);
                frame.setSprite(sprite);
            } catch (JSONException e) {}
        }

        return frame;
    }

    private static TubeHitbox readHurtbox(JSONObject hurtboxJson) {
        try {
            double center1X = (double) hurtboxJson.getJSONArray("center 1").getDouble(0);
            double center1Y = (double) hurtboxJson.getJSONArray("center 1").getDouble(1);

            double center2X = (double) hurtboxJson.getJSONArray("center 2").getDouble(0);
            double center2Y = (double) hurtboxJson.getJSONArray("center 2").getDouble(1);
            
            double radius = hurtboxJson.getDouble("radius");

            return new TubeHitbox(center1X, center1Y, center2X, center2Y, radius);
        } catch (JSONException e) {return null;}
    }

    private static AttackHitbox readAttackHitbox(JSONObject hitboxJson) {
        AttackHitbox attackHitbox;
        try {
            double center1X = (double) hitboxJson.getJSONArray("center 1").getDouble(0);
            double center1Y = (double) hitboxJson.getJSONArray("center 1").getDouble(1);

            double center2X = (double) hitboxJson.getJSONArray("center 2").getDouble(0);
            double center2Y = (double) hitboxJson.getJSONArray("center 2").getDouble(1);
            
            double radius = hitboxJson.getDouble("radius");
            attackHitbox = new AttackHitbox(center1X, center1Y, center2X, center2Y, radius);
        } catch (JSONException e) {return null;}
        
        if (hitboxJson.has("damage")) attackHitbox.setDamage(hitboxJson.getDouble("damage"));
        if (hitboxJson.has("stun duration")) attackHitbox.setDamage(hitboxJson.getDouble("stun duration"));
        if (hitboxJson.has("knockback amount")) attackHitbox.setDamage(hitboxJson.getDouble("knockback amount"));
        if (hitboxJson.has("knockback angle")) attackHitbox.setDamage(hitboxJson.getDouble("knockback angle"));

        return attackHitbox;
    }
}