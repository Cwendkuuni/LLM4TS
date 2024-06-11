package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JTextField;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

@RunWith(MockitoJUnitRunner.class)
public class FoxHuntFrameTest {

 private FoxHuntFrame fxf;
 private FoxTableModel model;
 private HuntDisplay display;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 fxf = new FoxHuntFrame();
 model = Mockito.spy(new FoxTableModel());
 display = Mockito.spy(new HuntDisplay());
 fxf.posModel = model;
 fxf.display = display;
 }

 @Test
 public void testFoxHuntFrameConstructor() {
 assertEquals("Triangulation", fxf.getTitle());
 assertEquals(JFrame.DEFAULT_CLOSE_OPERATION_EXIT_ON_CLOSE, fxf.getDefaultCloseOperation());
 }

 @Test
 public void testActionPerformed() {
 final ActionEvent event = new ActionEvent(new JButton(), 0, "Add It!!!");
 fxf.actionPerformed(event);
 assertEquals(1, fxf.points.size());
 assertEquals(1, fxf.fox.size());
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointInvalidTheta() {
 fxf.theta.setText("invalid");
 fxf.add = Mockito.mock(JButton.class);
 fxf.add.addActionListener(Mockito.any(ActionListener.class));
 fxf.actionPerformed(Mockito.any(ActionEvent.class));
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointInvalidLat() {
 fxf.theta.setText("45");
 fxf.lat.setText("invalid");
 fxf.add = Mockito.mock(JButton.class);
 fxf.add.addActionListener(Mockito.any(ActionListener.class));
 fxf.actionPerformed(Mockito.any(ActionEvent.class));
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointInvalidLon() {
 fxf.theta.setText("45");
 fxf.lat.setText("51.50.12");
 fxf.lon.setText("invalid");
 fxf.add = Mockito.mock(JButton.class);
 fxf.add.addActionListener(Mockito.any(ActionListener.class));
 fxf.actionPerformed(Mockito.any(ActionEvent.class));
 }

 @Test
 public void testAddPointValid() {
 fxf.theta.setText("45");
 fxf.lat.setText("51.50.12");
 fxf.lon.setText("0.12.34");
 fxf.add = Mockito.mock(JButton.class);
 fxf.add.addActionListener(Mockito.any(ActionListener.class));
 fxf.actionPerformed(Mockito.any(ActionEvent.class));
 assertEquals(2, fxf.points.size());
 assertEquals(2, fxf.fox.size());
 }

 @Test
 public void testDisplayAddLandMark() {
 final Landmark landmark = Mockito.mock(Landmark.class);
 display.addLandMark(landmark);
 assertEquals(1, display.getLandMarks().size());
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksFileNotFound() {
 fxf.loadLandmarks();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadLandmarksInvalidConfigFile() {
 final String[] configFile = new String[0];
 fxf.loadLandmarks(configFile);
 }

 @Test
 public void testLoadLandmarksValid() {
 final String[] configFile = new String[] { "FILE icon1.gif", "LAT 51.50.12", "LON 0.12.34", "FILE icon2.gif", "LAT 51.51.13", "LON 0.13.35" };
 fxf.loadLandmarks(configFile);
 assertEquals(2, display.getLandMarks().size());
 }

 @Test
 public void testErrorDialog() {
 fxf.error("test error message");
 }
}