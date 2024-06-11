package Joshua.FoxHunt;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;
 private HuntDisplay displayMock;
 private FoxTableModel tableModelMock;

 @Before
 public void setUp() {
 displayMock = mock(HuntDisplay.class);
 tableModelMock = mock(FoxTableModel.class);
 frame = new FoxHuntFrame() {
 @Override
 HuntDisplay getDisplay() {
 return displayMock;
 }

 @Override
 FoxTableModel getPosModel() {
 return tableModelMock;
 }
 };
 }

 @Test
 public void testFrameInitialization() {
 assertEquals("Triangulation", frame.getTitle());
 assertEquals(3, frame.getTheta().getText().length());
 assertEquals(9, frame.getLat().getText().length());
 assertEquals(10, frame.getLon().getText().length());
 assertNotNull(frame.getAdd());
 assertEquals(4, frame.getPanel1().getComponentCount());
 assertFalse(frame.isVisible());
 }

 @Test
 public void testAddPointWithValidInput() {
 final JTextField theta = new JTextField("45");
 final JTextField lat = new JTextField("37.123456");
 final JTextField lon = new JTextField("-122.345678");
 when(displayMock.addPoint(any(TrigPoint.class))).thenReturn(true);
 frame.setTheta(theta);
 frame.setLat(lat);
 frame.setLon(lon);
 frame.getAdd().doClick();
 verify(displayMock, times(1)).addPoint(new TrigPoint(37.123456, -122.345678, 45));
 assertEquals(1, frame.getPoints().size());
 assertEquals(1, frame.getFox().size());
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLatInput() {
 final JTextField theta = new JTextField("45");
 final JTextField lat = new JTextField("ddd");
 final JTextField lon = new JTextField("122.345678");
 when(displayMock.addPoint(any(TrigPoint.class))).thenReturn(true);
 frame.setTheta(theta);
 frame.setLat(lat);
 frame.setLon(lon);
 frame.getAdd().doClick();
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLonInput() {
 final JTextField theta = new JTextField("45");
 final JTextField lat = new JTextField("37.123456");
 final JTextField lon = new JTextField("-ddd.ddd");
 when(displayMock.addPoint(any(TrigPoint.class))).thenReturn(true);
 frame.setTheta(theta);
 frame.setLat(lat);
 frame.setLon(lon);
 frame.getAdd().doClick();
 }

 @Test
 public void testAddPointWithInvalidAngleInput() {
 final JTextField theta = new JTextField("abc");
 final JTextField lat = new JTextField("37.123456");
 final JTextField lon = new JTextField("122.345678");
 when(displayMock.addPoint(any(TrigPoint.class))).thenReturn(false);
 frame.setTheta(theta);
 frame.setLat(lat);
 frame.setLon(lon);
 frame.getAdd().doClick();
 verify(displayMock, times(0)).addPoint(any(TrigPoint.class));
 }

 @Test
 public void testAddPointWithExistingPoints() {
 final JTextField theta = new JTextField("45");
 final JTextField lat = new JTextField("37.123456");
 final JTextField lon = new JTextField("122.345678");
 final TrigPoint point1 = new TrigPoint(37.123456, 122.345678, 45);
 final TrigPoint point2 = new TrigPoint(41.123456, -71.345678, 135);
 when(displayMock.addPoint(any(TrigPoint.class))).thenReturn(true);
 frame.setTheta(theta);
 frame.setLat(lat);
 frame.setLon(lon);
 frame.getAdd().doClick();
 frame.setTheta(theta);
 frame.setLat(lat);
 frame.setLon(lon);
 frame.getAdd().doClick();
 assertEquals(2, frame.getPoints().size());
 assertEquals(2, frame.getFox().size());
 }

 @Test
 public void testDisplayLandmarks() {
 final Vector points = new Vector();
 final Vector fox = new Vector();
 frame.loadLandmarks();
 verify(displayMock, times(1)).addLandMark(any(Landmark.class));
 assertEquals(1, points.size());
 assertEquals(1, fox.size());
 }
}