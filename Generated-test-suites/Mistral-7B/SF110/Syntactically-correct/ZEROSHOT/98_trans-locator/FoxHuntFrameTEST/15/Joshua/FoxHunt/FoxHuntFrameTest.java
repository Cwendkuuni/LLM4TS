package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

import org.junit.*;

import Joshua.FoxHunt.*;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 }

 @Test
 public void testFoxHuntFrameConstruction() {
 assertEquals("Triangulation", frame.getTitle());
 assertTrue(frame.isVisible());
 }

 @Test
 public void testAddPoint() {
 final String theta = "45";
 final String lat = "51.5074, -0.1278";
 final String lon = "3.45678, -1.23456";
 frame.theta.setText(theta);
 frame.lat.setText(lat);
 frame.lon.setText(lon);
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!"));
 final int size = frame.points.size();
 assertEquals(1, size);
 }

 @Test
 public void testAddTwoPoints() {
 final String theta1 = "45";
 final String lat1 = "51.5074, -0.1278";
 final String lon1 = "3.45678, -1.23456";
 final String theta2 = "30";
 final String lat2 = "52.5074, -1.1278";
 final String lon2 = "3.55678, -1.13456";
 frame.theta.setText(theta1);
 frame.lat.setText(lat1);
 frame.lon.setText(lon1);
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!"));
 frame.theta.setText(theta2);
 frame.lat.setText(lat2);
 frame.lon.setText(lon2);
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!"));
 final int size = frame.points.size();
 assertEquals(2, size);
 }

 @Test
 public void testDisplay() {
 final JTable table = frame.pos;
 final int numRows = frame.fox.size();
 assertEquals(numRows, table.getRowCount());
 }

 @Test
 public void testLoadLandmarks() {
 final String[] expectedLandmarks = {"icon1.gif", "icon2.gif"};
 final double[] expectedLat = {51.5, 52.5};
 final double[] expectedLon = {-0.1278, -1.1278};
 final List<Landmark> landmarks = frame.display.getLandmarks();
 final int size = landmarks.size();
 assertEquals(2, size);
 for (int i = 0; i < size; ++i) {
 final Landmark landmark = landmarks.get(i);
 final String icon = landmark.getIcon().getDescription(null);
 final double lat = landmark.getLatitude();
 final double lon = landmark.getLongitude();
 assertEquals(expectedLandmarks[i], icon);
 assertEquals(expectedLat[i], lat, 0.001);
 assertEquals(expectedLon[i], lon, 0.001);
 }
 }

 @Test
 public void testErrorMessage() {
 final String errorMessage = "Test Error";
 final Object[] options = { "Abort", "Continue" };
 final int result = JOptionPane.showOptionDialog(frame, errorMessage, "Error", 0, 0, null, options, options[1]);
 assertEquals(1, result);
 }

 @After
 public void tearDown() {
 frame.dispose();
 }
}