package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay huntDisplay;

 @Before
 public void setUp() {
 huntDisplay = new HuntDisplay();
 }

 @Test
 public void testHuntDisplayConstructor() {
 HuntDisplay hd = new HuntDisplay();
 assertNotNull(hd);
 }

 @Test
 public void testGetPreferredSize() {
 Dimension d = new Dimension(200, 200);
 assertEquals(d, huntDisplay.getPreferredSize());
 }

 @Test
 public void testGetMinimumSize() {
 Dimension d = new Dimension(50, 50);
 assertEquals(d, huntDisplay.getMinimumSize());
 }

 @Test
 public void testAddPoint() {
 TrigPoint tp = new TrigPoint(1.0, 2.0, 0.0, true);
 huntDisplay.addPoint(tp);
 assertEquals(1, huntDisplay.points.size());
 }

 @Test(expected = NullPointerException.class)
 public void testAddPointWithNullTrigPoint() {
 huntDisplay.addPoint(null);
 }

 @Test
 public void testPaintComponentWithNoPoints() {
 Graphics2D g = huntDisplay.createGraphics();
 huntDisplay.paintComponent(g);
 assertTrue(huntDisplay.getWidth() * huntDisplay.getHeight() > 0);
 }

 @Test
 public void testPaintComponentWithPoints() {
 TrigPoint[] trigPoints = new TrigPoint[5];
 for (int i = 0; i < 5; i++) {
 trigPoints[i] = new TrigPoint(i / 10.0, i / 20.0, 0.0, false);
 huntDisplay.addPoint(trigPoints[i]);
 }
 Graphics2D g = huntDisplay.createGraphics();
 huntDisplay.paintComponent(g);
 for (int i = 0; i < trigPoints.length; i++) {
 TrigPoint trigPoint = trigPoints[i];
 int x = (int) ((trigPoint.getX() + 0.5) * huntDisplay.getWidth());
 int y = (int) ((trigPoint.getY() + 0.5) * huntDisplay.getHeight());
 Point point = huntDisplay.getLocationOnScreen();
 int xScreen = x + point.x;
 int yScreen = y + point.y;
 assertTrue(GraphicsEnvironment.getLocalGraphicsEnvironment().isPointInscreen(xScreen, yScreen));
 }
 }

 @Test
 public void testAddLandMark() {
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon("image.png"));
 huntDisplay.addLandMark(landmark);
 assertEquals(1, huntDisplay.landmarks.size());
 }

 @Test(expected = NullPointerException.class)
 public void testAddLandMarkWithNullLandMark() {
 huntDisplay.addLandMark(null);
 }

 @Test
 public void testPaintComponentWithLandMarks() {
 Landmark[] landMarks = new Landmark[5];
 for (int i = 0; i < 5; i++) {
 landMarks[i] = new Landmark(i / 10.0, i / 20.0, new ImageIcon("image.png"));
 huntDisplay.addLandMark(landMarks[i]);
 }
 Graphics2D g = huntDisplay.createGraphics();
 huntDisplay.paintComponent(g);
 for (int i = 0; i < landMarks.length; i++) {
 Landmark landMark = landMarks[i];
 int x = (int) ((landMark.getLon() + 0.5) * huntDisplay.getWidth());
 int y = (int) ((landMark.getLat() + 0.5) * huntDisplay.getHeight());
 Point point = huntDisplay.getLocationOnScreen();
 int xScreen = x + point.x;
 int yScreen = y + point.y;
 assertTrue(GraphicsEnvironment.getLocalGraphicsEnvironment().isPointInscreen(xScreen, yScreen));
 }
 }

 @Test
 public void testPaintComponentWithPointsAndLandMarks() {
 TrigPoint[] trigPoints = new TrigPoint[5];
 for (int i = 0; i < 5; i++) {
 trigPoints[i] = new TrigPoint(i / 10.0, i / 20.0, 0.0, false);
 huntDisplay.addPoint(trigPoints[i]);
 }
 Landmark[] landMarks = new Landmark[5];
 for (int i = 0; i < 5; i++) {
 landMarks[i] = new Landmark(i / 10.0, i / 20.0, new ImageIcon("image.png"));
 huntDisplay.addLandMark(landMarks[i]);
 }
 Graphics2D g = huntDisplay.createGraphics();
 huntDisplay.paintComponent(g);
 for (int i = 0; i < trigPoints.length; i++) {
 TrigPoint trigPoint = trigPoints[i];
 int x = (int) ((trigPoint.getX() + 0.5) * huntDisplay.getWidth());
 int y = (int) ((trigPoint.getY() + 0.5) * huntDisplay.getHeight());
 Point point = huntDisplay.getLocationOnScreen();
 int xScreen = x + point.x;
 int yScreen = y + point.y;
 assertTrue(GraphicsEnvironment.getLocalGraphicsEnvironment().isPointInscreen(xScreen, yScreen));
 }
 for (int i = 0; i < landMarks.length; i++) {
 Landmark landMark = landMarks[i];
 int x = (int) ((landMark.getLon() + 0.5) * huntDisplay.getWidth());
 int y = (int) ((landMark.getLat() + 0.5) * huntDisplay.getHeight());
 Point point = huntDisplay.getLocationOnScreen();
 int xScreen = x + point.x;
 int yScreen = y + point.y;
 assertTrue(GraphicsEnvironment.getLocalGraphicsEnvironment().isPointInscreen(xScreen, yScreen));
 }
 }

}