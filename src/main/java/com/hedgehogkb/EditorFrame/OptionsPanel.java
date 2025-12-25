package com.hedgehogkb.EditorFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.hedgehogkb.Fighter.Animation.AnimationFrame;

public class OptionsPanel {
    public JPanel panel;
    public OptionsPanelListener listener;

    public JPanel labels;
    public JLabel moveNameLabel;
    public JLabel frameNumberLabel;

    public JButton previousFrame;
    public JButton nextFrame;
    public JButton insertNextFrame; //adds frame to end
    public JButton addHurtbox;
    public JButton addHitbox;
    public JButton removeHitbox;

    public OptionsPanel() {
        this.panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(0, 50));
        initializeComponents();
        addComponents();
        addActionListeners();
    }

    public void initializeComponents() {
        this.labels = new JPanel();
        labels.setLayout(new GridBagLayout());
        labels.setPreferredSize(new Dimension(200, 0));
        labels.setBorder(BorderFactory.createLineBorder(Color.black));
        this.moveNameLabel = new JLabel();
        this.frameNumberLabel = new JLabel();

        this.previousFrame = new JButton("Previous Frame");
        this.nextFrame = new JButton("Next Frame");
        this.insertNextFrame = new JButton("Insert Frame Next");
        this.addHurtbox = new JButton("Add Hurtbox");
        this.addHitbox = new JButton("Add Hitbox");
        this.removeHitbox = new JButton("Remove Hitbox");
    }

    public void addComponents() {
        //default grid bag constraints
        GridBagConstraints c = new GridBagConstraints();

       
        //label panel
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(2,2,3,0);
        labels.add(moveNameLabel, c);

        c.gridy = 1;
        labels.add(frameNumberLabel, c);

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.weighty = 1;
        c.weightx = 0;
        c.fill = GridBagConstraints.VERTICAL;

        //everything else
        c.weightx = 1;
        panel.add(labels, c);

        c.weightx = 0;
        c.insets = new Insets(2, 5, 2, 5);
        c.anchor = GridBagConstraints.WEST;
        panel.add(previousFrame, c);
        panel.add(nextFrame, c);
        panel.add(insertNextFrame, c);
        panel.add(addHurtbox, c);

        panel.add(addHitbox, c);
        
        c.weightx = 1;
        panel.add(removeHitbox, c);

        for(Component component : panel.getComponents()) {
            if (component instanceof JComponent jComp) {
                jComp.setAlignmentX(JComponent.LEFT_ALIGNMENT);
            }
        }
        frameNumberLabel.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
    }

    public void addActionListeners() {
        previousFrame.addActionListener(e -> {if (listener != null) listener.onPreviousFrame();});
        nextFrame.addActionListener(e -> {if (listener != null) listener.onNextFrame();});
        insertNextFrame.addActionListener(e -> {if (listener != null) listener.onInsertNextFrame();});
        addHurtbox.addActionListener(e -> {if (listener != null) listener.onAddHurtbox();});
        addHitbox.addActionListener(e -> {if (listener != null) listener.onAddHitbox();});
        removeHitbox.addActionListener(e -> {if (listener != null) listener.onRemoveHitbox();});
    }


    // GETTERS AND SETTERS

    public void setMoveText(String moveText) {
        moveNameLabel.setText("Animation: " + moveText);
        SwingUtilities.invokeLater(() -> {
            panel.updateUI();
        });
    }

    public void setFrameNumber(int frameNumber) {
        frameNumberLabel.setText("Frame: " + String.valueOf(frameNumber));
        System.out.println("frame number: "+frameNumber);
        SwingUtilities.invokeLater(() -> {
            panel.updateUI();
        });
    }

    public void setOptionsPaneListener(OptionsPanelListener listener) {
        this.listener = listener;
    }

    public JPanel getPanel() {
        return this.panel;
    }

    public static interface OptionsPanelListener {
        public void onPreviousFrame();
        public void onNextFrame();
        public void onInsertNextFrame();
        public void onAddHitbox();
        public void onAddHurtbox();
        public void onRemoveHitbox();
    }
}
