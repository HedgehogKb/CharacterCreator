package com.hedgehogkb.EditorFrame;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.IntStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONException;

import com.hedgehogkb.EditorFrame.FrameInfoPanel.ChosenImageListener;
import com.hedgehogkb.EditorFrame.Popups.AnimationPreviewDialog;
import com.hedgehogkb.EditorFrame.Popups.FighterStatsDialog;
import com.hedgehogkb.EditorFrame.Popups.FrameChooserDialog;
import com.hedgehogkb.Fighter.FighterInfo;
import com.hedgehogkb.Fighter.MoveType;
import com.hedgehogkb.Fighter.Animation.SingleAnimation;
import com.hedgehogkb.Fighter.Animation.Animation;
import com.hedgehogkb.Fighter.Animation.AnimationFrame;
import com.hedgehogkb.Fighter.Animation.MultiAnimation;
import com.hedgehogkb.Hitbox.AttackHitbox;
import com.hedgehogkb.Hitbox.TubeHitbox;
import com.hedgehogkb.ImportingExporting.Exporter;
import com.hedgehogkb.ImportingExporting.Importer;
import com.hedgehogkb.ImportingExporting.IncorrectProjectException;

public class EditorFrame {
    private static final char[] ILLEGAL_CHARACTERS = { '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':' };
    private static final JFileChooser fileChooser = new JFileChooser();

    private static JFrame frame;
    private static InputDetector inputDetector;
    private static EditorMenuBar menu;
    private static OptionsPanel options;
    private static FrameInfoPanel frameInfo;
    private static HitboxInfoPanel hitboxInfo;
    private static BattleCanvas canvas;

    private static boolean projectOpen;
    private static String projectTitle;
    private static boolean saved;
    private static File projectsDirectory;

    private static FighterInfo fighterInfo;
    private static SingleAnimation curAnimation;
    private static int curAnimFrameIndex;
    private static AnimationFrame curAnimFrame;

    private static int mouseX;
    private static int mouseY;
    private static int xOffset;
    private static int yOffset;
    private static double zoom;
    private static TubeHitbox draggedHitbox;
    private static int draggedCircle;
    private static TubeHitbox selectedHitbox;

    public EditorFrame() {
        if (inputDetector == null) {
            inputDetector = new InputDetector();
        }
        if (frame == null) {
            frame = new JFrame("Character Creator");
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //does nothing so that we can show unsaved warning popup
            frame.setSize(1000, 800);

            initializeComponents();
            addComponents();
            addActionListeners();

            frame.setVisible(true);
            
            mouseX = -1;
            mouseY = -1;
            xOffset = 0;
            yOffset = 0;
            zoom = 1;
            draggedHitbox = null;
        }
    }

    private void initializeComponents() {
            menu = new EditorMenuBar();
            options = new OptionsPanel();
            frameInfo = new FrameInfoPanel();
            hitboxInfo = new HitboxInfoPanel();
            canvas = new BattleCanvas(new InputDetector(), new InputDetector());
    }

    private void addComponents() {
            frame.setJMenuBar(menu.getMenuBar());

            frame.setLayout(new java.awt.BorderLayout());
            frame.add(options.getPanel(), java.awt.BorderLayout.NORTH);
            frame.add(frameInfo.getPanel(), java.awt.BorderLayout.WEST);
            frame.add(hitboxInfo.getPanel(), java.awt.BorderLayout.EAST);
            frame.add(canvas, java.awt.BorderLayout.CENTER);
    }

    private void addActionListeners() {
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (!projectOpen) return;
                refreshCanvas();
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (projectOpen && !projectSaved()) {
                  int result = JOptionPane.showConfirmDialog(frame, "Your current project is not save. Do you want to save your current project?");
                    switch (result) {
                        case JOptionPane.YES_OPTION:
                            if (!saveExport()) {
                                JOptionPane.showMessageDialog(frame, "Failed to save :(");
                                return;
                            }
                            frame.dispose();
                            break;
                        case JOptionPane.NO_OPTION:
                            frame.dispose();
                            break;
                        case JOptionPane.CANCEL_OPTION:
                            return;
                        default:
                            System.out.println("create character selected not option hmm...");
                            break;
                    }  
                } else {
                    frame.dispose();
                }
            }
        });

        // MENU BAR ACTIONS
        menu.setListener(new EditorMenuBar.EditorMenuListener() {
            @Override
            public void onCreateCharacter() {
                if ( projectOpen && !projectSaved()) {
                    int result = JOptionPane.showConfirmDialog(frame, "Your current project is not save. Do you want to save your current project?");
                    switch (result) {
                        case JOptionPane.YES_OPTION:
                            if (!saveExport()) return;
                            break;
                        case JOptionPane.NO_OPTION:
                            //do nothing because we're good
                            break;
                        case JOptionPane.CANCEL_OPTION:
                            return;
                        default:
                            System.out.println("create character selected not option hmm...");
                            break;
                    }
                }

                projectOpen = true;

                String name;
                do {
                    name = (String)JOptionPane.showInputDialog(
                    frame,
                    "Character Name:",
                    "Character Name",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "name");
                    if (name == null) {
                        JOptionPane.showMessageDialog(frame, "You must put a character name.");
                    }
                    if (containsChar(name, ILLEGAL_CHARACTERS)) {
                        name = null;
                        JOptionPane.showMessageDialog(frame, "Your name must be a valid file becase\nit will be used as the pack name.");
                    }
                    
                } while (name == null);
                projectTitle = name;
                saved = false;

                //create new fighter stats
                FighterStatsDialog fighterStatsDialog = new FighterStatsDialog(frame, projectTitle);
                fighterStatsDialog.setVisible(true);
                fighterInfo = fighterStatsDialog.getFighterInfo();

                //create the first animation
                changeCurAnimation(MoveType.STANDING);

                curAnimation.addFrame();
                changeCurAnimationFrame(0);

                saved = true;
            }

            @Override
            public void onOpenCharacter() {
                if (projectOpen && projectSaved()) {
                    int result = JOptionPane.showConfirmDialog(frame, "Your current project is not save. Do you want to save your current project?");
                    switch (result) {
                        case JOptionPane.YES_OPTION:
                            if (!saveExport()) return;
                            break;
                        case JOptionPane.NO_OPTION:
                            //do nothing because we're good
                            break;
                        case JOptionPane.CANCEL_OPTION:
                            return;
                        default:
                            System.out.println("create character selected not option hmm...");
                            return;
                    }
                }

                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.showOpenDialog(frame);

                File newProjectFile = fileChooser.getSelectedFile();
                if (newProjectFile == null) return;

                try {
                    FighterInfo newFighterInfo = Importer.importFighterInfo(newProjectFile);
                    if (newFighterInfo == null) return;
                    EditorFrame.fighterInfo = newFighterInfo;
                    EditorFrame.projectTitle = fighterInfo.getName();
                    EditorFrame.projectOpen = true;
                    EditorFrame.saved = true;
                    frameInfo.setSaved(true);
                    hitboxInfo.setSaved(true);

                    changeCurAnimation(MoveType.STANDING);
                    if (curAnimation.frameCount() < 1) curAnimation.addFrame();
                    changeCurAnimationFrame(0);

                } catch (JSONException e) {
                    JOptionPane.showMessageDialog(frame, 
                        "Error: failed to precess json - "+ e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                } catch (IncorrectProjectException e) {
                    JOptionPane.showMessageDialog(frame, 
                        "Error: bad to project directory - "+ e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(frame, 
                        "Error: failed to read files - "+ e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onSaveExport() {
                if (!projectOpen) return;

                saveExport();
            }

            @Override
            public void onSaveAt() {
                if (!projectOpen) return;

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.showSaveDialog(frame);

                File selectedDir = fileChooser.getSelectedFile();
                if (selectedDir == null) return;

                projectsDirectory = selectedDir;
                saveExport();
                
            }

            @Override
            public void onChangeName() {
                if (!projectOpen) return;

                String name;
            
                name = (String)JOptionPane.showInputDialog(
                frame,
                "Character Name:",
                "Character Name",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                projectTitle);
                if (name == null) {
                    return;
                }
                if (containsChar(name, ILLEGAL_CHARACTERS)) {
                    name = null;
                    JOptionPane.showMessageDialog(frame, "Your name must be a valid file name because\nit will be used as the pack name.");
                    return;
                }
                    
                projectTitle = name;
                fighterInfo.setName(projectTitle);
                saved = false;
                
            }

            @Override
            public void onChooseFrame() {
                if (!projectOpen) return;

                FrameChooserDialog frameChooserDialog = new FrameChooserDialog(curAnimation, frame);
                frameChooserDialog.setVisible(true);
                int frameIndex = frameChooserDialog.getSelectedIndex();
                if (frameIndex == -1) {
                    return;
                }
                changeCurAnimationFrame(frameIndex);
            }

            @Override
            public void onAddMove() {
                if (!projectOpen) return;

                MoveType m = (MoveType)JOptionPane.showInputDialog(
                    frame,
                    "Which move would you like to make?",
                    "Create new move",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    MoveType.values(),
                    "STANDING");
                
                if (m == null) return;

                if (fighterInfo.containsAnimation(m)) {
                    JOptionPane.showMessageDialog(frame, "Sorry, that move already exists.");
                    return;
                }
                fighterInfo.createNewAnimation(m);
                changeCurAnimation(m);
                curAnimation.addFrame();
                changeCurAnimationFrame(0);
                saved = false;
            }

            @Override
            public void clearMove() {
                if (!projectOpen) return;

                MoveType m = (MoveType)JOptionPane.showInputDialog(
                    frame,
                    "Which move would you like to clear?",
                    "Clear Move",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    fighterInfo.getCurMoveTypes(),
                    fighterInfo.getCurMoveTypes()[0]);

                if (m == null) return;

                int result = JOptionPane.showConfirmDialog(frame, 
                    "Are you sure you want to clear this animation?",
                    "Clear Animation?",
                    JOptionPane.YES_NO_OPTION
                );

                if (result != JOptionPane.YES_OPTION) return;

                fighterInfo.clearAnimation(m);

                if (curAnimation.getMoveType() == m) {
                    changeCurAnimation(m);
                    changeCurAnimationFrame(0);
                }
                saved = false;
            }

            @Override
            public void addMoveVariation() {
                if (!projectOpen) return;

                MoveType m = (MoveType)JOptionPane.showInputDialog(
                    frame,
                    "Which move would you like to make?",
                    "Create new move",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    MoveType.values(),
                    "STANDING");
                
                if (m == null) return;

                if (fighterInfo.getAnimation(m) instanceof MultiAnimation multi) {
                    multi.addAnimation();
                    changeCurAnimation(m);
                    changeCurAnimationFrame(0);
                    return;
                }

                fighterInfo.makeMultiAnimation(m);
                ((MultiAnimation) fighterInfo.getAnimation(m)).addAnimation();
                changeCurAnimation(m);
                changeCurAnimationFrame(0);
                saved = false;
            }

            @Override
            public void removeMoveVariation() {
                if (!projectOpen) return;

                if (!(fighterInfo.getAnimation(curAnimation.getMoveType()) instanceof MultiAnimation)) {
                    JOptionPane.showMessageDialog(frame, "Current move isn't part of a variation set.");
                }

                MultiAnimation animations = (MultiAnimation) fighterInfo.getAnimation(curAnimation.getMoveType());

                Integer result = getIndexFromMultiAnimation(animations,
                "Which animation variation do you want to remove?");

                if (result == null) return;

                MoveType moveType = curAnimation.getMoveType();
                animations.removeAnimation(result);

                if (animations.size() == 1) fighterInfo.makeSingleAnimation(moveType);
                changeCurAnimation(moveType);
                changeCurAnimationFrame(0);

                saved = false;
            }

            @Override
            public void onOpenMove() {
                if (!projectOpen) return;

                MoveType m = (MoveType)JOptionPane.showInputDialog(
                    frame,
                    "Which move would you like to open?",
                    "Open Move",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    fighterInfo.getCurMoveTypes(),
                    fighterInfo.getCurMoveTypes()[0]);

                if (m == null) return;

                changeCurAnimation(m);
                changeCurAnimationFrame(0);
            }

            @Override
            public void onAddFrame() {
                if (!projectOpen) return;
                appendFrame();
                saved = false;
            }
            
            @Override 
            public void onRemoveFrame() {
                if (!projectOpen) return;

                String s = (String)JOptionPane.showInputDialog(frame,
                    "What frame do you want to remove?",
                    "Remove frame index",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null, 
                    0);

                int n;
                try {
                    n = Integer.valueOf(s);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame, "Must input integer number");
                    return;
                } 

                if (n < 0 || n >= curAnimation.frameCount()) {
                    JOptionPane.showMessageDialog(frame, "Index must be a valid frame.");
                    return;
                }
                curAnimation.removeFrame(n);

                if (curAnimation.frameCount() <= 0) {
                    curAnimation.addFrame();
                }

                changeCurAnimationFrame(Math.max(0, curAnimFrameIndex-1));
            }

            @Override
            public void onInsertFrame() {
                if (!projectOpen) return;

                Integer n = null;
                String s = (String)JOptionPane.showInputDialog(frame, 
                    "What index to insert frame?", 
                    "Insert frame index",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    0);
                try {
                    n = Integer.valueOf(s);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame, "Must input integer number");
                    return;
                }

                insertFrame(n);
            }

            @Override
            public void onEditCharacterStats() {
                if (!projectOpen) return;

                FighterStatsDialog fighterStatsDialog = new FighterStatsDialog(fighterInfo, frame);
                fighterStatsDialog.setVisible(true);
                saved = false;
            }

            @Override
            public void onViewAnimation() {
                if (!projectOpen) return;

                AnimationPreviewDialog animationPreviewDialog = new AnimationPreviewDialog(curAnimation, frame);
                animationPreviewDialog.setVisible(true);
            }

            @Override
            public void onReturnToCenter() {
                xOffset = 0;
                yOffset = 0;
                mouseX = -1;
                mouseY = -1;
                zoom = 1;
            }
        });

        // Options Panel Actions
        options.setOptionsPaneListener(new OptionsPanel.OptionsPanelListener() {
            @Override
            public void onPreviousFrame() {
               if (!projectOpen) return;
               if (curAnimFrameIndex == 0) {
                JOptionPane.showMessageDialog(frame, "No prior frame.");
                return;
               }

               changeCurAnimationFrame(curAnimFrameIndex -1);
            }

            @Override
            public void onNextFrame() {
                if (!projectOpen) return;
                if (curAnimFrameIndex+1 == curAnimation.frameCount()) {
                    JOptionPane.showMessageDialog(frame, "No next frame.");
                    return;
                }

               changeCurAnimationFrame(curAnimFrameIndex + 1);
            }

            @Override
            public void onInsertNextFrame() {
                if (!projectOpen) return;
                insertFrame(curAnimFrameIndex + 1);
            }

            @Override
            public void onAddHitbox() {
                if (!projectOpen) return;

                AttackHitbox hitbox = new AttackHitbox(32, 0,32, 32, 5);
                curAnimFrame.addAttackHitbox(hitbox);    
                changeCurHitbox(hitbox);
                refreshCanvas();
            }

            @Override
            public void onAddHurtbox() {
                if (!projectOpen) return;
                TubeHitbox hitbox = new TubeHitbox(32, 0,32, 32, 5);
                curAnimFrame.addHurtbox(hitbox);    
                changeCurHitbox(hitbox);
                refreshCanvas();
            }

            @Override
            public void onRemoveHitbox() {
                if (!projectOpen) return;

                if (selectedHitbox == null) {
                    JOptionPane.showMessageDialog(frame, "No hitbox selected");
                    return;
                }

                if (selectedHitbox instanceof AttackHitbox a) {
                    curAnimFrame.removeAttackHitbox(a);
                } else if (selectedHitbox instanceof TubeHitbox) {
                    curAnimFrame.removeHurtbox(selectedHitbox);
                }

                changeCurHitbox(null);
                refreshCanvas();
            }
            
        });

        frameInfo.setChosenImageListener(() -> {
            refreshCanvas();
        });

        hitboxInfo.setRefreshVisualsListener(() -> {
            refreshCanvas();
        });
    }

    public static boolean saveExport() {
        if (projectsDirectory == null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.showSaveDialog(frame);

            if (fileChooser.getSelectedFile() == null) return false;

            projectsDirectory = fileChooser.getSelectedFile();
        }
        boolean result = false;
        try {
            result = Exporter.export(fighterInfo, projectsDirectory);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Failed to create files :(");
            e.printStackTrace();
            return false;
        }

        if (!result) {
            JOptionPane.showMessageDialog(frame, "Something went wrong when creating files :(");
            return false;
        }

        frameInfo.setSaved(true);
        hitboxInfo.setSaved(true);
        saved = true;
        return true;
    }

    public static boolean containsChar(String s, char[] chars) {
        for (int i = 0; i < chars.length; i++) {
            if (s.indexOf(chars[i]) != -1 ) return true;
        }
        return false;
    }

    public static boolean projectSaved() {
        return saved && frameInfo.getSaved() && hitboxInfo.getSaved();
    }

    public static void changeCurAnimation(MoveType moveType) {
        if (fighterInfo.containsAnimation(moveType)) {
            Animation animation = fighterInfo.getAnimation(moveType);
            if (animation instanceof MultiAnimation m) {
                curAnimation = selectFromMultiAnimation(m);
            } else {
                curAnimation = (SingleAnimation) fighterInfo.getAnimation( moveType);
            }
            options.setMoveText(moveType.name().replace('_', ' '));
            return;
        }
        curAnimation = fighterInfo.createNewAnimation(moveType);
        options.setMoveText(moveType.name().replace('_', ' '));
    }

    public static void changeCurAnimationFrame(int index) {
        curAnimFrameIndex = index;
        curAnimFrame = curAnimation.getFrame(index);
        options.setFrameNumber(index);
        frameInfo.setAnimationFrame(curAnimFrame);
        refreshCanvas();
    }

    public static void changeCurHitbox(TubeHitbox hitbox) {
        hitboxInfo.setHitbox(hitbox);
        selectedHitbox = hitbox;
    }

    public static void appendFrame() {
        curAnimation.addFrame();
        changeCurAnimationFrame(curAnimation.frameCount()-1);
        saved = false;
    }

    public static void insertFrame(int index) {
        if (index > curAnimation.frameCount()) {
            JOptionPane.showMessageDialog(frame, "Index must not be larger than frame count (otherwise there would be a gap)");
        }
        curAnimation.insertFrame(index);
        changeCurAnimationFrame(index);
        saved = false;
    }

    public static SingleAnimation selectFromMultiAnimation(MultiAnimation animations) {
        Integer result = getIndexFromMultiAnimation(animations, "Which animation variation do you want to select?");

        if (result == null) result = 0;

        return animations.getAnimation(result);
    }

    public static Integer getIndexFromMultiAnimation(MultiAnimation animations, String message) {
        Integer[] possibilities = new Integer[animations.size()];;
        for (int i = 0; i < possibilities.length; i++) {
            possibilities[i] = i;
        }

        Integer result = (Integer) JOptionPane.showInputDialog(frame,
            message,
            "Animation variation select",
            JOptionPane.PLAIN_MESSAGE,
            null,
            possibilities,
            possibilities[0]
        );

        return result;
    }

    public static void refreshCanvas(){
        System.out.println("x offset" + xOffset);
        System.out.println("y offset: " + yOffset);
        canvas.render(curAnimFrame, xOffset, yOffset, zoom, selectedHitbox);
    }

    public static TubeHitbox touchingHitbox(int mouseX, int mouseY) {
        ArrayList<TubeHitbox> hitboxes = new ArrayList<>();
        hitboxes.addAll(curAnimFrame.getAttackHitboxs());
        hitboxes.addAll(curAnimFrame.getHurtboxes());

        double multiplier = Math.min(canvas.getWidth(), canvas.getHeight())/64.0;
        for (TubeHitbox hitbox : hitboxes) {
            int x1 =(int) (hitbox.getCenter1X()*multiplier*zoom + xOffset);
            int y1 =(int) (hitbox.getCenter1Y()*multiplier*zoom+ yOffset);
            
            int x2 =(int) (hitbox.getCenter2X()*multiplier*zoom + xOffset);
            int y2 =(int) (hitbox.getCenter2Y()*multiplier*zoom + yOffset);

            int radius = (int) (hitbox.getRadius()*multiplier*zoom);

            if (distance(x1, y1, mouseX, mouseY) <= radius){
                draggedCircle = 1;
                return hitbox;
            }
            if (distance(x2, y2, mouseX, mouseY) <= radius){
                draggedCircle = 2;
                return hitbox;
            }
        }
        return null;
    }

    public static double distance(int p1x, int p1y, int p2x, int p2y) {
        return Math.sqrt(Math.pow((p2y-p1y), 2) + Math.pow((p2x-p1x), 2));
    }

    private class InputDetector implements MouseListener, MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent e) {
            if (!projectOpen) return;

            int newMouseX = e.getX();
            int newMouseY = e.getY();

            if (draggedHitbox == null) {
                draggedHitbox = touchingHitbox(newMouseX, newMouseY);
                changeCurHitbox(draggedHitbox);
            }
            if (draggedHitbox == null) {
                if (mouseX == -1 || mouseY == -1) {
                    mouseX = newMouseX;
                    mouseY = newMouseY;
                    return;
                }
                xOffset += newMouseX - mouseX;
                yOffset += newMouseY - mouseY;
            }else {
                if (mouseX == -1 || mouseY == -1) {
                    mouseX = newMouseX;
                    mouseY = newMouseY;
                    return;
                }

                double multiplier = Math.min(canvas.getWidth(), canvas.getHeight())/64;
                if (draggedCircle == 1) {
                    System.out.println("c1x: "+ draggedHitbox.getCenter1X());
                    System.out.println("calculatio: " + ((newMouseX - mouseX)/multiplier));
                    draggedHitbox.setCenter1X(draggedHitbox.getCenter1X() + (newMouseX - mouseX)/multiplier);
                    System.out.println("c1x: "+ draggedHitbox.getCenter1X());

                    draggedHitbox.setCenter1Y(draggedHitbox.getCenter1Y() + (newMouseY - mouseY)/multiplier);
                } else if (draggedCircle == 2) {
                    draggedHitbox.setCenter2X(draggedHitbox.getCenter2X() + (newMouseX - mouseX)/multiplier);
                    draggedHitbox.setCenter2Y(draggedHitbox.getCenter2Y() + (newMouseY - mouseY)/multiplier);
                }
            }
            mouseX = newMouseX;
            mouseY = newMouseY;
            refreshCanvas();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            TubeHitbox hitbox = touchingHitbox(e.getX(), e.getY());
            changeCurHitbox(hitbox);
            refreshCanvas();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            mouseX = -1;
            mouseY = -1;
            draggedHitbox = null;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            
        }

        @Override
        public void mouseExited(MouseEvent e) {
           
        }
        
    }
}
