package com.hedgehogkb.EditorFrame;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class EditorMenuBar {
    private JMenuBar menuBar;

    private JMenu fileMenu;
    private JMenuItem createCharacter;
    private JMenuItem openCharacter;
    private JMenuItem save;
    private JMenuItem saveAt;
    private JMenuItem changeName;
    private JMenuItem chooseFrame;
    
    private JMenu editMenu;
    private JMenuItem addMove;
    private JMenuItem clearMove;
    private JMenuItem addMoveVariation;
    private JMenuItem removeMoveVariation;
    private JMenuItem openMove;
    private JMenuItem addFrame;
    private JMenuItem insertFrame;
    private JMenuItem removeFrame;
    private JMenuItem editorCharacterStats;

    private JMenu viewMenu;
    private JMenuItem viewAnimation;
    private JMenuItem returnToCenter;

    private EditorMenuListener listener;

    public EditorMenuBar() {
        menuBar = new JMenuBar();

        // File menu
        fileMenu = new JMenu("File");
        createCharacter = new JMenuItem("Create Character...");
        openCharacter = new JMenuItem("Open Character...");
        save = new JMenuItem("Save / Export...");
        saveAt = new JMenuItem("Save / Export at...");
        changeName = new JMenuItem("Change Name...");
        chooseFrame = new JMenuItem("Choose Frame...");

        fileMenu.add(createCharacter);
        fileMenu.add(openCharacter);
        fileMenu.add(save);
        fileMenu.add(saveAt);
        fileMenu.add(changeName);
        fileMenu.addSeparator();
        fileMenu.add(chooseFrame);
        menuBar.add(fileMenu);

        // Edit menu
        editMenu = new JMenu("Edit");
        addMove = new JMenuItem("Add Move...");
        clearMove = new JMenuItem("Clear Move...");
        addMoveVariation = new JMenuItem("Add Move Variation");
        removeMoveVariation = new JMenuItem("Remove Variation From Selected Move");
        openMove = new JMenuItem("Choose Move...");
        addFrame = new JMenuItem("Add Frame to End");
        insertFrame = new JMenuItem("Insert Frame");
        removeFrame = new JMenuItem("Remove Frame");
        editorCharacterStats = new JMenuItem("Edit Character Stats...");

        editMenu.add(addMove);
        editMenu.add(clearMove);
        editMenu.add(openMove);

        editMenu.addSeparator();
        editMenu.add(addMoveVariation);
        editMenu.add(removeMoveVariation);

        editMenu.addSeparator();
        editMenu.add(addFrame);
        editMenu.add(insertFrame);
        editMenu.add(removeFrame);
        editMenu.addSeparator();
        editMenu.add(editorCharacterStats);
        menuBar.add(editMenu);

        // View menu
        viewMenu = new JMenu("View");
        viewAnimation = new JMenuItem("Animation Preview");
        returnToCenter = new JMenuItem("Return to center");
        viewMenu.add(viewAnimation);
        viewMenu.add(returnToCenter);
        menuBar.add(viewMenu);

        // Attach lightweight listeners that forward to the external listener if present
        createCharacter.addActionListener(e -> { if (listener != null) listener.onCreateCharacter(); });
        openCharacter.addActionListener(e -> { if (listener != null) listener.onOpenCharacter(); });
        save.addActionListener(e -> { if (listener != null) listener.onSaveExport(); });
        saveAt.addActionListener(e -> { if (listener != null) listener.onSaveAt(); });
        changeName.addActionListener(e -> { if (listener != null) listener.onChangeName(); });
        chooseFrame.addActionListener(e -> { if (listener != null) listener.onChooseFrame(); });

        addMove.addActionListener(e -> { if (listener != null) listener.onAddMove(); });
        clearMove.addActionListener(e -> { if (listener != null) listener.clearMove(); });
        addMoveVariation.addActionListener(e -> { if (listener != null) listener.addMoveVariation(); });
        removeMoveVariation.addActionListener(e -> { if (listener != null) listener.removeMoveVariation(); });
        openMove.addActionListener(e -> { if (listener != null) listener.onOpenMove(); });
        addFrame.addActionListener(e -> { if (listener != null) listener.onAddFrame(); });
        insertFrame.addActionListener(e -> { if (listener != null) listener.onInsertFrame(); });
        removeFrame.addActionListener(e -> { if (listener != null) listener.onRemoveFrame(); });
        editorCharacterStats.addActionListener(e -> { if (listener != null) listener.onEditCharacterStats(); });

        viewAnimation.addActionListener(e -> { if (listener != null) listener.onViewAnimation(); });
        returnToCenter.addActionListener(e -> { if (listener != null) listener.onReturnToCenter();});
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    public void setListener(EditorMenuListener l) {
        this.listener = l;
    }

    public static interface EditorMenuListener {
        void onCreateCharacter();
        void onOpenCharacter();
        void onSaveExport();
        void onSaveAt();
        void onChangeName();
        void onChooseFrame();

        void onAddMove();
        void clearMove();
        void addMoveVariation();
        void removeMoveVariation();

        void onOpenMove();
        void onAddFrame();
        void onInsertFrame();
        void onRemoveFrame();
        void onEditCharacterStats();
        void onViewAnimation();
        void onReturnToCenter();
    }
}
