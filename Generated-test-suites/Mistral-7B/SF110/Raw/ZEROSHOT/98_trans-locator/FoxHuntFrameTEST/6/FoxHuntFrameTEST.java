package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.FoxCalc;
import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 }

 @Test
 public void testFoxHuntFrameConstruction() {
 assertEquals("Triangulation", frame.getTitle());
 assertNotNull(frame.theta);
 assertNotNull(frame.lat);
 assertNotNull(frame.lon);
 assertNotNull(frame.zoom);
 assertNotNull(frame.add);
 assertNotNull(frame.pos);
 assertNotNull(frame.posModel);
 assertNotNull(frame.display);
 assertNotNull(frame.panel1);
 assertNotNull(frame.panel2);
 assertNotNull(frame.panel3);
 assertNotNull(frame.panel);
 assertNotNull(frame.points);
 assertNotNull(frame.fox);
 }

 @Test
 public void testActionPerformed() {
 ActionEvent event = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "test");
 frame.actionPerformed(event);

 assertTrue(frame.points.size() > 0);
 assertTrue(frame.fox.size() > 0);
 }

 @Test
 public void testAddPoint() {
 frame.theta.setText("45");
 frame.lat.setText("51.506865");
 frame.lon.setText("-0.127762");
 frame.add.doClick();

 TrigPoint point = (TrigPoint) frame.points.lastElement();
 assertEquals("51.506865", Double.toString(point.getY()));
 assertEquals("-0.127762", Double.toString(point.getX()));
 assertEquals("45", Integer.toString(point.getAngle()));

 TrigPoint foxPoint = (TrigPoint) frame.fox.lastElement();
 assertEquals("Fox Point", FoxCalc.calcFox(point, (TrigPoint) frame.points.elementAt(frame.points.size() - 2)));
 assertEquals("Fox X", Double.toString(foxPoint.getX()));
 assertEquals("Fox Y", Double.toString(foxPoint.getY()));
 }

 @Test
 public void testFinalPrep() {
 assertNotNull(frame.panel3);
 assertNotNull(frame.display);
 }

 @Test
 public void testTextPrep() {
 assertNotNull(frame.theta);
 assertNotNull(frame.lat);
 assertNotNull(frame.lon);
 assertNotNull(frame.add);
 }

 @Test
 public void testTablePrep() {
 assertNotNull(frame.posModel);
 assertNotNull(frame.pos);
 }

 @Test
 public void testDisplayPrep() {
 assertNotNull(frame.display);
 }

 @Test
 public void testLoadLandmarks() {
 Vector<Landmark> landmarks = frame.display.getLandMarks();
 assertEquals(3, landmarks.size());
 }

 @Test(expected = NullPointerException.class)
 public void testError() {
 frame.error(null);
 }
}