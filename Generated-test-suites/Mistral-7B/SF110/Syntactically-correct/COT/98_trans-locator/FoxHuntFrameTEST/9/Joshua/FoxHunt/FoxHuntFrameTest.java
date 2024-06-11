package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 }

 @Test
 public void testFoxHuntFrameConstructor() {
 assertEquals("Triangulation", frame.getTitle());
 assertTrue(frame.isVisible());
 }

 @Test
 public void testActionPerformed() {
 final ActionEvent actionEvent = new ActionEvent(new Object(), 0, "Add It!!!");
 frame.actionPerformed(actionEvent);
 assertTrue(frame.points.size() > 0);
 assertTrue(frame.fox.size() > 0);
 }

 @Test
 public void testAddPointWithValidData() {
 final FoxHuntFrame frame = new FoxHuntFrame();
 frame.theta.setText("45");
 frame.lat.setText("37.42515428571429");
 frame.lon.setText("-122.08388530588833");
 frame.add.doClick();
 assertTrue(frame.points.size() == 1);
 assertTrue(frame.fox.size() == 1);
}

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidThetaData() {
 final FoxHuntFrame frame = new FoxHuntFrame();
 frame.theta.setText("abc");
 frame.lat.setText("37.42515428571429");
 frame.lon.setText("-122.08388530588833");
 frame.add.doClick();
 fail("Should have thrown NumberFormatException");
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLatData() {
 final FoxHuntFrame frame = new FoxHuntFrame();
 frame.theta.setText("45");
 frame.lat.setText("abc");
 frame.lon.setText("-122.08388530588833");
 frame.add.doClick();
 fail("Should have thrown NumberFormatException");
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLonData() {
 final FoxHuntFrame frame = new FoxHuntFrame();
 frame.theta.setText("45");
 frame.lat.setText("37.42515428571429");
 frame.lon.setText("abc");
 frame.add.doClick();
 fail("Should have thrown NumberFormatException");
 }

 @Test
 public void testTextPrep() {
 final JTextField theta = frame.theta;
 final JTextField lat = frame.lat;
 final JTextField lon = frame.lon;
 final JButton add = frame.add;
 final JLabel thetaL = frame.panel1.getComponentAt(0, 0);
 final JLabel latL = frame.panel1.getComponentAt(0, 1);
 final JLabel lonL = frame.panel1.getComponentAt(0, 2);
 assertEquals("Theta in Degrees", thetaL.getText());
 assertEquals("Lat. dd*mm.mmm", latL.getText());
 assertEquals("lon. ddd*mm.mmm", lonL.getText());
 }

 @Test
 public void testTablePrep() {
 final FoxTableModel model = frame.posModel;
 assertEquals(2, model.getColumnCount());
 assertTrue(model instanceof AbstractTableModel);
 }

 @Test
 public void testDisplayPrep() {
 final HuntDisplay display = frame.display;
 assertNotNull(display);
 }

 @Test
 public void testLoadLandmarks() {
 final FoxHuntFrame frame = new FoxHuntFrame();
 frame.loadLandmarks();
 assertTrue(frame.display.getLandmarks().size() > 0);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksWithFileNotFound() {
 final FoxHuntFrame frame = new FoxHuntFrame();
 throw new RuntimeException("Mock FileNotFoundException");
 }

 @Test(expected = IOException.class)
 public void testLoadLandmarksWithIOException() {
 final FoxHuntFrame frame = new FoxHuntFrame();
 throw new RuntimeException("Mock IOException");
 }

 @Test
 public void testError() {
 final FoxHuntFrame frame = new FoxHuntFrame();
 frame.error("Test Error");
 fail("Should have displayed an error dialog");
 }
}