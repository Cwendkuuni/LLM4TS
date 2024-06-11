package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 }

 @Test
 public void testFoxHuntFrameConstructor() {
 // Check if the frame is initialized correctly
 assertNotNull(frame.getTheta());
 assertNotNull(frame.getLat());
 assertNotNull(frame.getLon());
 assertNotNull(frame.getZoom());
 assertNotNull(frame.getAdd());
 assertNotNull(frame.getPos());
 assertNotNull(frame.getPosModel());
 assertNotNull(frame.getDisplay());
 assertNotNull(frame.getPanel1());
 assertNotNull(frame.getPanel2());
 assertNotNull(frame.getPanel3());
 assertNotNull(frame.getPanel());
 assertNotNull(frame.getPoints());
 assertNotNull(frame.getFox());
 }

 @Test
 public void testAddPoint() {
 // Test adding a single point
 frame.theta.setText("45");
 frame.lat.setText("40.123456");
 frame.lon.setText("-73.234567");
 frame.actionPerformed(new ActionEvent(frame.getAdd(), 0, "ActionPerformed"));

 // Check if the point was added to the vector
 assertEquals(1, frame.getPoints().size());

 // Test adding multiple points
 frame.theta.setText("90");
 frame.lat.setText("40.234567");
 frame.lon.setText("-73.345678");
 frame.actionPerformed(new ActionEvent(frame.getAdd(), 0, "ActionPerformed"));

 // Check if the fox position was calculated correctly
 assertEquals(2, frame.getPoints().size());
 assertEquals(1, frame.getFox().size());
}

 @Test(expected = NumberFormatException.class)
 public void testAddPointInvalidInput() {
 // Test adding a point with invalid input
 frame.theta.setText("abc");
 frame.lat.setText("40.123");
 frame.lon.setText("ddd.234567");
 frame.actionPerformed(new ActionEvent(frame.getAdd(), 0, "ActionPerformed"));
}

 @Test
 public void testAddLandmarks() {
 // TODO: Implement test for loadLandmarks method
 }

 @Test
 public void testTableModel() {
 // Check if the table model displays the correct data
 FoxTableModel model = frame.getPosModel();
 assertEquals(0, model.getRowCount());

 frame.addPoint();
 assertEquals(1, model.getRowCount());

 frame.addPoint();
 assertEquals(2, model.getRowCount());
}

 @Test
 public void testDisplay() {
 // TODO: Implement test for HuntDisplay class
 }

}