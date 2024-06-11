package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay display;
 private Vector<TrigPoint> points;
 private Vector<Landmark> landmarks;

 @Before
 public void setUp() {
 display = new HuntDisplay();
 points = new Vector<>();
 landmarks = new Vector<>();
 }

 @Test
 public void testConstructor() {
 HuntDisplay hd = new HuntDisplay();
 assertNotNull(hd);
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
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 display.addPoint(point);
 points.add(point);
 assertEquals(1, points.size());
 assertEquals(1, display.getPoints().size());
 assertNotNull(display.getPoints().elementAt(0));
 }

 @Test(expected = NullPointerException.class)
 public void testAddPointNullPoint() {
 display.addPoint(null);
 }

 @Test
 public void testAddLandmark() {
 Landmark landmark = new Landmark(3.0, 4.0, new ImageIcon(getClass().getResource("/icon.png")));
 display.addLandmark(landmark);
 landmarks.add(landmark);
 assertEquals(1, landmarks.size());
 assertEquals(1, display.getLandmarks().size());
 assertNotNull(display.getLandmarks().elementAt(0));
 }

 @Test(expected = NullPointerException.class)
 public void testAddLandmarkNullLandmark() {
 display.addLandmark(null);
 }

 @Test
 public void testPaintComponent() {
 Graphics g = createMockGraphics();
 display.paintComponent(g);
 assertEquals(Color.WHITE, display.getBackground());
 }

 @Test
 public void testPaintComponentWithPoints() {
 Graphics g = createMockGraphics();
 addPointTestData(g, 3);
 display.paintComponent(g);
 for (TrigPoint point : points) {
 Point expectedPoint = new Point((int)((point.getX() + 0.5 - display.getTrigPointAvgX()) * display.getWidth()),
 (int)((point.getY() + 0.5 - display.getTrigPointAvgY()) * display.getHeight()));
 Point actualPoint = display.getPoints().elementAt(points.indexOf(point)).getLocation();
 assertEquals(expectedPoint, actualPoint);
 }
 }

 @Test
 public void testPaintComponentWithLandmarks() {
 Graphics g = createMockGraphics();
 addLandmarkTestData(g, 3);
 display.paintComponent(g);
 for (Landmark landmark : landmarks) {
 Rectangle expectedRect = new Rectangle((int)((landmark.getLon() + 0.5 - display.getTrigPointAvgX()) * display.getWidth()),
 (int)((landmark.getLat() + 0.5 - display.getTrigPointAvgY()) * display.getHeight()), 10, 10);
 Rectangle actualRect = display.getLandmarks().elementAt(landmarks.indexOf(landmark)).getBounds();
 assertEquals(expectedRect, actualRect);
 }
 }

 @Test
 public void testPaintComponentWithPointsAndLandmarks() {
 Graphics g = createMockGraphics();
 addPointTestData(g, 3);
 addLandmarkTestData(g, 3);
 display.paintComponent(g);
 }

 private void addPointTestData(Graphics g, int numberOfPoints) {
 for (int i = 0; i < numberOfPoints; i++) {
 TrigPoint point = new TrigPoint(i + 1.0, i + 1.0, 0.0, false);
 display.addPoint(point);
 points.add(point);
 }
 }

 private void addLandmarkTestData(Graphics g, int numberOfLandmarks) {
 for (int i = 0; i < numberOfLandmarks; i++) {
 Landmark landmark = new Landmark(i + 1.0, i + 1.0, new ImageIcon(getClass().getResource("/icon.png")));
 display.addLandmark(landmark);
 landmarks.add(landmark);
 }
 }

 private Graphics createMockGraphics() {
 return mock(Graphics.class);
 }

}