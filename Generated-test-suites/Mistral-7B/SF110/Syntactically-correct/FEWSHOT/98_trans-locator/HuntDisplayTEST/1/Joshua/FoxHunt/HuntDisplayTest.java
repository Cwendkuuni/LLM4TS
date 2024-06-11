package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

import javax.swing.*;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

    private HuntDisplay huntDisplay;
    private Vector<TrigPoint> points;
    private Vector<Landmark> landmarks;

    @Before
    public void setup() {
        huntDisplay = new HuntDisplay();
        points = new Vector<>();
        landmarks = new Vector<>();
    }

    @Test
    public void testHuntDisplayConstructor() {
        assertNotNull(huntDisplay);
        assertEquals(new Dimension(200, 200), huntDisplay.getPrefSize());
        assertEquals(new Dimension(200, 200), huntDisplay.getMinSize());
        assertEquals(Color.WHITE, huntDisplay.getBackground());
    }

    @Test
    public void testAddPoint() {
        TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, true);
        huntDisplay.addPoint(trigPoint);
        assertEquals(1, huntDisplay.points.size());
        verify(huntDisplay, times(1)).repaint();
    }

    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(1.0, 2.0, getMockIcon());
        huntDisplay.addLandMark(landmark);
        assertEquals(0, huntDisplay.points.size());
        assertEquals(1, huntDisplay.landmarks.size());
        verify(huntDisplay, times(1)).repaint();
    }

    @Test
    public void testGetPreferredSize() {
        assertEquals(new Dimension(200, 200), huntDisplay.getPreferredSize());
    }

    @Test
    public void testGetMinimumSize() {
        assertEquals(new Dimension(50, 50), huntDisplay.getMinimumSize());
    }

    @Test
    public void testPaintComponentWithPoints() {
        TrigPoint trigPoint1 = new TrigPoint(1.0, 2.0, 0.0, false);
        TrigPoint trigPoint2 = new TrigPoint(3.0, 4.0, 0.0, false);
        points.add(trigPoint1);
        points.add(trigPoint2);

        huntDisplay.points = points;

        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        huntDisplay.paintComponent(g);

        int xAdd = 0.5 - (trigPoint1.getX() + trigPoint2.getX()) / 2.0;
        int yAdd = 0.5 - (trigPoint1.getY() + trigPoint2.getY()) / 2.0;

        for (int i = 0; i < points.size(); i++) {
            TrigPoint trigPoint = points.elementAt(i);
            int X = (int) ((trigPoint.getX() + xAdd) * image.getWidth());
            int Y = (int) ((trigPoint.getY() + yAdd) * image.getHeight());
            Color color = trigPoint.isFox() ? Color.GREEN : Color.BLACK;

            int expectedRadius = 5;

            int rgb = color.getRGB();
            int alpha = (rgb >> 24) & 0xFF;
            int red = (rgb >> 16) & 0xFF;
            int green = (rgb >> 8) & 0xFF;
            int blue = rgb & 0xFF;

            for (int j = 0; j < expectedRadius; j++) {
                int x = X - j;
                int y = Y - j;

                if (x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight()) {
                    int pixelRgb = image.getRGB(x, y);
                    assertEquals(rgb, pixelRgb);
                }
            }
        }
    }

    @Test
    public void testPaintComponentWithLandMarks() {
        Landmark landmark1 = new Landmark(1.0, 2.0, getMockIcon());
        Landmark landmark2 = new Landmark(3.0, 4.0, getMockIcon());
        landmarks.add(landmark1);
        landmarks.add(landmark2);

        huntDisplay.landmarks = landmarks;

        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        huntDisplay.paintComponent(g);

        int xAdd = 0.5 - (landmark1.getLon() + landmark2.getLon()) / 2.0;
        int yAdd = 0.5 - (landmark1.getLat() + landmark2.getLat()) / 2.0;

        for (int i = 0; i < landmarks.size(); i++) {
            Landmark landmark = landmarks.elementAt(i);
            int X = (int) ((landmark.getLon() + xAdd) * image.getWidth());
            int Y = (int) ((landmark.getLat() + yAdd) * image.getHeight());

            assertEquals(10, image.getWidth() / 2);
            assertEquals(10, image.getHeight() / 2);

            int expectedIconWidth = 10;
            int expectedIconHeight = 10;

            int xIcon = X - expectedIconWidth / 2;
            int yIcon = Y - expectedIconHeight / 2;

            for (int j = 0; j < expectedIconWidth; j++) {
                for (int k = 0; k < expectedIconHeight; k++) {
                    int x = xIcon + j;
                    int y = yIcon + k;

                    if (x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight()) {
                        int pixelRgb = image.getRGB(x, y);
                        landmark.getIcon().getImage().getRGB(j, k);
                        assertEquals(pixelRgb, landmark.getIcon().getImage().getRGB(j, k));
                    }
                }
            }
        }
    }

    private Icon getMockIcon() {
        BufferedImage bufferedImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.setColor(Color.BLACK);
        g.fillOval(0, 0, 10, 10);
        g.dispose();

        return new Icon() {
            @Override
            public int getIconHeight() {
                return 10;
            }

            @Override
            public int getIconWidth() {
                return 10;
            }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.drawImage(bufferedImage, x, y, null);
            }
        };
    }
}