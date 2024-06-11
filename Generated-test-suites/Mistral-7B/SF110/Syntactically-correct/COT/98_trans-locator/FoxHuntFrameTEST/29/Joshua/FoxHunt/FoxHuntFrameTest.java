package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 }

 @Test
 public void testFoxHuntFrameConstructor() {
 // Check if frame is created successfully
 assertNotNull(frame);
 }

 @Test
 public void testAddPointMethod() {
 // Given
 String angle = "45";
 String latitude = "51.5074";
 String longitude = "-0.1278";
 frame.theta.setText(angle);
 frame.lat.setText(latitude);
 frame.lon.setText(longitude);
 // When
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionPerformed"));
 // Then
 assertEquals(1, frame.points.size());
 assertEquals(1, frame.fox.size());
 // Edge case testing
 frame.theta.setText("180");
 frame.lat.setText("51.5074");
 frame.lon.setText("0.1278");
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionPerformed"));
 assertEquals(2, frame.points.size());
 assertEquals(2, frame.fox.size());
 // Exception handling testing
 frame.theta.setText("");
 frame.lat.setText("51.5074");
 frame.lon.setText("0.1278");
 try {
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionPerformed"));
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 frame.theta.setText("45");
 frame.lat.setText("");
 frame.lon.setText("0.1278");
 try {
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionPerformed"));
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 frame.theta.setText("45");
 frame.lat.setText("51.5074");
 frame.lon.setText("");
 try {
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionPerformed"));
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 frame.theta.setText("45");
 frame.lat.setText("51.5074");
 frame.lon.setText("abc");
 try {
 frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionPerformed"));
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
}

 @Test
 public void testAddLandMarkMethod() {
 // Given
 frame.loadLandmarks();
 int numLandmarksBefore = frame.display.getLandmarks().size();
 // When
 frame.loadLandmarks();
 // Then
 int numLandmarksAfter = frame.display.getLandmarks().size();
 assertEquals(numLandmarksBefore, numLandmarksAfter);
 // Edge case testing
 frame.loadLandmarks(); // The file is already loaded.
 int numLandmarksAfterEdge = frame.display.getLandmarks().size();
 assertEquals(numLandmarksBefore, numLandmarksAfterEdge);
 // Exception handling testing
 fileDoesNotExist("./landmarks.cfg");
 // The method should not be called
 assertEquals(numLandmarksBefore, frame.display.getLandmarks().size());
}

 @Test
 public void testErrorMethod() {
 // Given
 frame.loadLandmarks = throwException(); // Replace with a real exception
 // When
 frame.error("Can't Find the file");
 // Then
 String[] options = {"Abort", "Continue"};
 int ans = 0;
 try {
 ans = JOptionPane.showOptionDialog(frame, "Error", "Error", 0, 0, null, options, options[0]);
} catch (HeadlessException e) {
 fail("Expected HeadlessException");
}
 assertEquals(0, ans);
 System.exit(1);
}

 private void fileDoesNotExist(String filePath) {
 try {
 File file = new File(filePath);
 FileInputStream inFile = new FileInputStream(file);
 BufferedReader in = new BufferedReader(new InputStreamReader(inFile));
 in.close();
 inFile.close();
} catch (FileNotFoundException e) {
 // Do nothing
}
}

 private ActionEvent throwException() {
 return new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "ActionPerformed");
}
}