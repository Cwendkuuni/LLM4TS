package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 }

 @Test
 public void testFoxHuntFrameConstructor() {
 // Test if the constructor initializes the frame correctly
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
 public void testAddPointMethod() {
 // Test if the addPoint method correctly adds a new point to the points vector and the display
 String theta = "45";
 String lat = "51.5074";
 String lon = "0.1278";
 frame.theta.setText(theta);
 frame.lat.setText(lat);
 frame.lon.setText(lon);
 frame.actionPerformed(new ActionEvent(frame.getAdd(), 0, "ActionEvent"));
 TrigPoint expectedPoint = new TrigPoint(Double.parseDouble(lat), Double.parseDouble(lon), Integer.parseInt(theta));
 assertTrue(frame.getPoints().contains(expectedPoint));
 assertFalse(frame.getFox().isEmpty());
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointMethodWithInvalidInput() {
 // Test if the addPoint method throws a NumberFormatException when the input is invalid
 frame.theta.setText("abc");
 frame.lat.setText("51.5074.2");
 frame.lon.setText("0.1278.3");
 frame.actionPerformed(new ActionEvent(frame.getAdd(), 0, "ActionEvent"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointMethodWithTwoPoints() {
 // Test if the addPoint method throws an IllegalArgumentException when there are already two points
 frame.theta.setText("45");
 frame.lat.setText("51.5074");
 frame.lon.setText("0.1278");
 frame.actionPerformed(new ActionEvent(frame.getAdd(), 0, "ActionEvent"));
 TrigPoint firstPoint = new TrigPoint(Double.parseDouble("51.5074"), Double.parseDouble("0.1278"), Integer.parseInt("45"));
 TrigPoint secondPoint = new TrigPoint(Double.parseDouble("51.5074"), Double.parseDouble("0.1278"), Integer.parseInt("45"));
 frame.theta.setText("30");
 frame.lat.setText("51.5074");
 frame.lon.setText("0.1278");
 frame.actionPerformed(new ActionEvent(frame.getAdd(), 0, "ActionEvent"));
 assertEquals(firstPoint, frame.getPoints().lastElement());
 assertTrue(frame.getFox().isEmpty());
 frame.theta.setText("80");
 frame.lat.setText("51.5074");
 frame.lon.setText("0.1278");
 frame.actionPerformed(new ActionEvent(frame.getAdd(), 0, "ActionEvent"));
 assertEquals(secondPoint, frame.getPoints().lastElement());
 frame.getFox().remove(0);
 TrigPoint thirdPoint = new TrigPoint(Double.parseDouble("51.5074"), Double.parseDouble("0.1278"), Integer.parseInt("80"));
 frame.theta.setText("135");
 frame.lat.setText("51.5074");
 frame.lon.setText("0.1278");
 frame.actionPerformed(new ActionEvent(frame.getAdd(), 0, "ActionEvent"));
 assertNotEquals(thirdPoint, frame.getPoints().lastElement());
 assertNotNull(frame.getFox().get(0));
 }

 @Test
 public void testLoadLandmarksMethod() {
 // Test if the loadLandmarks method correctly loads landmarks from the file
 frame.loadLandmarks();
 assertFalse(frame.getDisplay().isEmpty());
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksMethodWithFileNotFoundException() {
 // Test if the loadLandmarks method throws a FileNotFoundException when the file is not found
 frame = new FoxHuntFrame();
 try {
 frame.loadLandmarks();
 fail("Expected FileNotFoundException");
 } catch (FileNotFoundException e) {
 // Empty body
 }
 }

 @Test(expected = IOException.class)
 public void testLoadLandmarksMethodWithIOException() {
 // Test if the loadLandmarks method throws an IOException when there is an error communicating with the file
 frame = new FoxHuntFrame();
 try (FileInputStream inFile = new FileInputStream("landmarks.cfg");
 BufferedReader in = new BufferedReader(new InputStreamReader(inFile))) {
 frame.loadLandmarks();
 fail("Expected IOException");
 } catch (IOException e) {
 // Empty body
 }
 }

 @Test
 public void testFoxTableModel() {
 // Test if the FoxTableModel correctly displays the latitude and longitude of the foxes
 FoxTableModel model = frame.getPosModel();
 assertEquals("Lattitude", model.getColumnName(0));
 assertEquals("Longitude", model.getColumnName(1));
 TrigPoint[] foxes = new TrigPoint[frame.getFox().size()];
 for (int i = 0; i < frame.getFox().size(); i++) {
 foxes[i] = (TrigPoint) frame.getFox().get(i);
 }
 Object[][] data = model.to2DArray();
 for (int i = 0; i < data.length; i++) {
 assertEquals(foxes[i].getY(), data[i][0]);
 assertEquals(foxes[i].getX(), data[i][1]);
 }
 }
}