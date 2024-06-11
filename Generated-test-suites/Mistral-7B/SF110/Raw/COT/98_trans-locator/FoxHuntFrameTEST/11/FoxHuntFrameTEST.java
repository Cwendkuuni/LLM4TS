package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.*;

import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;

import Joshua.FoxHunt.*;
import Joshua.FoxHunt.Model.*;
import javax.swing.*;
import javax.swing.table.*;

@RunWith(MockitoJUnitRunner.class)
public class FoxHuntFrameTest {

 private FoxHuntFrame frame;
 @Mock
 private HuntDisplay display;
 @Mock
 private FoxTableModel posModel;
 @Mock
 private JTable pos;
 @Mock
 private JTextField theta;
 @Mock
 private JTextField lat;
 @Mock
 private JTextField lon;
 @Mock
 private JTextField zoom;
 @Mock
 private JButton add;
 @Mock
 private JPanel panel1;
 @Mock
 private JPanel panel2;
 @Mock
 private JPanel panel3;
 @Mock
 private JPanel panel;
 @Mock
 private JSplitPane controls;
 @Mock
 private JSplitPane map;
 @Mock
 private JOptionPane optionPane;

 @Before
 public void setUp() {
  MockitoAnnotations.initMocks(this);
  frame = new FoxHuntFrame();
  frame.display = display;
  frame.posModel = posModel;
  frame.pos = pos;
  frame.theta = theta;
  frame.lat = lat;
  frame.lon = lon;
  frame.zoom = zoom;
  frame.add = add;
  frame.panel1 = panel1;
  frame.panel2 = panel2;
  frame.panel3 = panel3;
  frame.panel = panel;
  frame.controls = controls;
  frame.map = map;
 }

 @Test
 public void testFoxHuntFrameConstructor() {
  assertNotNull(frame);
  assertEquals("Triangulation", frame.getTitle());
  assertTrue(frame instanceof JFrame);
 }

 @Test
 public void testActionPerformedWhenAddPoint() {
  double latValue = 32.1321;
  double lonValue = -112.3123;
  int angleValue = 180;
  when(theta.getText()).thenReturn("180");
  when(lat.getText()).thenReturn("32.1321");
  when(lon.getText()).thenReturn("-112.3123");

  frame.actionPerformed(mock(ActionEvent.class));

  verify(display, times(1)).addPoint(argThat(trigPoint -> trigPoint.getX() == lonValue && trigPoint.getY() == latValue && trigPoint.getAngle() == angleValue));
  verify(posModel, times(1)).fireTableDataChanged();

  verifyNoMoreInteractions(display);
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWhenInvalidData() {
  when(theta.getText()).thenReturn("abc");
  when(lat.getText()).thenReturn("asdf");
  when(lon.getText()).thenReturn("123asdf");

  frame.actionPerformed(mock(ActionEvent.class));
 }

 @Test
 public void testLoadLandmarks() {
  String[] iconFile = { "icon1.gif", "icon2.gif" };
  double[] lat = { 1.1, 2.2 };
  double[] lon = { 3.3, 4.4 };

  when(posModel.getRowCount()).thenReturn(iconFile.length);

  frame.loadLandmarks();

  for (int i = 0; i < iconFile.length; i++) {
   verify(display, times(1)).addLandMark(argThat(landmark -> landmark.getIcon().getImage().toString().equals(iconFile[i]) && landmark.getLatitude() == lat[i] && landmark.getLongitude() == lon[i]));
  }
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksWhenFileNotFound() {
  when(posModel.getRowCount()).thenReturn(0);

  frame.loadLandmarks();
 }

 @Test(expected = IOException.class)
 public void testLoadLandmarksWhenIOException() {
  when(posModel.getRowCount()).thenReturn(1);

  doThrow(new IOException("Error communicating with the file")).when(frame).error(argThat(s -> s.contains("Error communicating with the file")));

  frame.loadLandmarks();
 }

 @Test
 public void testErrorDialog() {
  String errorMessage = "Can't Find the file";

  frame.error(errorMessage);

  verify(optionPane, times(1)).showOptionDialog(frame, errorMessage, "Error", 0, 0, null, new Object[] { "Abort", "Continue" }, "Abort");
 }

 @Test
 public void testTableData() {
  double latValue = 32.1321;
  double lonValue = -112.3123;
  int angleValue = 180;

  TrigPoint point = new TrigPoint(latValue, lonValue, angleValue);

  when(posModel.getValueAt(0, 0)).thenReturn(Double.toString(latValue));
  when(posModel.getValueAt(0, 1)).thenReturn(Double.toString(lonValue));

  assertEquals(Double.toString(latValue), frame.posModel.getValueAt(0, 0).toString());
  assertEquals(Double.toString(lonValue), frame.posModel.getValueAt(0, 1).toString());
 }

 @After
 public void tearDown() {
  Mockito.reset(display, posModel, pos, theta, lat, lon, zoom, add, panel1, panel2, panel3, panel, controls, map, optionPane);
 }
}