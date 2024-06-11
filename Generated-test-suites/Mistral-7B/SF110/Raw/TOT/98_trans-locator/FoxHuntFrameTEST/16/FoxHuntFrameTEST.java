package Joshua.FoxHunt;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;
 private DefaultTableModel tableModel;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 frame = new FoxHuntFrame();
 tableModel = new DefaultTableModel(new Object[][]{}, new String[]{"Lattitude", "Longitude"});
 frame.posModel = tableModel;
 frame.pos = new JTable(tableModel);
 }

 @Test
 public void testAddPoint() {
 JTextField theta = mock(JTextField.class);
 JTextField lat = mock(JTextField.class);
 JTextField lon = mock(JTextField.class);
 when(lat.getText()).thenReturn("51.5074");
 when(lon.getText()).thenReturn("-0.0822");
 when(theta.getText()).thenReturn("180");
 doNothing().when(frame).addPoint();
 frame.theta = theta;
 frame.lat = lat;
 frame.lon = lon;
 frame.addActionListener(frame);
 frame.theta.setText("180");
 frame.lat.setText("51.5074");
 frame.lon.setText("-0.0822");
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));
 verify(frame, times(1)).addPoint();

 TrigPoint expectedPoint = new TrigPoint(51.5074, -0.0822, 180);
 assertEquals(expectedPoint, frame.points.lastElement());
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidLat() {
 JTextField theta = new JTextField("180");
 JTextField lat = new JTextField("Abcde");
 JTextField lon = new JTextField("51.5074");
 frame.theta = theta;
 frame.lat = lat;
 frame.lon = lon;
 frame.addActionListener(frame);
 frame.theta.setText("180");
 frame.lat.setText("Abcde");
 frame.lon.setText("51.5074");
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidLon() {
 JTextField theta = new JTextField("180");
 JTextField lat = new JTextField("51.5074");
 JTextField lon = new JTextField("Abcd.efg");
 frame.theta = theta;
 frame.lat = lat;
 frame.lon = lon;
 frame.addActionListener(frame);
 frame.theta.setText("180");
 frame.lat.setText("51.5074");
 frame.lon.setText("Abcd.efg");
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidTheta() {
 JTextField theta = new JTextField("Abcde");
 JTextField lat = new JTextField("51.5074");
 JTextField lon = new JTextField("51.5074");
 frame.theta = theta;
 frame.lat = lat;
 frame.lon = lon;
 frame.addActionListener(frame);
 frame.theta.setText("Abcde");
 frame.lat.setText("51.5074");
 frame.lon.setText("51.5074");
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddPointWhenNoPreviousPoint() {
 JTextField theta = mock(JTextField.class);
 JTextField lat = mock(JTextField.class);
 JTextField lon = mock(JTextField.class);
 when(lat.getText()).thenReturn("51.5074");
 when(lon.getText()).thenReturn("-0.0822");
 when(theta.getText()).thenReturn("180");
 doThrow(IllegalStateException.class).when(frame).addPoint();
 frame.theta = theta;
 frame.lat = lat;
 frame.lon = lon;
 frame.addActionListener(frame);
 frame.theta.setText("180");
 frame.lat.setText("51.5074");
 frame.lon.setText("-0.0822");
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));
 }

 @Test
 public void testDisplayAddPoint() {
 final HuntDisplay display = mock(HuntDisplay.class);
 when(display.addPoint(any(TrigPoint.class))).thenReturn(true);
 frame.display = display;
 frame.addPoint();
 verify(display, times(1)).addPoint(any(TrigPoint.class));
 }

 @Test
 public void testDisplayAddLandmark() {
 final HuntDisplay display = mock(HuntDisplay.class);
 when(display.addLandMark(any(Landmark.class))).thenReturn(true);
 frame.display = display;
 frame.loadLandmarks();
 verify(display, times(8)).addLandMark(any(Landmark.class));
 }
}