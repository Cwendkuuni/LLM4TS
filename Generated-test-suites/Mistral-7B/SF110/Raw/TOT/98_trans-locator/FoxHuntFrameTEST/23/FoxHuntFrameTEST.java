package Joshua.FoxHunt;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextField;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;
 private JTextField theta;
 private JTextField lat;
 private JTextField lon;
 private JButton add;
 private JTable pos;
 private HuntDisplay display;
 private FoxTableModel posModel;

 @Before
 public void setup() {
  frame = new FoxHuntFrame();
  theta = mock(JTextField.class);
  lat = mock(JTextField.class);
  lon = mock(JTextField.class);
  add = mock(JButton.class);
  pos = mock(JTable.class);
  display = mock(HuntDisplay.class);
  posModel = mock(FoxTableModel.class);

  when(frame.getTheta()).thenReturn(theta);
  when(frame.getLat()).thenReturn(lat);
  when(frame.getLon()).thenReturn(lon);
  when(frame.getAdd()).thenReturn(add);
  when(frame.getPos()).thenReturn(pos);
  when(frame.getDisplay()).thenReturn(display);
  when(frame.getPosModel()).thenReturn(posModel);
 }

 @Test
 public void testFoxHuntFrameConstructor() {
  assertTrue(frame instanceof FoxHuntFrame);
  assertNotNull(frame.getPoints());
  assertNotNull(frame.getFox());
  assertNotNull(frame.getPanel1());
  assertNotNull(frame.getPanel2());
  assertNotNull(frame.getPanel3());
  assertNotNull(frame.getPanel());
  assertEquals("Triangulation", frame.getTitle());
 }

 @Test
 public void testAddPointMethod() {
  final double LAT = 37.4219;
  final double LON = -122.0841;
  final int angle = 45;

  frame.addPoint();

  verify(theta, times(1)).setText("45");
  verify(lat, times(1)).setText("37.4219");
  verify(lon, times(1)).setText("-122.0841");

  TrigPoint point = new TrigPoint(LAT, LON, angle);
  verify(points, times(1)).add(point);

  verify(display, times(1)).addPoint(point);
  verify(posModel, times(1)).fireTableDataChanged();

  TrigPoint fox = FoxCalc.calcFox(point, point);
  verify(fox, times(1)).equals(FoxCalc.calcFox(point, point));

  verify(foxModel, times(1)).addRow(new Object[]{point.getY(), point.getX()});
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidInputLat() {
  when(lat.getText()).thenReturn("Invalid Input");
  frame.addPoint();

  verify(lat, times(1)).setText("Invalid Input");
  verifyNoMoreInteractions(points);
  verifyNoMoreInteractions(display);
  verifyNoMoreInteractions(posModel);
  verifyNoMoreInteractions(fox);
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidInputLon() {
  when(lon.getText()).thenReturn("Invalid Input");
  frame.addPoint();

  verify(lon, times(1)).setText("Invalid Input");
  verifyNoMoreInteractions(points);
  verifyNoMoreInteractions(display);
  verifyNoMoreInteractions(posModel);
  verifyNoMoreInteractions(fox);
 }

 @Test
 public void testAddPointWithTwoPoints() {
  final double LAT1 = 37.4219;
  final double LON1 = -122.0841;
  final double LAT2 = 37.4220;
  final double LON2 = -122.0842;
  final int angle = 45;

  frame.addPoint();
  frame.addPoint();

  TrigPoint point1 = new TrigPoint(LAT1, LON1, angle);
  TrigPoint point2 = new TrigPoint(LAT2, LON2, angle);
  TrigPoint fox = FoxCalc.calcFox(point1, point2);

  verify(points, times(2)).add(any(TrigPoint.class));

  verify(display, times(2)).addPoint(any(TrigPoint.class));
  verify(posModel, times(1)).fireTableDataChanged();

  verify(fox, times(1)).equals(FoxCalc.calcFox(point1, point2));
 }

 @Test
 public void testLoadLandmarksMethod() {
  final double[] lat = { 37.4219, 37.4220 };
  final double[] lon = { -122.0841, -122.0842 };
  final String[] iconFile = { "icon1.gif", "icon2.gif" };

  when(display.getNumLandmarks()).thenReturn(lat.length);
  doReturn(lat).when(display).getLat();
  doReturn(lon).when(display).getLon();
  doReturn(iconFile).when(display).getIconFile();

  frame.loadLandmarks();

  for (int i = 0; i < lat.length; i++) {
   Landmark landmark = new Landmark(lat[i], lon[i], new ImageIcon(iconFile[i]));
   verify(display, times(1)).addLandMark(landmark);
  }
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksWithInvalidFile() {
  when(display.getNumLandmarks()).thenReturn(0);

  frame.loadLandmarks();

  verifyNoMoreInteractions(display);
 }
}