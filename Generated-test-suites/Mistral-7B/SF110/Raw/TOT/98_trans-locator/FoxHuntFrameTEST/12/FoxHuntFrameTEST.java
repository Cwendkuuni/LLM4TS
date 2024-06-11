package Joshua.FoxHunt;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;
 private FoxTableModel model;
 private HuntDisplay display;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 frame = new FoxHuntFrame();
 model = mock(FoxTableModel.class);
 frame.posModel = model;
 display = mock(HuntDisplay.class);
 frame.display = display;
 }

 @Test
 public void testAddPointWithValidInput() {
 // Set up input fields with valid values
 JTextField theta = new JTextField("45");
 JTextField lat = new JTextField("51.123456");
 JTextField lon = new JTextField("-0.567891");
 frame.theta = theta;
 frame.lat = lat;
 frame.lon = lon;

 // Simulate button click event
 ActionEvent event = mock(ActionEvent.class);
 frame.actionPerformed(event);

 // Check if points vector contains new point
 verify(model).addElement(any(TrigPoint.class));
 }

 @Test
 public void testAddPointWithInvalidInput() {
 // Set up input fields with invalid values
 JTextField theta = new JTextField("");
 JTextField lat = new JTextField("ABC");
 JTextField lon = new JTextField("XYZ");
 frame.theta = theta;
 frame.lat = lat;
 frame.lon = lon;

 // Simulate button click event
 ActionEvent event = mock(ActionEvent.class);
 frame.actionPerformed(event);

 // Check if no new point is added and error message is displayed
 verify(model, never()).addElement(any(TrigPoint.class));
 assertEquals(JOptionPane.ERROR_MESSAGE, frame.JOptionPane.showOptionDialog(frame, "Error message", "Error", 0, JOptionPane.PLAIN_MESSAGE, null, new Object[]{"OK"}, null));
 }

 @Test
 public void testAddPointWithEmptyPointsVector() {
 frame = new FoxHuntFrame();
 frame.points = new Vector<>();
 frame.add();
 assertTrue(frame.points.isEmpty());
 }

 @Test
 public void testAddPointWithTwoPoints() {
 // Add first point
 JTextField theta1 = new JTextField("45");
 JTextField lat1 = new JTextField("51.123456");
 JTextField lon1 = new JTextField("-0.567891");
 frame.theta = theta1;
 frame.lat = lat1;
 frame.lon = lon1;
 frame.actionPerformed(mock(ActionEvent.class));

 // Add second point
 JTextField theta2 = new JTextField("30");
 JTextField lat2 = new JTextField("52.123456");
 JTextField lon2 = new JTextField("-1.567891");
 frame.theta = theta2;
 frame.lat = lat2;
 frame.lon = lon2;
 frame.actionPerformed(mock(ActionEvent.class));

 // Check if fox vector contains new point
 verify(display).addPoint(argThat(point -> point.getX() == -0.567891 && point.getY() == 51.123456));
 verify(display).addPoint(argThat(point -> point.getX() == -1.567891 && point.getY() == 52.123456));
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLatInput() {
 // Set up input fields with invalid lat value
 JTextField theta = new JTextField("45");
 JTextField lat = new JTextField("ABC");
 JTextField lon = new JTextField("0.567891");
 frame.theta = theta;
 frame.lat = lat;
 frame.lon = lon;

 // Simulate button click event
 ActionEvent event = mock(ActionEvent.class);
 frame.actionPerformed(event);

 // Check if exception is thrown
 fail("Exception expected: NumberFormatException");
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLonInput() {
 // Set up input fields with invalid lon value
 JTextField theta = new JTextField("45");
 JTextField lat = new JTextField("51.123456");
 JTextField lon = new JTextField("ABC");
 frame.theta = theta;
 frame.lat = lat;
 frame.lon = lon;

 // Simulate button click event
 ActionEvent event = mock(ActionEvent.class);
 frame.actionPerformed(event);

 // Check if exception is thrown
 fail("Exception expected: NumberFormatException");
 }
}