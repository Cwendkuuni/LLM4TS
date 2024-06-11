package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 }

 @After
 public void tearDown() {
 if (frame != null) {
 frame.dispose();
 frame = null;
 }
 }

 @Test
 public void testFoxHuntFrameConstructor() {
 assertEquals("Triangulation", frame.getTitle());
 assertTrue(frame.isVisible());
 assertTrue(frame.getDefaultCloseOperation() == JFrame.EXIT_ON_CLOSE);
 }

 @Test
 public void testAddPoint() {
 final String angleStr = "45";
 final double lat = 37.7749;
 final double lon = -122.4194;
 frame.theta.setText(angleStr);
 frame.lat.setText(String.format("%.6f%n%s%s%s", lat, "(dd*mm.mmm)", "37", "52.2760"));
 frame.lon.setText(String.format("%.6f%n%s%s%s%s%s", lon, "(ddd*mm.mmm)", "122", "25", "15.4536", "02.7744"));
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, null));

 final TrigPoint expectedPoint = new TrigPoint(lat, lon, Integer.parseInt(angleStr));
 final int expectedSize = frame.points.size();
 final int expectedFoxSize = frame.fox.size();

 assertEquals(expectedSize + 1, frame.points.size());
 assertEquals(expectedFoxSize, frame.fox.size());
 assertTrue(frame.points.contains(expectedPoint));

 final FoxTableModel model = (FoxTableModel) frame.pos.getModel();
 final Object[] expectedValues = { expectedPoint.getY(), expectedPoint.getX() };
 final Object[] actualValues = model.getDataVector().toArray();

 assertArrayEquals(expectedValues, actualValues);
 }

 @Test
 public void testAddMultiplePoints() {
 final String angleStr1 = "45";
 final double lat1 = 37.7749;
 final double lon1 = -122.4194;
 final String angleStr2 = "90";
 final double lat2 = 37.7733;
 final double lon2 = -122.4188;

 frame.theta.setText(angleStr1);
 frame.lat.setText(String.format("%.6f%n%s%s%s", lat1, "(dd*mm.mmm)", "37", "52.2760"));
 frame.lon.setText(String.format("%.6f%n%s%s%s%s%s", lon1, "(ddd*mm.mmm)", "122", "25", "15.4536", "02.7744"));
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, null));

 final TrigPoint point1 = new TrigPoint(lat1, lon1, Integer.parseInt(angleStr1));
 final int expectedSize1 = frame.points.size();
 final int expectedFoxSize1 = frame.fox.size();

 assertEquals(expectedSize1 + 1, frame.points.size());
 assertEquals(expectedFoxSize1, frame.fox.size());
 assertTrue(frame.points.contains(point1));

 frame.theta.setText(angleStr2);
 frame.lat.setText(String.format("%.6f%n%s%s%s", lat2, "(dd*mm.mmm)", "37", "52.1670"));
 frame.lon.setText(String.format("%.6f%n%s%s%s%s%s", lon2, "(ddd*mm.mmm)", "122", "25", "15.4534", "02.7742"));
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, null));

 final TrigPoint point2 = new TrigPoint(lat2, lon2, Integer.parseInt(angleStr2));
 final int expectedSize2 = frame.points.size();
 final int expectedFoxSize2 = frame.fox.size();

 assertEquals(expectedSize2, frame.points.size());
 assertEquals(expectedFoxSize1 + 1, frame.fox.size());
 assertTrue(frame.points.contains(point2));

 final FoxTableModel model = (FoxTableModel) frame.pos.getModel();
 final Object[][] expectedValues = { { point1.getY(), point1.getX() }, { point2.getY(), point2.getX() } };
 final Object[][] actualValues = ((DefaultTableModel) model).getDataVector().toArray();

 assertArrayEquals(expectedValues, actualValues);
 }

 @Test
 public void testCalculateFox() {
 final double lat1 = 37.7749;
 final double lon1 = -122.4194;
 final double lat2 = 37.7733;
 final double lon2 = -122.4188;

 final TrigPoint point1 = new TrigPoint(lat1, lon1, 0);
 final TrigPoint point2 = new TrigPoint(lat2, lon2, 0);
 final TrigPoint expectedFox = FoxCalc.calcFox(point1, point2);

 final TrigPoint actualFox = ((Vector) frame.fox.lastElement()).lastElement();

 assertEquals(expectedFox.getX(), actualFox.getX(), 0.0001);
 assertEquals(expectedFox.getY(), actualFox.getY(), 0.0001);
 }

 @Test
 public void testLoadLandmarks() {
 final int expectedNumLandmarks = 5;
 final String[] expectedIconFiles = new String[expectedNumLandmarks];
 final double[] expectedLat = new double[expectedNumLandmarks];
 final double[] expectedLon = new double[expectedNumLandmarks];

 try {
 frame.loadLandmarks();
 } catch (FileNotFoundException e) {
 fail("Expected file to exist.");
 } catch (IOException e) {
 fail("Expected no I/O error.");
 }

 final int actualNumLandmarks = ((Vector) frame.display.getLandmarks()).size();
 assertEquals(expectedNumLandmarks, actualNumLandmarks);

 for (int i = 0; i < expectedNumLandmarks; i++) {
 expectedIconFiles[i] = String.format("icon%d.gif", i + 1);
 final Landmark landmark = (Landmark) ((Vector) frame.display.getLandmarks()).elementAt(i);
 final ImageIcon icon = landmark.getIcon();
 assertEquals(expectedIconFiles[i], icon.getDescription(null));
 expectedLat[i] = landmark.getX();
 expectedLon[i] = landmark.getY();
 }

 final Landmark landmark = (Landmark) ((Vector) frame.display.getLandmarks()).lastElement();
 final ImageIcon icon = landmark.getIcon();
 assertEquals("icon5.gif", icon.getDescription(null));

 final double[] actualLat = new double[actualNumLandmarks];
 final double[] actualLon = new double[actualNumLandmarks];
 for (int i = 0; i < actualNumLandmarks; i++) {
 actualLat[i] = ((Landmark) ((Vector) frame.display.getLandmarks()).elementAt(i)).getX();
 actualLon[i] = ((Landmark) ((Vector) frame.display.getLandmarks()).elementAt(i)).getY();
 }

 for (int i = 0; i < expectedNumLandmarks; i++) {
 assertEquals(expectedLat[i], actualLat[i], 0.0001);
 assertEquals(expectedLon[i], actualLon[i], 0.0001);
 }
 }
}