package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import Joshua.FoxHunt.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 }

 @Test
 public void testConstructor() {
 assertEquals("Triangulation", frame.getTitle());
 assertNotNull(frame.display);
 assertNotNull(frame.posModel);
 assertNotNull(frame.pos);
 assertNotNull(frame.panel1);
 assertNotNull(frame.panel2);
 assertNotNull(frame.panel3);
 assertNotNull(frame.panel);
 assertNotNull(frame.points);
 assertNotNull(frame.fox);
 }

 @Test
 public void testAddPoint() {
 frame.theta.setText("10");
 frame.lat.setText("45.342334");
 frame.lon.setText("78.983212");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));

 assertEquals(1, frame.points.size());
 assertEquals(1, frame.fox.size());

 TrigPoint point = (TrigPoint) frame.points.elementAt(0);
 TrigPoint fox = (TrigPoint) frame.fox.elementAt(0);

 assertEquals("45.342334", point.getLatitude(), 0.001);
 assertEquals("78.983212", point.getLongitude(), 0.001);
 assertEquals("45.342334", fox.getLatitude(), 0.001);
 assertEquals("78.983212", fox.getLongitude(), 0.001);
 }

 @Test
 public void testAddSecondPoint() {
 frame.theta.setText("20");
 frame.lat.setText("51.234523");
 frame.lon.setText("12.345678");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));

 assertEquals(2, frame.points.size());
 assertEquals(2, frame.fox.size());

 TrigPoint point1 = (TrigPoint) frame.points.elementAt(0);
 TrigPoint point2 = (TrigPoint) frame.points.elementAt(1);
 TrigPoint fox1 = (TrigPoint) frame.fox.elementAt(0);
 TrigPoint fox2 = (TrigPoint) frame.fox.elementAt(1);

 assertEquals("45.342334", point1.getLatitude(), 0.001);
 assertEquals("78.983212", point1.getLongitude(), 0.001);
 assertEquals("51.234523", point2.getLatitude(), 0.001);
 assertEquals("12.345678", point2.getLongitude(), 0.001);
 assertEquals("50.246897", fox1.getLatitude(), 0.001);
 assertEquals("12.234567", fox1.getLongitude(), 0.001);
 assertEquals("51.234523", fox2.getLatitude(), 0.001);
 assertEquals("12.345678", fox2.getLongitude(), 0.001);
 }

 @Test
 public void testLoadLandmarks() {
 frame.loadLandmarks();
 assertNotNull(frame.display.landmarks);
 assertTrue(frame.display.landmarks.size() > 0);
 }

 @Test
 public void testError() {
 frame.error("Test Error");
 fail("Expected JOptionPane to display an error message");
 }
}