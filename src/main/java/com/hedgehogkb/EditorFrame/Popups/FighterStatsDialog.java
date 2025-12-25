package com.hedgehogkb.EditorFrame.Popups;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.NumberFormat;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.hedgehogkb.Fighter.FighterInfo;

public class FighterStatsDialog extends JDialog {
    private FighterInfo fighterInfo;
    private JPanel panel;

    private JFormattedTextField weightField;
    private JFormattedTextField maxGroundedTimeField;
    private JFormattedTextField maxJumpsField;
    private JFormattedTextField standingDecelField;
    private JFormattedTextField airDecelField;
    private JFormattedTextField walkingAccField;
    private JFormattedTextField maxWalkingVelField;
    private JFormattedTextField sprintingAccField;
    private JFormattedTextField maxSprintingVelField;
    private JFormattedTextField maxYVelField;

    private JButton confirmButton;

    public FighterStatsDialog(JFrame frame, String projectName) {
        super(frame, "Fighter Stats", true);
        this.fighterInfo = new FighterInfo(projectName);
        init();
    }

    public FighterStatsDialog(FighterInfo fighterInfo, JFrame frame) {
        super(frame, "Fighter Stats", true);
        this.fighterInfo = fighterInfo;
        init();
    }

    private void init() {
        panel = new JPanel();
        this.setSize(300, 280);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        NumberFormat numFormat = NumberFormat.getNumberInstance();

        c.fill = GridBagConstraints.HORIZONTAL;

        // ROW 0
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.gridwidth = 1;
        panel.add(new JLabel("Weight:"), c);

        weightField = new JFormattedTextField(numFormat);
        c.gridx = 1;
        c.gridwidth = 2;
        panel.add(weightField, c);

        // ROW 1
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.gridwidth = 1;
        panel.add(new JLabel("Max Grounded Time:"), c);

        maxGroundedTimeField = new JFormattedTextField(numFormat);
        c.gridx = 1;
        c.gridwidth = 2;
        panel.add(maxGroundedTimeField, c);

        // ROW 2
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;
        c.gridwidth = 1;
        panel.add(new JLabel("Max Jumps:"), c);

        maxJumpsField = new JFormattedTextField(numFormat);
        c.gridx = 1;
        c.weightx = 1;
        c.gridwidth = 2;
        panel.add(maxJumpsField, c);

        // ROW 3
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 0;
        c.gridwidth = 1;
        panel.add(new JLabel("Standing Decel:"), c);

        standingDecelField = new JFormattedTextField(numFormat);
        c.gridx = 1;
        c.weightx = 1;
        c.gridwidth = 2;
        panel.add(standingDecelField, c);

        // ROW 4
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 0;
        c.gridwidth = 1;
        panel.add(new JLabel("Air Decel:"), c);

        airDecelField = new JFormattedTextField(numFormat);
        c.gridx = 1;
        c.weightx = 1;
        c.gridwidth = 2;
        panel.add(airDecelField, c);

        // ROW 5
        c.gridx = 0;
        c.gridy = 5;
        c.weightx = 0;
        c.gridwidth = 1;
        panel.add(new JLabel("Walking Acc:"), c);

        walkingAccField = new JFormattedTextField(numFormat);
        c.gridx = 1;
        c.weightx = 1;
        c.gridwidth = 2;
        panel.add(walkingAccField, c);

        // ROW 6
        c.gridx = 0;
        c.gridy = 6;
        c.weightx = 0;
        c.gridwidth = 1;
        panel.add(new JLabel("Max Walking Vel:"), c);

        maxWalkingVelField = new JFormattedTextField(numFormat);
        c.gridx = 1;
        c.weightx = 1;
        c.gridwidth = 2;
        panel.add(maxWalkingVelField, c);

        // ROW 7
        c.gridx = 0;
        c.gridy = 7;
        c.weightx = 0;
        c.gridwidth = 1;
        panel.add(new JLabel("Sprinting Acc:"), c);

        sprintingAccField = new JFormattedTextField(numFormat);
        c.gridx = 1;
        c.weightx = 1;
        c.gridwidth = 2;
        panel.add(sprintingAccField, c);

        // ROW 8
        c.gridx = 0;
        c.gridy = 8;
        c.weightx = 0;
        c.gridwidth = 1;
        panel.add(new JLabel("Max Sprinting Vel:"), c);

        maxSprintingVelField = new JFormattedTextField(numFormat);
        c.gridx = 1;
        c.weightx = 1;
        c.gridwidth = 2;
        panel.add(maxSprintingVelField, c);

        // ROW 9
        c.gridx = 0;
        c.gridy = 9;
        c.weightx = 0;
        c.gridwidth = 1;
        panel.add(new JLabel("Max Y Velocity:"), c);

        maxYVelField = new JFormattedTextField(numFormat);
        c.gridx = 1;
        c.weightx = 1;
        c.gridwidth = 2;
        panel.add(maxYVelField, c);

        confirmButton = new JButton("Confirm");
        c.gridx = 0;
        c.gridy = 10;
        c.gridwidth = 3;
        c.weighty = 1;
        c.weightx = 1;
        panel.add(confirmButton, c);

        add(panel);
        setLocationRelativeTo(getOwner());

        addListeners();
        populateFromFighter();
    }

