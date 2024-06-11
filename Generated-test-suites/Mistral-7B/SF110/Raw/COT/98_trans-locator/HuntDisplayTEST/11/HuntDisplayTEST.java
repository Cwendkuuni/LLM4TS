package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
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

 @After
 public void tearDown() {
 display = null;
 }

 @Test
 public void testHuntDisplayConstructor() {
 HuntDisplay hd = new HuntDisplay();
 assertNotNull(hd);
 }

 @Test
 public void testGetPreferredSize() {
 HuntDisplay hd = new HuntDisplay();
 Dimension expected = new Dimension(200, 200);
 Dimension actual = hd.getPreferredSize();
 assertEquals(expected.getWidth(), actual.getWidth());
 assertEquals(expected.getHeight(), actual.getHeight());
 }

 @Test
 public void testGetMinimumSize() {
 HuntDisplay hd = new HuntDisplay();
 Dimension expected = new Dimension(50, 50);
 Dimension actual = hd.getMinimumSize();
 assertEquals(expected.getWidth(), actual.getWidth());
 assertEquals(expected.getHeight(), actual.getHeight());
 }

 @Test
 public void testAddPoint() {
 HuntDisplay hd = new HuntDisplay();
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 hd.addPoint(point);
 assertEquals(1, hd.getPoints().size());
 }

 @Test(expected = NullPointerException.class)
 public void testAddPointNullPoint() {
 HuntDisplay hd = new HuntDisplay();
 hd.addPoint(null);
 }

 @Test
 public void testAddLandmark() {
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(getClass().getResource("testImage.png")));
 HuntDisplay hd = new HuntDisplay();
 hd.addLandmark(landmark);
 assertEquals(1, hd.getLandmarks().size());
 }

 @Test(expected = NullPointerException.class)
 public void testAddLandmarkNullLandmark() {
 HuntDisplay hd = new HuntDisplay();
 hd.addLandmark(null);
 }

 @Test
 public void testPaintComponentBasic() {
 HuntDisplay hd = new HuntDisplay();
 hd.addPoint(new TrigPoint(1.0, 1.0, 0.0, false));
 hd.addLandmark(new Landmark(1.0, 1.0, new ImageIcon(getClass().getResource("testImage.png"))));
 SwingUtilities.invokeLater(() -> {
 JFrame frame = new JFrame();
 frame.add(hd);
 frame.pack();
 JComponent c = (JComponent) frame.getContentPane();
 Graphics g = c.getGraphics();
 hd.paintComponent(g);
 });
 }

 @Test
 public void testPaintComponentWithPoints() {
 HuntDisplay hd = new HuntDisplay();
 Vector<TrigPoint> expected = new Vector<>();
 expected.add(new TrigPoint(1.0, 1.0, 0.0, false));
 expected.add(new TrigPoint(2.0, 2.0, 0.0, false));
 hd.addPoint(new TrigPoint(1.0, 1.0, 0.0, false));
 hd.addPoint(new TrigPoint(2.0, 2.0, 0.0, false));
 SwingUtilities.invokeLater(() -> {
 JFrame frame = new JFrame();
 frame.add(hd);
 frame.pack();
 JComponent c = (JComponent) frame.getContentPane();
 Graphics g = c.getGraphics();
 hd.paintComponent(g);
 Vector<TrigPoint> actual = new Vector<>();
 for (int x = 0; x < hd.getPoints().size(); x++) {
 TrigPoint trigPoint = (TrigPoint) hd.getPoints().elementAt(x);
 actual.add(new TrigPoint(trigPoint.getX(), trigPoint.getY(), trigPoint.getTheta(), trigPoint.isFox()));
 }
 assertEquals(expected, actual);
 });
 }

 @Test
 public void testPaintComponentWithLandmarks() {
 HuntDisplay hd = new HuntDisplay();
 Vector<Landmark> expected = new Vector<>();
 expected.add(new Landmark(1.0, 1.0, new ImageIcon(getClass().getResource("testImage.png"))));
 expected.add(new Landmark(2.0, 2.0, new ImageIcon(getClass().getResource("testImage.png"))));
 hd.addLandmark(new Landmark(1.0, 1.0, new ImageIcon(getClass().getResource("testImage.png"))));
 hd.addLandmark(new Landmark(2.0, 2.0, new ImageIcon(getClass().getResource("testImage.png"))));
 SwingUtilities.invokeLater(() -> {
 JFrame frame = new JFrame();
 frame.add(hd);
 frame.pack();
 JComponent c = (JComponent) frame.getContentPane();
 Graphics g = c.getGraphics();
 hd.paintComponent(g);
 Vector<Landmark> actual = new Vector<>();
 for (int x = 0; x < hd.getLandmarks().size(); x++) {
 Landmark landmark = (Landmark) hd.getLandmarks().elementAt(x);
 actual.add(new Landmark(landmark.getLon(), landmark.getLat(), landmark.getIcon()));
 }
 assertEquals(expected, actual);
 });
 }

 @Test
 public void testPaintComponentWithMixedPointsAndLandmarks() {
 HuntDisplay hd = new HuntDisplay();
 Vector<TrigPoint> points = new Vector<>();
 points.add(new TrigPoint(1.0, 1.0, 0.0, false));
 points.add(new TrigPoint(2.0, 2.0, 0.0, false));
 hd.addPoint(new TrigPoint(1.0, 1.0, 0.0, false));
 hd.addPoint(new TrigPoint(2.0, 2.0, 0.0, false));
 Vector<Landmark> landmarks = new Vector<>();
 landmarks.add(new Landmark(1.0, 1.0, new ImageIcon(getClass().getResource("testImage.png"))));
 landmarks.add(new Landmark(2.0, 2.0, new ImageIcon(getClass().getResource("testImage.png"))));
 hd.addLandmark(new Landmark(1.0, 1.0, new ImageIcon(getClass().getResource("testImage.png"))));
 hd.addLandmark(new Landmark(2.0, 2.0, new ImageIcon(getClass().getResource("testImage.png"))));
 SwingUtilities.invokeLater(() -> {
 JFrame frame = new JFrame();
 frame.add(hd);
 frame.pack();
 JComponent c = (JComponent) frame.getContentPane();
 Graphics g = c.getGraphics();
 hd.paintComponent(g);
 Vector<TrigPoint> actualPoints = new Vector<>();
 for (int x = 0; x < hd.getPoints().size(); x++) {
 TrigPoint trigPoint = (TrigPoint) hd.getPoints().elementAt(x);
 actualPoints.add(new TrigPoint(trigPoint.getX(), trigPoint.getY(), trigPoint.getTheta(), trigPoint.isFox()));
 }
 Vector<Landmark> actualLandmarks = new Vector<>();
 for (int x = 0; x < hd.getLandmarks().size(); x++) {
 Landmark landmark = (Landmark) hd.getLandmarks().elementAt(x);
 actualLandmarks.add(new Landmark(landmark.getLon(), landmark.getLat(), landmark.getIcon()));
 }
 assertEquals(points, actualPoints);
 assertEquals(landmarks, actualLandmarks);
 });
 }
}