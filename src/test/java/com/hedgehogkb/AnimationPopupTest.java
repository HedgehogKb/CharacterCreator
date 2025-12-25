package com.hedgehogkb;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.image.BufferedImage;

import com.hedgehogkb.EditorFrame.Popups.AnimationPreviewDialog;
import com.hedgehogkb.EditorFrame.Popups.FrameChooserDialog;
import com.hedgehogkb.Fighter.MoveType;
import com.hedgehogkb.Fighter.Animation.SingleAnimation;

public class AnimationPopupTest {
    public static double FRAME_TIME = 1.0/30.0;
    private static SingleAnimation animation;
     public static void main(String[] args) {
        animation = new SingleAnimation(MoveType.STANDING);
        File spriteFolder = new File("Datapacks\\Marnie\\SpritesSheets");
        for(File imageFile : spriteFolder.listFiles()) {
            try {
                BufferedImage image = ImageIO.read(imageFile);
                animation.addFrame();
                animation.getFrame(animation.frameCount()-1).setSprite(image);
            } catch (IOException e) {
                e.printStackTrace();
            } 
        }

        animation.getFrame(0).setDuration(FRAME_TIME * 20);
        animation.getFrame(1).setDuration(FRAME_TIME * 2);
        animation.getFrame(2).setDuration(FRAME_TIME * 2);
        animation.getFrame(3).setDuration(FRAME_TIME * 20);

        animation.addFrame();
        animation.getFrame(4).setSprite(animation.getFrame(2).getSprite());
        animation.addFrame();
        animation.getFrame(5).setSprite(animation.getFrame(1).getSprite());


        animation.getFrame(4).setDuration(FRAME_TIME * 2);
        animation.getFrame(5).setDuration(FRAME_TIME * 2);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,800);
        frame.setVisible(true);
        AnimationPreviewDialog animPreview = new AnimationPreviewDialog(animation, frame);
        animPreview.setVisible(true);

        /* FrameChooserDialog frameChooser = new FrameChooserDialog(animation, frame);
        frameChooser.setVisible(true); */

        //System.out.println("Selected Index: " + frameChooser.getSelectedIndex());
     }
}