    private void addListeners() {
        weightField.getDocument().addDocumentListener(doubleSetter(weightField, fighterInfo::setWeight));
        maxGroundedTimeField.getDocument().addDocumentListener(doubleSetter(maxGroundedTimeField, fighterInfo::setMaxGroundedTime));
        maxJumpsField.getDocument().addDocumentListener(intSetter(maxJumpsField, fighterInfo::setMaxJumps));
        standingDecelField.getDocument().addDocumentListener(doubleSetter(standingDecelField, fighterInfo::setStandingDecel));
        airDecelField.getDocument().addDocumentListener(doubleSetter(airDecelField, fighterInfo::setAirDecel));
        walkingAccField.getDocument().addDocumentListener(doubleSetter(walkingAccField, fighterInfo::setWalkingAcc));
        maxWalkingVelField.getDocument().addDocumentListener(doubleSetter(maxWalkingVelField, fighterInfo::setMaxWalkingVel));
        sprintingAccField.getDocument().addDocumentListener(doubleSetter(sprintingAccField, fighterInfo::setSprintingAcc));
        maxSprintingVelField.getDocument().addDocumentListener(doubleSetter(maxSprintingVelField, fighterInfo::setMaxSprintingVel));
        maxYVelField.getDocument().addDocumentListener(doubleSetter(maxYVelField, fighterInfo::setMaxYVel));
        confirmButton.addActionListener(e -> dispose());
    }

    private void populateFromFighter() {
        weightField.setValue(fighterInfo.getWeight());
        maxGroundedTimeField.setValue(fighterInfo.getMaxGroundedTime());
        maxJumpsField.setValue(fighterInfo.getMaxJumps());
        standingDecelField.setValue(fighterInfo.getStandingDecel());
        airDecelField.setValue(fighterInfo.getAirDecel());
        walkingAccField.setValue(fighterInfo.getWalkingAcc());
        maxWalkingVelField.setValue(fighterInfo.getMaxWalkingVel());
        sprintingAccField.setValue(fighterInfo.getSprintingAcc());
        maxSprintingVelField.setValue(fighterInfo.getMaxSprintingVel());
        maxYVelField.setValue(fighterInfo.getMaxYVel());
    }

    public FighterInfo getFighterInfo() {
        return this.fighterInfo;
    }

    private DocumentListener doubleSetter(JFormattedTextField field, DoubleConsumer setter) {
        return new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { update(); }
            @Override public void removeUpdate(DocumentEvent e) { update(); }
            @Override public void changedUpdate(DocumentEvent e) {}
            private void update() {
                if (field.getValue() instanceof Number n) {
                    setter.accept(n.doubleValue());
                }
            }
        };
    }

    private DocumentListener intSetter(JFormattedTextField field, IntConsumer setter) {
        return new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { update(); }
            @Override public void removeUpdate(DocumentEvent e) { update(); }
            @Override public void changedUpdate(DocumentEvent e) {}
            private void update() {
                if (field.getValue() instanceof Number n) {
                    setter.accept(n.intValue());
                }
            }
        };
    }
}
