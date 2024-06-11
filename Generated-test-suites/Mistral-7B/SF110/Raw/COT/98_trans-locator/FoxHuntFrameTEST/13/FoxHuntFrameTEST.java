package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setup() {
 frame = new FoxHuntFrame();
 }

 @Test
 public void testFoxHuntFrameConstructor() {
 // Testing if the frame is created successfully
 assertNotNull(frame);
 }

 @Test
 public void testActionPerformed() {
 // Given: theta, lat, and lon fields have valid values
 theta = new JTextField("45");
 lat = new JTextField("51.5074");
 lon = new JTextField("0.0879");
 frame.theta = theta;
 frame.lat = lat;
 frame.lon = lon;

 // When: the add button is clicked
 ActionEvent event = new ActionEvent(add, 0, "Clicked");
 frame.actionPerformed(event);

 // Then: a new TrigPoint should be added to the points vector
 assertEquals(1, frame.points.size());
 }

 @Test
 public void testAddPointWithInvalidTheta() {
 // Given: theta field has an invalid value
 theta = new JTextField("abc");
 frame.theta = theta;

 // When: the add button is clicked
 ActionEvent event = new ActionEvent(add, 0, "Clicked");
 frame.actionPerformed(event);

 // Then: no new TrigPoint should be added to the points vector
 assertEquals(0, frame.points.size());
 }

 @Test
 public void testAddPointWithInvalidLat() {
 // Given: lat field has an invalid value
 lat = new JTextField("51.5074a");
 frame.lat = lat;

 // When: the add button is clicked
 ActionEvent event = new ActionEvent(add, 0, "Clicked");
 frame.actionPerformed(event);

 // Then: no new TrigPoint should be added to the points vector
 assertEquals(0, frame.points.size());
 }

 @Test
 public void testAddPointWithInvalidLon() {
 // Given: lon field has an invalid value
 lon = new JTextField("51.5074 0.0879");
 frame.lon = lon;

 // When: the add button is clicked
 ActionEvent event = new ActionEvent(add, 0, "Clicked");
 frame.actionPerformed(event);

 // Then: no new TrigPoint should be added to the points vector
 assertEquals(0, frame.points.size());
 }

 @Test
 public void testAddPointWithValidValues() {
 // Given: theta, lat, and lon fields have valid values
 theta = new JTextField("45");
 lat = new JTextField("51.5074");
 lon = new JTextField("0.0879");
 frame.theta = theta;
 frame.lat = lat;
 frame.lon = lon;

 // When: the add button is clicked
 ActionEvent event = new ActionEvent(add, 0, "Clicked");
 frame.actionPerformed(event);

 // Then: a new TrigPoint should be added to the points vector
 assertEquals(1, frame.points.size());
 }

 @Test
 public void testAddPointWithTwoPoints() {
 // Given: two valid points have been added
 theta = new JTextField("45");
 lat = new JTextField("51.5074");
 lon = new JTextField("0.0879");
 frame.theta = theta;
 frame.lat = lat;
 frame.lon = lon;
 ActionEvent event1 = new ActionEvent(add, 0, "Clicked");
 frame.actionPerformed(event1);
 theta = new JTextField("30");
 lat = new JTextField("52.5074");
 lon = new JTextField("0.1879");
 frame.theta = theta;
 frame.lat = lat;
 frame.lon = lon;
 ActionEvent event2 = new ActionEvent(add, 0, "Clicked");
 frame.actionPerformed(event2);

 // Then: both points should be in the points vector
 assertEquals(2, frame.points.size());
 }

 @Test
 public void testDisplayAddPoint() {
 // Given: two valid points have been added
 theta = new JTextField("45");
 lat = new JTextField("51.5074");
 lon = new JTextField("0.0879");
 frame.theta = theta;
 frame.lat = lat;
 frame.lon = lon;
 ActionEvent event1 = new ActionEvent(add, 0, "Clicked");
 frame.actionPerformed(event1);
 theta = new JTextField("30");
 lat = new JTextField("52.5074");
 lon = new JTextField("0.1879");
 frame.theta = theta;
 frame.lat = lat;
 frame.lon = lon;
 ActionEvent event2 = new ActionEvent(add, 0, "Clicked");
 frame.actionPerformed(event2);

 // When: the display is updated
 frame.actionPerformed(event1);
 frame.display.paintComponent(null);
 frame.display.paintComponent(null);
 frame.display.paintComponent(null);
 frame.actionPerformed(event2);
 frame.display.paintComponent(null);
 frame.display.paintComponent(null);
 frame.display.paintComponent(null);

 // Then: both points should be displayed
 assertTrue(frame.display.getLandmarks().size() == 2);
 }

 @Test
 public void testLoadLandmarks() {
 // Given: a landmarks.cfg file is present
 String filePath = "landmarks.cfg";
 String expectedFileContent = "FILE icon1.jpg\n" +
                             "LAT 51.5074\n" +
                             "LON 0.0879\n" +
                             "FILE icon2.jpg\n" +
                             "LAT 52.5074\n" +
                             "LON 0.1879";
 String actualFileContent = "";
 try {
 File file = new File(filePath);
 FileInputStream inFile = new FileInputStream(file);
 BufferedReader in = new BufferedReader(new InputStreamReader(inFile));
 String input = "";
 while ((input = in.readLine()) != null) {
 actualFileContent += input + "\n";
 }
 in.close();
 inFile.close();
 } catch (FileNotFoundException e) {
 fail("File not found: " + filePath);
 } catch (IOException e) {
 fail("Error communicating with the file: " + filePath);
 }

 assertEquals(expectedFileContent, actualFileContent);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksWithMissingFile() {
 String filePath = "nonexistentfile.cfg";
 try {
 frame.loadLandmarks();
 } catch (FileNotFoundException e) {
 // Expected exception
 }
 }

 @Test(expected = IOException.class)
 public void testLoadLandmarksWithIOException() {
 String filePath = "landmarks.cfg";
 try {
 final File file = new File(filePath);
 final FileInputStream inFile = new FileInputStream(file);
 final BufferedReader in = new BufferedReader(new InputStreamReader(inFile));
 in.readLine();
 in.readLine();
 throw new IOException("Test exception");
 } catch (FileNotFoundException e) {
 fail("File not found: " + filePath);
 } catch (IOException e) {
 // Expected exception
 } finally {
 try {
 in.close();
 inFile.close();
 } catch (IOException e) {
 fail("Error closing the file: " + filePath);
 }
 }
 }

 @Test
 public void testFoxTableModelGetColumnCount() {
 FoxTableModel model = new FoxTableModel();
 assertEquals(2, model.getColumnCount());
 }

 @Test
 public void testFoxTableModelGetRowCount() {
 FoxTableModel model = new FoxTableModel();
 assertEquals(0, model.getRowCount());
 }

 @Test
 public void testFoxTableModelGetColumnName() {
 FoxTableModel model = new FoxTableModel();
 assertEquals("Lattitude", model.getColumnName(0));
 assertEquals("Longitude", model.getColumnName(1));
 }

 @Test
 public void testFoxTableModelGetValueAt() {
 FoxTableModel model = new FoxTableModel();
 TrigPoint point = new TrigPoint(51.5074, 0.0879, 45);
 model.points.add(point);
 assertEquals("51.5074", model.getValueAt(0, 0).toString());
 assertEquals("0.0879", model.getValueAt(0, 1).toString());
 }

 @Test
 public void testFoxTableModelGetColumnClass() {
 FoxTableModel model = new FoxTableModel();
 assertEquals(String.class, model.getColumnClass(0));
 assertEquals(String.class, model.getColumnClass(1));
 }

 @Test
 public void testFoxTableModelIsCellEditable() {
 FoxTableModel model = new FoxTableModel();
 assertFalse(model.isCellEditable(0, 0));
 assertFalse(model.isCellEditable(0, 1));
 }
}