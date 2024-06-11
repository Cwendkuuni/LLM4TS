package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay display;

 @Before
 public void setUp() {
 display = new HuntDisplay();
 }

 @Test
 public void testHuntDisplayConstructor() {
 HuntDisplay huntDisplay = new HuntDisplay();
 assertNotNull(huntDisplay);
 }

 @Test
 public void testGetPreferredSize() {
 assertEquals(new Dimension(200, 200), display.getPreferredSize());
 }

 @Test
 public void testGetMinimumSize() {
 assertEquals(new Dimension(50, 50), display.getMinimumSize());
 }

 @Test
 public void testAddPoint() {
 TrigPoint trigPoint = new TrigPoint(50.0, 50.0, 0.0, false);
 display.addPoint(trigPoint);
 assertEquals(1, display.points.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointNullPoint() {
 TrigPoint trigPoint = null;
 display.addPoint(trigPoint);
 }

 @Test
 public void testAddLandMark() {
 Landmark landmark = new Landmark(50.0, 50.0, new ImageIcon(BufferedImage.TYPE_INT_ARGB));
 display.addLandMark(landmark);
 assertEquals(1, display.landmarks.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLandMarkNullLandmark() {
 Landmark landmark = null;
 display.addLandMark(landmark);
 }

 @Test
 public void testPaintComponent() {
 Graphics g = display.createGraphics();
 display.paintComponent(g);
 assertNotNull(g);
 }

 @Test(expected = NullPointerException.class)
 public void testPaintComponentNullGraphics() {
 Graphics g = null;
 display.paintComponent(g);
 }

 @Test
 public void testPaintComponentWithPointsAndLandmarks() {
 addPointsAndLandmarksToDisplay(display, 5, 5, new ImageIcon(BufferedImage.TYPE_INT_ARGB), new ImageIcon(BufferedImage.TYPE_INT_ARGB));
 Graphics g = display.createGraphics();
 display.paintComponent(g);
 assertNotNull(g);
 }

 @Test
 public void testPaintComponentWithOnlyLandmarks() {
 addLandmarksToDisplay(display, 5, new ImageIcon(BufferedImage.TYPE_INT_ARGB));
 Graphics g = display.createGraphics();
 display.paintComponent(g);
 assertNotNull(g);
 }

 private void addLandmarksToDisplay(HuntDisplay display, int numberOfLandmarks) {
 for (int i = 0; i < numberOfLandmarks; i++) {
 Landmark landmark = new Landmark(50.0 + i, 50.0, new ImageIcon(BufferedImage.TYPE_INT_ARGB));
 display.addLandMark(landmark);
 }
 }

 private void addPointsAndLandmarksToDisplay(HuntDisplay display, int numberOfPoints, int numberOfLandmarks, ImageIcon pointIcon, ImageIcon landmarkIcon) {
 for (int i = 0; i < numberOfPoints; i++) {
 TrigPoint trigPoint = new TrigPoint(50.0 + i, 50.0, 0.0, false);
 display.addPoint(trigPoint);
 }
 for (int i = 0; i < numberOfLandmarks; i++) {
 Landmark landmark = new Landmark(50.0 + i, 50.0, landmarkIcon);
 display.addLandMark(landmark);
 }
 }

}