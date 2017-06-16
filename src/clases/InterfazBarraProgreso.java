/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.plaf.basic.*;

/**
 *
 * @author david
 */
public class InterfazBarraProgreso extends BasicProgressBarUI{

    private final int[] pallet;
    private final JProgressBar pb;

    public InterfazBarraProgreso(JProgressBar pb) {
        super();
        this.pallet = makeGradientPallet();
        this.pb = pb;
    }

    private static int[] makeGradientPallet() {
        BufferedImage image = new BufferedImage(100, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        Point2D start = new Point2D.Float(0f, 0f);
        Point2D end = new Point2D.Float(99f, 0f);
        float[] dist = {0.0f, 0.5f, 1.0f};
        Color[] colors = {new Color(255,0,0), new Color(232,101,0), new Color(0,255,0)};
        g2.setPaint(new LinearGradientPaint(start, end, dist, colors));
        g2.fillRect(0, 0, 100, 1);
        g2.dispose();

        int width = image.getWidth(null);
        int[] pallet = new int[width];
        PixelGrabber pg = new PixelGrabber(image, 0, 0, width, 1, pallet, 0, width);
        try {
            pg.grabPixels();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pallet;
    }

    private static Color getColorFromPallet(int[] pallet, float x) {
        if (x < 0.0 || x > 1.0) {
            throw new IllegalArgumentException("Parameter outside of expected range");
        }
        int i = (int) (pallet.length * x);
        int max = pallet.length - 1;
        int index = i < 0 ? 0 : i > max ? max : i;
        int pix = pallet[index] & 0x00ffffff | (0x64 << 24);
        return new Color(pix, true);
    }

    @Override
    public void paintDeterminate(Graphics g, JComponent c) {
        if (!(g instanceof Graphics2D)) {
            return;
        }
        Insets b = pb.getInsets(); // area for border
        int barRectWidth = pb.getWidth() - (b.right + b.left);
        int barRectHeight = pb.getHeight() - (b.top + b.bottom);
        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return;
        }
        int cellLength = getCellLength();
        int cellSpacing = getCellSpacing();
        // amount of progress to draw
        int amountFull = getAmountFull(b, barRectWidth, barRectHeight);

        if (pb.getOrientation() == JProgressBar.HORIZONTAL) {
            // draw the cells
            float x = amountFull / (float) barRectWidth;
            g.setColor(getColorFromPallet(pallet, x));
            g.fillRect(b.left, b.top, amountFull, barRectHeight);

        } else { // VERTICAL
            //...
        }
        // Deal with possible text painting
        if (pb.isStringPainted()) {
            g.setColor(Color.BLACK);
            paintString(g, b.left, b.top, barRectWidth, barRectHeight, amountFull, b);
        }
    }
}
