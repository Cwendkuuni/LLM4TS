package Joshua.FoxHunt.Test;

import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class HuntDisplayTest {

    @Test
    public void testHuntDisplayConstructor() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Assert.assertNotNull(huntDisplay);
        Assert.assertNotNull(huntDisplay.points);
        Assert.assertNotNull(huntDisplay.landmarks);
        Assert.assertNotNull(huntDisplay.getPreferredSize());
        Assert.assertNotNull(huntDisplay.getMinimumSize());
    }

    @Test
    public void testAddPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
        huntDisplay.addPoint(trigPoint);
        Vector<TrigPoint> points = (Vector<TrigPoint>) huntDisplay.points;
        Assert.assertEquals(1, points.size());
        Assert.assertEquals(1.0 / 90.0, points.get(0).getY(), 0.01);
        Assert.assertEquals(2.0 / 180.0, points.get(0).getX(), 0.01);
        Assert.assertEquals(0.0, points.get(0).getTheta(), 0.01);
        Assert.assertFalse(points.get(0).isFox());
    }

    @Test
    public void testAddLandmark() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = new Landmark(3.0, 4.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));
        huntDisplay.addLandmark(landmark);
        Vector<Landmark> landmarks = (Vector<Landmark>) huntDisplay.landmarks;
        Assert.assertEquals(1, landmarks.size());
        Assert.assertEquals(3.0 / 90.0, landmarks.get(0).getLat(), 0.01);
        Assert.assertEquals(4.0 / 180.0, landmarks.get(0).getLon(), 0.01);
        Assert.assertNotNull(landmarks.get(0).getIcon());
    }

    @Test
    public void testPaintComponent() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint1 = new TrigPoint(1.0, 2.0, 0.0, false);
        TrigPoint trigPoint2 = new TrigPoint(3.0, 4.0, 0.0, false);
        Landmark landmark = new Landmark(5.0, 6.0, new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));

        huntDisplay.addPoint(trigPoint1);
        huntDisplay.addPoint(trigPoint2);
        huntDisplay.addLandmark(landmark);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(huntDisplay);
        frame.pack();
        frame.setVisible(true);

        Graphics2D g = huntDisplay.getGraphics();
        Assert.assertNotNull(g);

        double trigPointAvgX = 0.0;
        int totalPoints = huntDisplay.points.size();
        for (int i = 0; i < totalPoints; ++i) {
            TrigPoint trigPoint = (TrigPoint) huntDisplay.points.get(i);
            trigPointAvgX += trigPoint.getX();
        }
        trigPointAvgX /= totalPoints;

        double trigPointAvgY = 0.0;
        for (int i = 0; i < totalPoints; ++i) {
            TrigPoint trigPoint = (TrigPoint) huntDisplay.points.get(i);
            trigPointAvgY += trigPoint.getY();
        }
        trigPointAvgY /= totalPoints;

        int width = huntDisplay.getWidth();
        int height = huntDisplay.getHeight();

        for (int i = 0; i < totalPoints; ++i) {
            TrigPoint trigPoint = (TrigPoint) huntDisplay.points.get(i);
            int expectedX = (int) ((trigPoint.getX() + trigPointAvgX) * width);
            int expectedY = (int) ((trigPoint.getY() + trigPointAvgY) * height);

            int actualX = (int) g.getTranslateX() + (int) ((trigPoint.getX() + trigPointAvgX) * width);
            int actualY = (int) g.getTranslateY() + (int) ((trigPoint.getY() + trigPointAvgY) * height);

            if (trigPoint.isFox()) {
                BufferedImage expectedIcon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
                BufferedImage actualIcon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
                Graphics2D expectedGraphics = expectedIcon.createGraphics();
                Graphics2D actualGraphics = g.create();

                landmark = (Landmark) huntDisplay.landmarks.get(0);
                expectedGraphics.drawImage(landmark.getIcon().getImage(), 0, 0, null);
                actualGraphics.drawImage(landmark.getIcon().getImage(), actualX, actualY, null);

                Assert.assertEquals(expectedIcon, actualIcon);

                expectedGraphics.dispose();
                actualGraphics.dispose();
            } else {
                Assert.assertEquals(expectedX, actualX);
                Assert.assertEquals(expectedY, actualY);
            }
        }

        g.dispose();
        frame.dispose();
    }
}