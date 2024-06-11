package Joshua.FoxHunt;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;
 private ActionEvent actionEvent;

 @Before
 public void setUp() {
 this.frame = new FoxHuntFrame();
 this.actionEvent = mock(ActionEvent.class);
 }

 @Test
 public void testAddPointWithValidValues() {
 final int angle = 35;
 final double lat = 51.5074;
 final double lon = -0.0129;
 final TrigPoint expectedPoint = new TrigPoint(lat, lon, angle);
 when(this.frame.theta.getText()).thenReturn("35");
 when(this.frame.lat.getText()).thenReturn("51.5074");
 when(this.frame.lon.getText()).thenReturn("-0.0129");
 this.frame.actionPerformed(this.actionEvent);
 verify(this.frame).addPoint();
 assertEquals(expectedPoint, this.frame.points.lastElement());
 }

 @Test
 public void testAddPointWithInvalidAngle() {
 when(this.frame.theta.getText()).thenReturn("0");
 this.frame.actionPerformed(this.actionEvent);
 assertEquals(0, this.frame.points.size());
 }

 @Test
 public void testAddPointWithInvalidLat() {
 when(this.frame.lat.getText()).thenReturn("XX.XXX.XXX");
 this.frame.actionPerformed(this.actionEvent);
 assertEquals(0, this.frame.points.size());
 }

 @Test
 public void testAddPointWithInvalidLon() {
 when(this.frame.lon.getText()).thenReturn("XXX.XXX.XXX");
 this.frame.actionPerformed(this.actionEvent);
 assertEquals(0, this.frame.points.size());
 }

 @Test
 public void testAddPointWithOnePoint() {
 this.frame.actionPerformed(this.actionEvent);
 final TrigPoint point = this.frame.points.elementAt(0);
 final TrigPoint expectedFox = new TrigPoint(point.getY(), point.getX(), point.getAngle());
 this.frame.addPoint();
 assertEquals(expectedFox, this.frame.fox.lastElement());
 }

 @Test
 public void testAddPointWithTwoPoints() {
 this.frame.actionPerformed(this.actionEvent);
 this.frame.actionPerformed(this.actionEvent);
 final TrigPoint firstPoint = this.frame.points.elementAt(0);
 final TrigPoint secondPoint = this.frame.points.elementAt(1);
 final TrigPoint expectedFox = FoxCalc.calcFox(firstPoint, secondPoint);
 this.frame.addPoint();
 assertEquals(expectedFox, this.frame.fox.lastElement());
 }
}