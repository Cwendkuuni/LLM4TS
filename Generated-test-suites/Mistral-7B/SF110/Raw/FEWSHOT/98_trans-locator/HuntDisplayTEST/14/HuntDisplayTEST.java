package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class HuntDisplayTest {

    @Test
    public void testHuntDisplay() {
        HuntDisplay hd = new HuntDisplay();
        assertNotNull(hd);
        assertEquals(new Dimension(200, 200), hd.getPrefSize());
        assertEquals(new Dimension(50, 50), hd.getMinSize());
    }

    @Test
    public void testAddPoint() {
        HuntDisplay hd = new HuntDisplay();
        TrigPoint tp = new TrigPoint(1.0, 2.0, 3.0, true);
        hd.addPoint(tp);
        assertEquals(1, hd.points.size());
        hd.repaint();
    }

    @Test
    public void testAddLandmark() {
        HuntDisplay hd = new HuntDisplay();
        Landmark lm = new Landmark(1.5, 2.5, new ImageIcon("icon.png"));
        hd.addLandmark(lm);
        assertEquals(1, hd.landmarks.size());
        hd.repaint();
    }

    @Test
    public void testPaintComponent() {
        HuntDisplay hd = new HuntDisplay();
        hd.addPoint(new TrigPoint(1.0, 2.0, 3.0, true));
        hd.addPoint(new TrigPoint(3.0, 4.0, 6.0, false));
        ImageIcon icon = new ImageIcon("icon.png");
        hd.addLandmark(new Landmark(5.5, 6.5, icon));
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        hd.paintComponent(g);
        g.dispose();
        int x = 0, y = 0;
        for (Object o : hd.points) {
            TrigPoint tp = (TrigPoint) o;
            if (tp.isFox()) {
                x = (int) ((tp.getX() * 200) + 0.5);
                y = (int) ((tp.getY() * 200) + 0.5);
                assertEquals(Color.GREEN, image.getRGB(x, y));
            } else {
                x = (int) ((tp.getX() * 200) + 0.5);
                y = (int) ((tp.getY() * 200) + 0.5);
                assertEquals(Color.BLACK.getRGB(), image.getRGB(x, y));
            }
        }
        x = 0;
        y = 0;
        for (Object o : hd.landmarks) {
            Landmark lm = (Landmark) o;
            x = (int) ((lm.getLon() * 200) + 0.5);
            y = (int) ((lm.getLat() * 200) + 0.5);
            Image image = lm.getIcon().getImage();
            assertEquals(image.getWidth(), 10);
            assertEquals(image.getHeight(), 10);
            assertEquals(image, image.getScaledInstance(10, 10, Image.SCALE_SMOOTH));
            assertEquals(Color.LIGHT_GRAY.getRGB(), image.getRGB(0, 0));
            assertEquals(Color.LIGHT_GRAY.getRGB(), image.getRGB(image.getWidth() - 1, image.getHeight() - 1));
        }
    }
}