package com.hedgehogkb;

// Source - https://stackoverflow.com/q
// Posted by user2618518, modified by community. See post 'Timeline' for change history
// Retrieved 2025-12-23, License - CC BY-SA 3.0

import javax.swing.JFrame;

public class MainW {


    public static void main(String[] args) {
        Game ga = new Game();
        JFrame frame = new JFrame ();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(ga);
        frame.setVisible(true);
        frame.setSize(800,800);
        ga.start();

    }

}
