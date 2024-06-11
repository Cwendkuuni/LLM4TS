package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

import javax.swing.*;

import org.junit.Test;

public class HuntDisplayTest {

 private HuntDisplay huntDisplay;

 @Test
 public void testHuntDisplayConstruction() {
 huntDisplay = new HuntDisplay();
 assertNotNull(huntDisplay);
 assertEquals(new Dimension(200, 200), huntDisplay.getPrefSize());
 assertEquals(new Dimension(50, 50), huntDisplay.getMinSize());
 }

 @Test
 public void testAddPoint() {
 huntDisplay = new HuntDisplay();
 huntDisplay.addPoint(new TrigPoint(1.0, 2.0, 0.0, false));
 assertEquals(1, huntDisplay.points.size());
 huntDisplay.repaint();
 }

 @Test
 public void testAddLandmark() {
 huntDisplay = new HuntDisplay();
 Image icon = new ImageIcon("path/to/icon.png").getImage();
 huntDisplay.addLandMark(new Landmark(1.0, 2.0, icon));
 assertEquals(1, huntDisplay.landmarks.size());
 huntDisplay.repaint();
 }

 @Test
 public void testPaintComponent_Empty() {
 huntDisplay = new HuntDisplay();
 Graphics2D g = (Graphics2D) huntDisplay.createGraphics();
 huntDisplay.paintComponent(g);
 huntDisplay.dispose();
 // Add assertions for empty state
}

 @Test
 public void testPaintComponent_WithPoints() {
 huntDisplay = new HuntDisplay();
 TrigPoint p1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint p2 = new TrigPoint(3.0, 4.0, 0.0, false);
 huntDisplay.addPoint(p1);
 huntDisplay.addPoint(p2);
 huntDisplay.repaint();
 Graphics2D g = (Graphics2D) huntDisplay.createGraphics();
 int width = huntDisplay.getWidth();
 int height = huntDisplay.getHeight();
 for (int i = 0; i < huntDisplay.points.size(); i++) {
 TrigPoint point = (TrigPoint) huntDisplay.points.elementAt(i);
 double x = (point.getX() + 0.5) * width;
 double y = (point.getY() + 0.5) * height;
 int expectedX = (int) Math.round(x);
 int expectedY = (int) Math.round(y);
 Rectangle rectangle = new Rectangle(expectedX - 2, expectedY - 2, 6, 6);
 assertTrue(g.getColor().equals(Color.BLACK));
 assertTrue(g.fillOval(expectedX - 2, expectedY - 2, 6, 6).contains(expectedX, expectedY));
 }
 huntDisplay.dispose();
 }

 @Test
 public void testPaintComponent_WithLandmarks() {
 huntDisplay = new HuntDisplay();
 Image icon = new ImageIcon("path/to/icon.png").getImage();
 Landmark l1 = new Landmark(1.0, 2.0, icon);
 Landmark l2 = new Landmark(3.0, 4.0, icon);
 huntDisplay.addLandMark(l1);
 huntDisplay.addLandMark(l2);
 huntDisplay.repaint();
 Graphics2D g = (Graphics2D) huntDisplay.createGraphics();
 int width = huntDisplay.getWidth();
 int height = huntDisplay.getHeight();
 for (int i = 0; i < huntDisplay.landmarks.size(); i++) {
 Landmark landmark = (Landmark) huntDisplay.landmarks.elementAt(i);
 double x = (landmark.getLon() + 0.5) * width;
 double y = (landmark.getLat() + 0.5) * height;
 int expectedX = (int) Math.round(x);
 int expectedY = (int) Math.round(y);
 Rectangle rectangle = new Rectangle(expectedX - 5, expectedY - 5, 10, 10);
 assertTrue(g.getColor().equals(Color.getColor("lightGray")));
 assertTrue(g.drawImage(icon, expectedX - 5, expectedY - 5, 10, 10, null));
 }
 huntDisplay.dispose();
 }
}