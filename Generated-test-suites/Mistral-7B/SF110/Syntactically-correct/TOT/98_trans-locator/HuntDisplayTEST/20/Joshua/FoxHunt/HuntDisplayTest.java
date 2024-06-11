import org.junit.Before;
import org.junit.Test;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import static org.junit.Assert.*;

public class HuntDisplayTest {

 private HuntDisplay huntDisplay;
 private TrigPoint trigPoint1;
 private TrigPoint trigPoint2;
 private Landmark landmark1;
 private Landmark landmark2;

 @Before
 public void setUp() {
 huntDisplay = new HuntDisplay();
 trigPoint1 = new TrigPoint(1.0, 0.0, 0.0, false);
 trigPoint2 = new TrigPoint(0.0, 1.0, Math.PI / 2.0, false);
 landmark1 = new Landmark(0.0, 0.0, createImageIcon("/path/to/icon1.png"));
 landmark2 = new Landmark(1.0, 1.0, createImageIcon("/path/to/icon2.png"));
 }

 @Test
 public void testAddPoint() {
 huntDisplay.addPoint(trigPoint1);
 huntDisplay.addPoint(trigPoint2);
 // Testing size and position of points
 assertEquals(2, huntDisplay.points.size());
 final TrigPoint[] trigpoints = new TrigPoint[huntDisplay.points.size()];
 for (int i = 0; i < huntDisplay.points.size(); ++i) {
 trigpoints[i] = (TrigPoint) huntDisplay.points.elementAt(i);
 }
 assertTrue(trigpoints[0].getX() < trigpoints[1].getX());
 assertTrue(trigpoints[0].getY() < trigpoints[1].getY());
 }

 @Test
 public void testAddLandmark() {
 huntDisplay.addLandMark(landmark1);
 huntDisplay.addLandMark(landmark2);
 // Testing size and position of landmarks
 assertEquals(2, huntDisplay.landmarks.size());
 final Landmark[] landMarks = new Landmark[huntDisplay.landmarks.size()];
 for (int i = 0; i < huntDisplay.landmarks.size(); ++i) {
 landMarks[i] = (Landmark) huntDisplay.landmarks.elementAt(i);
 }
 assertTrue(landMarks[0].getLon() < landMarks[1].getLon());
 assertTrue(landMarks[0].getLat() < landMarks[1].getLat());
 }

 @Test
 public void testPaintComponent() {
 Graphics g = huntDisplay.createGraphics();
 huntDisplay.paintComponent(g);
 // Testing if points are drawn in correct position and color
 for (int i = 0; i < huntDisplay.points.size(); ++i) {
 final TrigPoint trigpoint = (TrigPoint) huntDisplay.points.elementAt(i);
 final int x = (int)((trigpoint.getX() * huntDisplay.getWidth()) + 0.5);
 final int y = (int)((trigpoint.getY() * huntDisplay.getHeight()) + 0.5);
 final Color color = trigpoint.isFox() ? Color.GREEN : Color.BLACK;
 assertEquals(color, g.getColor());
 g.fillOval(x, y, 5, 5);
 }
 // Testing if landmarks are drawn in correct position
 for (int i = 0; i < huntDisplay.landmarks.size(); ++i) {
 final Landmark landmark = (Landmark) huntDisplay.landmarks.elementAt(i);
 final int x = (int)((landmark.getLon() * huntDisplay.getWidth()) + 0.5);
 final int y = (int)((landmark.getLat() * huntDisplay.getHeight()) + 0.5);
 final BufferedImage image = (BufferedImage)landmark.getIcon().getImage();
 final int width = image.getWidth();
 final int height = image.getHeight();
 g.drawImage(image, x, y, width, height, null);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointWithNullTrigPoint() {
 huntDisplay.addPoint(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandmarkWithNullLandmark() {
 huntDisplay.addLandMark(null);
 }
}