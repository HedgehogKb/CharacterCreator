package com.hedgehogkb.EditorFrame;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import com.hedgehogkb.Fighter.Animation.AnimationFrame;
import com.hedgehogkb.Hitbox.TubeHitbox;

public class BattleCanvas extends Canvas {

    public BattleCanvas(MouseListener mouseListener, MouseMotionListener mouseMotionListener) {
        setIgnoreRepaint(true); // IMPORTANT
        setPreferredSize(new Dimension(800,800));
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseMotionListener);
    }

    public void render(AnimationFrame frame, int xOffset, int yOffset, double zoom, TubeHitbox selectedHitbox) {

        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            
            bs = getBufferStrategy();
        }
        Graphics2D g2d = null;
        do {
            try {
                g2d = (Graphics2D) bs.getDrawGraphics();

             // --- Clear screen ---
                g2d.setColor(Color.white);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                BufferedImage sprite = frame.getSprite();
                int maxSideLength = Math.min(getWidth(), getHeight());
                if (sprite != null) {
                    g2d.drawImage(sprite, xOffset, yOffset, (int) (maxSideLength * zoom), (int) (maxSideLength * zoom), null);
                }
                g2d.setStroke(new BasicStroke(3));
                for (TubeHitbox hitbox : frame.getHurtboxes()) {
                    Color color = new Color(0,0,255,100);
                    if (hitbox == selectedHitbox) color = new Color(0,255,0,100);
                    drawHitbox(g2d, hitbox, maxSideLength, xOffset, yOffset, zoom, color);
                }

                for (TubeHitbox hitbox : frame.getAttackHitboxs()) {
                    Color color = new Color(255,0,0,100);
                    if (hitbox == selectedHitbox) color = new Color(0,255,0,100);
                    drawHitbox(g2d, hitbox, maxSideLength, xOffset, yOffset, zoom, color);
                }
            } finally {
                g2d.dispose();
            }
            bs.show();
        } while (bs.contentsLost());
        



        bs.show();
        Toolkit.getDefaultToolkit().sync(); // smooth on Linux
    }

    public void drawHitbox(Graphics2D g2d, TubeHitbox hitbox, int maxSideLength, int xOffset, int yOffset, double zoom, Color color) {
        g2d.setColor(color);

        double multiplier = maxSideLength/64.0;

        int x1 =(int) (hitbox.getCenter1X()*multiplier*zoom + xOffset);
        int y1 =(int) (hitbox.getCenter1Y()*multiplier*zoom+ yOffset);
        
        int x2 =(int) (hitbox.getCenter2X()*multiplier*zoom + xOffset);
        int y2 =(int) (hitbox.getCenter2Y()*multiplier*zoom + yOffset);

        int radius = (int) (hitbox.getRadius()*multiplier*zoom);
        g2d.fillOval(x1, y1, 3, 3);

        g2d.drawOval(x1-radius, y1-radius, radius*2, radius*2);
        g2d.drawOval(x2-radius, y2-radius, radius*2, radius*2);


        double theta = Math.atan2(y2 - y1, x2 - x1);
        double dx = radius * Math.sin(theta);
        double dy = radius * Math.cos(theta);

        int[] xPoints = {
            (int) (x1 + dx),
            (int) (x2 + dx),
            (int) (x2 - dx),
            (int) (x1 - dx)
        };
        int[] yPoints = {
            (int) (y1 - dy),
            (int) (y2 - dy),
            (int) (y2 + dy),
            (int) (y1 + dy)
        };

        g2d.drawPolygon(xPoints, yPoints, 4);
    }
}
