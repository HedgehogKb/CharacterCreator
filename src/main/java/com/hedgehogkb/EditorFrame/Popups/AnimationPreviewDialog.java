package com.hedgehogkb.EditorFrame.Popups;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.hedgehogkb.Fighter.Animation.SingleAnimation;

public class AnimationPreviewDialog extends JDialog {
    private SingleAnimation animation;

    private JPanel panel;
    private Timer timer;
    private int frameIndex;
    private double frameTime;

    public AnimationPreviewDialog(SingleAnimation animation, Frame owner) {
        super(owner, "Animation Preview", true);
        this.animation = animation;
        this.frameIndex = 0;
        this.frameTime = 0;
        initUI();
    }

    private void initUI() {
        this.setSize(64*5, 64*5);
        this.setLayout(new BorderLayout());

        panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                BufferedImage curSprite = animation.getFrame(frameIndex).getSprite();
                g.setColor(Color.black);
                g.fillRect(0,0,getWidth(), getHeight());
                if(curSprite != null) {
                    g.drawImage(curSprite, 0, 0, getWidth(), getHeight(), null);
                }
            }
        };

        this.add(panel);
        setLocationRelativeTo(getOwner());

        playAnimation();
    }

    public void playAnimation() {
        timer = new Timer(1000/30, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (frameTime > animation.getFrame(frameIndex).getDuration()) {
                    frameIndex = (frameIndex + 1) % animation.frameCount();
                    frameTime = 0;
                }
                SwingUtilities.invokeLater(() -> {
                    panel.repaint();
                });
                frameTime += 1.0/30.0;
            }
        });
        timer.start();
    }
}