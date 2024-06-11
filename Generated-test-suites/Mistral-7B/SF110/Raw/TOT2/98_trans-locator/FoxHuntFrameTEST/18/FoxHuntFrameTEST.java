import org.junit.Assert;
import org.junit.Test;
import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Test
 public void testFoxHuntFrameConstructor() {
 frame = new FoxHuntFrame();
 Assert.assertEquals("Triangulation", frame.getTitle());
 }

 @Test
 public void testAddPointWithValidValues() {
 frame = new FoxHuntFrame();
 frame.theta.setText("30");
 frame.lat.setText("40.123456");
 frame.lon.setText("-73.654321");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));
 TrigPoint expectedPoint = new TrigPoint(40.123456, -73.654321, 30);
 Assert.assertEquals(expectedPoint.toString(), frame.points.lastElement().toString());
 }

 @Test
 public void testAddPointWithInvalidValueForTheta() {
 frame = new FoxHuntFrame();
 frame.theta.setText("abc");
 frame.lat.setText("40.123456");
 frame.lon.setText("-73.654321");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));
 Assert.assertEquals(0, frame.points.size());
 }

 @Test
 public void testAddPointWithInvalidValueForLat() {
 frame = new FoxHuntFrame();
 frame.theta.setText("30");
 frame.lat.setText("abc");
 frame.lon.setText("-73.654321");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));
 Assert.assertEquals(0, frame.points.size());
 }

 @Test
 public void testAddPointWithInvalidValueForLon() {
 frame = new FoxHuntFrame();
 frame.theta.setText("30");
 frame.lat.setText("40.123456");
 frame.lon.setText("abc");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));
 Assert.assertEquals(0, frame.points.size());
 }

 @Test
 public void testAddLandmarks() {
 frame = new FoxHuntFrame();
 frame.loadLandmarks();
 List<Landmark> expectedLandmarks = new ArrayList<>();
 expectedLandmarks.add(new Landmark(53.421385, -112.571385, new ImageIcon("icon1.gif")));
 expectedLandmarks.add(new Landmark(53.421456, -112.571412, new ImageIcon("icon2.gif")));
 Assert.assertEquals(expectedLandmarks, frame.display.getLandmarks());
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksWithFileNotFoundException() {
 frame = new FoxHuntFrame();
 frame.loadLandmarks();
 }

 @Test(expected = IOException.class)
 public void testLoadLandmarksWithIOException() {
 frame = new FoxHuntFrame();
 frame.loadLandmarks();
 }

 @Test
 public void testDisplayPointsAndFox() {
 frame = new FoxHuntFrame();
 frame.theta.setText("30");
 frame.lat.setText("40.123456");
 frame.lon.setText("-73.654321");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));
 frame.theta.setText("45");
 frame.lat.setText("41.123456");
 frame.lon.setText("-72.654321");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));
 Assert.assertEquals(2, frame.points.size());
 Assert.assertEquals(2, frame.fox.size());
 }
}