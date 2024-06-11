package Joshua.FoxHunt;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;
 private FoxTableModel posModel;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 frame = new FoxHuntFrame();
 posModel = mock(FoxTableModel.class);
 frame.posModel = posModel;
 }

 @Test
 public void testAddPointWithValidInput() {
 final String theta = "30";
 final String lat = "40.123456";
 final String lon = "71.123456";
 final ActionEvent actionEvent = mock(ActionEvent.class);

 when(frame.theta.getText()).thenReturn(theta);
 when(frame.lat.getText()).thenReturn(lat);
 when(frame.lon.getText()).thenReturn(lon);
 when(frame.posModel.fireTableDataChanged()).thenReturn(true);

 frame.actionPerformed(actionEvent);

 assertEquals(1, frame.points.size());
 assertEquals(1, frame.fox.size());
 verify(frame.posModel).fireTableDataChanged();
 }

 @Test
 public void testAddPointWithInvalidInput() {
 final String theta = "";
 final String lat = "";
 final String lon = "";
 final ActionEvent actionEvent = mock(ActionEvent.class);

 when(frame.theta.getText()).thenReturn(theta);
 when(frame.lat.getText()).thenReturn(lat);
 when(frame.lon.getText()).thenReturn(lon);

 frame.actionPerformed(actionEvent);

 assertEquals(0, frame.points.size());
 assertEquals(0, frame.fox.size());
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLat() {
 final String theta = "30";
 final String lat = "dddd";
 final String lon = "71.123456";
 final ActionEvent actionEvent = mock(ActionEvent.class);

 when(frame.theta.getText()).thenReturn(theta);
 when(frame.lat.getText()).thenReturn(lat);
 when(frame.lon.getText()).thenReturn(lon);

 frame.actionPerformed(actionEvent);
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLon() {
 final String theta = "30";
 final String lat = "40.123456";
 final String lon = "dddddddd";
 final ActionEvent actionEvent = mock(ActionEvent.class);

 when(frame.theta.getText()).thenReturn(theta);
 when(frame.lat.getText()).thenReturn(lat);
 when(frame.lon.getText()).thenReturn(lon);

 frame.actionPerformed(actionEvent);
 }

 @Test
 public void testLoadLandmarks() {
 final int NUM = 3;
 final String[] iconFile = new String[NUM];
 final double[] lat = new double[NUM];
 final double[] lon = new double[NUM];

 when(frame.loadLandmarks()).thenReturn(true);

 frame.loadLandmarks();

 verify(frame).display.addLandMark(argThat(landmark -> landmark.getX() == 0.0));
 verify(frame).display.addLandMark(argThat(landmark -> landmark.getY() == 1.0));
 verify(frame).display.addLandMark(argThat(landmark -> landmark.getY() == 2.0));
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksWithFileNotFound() {
 when(frame.loadLandmarks()).thenThrow(new FileNotFoundException());
 frame.loadLandmarks();
 }

 @Test(expected = IOException.class)
 public void testLoadLandmarksWithIOException() {
 when(frame.loadLandmarks()).thenThrow(new IOException());
 frame.loadLandmarks();
 }
}