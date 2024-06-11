package Joshua.FoxHunt.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 }

 @After
 public void tearDown() {
 frame.dispose();
 }

 @Test
 public void testFoxHuntFrameConstructor() {
 // Check if the frame is created successfully
 assertNotNull(frame);
 // Check if the title is set correctly
 assertEquals("Triangulation", frame.getTitle());
 }

 @Test
 public void testAddPointMethod() {
 // Initialize test data
 final String thetaText = "45";
 final String latText = "40.7128";
 final String lonText = "-74.0060";
 final JTextField theta = frame.theta;
 final JTextField lat = frame.lat;
 final JTextField lon = frame.lon;

 // Set test data in text fields
 theta.setText(thetaText);
 lat.setText(latText);
 lon.setText(lonText);

 // Simulate button click event
 final ActionEvent actionEvent = new ActionEvent(new Object(), 0, "Action Performed");
 frame.actionPerformed(actionEvent);

 // Check if the point is added to the points vector
 assertEquals(1, frame.points.size());

 // Check if the point is added to the display
 assertTrue(frame.display.getPoints().size() > 0);
 }

 @Test
 public void testAddPointMethodWithInvalidData() {
 // Initialize test data
 final String thetaText = "";
 final String latText = "";
 final String lonText = "";
 final JTextField theta = frame.theta;
 final JTextField lat = frame.lat;
 final JTextField lon = frame.lon;

 // Set test data in text fields
 theta.setText(thetaText);
 lat.setText(latText);
 lon.setText(lonText);

 // Simulate button click event
 final ActionEvent actionEvent = new ActionEvent(new Object(), 0, "Action Performed");
 frame.actionPerformed(actionEvent);

 // Check if no point is added to the points vector
 assertEquals(0, frame.points.size());

 // Check if no point is added to the display
 assertTrue(frame.display.getPoints().size() == 0);
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointMethodWithInvalidLatOrLonData() {
 // Initialize test data
 final String thetaText = "45";
 final String latText = "dd*mm.mmm";
 final String lonText = "ddd*mm.mmm";
 final JTextField theta = frame.theta;
 final JTextField lat = frame.lat;
 final JTextField lon = frame.lon;

 // Set test data in text fields
 theta.setText(thetaText);
 lat.setText(latText);
 lon.setText(lonText);

 // Simulate button click event
 final ActionEvent actionEvent = new ActionEvent(new Object(), 0, "Action Performed");
 frame.actionPerformed(actionEvent);
 }

 @Test
 public void testFinalPrepMethod() {
 // Check if the panel3 and display are added to the panel
 assertNotNull(frame.panel3);
 assertNotNull(frame.display);
 assertTrue(frame.panel.getComponentCount() == 2);
 assertTrue(frame.panel.getComponent(0) == frame.panel3);
 assertTrue(frame.panel.getComponent(1) == frame.display);
 }

 @Test
 public void testTablePrepMethod() {
 // Check if the posModel and pos are initialized
 assertNotNull(frame.posModel);
 assertNotNull(frame.pos);

 // Check if the table model has the correct number of columns
 assertEquals(2, frame.posModel.getColumnCount());

 // Check if the table has the correct number of rows
 assertEquals(0, frame.pos.getRowCount());
 }

 @Test
 public void testLoadLandmarksMethod() {
 // Prepare test data
 final String fileName = "landmarks.cfg";
 final int numOfLandmarks = 3;
 final String[] iconFile = new String[numOfLandmarks];
 final double[] lat = new double[numOfLandmarks];
 final double[] lon = new double[numOfLandmarks];

 // Initialize landmarks with test data
 final Landmark[] landmarks = new Landmark[numOfLandmarks];
 for (int i = 0; i < numOfLandmarks; i++) {
 landmarks[i] = new Landmark(lat[i], lon[i], new ImageIcon(iconFile[i]));
 }

 // Simulate loading landmarks from file
 frame.loadLandmarks();

 // Check if the display has the correct number of landmarks
 assertEquals(numOfLandmarks, frame.display.getLandmarks().size());

 // Check if the landmarks in the display are equal to the test data
 for (int i = 0; i < numOfLandmarks; i++) {
 final Landmark landmark = frame.display.getLandmarks().get(i);
 assertEquals(landmarks[i].getLat(), landmark.getLat(), 0.001);
 assertEquals(landmarks[i].getLon(), landmark.getLon(), 0.001);
 assertEquals(landmarks[i].getIcon(), landmark.getIcon());
 }
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksMethodWithMissingFile() {
 final String fileName = "missingFile.cfg";
 frame.loadLandmarks();
 }

 @Test(expected = IOException.class)
 public void testLoadLandmarksMethodWithIOException() {
 final String fileName = "landmarks.cfg";
 final ByteArrayInputStream inputStream = new ByteArrayInputStream("Error".getBytes());
 final File file = new File(fileName);
 final FileInputStream inFile = new FileInputStream(file);
 final BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
 frame.loadLandmarks();
 in.close();
 inFile.close();
 }
}