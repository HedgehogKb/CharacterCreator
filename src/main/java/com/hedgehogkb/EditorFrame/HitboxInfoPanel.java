package com.hedgehogkb.EditorFrame;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.hedgehogkb.Hitbox.AttackHitbox;
import com.hedgehogkb.Hitbox.TubeHitbox;

public class HitboxInfoPanel {
    private JPanel panel;
    private TubeHitbox hitbox;
    private boolean saved;
    private boolean updateValueLock;

    private RefreshVisualsListener refreshVisualsListener;

    private JLabel center1XLabel;
    private JFormattedTextField center1XField;

    private JLabel center1YLabel;
    private JFormattedTextField center1YField;

    private JLabel center2XLabel;
    private JFormattedTextField center2XField;

    private JLabel center2YLabel;
    private JFormattedTextField center2YField;

    private JLabel radiusLabel;
    private JFormattedTextField radiusField;

    private JLabel damageLabel;
    private JFormattedTextField damageField;

    private JLabel stunDurationLabel;
    private JFormattedTextField stunDurationField;

    private JLabel knockbackAmountLabel;
    private JFormattedTextField knockbackAmountField;

    private JLabel knockbackAngleLabel;
    private JFormattedTextField knockbackAngleField;

    public HitboxInfoPanel() {
        updateValueLock = false;
        panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), "Hitbox Info"));
        doLayout();
        addActionListeners();
        setComponentsSelectable(false);
    }

    private void doLayout() {
        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(200, 0));
        GridBagConstraints c = new GridBagConstraints();
        NumberFormat numFormat = NumberFormat.getNumberInstance();

        //Center 1 stuff
        center1XLabel = new JLabel("Center 1 X:");
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.insets = new Insets(0,0,3,0);
        panel.add(center1XLabel, c);

        center1XField = new JFormattedTextField(numFormat);
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        panel.add(center1XField, c);

        center1YLabel = new JLabel("Center 1 Y:");
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.insets = new Insets(0,0,15,0);
        panel.add(center1YLabel, c);

        center1YField = new JFormattedTextField(numFormat);
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1;
        panel.add(center1YField, c);

        //Center 2 stuff
        center2XLabel = new JLabel("Center 2 X:");
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,0,3,0);
        panel.add(center2XLabel, c);

        center2XField = new JFormattedTextField(numFormat);
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 1;
        panel.add(center2XField, c);

        center2YLabel = new JLabel("Center 2 Y:");
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 0;
        c.insets = new Insets(0,0,15,0);
        panel.add(center2YLabel, c);

        center2YField = new JFormattedTextField(numFormat);
        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 1;
        panel.add(center2YField, c);

        //radius stuff
        radiusLabel = new JLabel("Radius:");
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 0;
        c.insets = new Insets(0,0,15,0);
        panel.add(radiusLabel, c);

        radiusField = new JFormattedTextField(numFormat);
        c.gridx = 1;
        c.gridy = 4;
        c.weightx = 1;
        panel.add(radiusField, c);

        //damage stuff
        damageLabel = new JLabel("Damage:");
        c.gridx = 0;
        c.gridy = 5;
        c.weightx = 0;
        c.insets = new Insets(0,0,3,0);
        panel.add(damageLabel, c);

        damageField = new JFormattedTextField(numFormat);
        c.gridx = 1;
        c.gridy = 5;
        c.weightx = 1;
        panel.add(damageField, c);

        //Stun Duration stuff
        stunDurationLabel = new JLabel("Stun Duration:");
        c.gridx = 0;
        c.gridy = 6;
        c.weightx = 0;
        c.insets = new Insets(0,0,3,0);
        panel.add(stunDurationLabel, c);

        stunDurationField = new JFormattedTextField(numFormat);
        c.gridx = 1;
        c.gridy = 6;
        c.weightx = 1;
        panel.add(stunDurationField, c);
    
        //Knockback Amount Stuff
        knockbackAmountLabel = new JLabel("Knockback Amount:");
        c.gridx = 0;
        c.gridy = 7;
        c.weightx = 0;
        c.insets = new Insets(0,0,3,0);
        panel.add(knockbackAmountLabel, c);

        knockbackAmountField = new JFormattedTextField(numFormat);
        c.gridx = 1;
        c.gridy = 7;
        c.weightx = 1;
        panel.add(knockbackAmountField, c);

        //Knockback Angle Stuff
        knockbackAngleLabel = new JLabel("Knockback Angle:");
        c.gridx = 0;
        c.gridy = 8;
        c.weightx = 0;
        c.insets = new Insets(0,0,0,0);
        panel.add(knockbackAngleLabel, c);

        knockbackAngleField = new JFormattedTextField(numFormat);
        c.gridx = 1;
        c.gridy = 8;
        c.weightx = 0;
        c.weighty = 1;
        panel.add(knockbackAngleField, c);
    }

    private void addActionListeners() {
        center1XField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (hitbox != null && center1XField.getValue() instanceof Number n) {
                    hitbox.setCenter1X(n.doubleValue());
                    refreshVisualsListener.refreshVisuals();
                    saved = false;
                }
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                if (hitbox != null && center1XField.getValue() instanceof Number n) {
                    hitbox.setCenter1X(n.doubleValue());
                    refreshVisualsListener.refreshVisuals();
                    saved = false;
                }
            }
            @Override
            public void changedUpdate(DocumentEvent e) {}
        });

        center1YField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (hitbox != null && center1YField.getValue() instanceof Number n) {
                    hitbox.setCenter1Y(n.doubleValue());
                    refreshVisualsListener.refreshVisuals();
                    saved = false;
                }
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                if (hitbox != null && center1YField.getValue() instanceof Number n) {
                    hitbox.setCenter1Y(n.doubleValue());
                    refreshVisualsListener.refreshVisuals();
                    saved = false;
                }
            }
            @Override
            public void changedUpdate(DocumentEvent e) {}
        });

        center2XField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (hitbox != null && center2XField.getValue() instanceof Number n) {
                    hitbox.setCenter2X(n.doubleValue());
                    refreshVisualsListener.refreshVisuals();
                    saved = false;
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (hitbox != null && center2XField.getValue() instanceof Number n) {
                    hitbox.setCenter2X(n.doubleValue());
                    refreshVisualsListener.refreshVisuals();
                    saved = false;
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {}
        });

        center2YField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (updateValueLock) return;
                
                if (hitbox != null && center2YField.getValue() instanceof Number n) {
                    hitbox.setCenter2Y(n.doubleValue());
                    refreshVisualsListener.refreshVisuals();
                    saved = false;
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (updateValueLock) return;

                if (hitbox != null && center2YField.getValue() instanceof Number n) {
                    hitbox.setCenter2Y(n.doubleValue());
                    refreshVisualsListener.refreshVisuals();
                    saved = false;
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {}
        });

        radiusField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (updateValueLock) return;

                if (hitbox != null && radiusField.getValue() instanceof Number n) {
                    if (n.doubleValue() < 0.1) {
                        radiusField.setText(String.valueOf(hitbox.getRadius()));
                        return;
                    }
                    hitbox.setRadius(n.doubleValue());
                    refreshVisualsListener.refreshVisuals();
                    saved = false;
                }
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                if (hitbox != null && radiusField.getValue() instanceof Number n) {
                if (updateValueLock) return;

                    if (n.doubleValue() < 0.1) {
                        radiusField.setText(String.valueOf(hitbox.getRadius()));
                        return;
                    }
                    hitbox.setRadius(n.doubleValue());
                    refreshVisualsListener.refreshVisuals();
                    saved = false;
                }
            }
            @Override
            public void changedUpdate(DocumentEvent e) {}
        });

        damageField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (updateValueLock) return;

                if (hitbox instanceof AttackHitbox a && damageField.getValue() instanceof Number n) {
                    a.setDamage(n.doubleValue());
                    saved = false;
                }
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                if (updateValueLock) return;

                if (hitbox instanceof AttackHitbox a && damageField.getValue() instanceof Number n) {
                    a.setDamage(n.doubleValue());
                    saved = false;
                }
            }
            @Override
            public void changedUpdate(DocumentEvent e) {}
        });

        stunDurationField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (updateValueLock) return;

                if (hitbox instanceof AttackHitbox a && stunDurationField.getValue() instanceof Number n) {
                    a.setStunDuration(n.doubleValue());
                    saved = false;
                }
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                if (updateValueLock) return;

                if (hitbox instanceof AttackHitbox a && stunDurationField.getValue() instanceof Number n) {
                    a.setStunDuration(n.doubleValue());
                    saved = false;
                }
            }
            @Override
            public void changedUpdate(DocumentEvent e) {}
        });

        knockbackAmountField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (updateValueLock) return;

                if (hitbox instanceof AttackHitbox a && knockbackAmountField.getValue() instanceof Number n) {
                    a.setKnockbackAmount(n.doubleValue());
                    saved = false;
                }
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                if (updateValueLock) return;

                if (hitbox instanceof AttackHitbox a && knockbackAmountField.getValue() instanceof Number n) {
                    a.setKnockbackAmount(n.doubleValue());
                    saved = false;
                }
            }
            @Override
            public void changedUpdate(DocumentEvent e) {}
        });

        knockbackAngleField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (updateValueLock) return;

                if (hitbox instanceof AttackHitbox a && knockbackAngleField.getValue() instanceof Number n) {
                    a.setKnockbackAngle(n.doubleValue());
                    saved = false;
                }
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                if (updateValueLock) return;

                if (hitbox instanceof AttackHitbox a && knockbackAngleField.getValue() instanceof Number n) {
                    a.setKnockbackAngle(n.doubleValue());
                    saved = false;
                }
            }
            @Override
            public void changedUpdate(DocumentEvent e) {}
        });
    }

    public void setComponentsSelectable(boolean selectable) {
        center1XField.setEditable(selectable);
        center1XField.setFocusable(selectable);
        center1YField.setEditable(selectable);
        center1YField.setFocusable(selectable);

        center2XField.setEditable(selectable);
        center2XField.setFocusable(selectable);
        center2YField.setEditable(selectable);
        center2YField.setFocusable(selectable);

        radiusField.setEditable(selectable);
        radiusField.setFocusable(selectable);
        
        damageField.setEditable(selectable);
        damageField.setFocusable(selectable);
        stunDurationField.setEditable(selectable);
        stunDurationField.setFocusable(selectable);
        knockbackAmountField.setEditable(selectable);
        knockbackAmountField.setFocusable(selectable);
        knockbackAngleField.setEditable(selectable);
        knockbackAngleField.setFocusable(selectable);
    }

    public void setAttackComponentsSelectable(boolean selectable) {
        damageField.setEditable(selectable);
        stunDurationField.setEditable(selectable);
        knockbackAmountField.setEditable(selectable);
        knockbackAngleField.setEditable(selectable);
    }

    public void inputValues() {
        updateValueLock = true;

        center1XField.setText(String.valueOf(hitbox.getCenter1X()));
        center1YField.setText(String.valueOf(hitbox.getCenter1Y()));

        center2XField.setText(String.valueOf(hitbox.getCenter2X()));
        center2YField.setText(String.valueOf(hitbox.getCenter2Y()));

        radiusField.setText(String.valueOf(hitbox.getRadius()));

        if (hitbox instanceof AttackHitbox a) {
            damageField.setText(String.valueOf(a.getDamage()));
            stunDurationField.setText(String.valueOf(a.getStunDuration()));
            knockbackAmountField.setText(String.valueOf(a.getKnockbackAmount()));
            knockbackAngleField.setText(String.valueOf(a.getKnockbackAngle()));
        } else {
            damageField.setText("");
            stunDurationField.setText("");
            knockbackAmountField.setText("");
            knockbackAngleField.setText("");
        }

        updateValueLock = false;
    } 

    public void setHitbox(TubeHitbox hitbox) {
        this.hitbox = hitbox;

        if (hitbox == null) {
            updateValueLock = true;
            center1XField.setText("");
            center1YField.setText("");

            center2XField.setText("");
            center2YField.setText("");
            radiusField.setText("");
            
            damageField.setText("");
            stunDurationField.setText("");
            knockbackAmountField.setText("");
            knockbackAngleField.setText("");
            updateValueLock = false;
            setComponentsSelectable(false);
            return;
        }

        setComponentsSelectable(true);
        if (!(hitbox instanceof AttackHitbox)) {
            setAttackComponentsSelectable(false);
        }

        inputValues();
    }

    public JPanel getPanel() {
        return this.panel;
    }

    public boolean getSaved() {
        return this.saved;
    }
    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public void setRefreshVisualsListener(RefreshVisualsListener listener) {
        this.refreshVisualsListener = listener;
    }

    public static interface RefreshVisualsListener {
        public void refreshVisuals();
    }
}