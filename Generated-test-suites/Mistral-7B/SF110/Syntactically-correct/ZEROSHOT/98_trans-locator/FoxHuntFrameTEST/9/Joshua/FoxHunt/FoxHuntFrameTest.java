package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Point;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 try {
 UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
 } catch (Exception e) {
 fail("Failed to set UILookAndFeel");
 }
 frame = new FoxHuntFrame();
 }

 @After
 public void tearDown() {
 frame.dispose();
 }

 @Test
 public void testFoxHuntFrameConstruction() {
 assertEquals("Triangulation", frame.getTitle());
 assertTrue(frame.isVisible());
 assertNotNull(frame.getContentPane());
 }

 @Test
 public void testAddPoint() {
 final String thetaText = "45";
 final String latText = "51.503751, -0.124821";
 final String lonText = "0.124821, 51.503751";
 frame.theta.setText(thetaText);
 frame.lat.setText(latText);
 frame.lon.setText(lonText);
 frame.add.doClick();

 final Vector<TrigPoint> points = frame.points;
 final Vector<TrigPoint> fox = frame.fox;
 assertEquals(1, points.size());
 assertEquals(1, fox.size());

 final TrigPoint expectedPoint = new TrigPoint(51.503751, -0.124821, Integer.parseInt(thetaText));
 final TrigPoint actualPoint = points.lastElement();
 assertEquals(expectedPoint.getX(), actualPoint.getX(), 0.0);
 assertEquals(expectedPoint.getY(), actualPoint.getY(), 0.001);
 assertEquals(expectedPoint.getAngle(), actualPoint.getAngle());

 final TrigPoint expectedFox = FoxCalc.calcFox(expectedPoint, points.elementAt(0));
 final TrigPoint actualFox = fox.lastElement();
 assertEquals(expectedFox.getX(), actualFox.getX(), 0.0);
 assertEquals(expectedFox.getY(), actualFox.getY(), 0.001);
 }

 @Test
 public void testAddMultiplePoints() {
 final String theta1Text = "45";
 final String lat1Text = "51.503751, -0.124821";
 final String lon1Text = "0.124821, 51.503751";
 frame.theta.setText(theta1Text);
 frame.lat.setText(lat1Text);
 frame.lon.setText(lon1Text);
 frame.add.doClick();

 final String theta2Text = "135";
 final String lat2Text = "51.513751, -0.125421";
 final String lon2Text = "0.125421, 51.513751";
 frame.theta.setText(theta2Text);
 frame.lat.setText(lat2Text);
 frame.lon.setText(lon2Text);
 frame.add.doClick();

 final Vector<TrigPoint> points = frame.points;
 final Vector<TrigPoint> fox = frame.fox;
 assertEquals(2, points.size());
 assertEquals(2, fox.size());

 final TrigPoint p1 = points.elementAt(0);
 final TrigPoint p2 = points.elementAt(1);
 final TrigPoint f1 = fox.elementAt(0);
 final TrigPoint f2 = fox.elementAt(1);

 assertEquals(p1.getX(), f2.getX(), 0.0);
 assertEquals(p1.getY(), f2.getY(), 0.001);
 assertEquals(p1.getAngle(), f2.getAngle(), 0.0);

 assertEquals(p2.getX(), f1.getX(), 0.0);
 assertEquals(p2.getY(), f1.getY(), 0.001);
 assertEquals(p2.getAngle(), f1.getAngle(), 0.0);
 }

 @Test
 public void testLoadLandmarks() {
 final int expectedNumLandmarks = 4;
 final Vector<Landmark> landmarks = frame.display.getLandmarks();
 assertEquals(expectedNumLandmarks, landmarks.size());
 }

 @Test
 public void testTableModelData() {
 final TableModel tableModel = frame.pos.getModel();
 assertTrue(tableModel instanceof FoxTableModel);
 }

 @Test
 public void testTableModelDataValues() {
 final TableModel tableModel = frame.pos.getModel();
 final FoxTableModel foxTableModel = (FoxTableModel) tableModel;
 assertEquals(frame.fox.size(), foxTableModel.getRowCount());
 }

 @Test
 public void testTableCellValues() {
 final TableModel tableModel = frame.pos.getModel();
 final FoxTableModel foxTableModel = (FoxTableModel) tableModel;
 final TrigPoint fox1 = frame.fox.elementAt(0);
 final TrigPoint fox2 = frame.fox.elementAt(1);

 final Object valueX1 = foxTableModel.getValueAt(0, 0);
 final Object valueY1 = foxTableModel.getValueAt(0, 1);
 final Object valueX2 = foxTableModel.getValueAt(1, 0);
 final Object valueY2 = foxTableModel.getValueAt(1, 1);

 final DecimalFormat formatter = new DecimalFormat("###.##############");
 final String expectedX1 = formatter.format(fox1.getX());
 final String expectedY1 = formatter.format(fox1.getY());
 final String expectedX2 = formatter.format(fox2.getX());
 final String expectedY2 = formatter.format(fox2.getY());

 assertEquals(expectedX1, valueX1);
 assertEquals(expectedY1, valueY1);
 assertEquals(expectedX2, valueX2);
 assertEquals(expectedY2, valueY2);
 }
}