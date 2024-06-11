package Joshua.FoxHunt;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.FoxCalc;
import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setup() {
 frame = new FoxHuntFrame();
 }

 @Test
 public void testAddPoint() {
 // Test valid input
 frame.theta.setText("45");
 frame.lat.setText("51.506961");
 frame.lon.setText("-0.127796");
 frame.actionPerformed(new ActionEvent(frame.add, 0, 0));
 assertEquals(3, frame.points.size());

 TrigPoint expectedPoint = new TrigPoint(51.506961, -0.127796, 45);
 TrigPoint actualPoint = (TrigPoint) frame.points.lastElement();
 assertEquals(expectedPoint.getX(), actualPoint.getX(), 0.001);
 assertEquals(expectedPoint.getY(), actualPoint.getY(), 0.001);

 // Test invalid input
 frame.theta.setText("abcd");
 frame.lat.setText("51.506961");
 frame.lon.setText("-0.127796");
 frame.actionPerformed(new ActionEvent(frame.add, 0, 0));
 assertFalse(frame.points.contains(expectedPoint));
 assertSame(2, frame.points.size());

 // Test edge cases
 frame.theta.setText("0");
 frame.lat.setText("90.000000");
 frame.lon.setText("180.000000");
 frame.actionPerformed(new ActionEvent(frame.add, 0, 0));
 assertEquals(3, frame.points.size());

 TrigPoint firstPoint = (TrigPoint) frame.points.elementAt(0);
 TrigPoint secondPoint = expectedPoint;
 TrigPoint thirdPoint = (TrigPoint) frame.points.lastElement();
 assertEquals(firstPoint.getX(), secondPoint.getX(), 0.001);
 assertEquals(firstPoint.getY(), secondPoint.getY(), 0.001);
 assertEquals(secondPoint.getX(), thirdPoint.getX(), 0.001);
 assertEquals(secondPoint.getY(), thirdPoint.getY(), 0.001);
 }

 @Test
 public void testAddPoints() {
 // Test adding two points
 frame.theta.setText("45");
 frame.lat.setText("51.506961");
 frame.lon.setText("-0.127796");
 frame.actionPerformed(new ActionEvent(frame.add, 0, 0));

 frame.theta.setText("30");
 frame.lat.setText("52.506961");
 frame.lon.setText("-0.117796");
 frame.actionPerformed(new ActionEvent(frame.add, 0, 0));

 assertEquals(4, frame.points.size());
 TrigPoint expectedFox = new TrigPoint(52.037969, -0.120879, 12.962962);
 TrigPoint actualFox = (TrigPoint) frame.fox.lastElement();
 assertEquals(expectedFox.getX(), actualFox.getX(), 0.001);
 assertEquals(expectedFox.getY(), actualFox.getY(), 0.001);

 // Test adding three points
 frame.theta.setText("135");
 frame.lat.setText("53.506961");
 frame.lon.setText("-0.087796");
 frame.actionPerformed(new ActionEvent(frame.add, 0, 0));

 assertEquals(5, frame.points.size());
 TrigPoint actualLastPoint = (TrigPoint) frame.points.lastElement();
 assertNotEquals(expectedFox, actualLastPoint);
}

 @Test
 public void testLoadLandmarks() {
 try {
 frame.loadLandmarks();
 assertTrue(frame.display.getLandmarkCount() > 0);
 } catch (Exception e) {
 fail("Exception should not be thrown");
 }
}

 @Test(expected = IllegalArgumentException.class)
 public void testLoadLandmarksWithInvalidFile() {
 frame.loadLandmarks = new File("non_existent_file.cfg");
 frame.loadLandmarks();
}

 @Test(expected = IOException.class)
 public void testLoadLandmarksWithIOException() {
 frame.loadLandmarks = new File("landmarks.cfg") {
 @Override
 public InputStream openStream() throws FileNotFoundException {
 throw new IOException();
 }
 };
 frame.loadLandmarks();
}
}