package com.hedgehogkb.EditorFrame.Popups;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.hedgehogkb.Fighter.Animation.SingleAnimation;
import com.hedgehogkb.Fighter.Animation.AnimationFrame;

public class FrameChooserDialog extends JDialog {
    private final int BUTTON_SIZE = 64*3;

    private JScrollPane scrollPane;
    private JPanel panel;
    private SingleAnimation animation;
    private int selectedIndex;

    public FrameChooserDialog(SingleAnimation animation, Frame owner) {
        super(owner, "Choose Frame", true);
        this.animation = animation;
        this.selectedIndex = -1;
        this.setSize(820,500);
        this.setResizable(false);
        initUI();
    }

    private void initUI() {
        scrollPane = new JScrollPane();
        panel = new JPanel();
        GridLayout layout = new GridLayout(0,4,8,8);
        panel.setLayout(layout);

        for (int i = 0; i < animation.frameCount(); i++) {
            //create final index for later use
            final int index = i;

            //grab the sprite and scale it properly
            BufferedImage sprite = animation.getFrame(index).getSprite();
            BufferedImage image;
            if (sprite != null) {
                image = scaleToFit(sprite, BUTTON_SIZE);
            } else {
                image = scaleToFit(new BufferedImage(64, 64, BufferedImage.TYPE_3BYTE_BGR), BUTTON_SIZE);
            }

            //create a new button with the created icon
            JButton button = new JButton(new ImageIcon(image));
            button.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
            button.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Frame: "+index));
            button.setContentAreaFilled(false);

            //add a mouse listener that returns the button index when double clicked.
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2 && !e.isConsumed()) {
                        e.consume();
                        selectedIndex = index;
                        dispose();
                    }
                    selectedIndex = index;
                }
            });

            //add the button to the panel.
            panel.add(button);
            setLocationRelativeTo(getOwner());
        }
        scrollPane = new JScrollPane(panel);
        this.add(scrollPane);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public static BufferedImage scaleToFit(BufferedImage src, int maxSize) {
        int w = src.getWidth();
        int h = src.getHeight();

        double scale = Math.min(
            (double) maxSize / w,
            (double) maxSize / h
        );

        int newW = (int) (w * scale);
        int newH = (int) (h * scale);

        BufferedImage scaled = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaled.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.drawImage(src, 0, 0, newW, newH, null);
        g2.dispose();

        return scaled;
    }

}