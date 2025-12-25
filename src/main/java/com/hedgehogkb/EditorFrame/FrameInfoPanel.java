package com.hedgehogkb.EditorFrame;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.NumberFormat;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.hedgehogkb.Fighter.Animation.AnimationFrame;

public class FrameInfoPanel {
    private final JFileChooser fileChooser;
    private JPanel panel;
    private boolean saved;

    private JButton chooseImageButton;
    private ChosenImageListener chosenImageListener;

    private JLabel durationLabel;
    private JFormattedTextField durationField;
    
    private JLabel changeXVelLabel;
    private JCheckBox changeXVelCheckBox;
    private JLabel xVelLabel;
    private JFormattedTextField xVelField;

    private JLabel changeYVelLabel;
    private JCheckBox changeYVelCheckBox;
    private JLabel yVelLabel;
    private JFormattedTextField yVelField;

    private JLabel changeXAccLabel;
    private JCheckBox changeXAccCheckBox;
    private JLabel xAccLabel;
    private JFormattedTextField xAccField;

    private JLabel changeYAccLabel;
    private JCheckBox changeYAccCheckBox;
    private JLabel yAccLabel;
    private JFormattedTextField yAccField;


    private AnimationFrame animFrame;
    private boolean updateValueLock;


    public FrameInfoPanel() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);

        updateValueLock = false;

        panel = new JPanel();
        panel.setMaximumSize(new Dimension(20, 0));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), "Animation Frame Info"));
        doLayout();
        addActionListeners();
        setComponentsSelectable(false);
    }

    private void doLayout() {
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        GridBagConstraints c = new GridBagConstraints();

        NumberFormat numFormat = NumberFormat.getNumberInstance();


        chooseImageButton = new JButton("Select Image:");
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.insets = new Insets(0,0,15,0);
        panel.add(chooseImageButton, c);

        //DURATION STUFF
        durationLabel = new JLabel("Duration:");
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHEAST;
        panel.add(durationLabel, c);

        durationField = new JFormattedTextField(numFormat);
        durationField.setColumns(5);
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.insets = new Insets(0,0,15,0);
        c.weightx = 1;
        panel.add(durationField, c);

        //X VEL STUFF
        changeXVelLabel = new JLabel("Change X Velocity:");
        c.insets = new Insets(0,0,3,0);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.weightx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHEAST;
        panel.add(changeXVelLabel, c);

        changeXVelCheckBox = new JCheckBox();
        c.gridx = 2;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHEAST;
        panel.add(changeXVelCheckBox, c);

        xVelLabel = new JLabel("X Velocity");
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHEAST;
        panel.add(xVelLabel, c);

        xVelField = new JFormattedTextField(numFormat);
        xVelField.setColumns(5);
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.insets = new Insets(0,0,15,0);
        panel.add(xVelField, c);

        //Y VEL STUFF
        changeYVelLabel = new JLabel("Change Y Velocity:");
        c.insets = new Insets(0,0,3,0);
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHEAST;
        panel.add(changeYVelLabel, c);

        changeYVelCheckBox = new JCheckBox();
        c.gridx = 2;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHEAST;
        panel.add(changeYVelCheckBox, c);

        yVelLabel = new JLabel("Y Velocity");
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHEAST;
        panel.add(yVelLabel, c);

        yVelField = new JFormattedTextField(numFormat);
        yVelField.setColumns(5);
        c.gridx = 1;
        c.gridy = 5;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.insets = new Insets(0,0,15,0);
        panel.add(yVelField, c);

        //X ACC STUFF
        changeXAccLabel = new JLabel("Change X Acceleration:");
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.insets = new Insets(0,0,3,0);
        panel.add(changeXAccLabel, c);

        changeXAccCheckBox = new JCheckBox();
        c.gridx = 2;
        c.gridy = 6;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHEAST;
        panel.add(changeXAccCheckBox, c);

        xAccLabel = new JLabel("X Acceleration");
        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHEAST;
        panel.add(xAccLabel, c);

        xAccField = new JFormattedTextField(numFormat);
        xAccField.setColumns(5);
        c.gridx = 1;
        c.gridy = 7;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.insets = new Insets(0,0,15,0);
        panel.add(xAccField, c);

        //Y ACC Stuff
        changeYAccLabel = new JLabel("Change Y Acceleration:");
        c.gridx = 0;
        c.gridy = 8;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.insets = new Insets(0,0,3,0);
        panel.add(changeYAccLabel, c);

        changeYAccCheckBox = new JCheckBox();
        c.gridx = 2;
        c.gridy = 8;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHEAST;
        panel.add(changeYAccCheckBox, c);

        yAccLabel = new JLabel("Y Acceleration");
        c.gridx = 0;
        c.gridy = 9;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHEAST;
        panel.add(yAccLabel, c);

        yAccField = new JFormattedTextField(numFormat);
        yAccField.setColumns(5);
        c.gridx = 1;
        c.gridy = 9;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHEAST;
        panel.add(yAccField, c);
    }

    private void addActionListeners() {
        chooseImageButton.addActionListener(e -> {
            BufferedImage image = null;
            try {
                int outcome = fileChooser.showOpenDialog(panel);
                if (outcome == JFileChooser.APPROVE_OPTION) {
                    File imageFile = fileChooser.getSelectedFile();
                    image = ImageIO.read(imageFile);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            
            if (image == null) {
                JOptionPane.showMessageDialog(panel, "Warning: No image chosen");
                return;
            }

            if (image.getHeight() != 64 || image.getWidth() != 64) {
                JOptionPane.showMessageDialog(panel, "Sprites must be 64 x 64");
                return;
            }
            animFrame.setSprite(image);
            if (chosenImageListener != null) {
                chosenImageListener.onChosenImage();
            }
            saved = false;
        });

        durationField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (updateValueLock) return;
                Object value = durationField.getValue();
                if (value != null && value instanceof Number n) {
                    System.out.println("setting cur value "+n);
                    animFrame.setDuration(n.doubleValue());
                    saved = false;
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (updateValueLock) return;
                Object value = durationField.getValue();
                if (value != null && value instanceof Number n) {
                    System.out.println("setting rem value " +n);
                    animFrame.setDuration(n.doubleValue());
                    saved = false;
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {}
            
        });

        changeXVelCheckBox.addActionListener(e -> {
            if (updateValueLock) return;

            animFrame.setChangeXVel(changeXVelCheckBox.isSelected());
            saved = false;
        });

        xVelField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (updateValueLock) return;

                Object value = xVelField.getValue();
                if (value != null && value instanceof Number n) {
                    animFrame.setXVel(n.doubleValue());
                    saved = false;
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (updateValueLock) return;

                Object value = xVelField.getValue();
                if (value != null && value instanceof Number n) {
                    animFrame.setXVel(n.doubleValue());
                    saved = false;
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {}
        });

        changeYVelCheckBox.addActionListener(e -> {
            if (updateValueLock) return;

            animFrame.setChangeYVel(changeYVelCheckBox.isSelected());
            saved = false;
        });

        yVelField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (updateValueLock) return;

                Object value = yVelField.getValue();
                if (value != null && value instanceof Number n) {
                    animFrame.setYVel(n.doubleValue());
                    saved = false;
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (updateValueLock) return;

                Object value = yVelField.getValue();
                if (value != null && value instanceof Number n) {
                    animFrame.setYVel(n.doubleValue());
                    saved = false;
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {}
        });

        changeXAccCheckBox.addActionListener(e -> {
            if (updateValueLock) return;

            animFrame.setChangeXAcc(changeXAccCheckBox.isSelected());
            saved = false;
        });

        xAccField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (updateValueLock) return;

                Object value = xAccField.getValue();
                if (value != null && value instanceof Number n) {
                    animFrame.setXAcc(n.doubleValue());
                    saved = false;
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (updateValueLock) return;

                Object value = xAccField.getValue();
                if (value != null && value instanceof Number n) {
                    animFrame.setXAcc(n.doubleValue());
                    saved = false;
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {}
        });

        changeYAccCheckBox.addActionListener(e -> {
            if (updateValueLock) return;

            animFrame.setChangeYAcc(changeYAccCheckBox.isSelected());
            saved = false;
        });

        yAccField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (updateValueLock) return;

                Object value = yAccField.getValue();
                if (value != null && value instanceof Number n) {
                    animFrame.setYAcc(n.doubleValue());
                    saved = false;
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (updateValueLock) return;

                Object value = yAccField.getValue();
                if (value != null && value instanceof Number n) {
                    animFrame.setYAcc(n.doubleValue());
                    saved = false;
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {}
        });
    }

    public void setComponentsSelectable(boolean selectable) {
        chooseImageButton.setEnabled(selectable);
        chooseImageButton.setFocusable(selectable);
        
        durationField.setEditable(selectable);
        durationField.setFocusable(selectable);

        changeXVelCheckBox.setEnabled(selectable);
        changeYVelCheckBox.setEnabled(selectable);
        changeXAccCheckBox.setEnabled(selectable);
        changeYAccCheckBox.setEnabled(selectable);

        xVelField.setEditable(selectable);
        xVelField.setEnabled(selectable);

        yVelField.setEditable(selectable);
        yVelField.setEnabled(selectable);

        xAccField.setEditable(selectable);
        xAccField.setEnabled(selectable);

        yAccField.setEditable(selectable);
        yAccField.setEnabled(selectable);
    }

    private void refreshValues() {
        updateValueLock = true;

        durationField.setText(String.valueOf(animFrame.getDuration()));
        changeXVelCheckBox.setSelected(animFrame.getChangeXVel());
        xVelField.setText(String.valueOf(animFrame.getXVel()));
        changeYVelCheckBox.setSelected(animFrame.getChangeYVel());
        yVelField.setText(String.valueOf(animFrame.getYVel()));

        changeXAccCheckBox.setSelected(animFrame.getChangeXAcc());
        xAccField.setText(String.valueOf(animFrame.getXAcc()));
        changeYAccCheckBox.setSelected(animFrame.getChangeYAcc());
        yAccField.setText(String.valueOf(animFrame.getYAcc()));

        updateValueLock = false;
    }

    public JPanel getPanel() {
        return this.panel;
    }

    public void setAnimationFrame(AnimationFrame animFrame) {
        this.animFrame = animFrame;
        setComponentsSelectable(true);
        refreshValues();
        SwingUtilities.invokeLater(() -> panel.updateUI());
    }

    public void setChosenImageListener(ChosenImageListener listener) {
        this.chosenImageListener = listener;
    }

    public boolean getSaved() {
        return this.saved;
    }
    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public static interface ChosenImageListener {
        public void onChosenImage();
    }
}
