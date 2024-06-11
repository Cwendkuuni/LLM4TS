package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 this.frame = new FoxHuntFrame();
 }

 @After
 public void tearDown() {
 this.frame.dispose();
 }

 @Test
 public void testConstructor() {
 assertTrue(this.frame instanceof FoxHuntFrame);
 assertEquals("Triangulation", this.frame.getTitle());
 }

 @Test
 public void testActionPerformed() {
 ActionEvent event = new ActionEvent(new Object(), 0, "ActionCommand");
 this.frame.actionPerformed(event);
 verify(this.frame).addPoint();
 }

 @Test
 public void testAddPoint() {
 final String thetaText = "45";
 final String latText = "51.507411";
 final String lonText = "-0.127768";
 this.frame.theta.setText(thetaText);
 this.frame.lat.setText(latText);
 this.frame.lon.setText(lonText);
 this.frame.theta.requestFocus();
 this.frame.theta.dispatchEvent(new FocusEvent(this.frame, FocusEvent.FOCUS_GAINED, false));
 this.frame.theta.dispatchEvent(new ActionEvent(this.frame.theta, 0, "textChanged"));
 this.frame.add.requestFocus();
 this.frame.add.dispatchEvent(new ActionEvent(this.frame.add, 0, "ActionCommand"));

 final int numberOfPointsBefore = this.frame.points.size();
 final TrigPoint expectedPoint = new TrigPoint(51.507411, -0.127768, Integer.parseInt(thetaText));
 final TrigPoint actualPoint = (TrigPoint) this.frame.points.lastElement();
 assertEquals(expectedPoint.getX(), actualPoint.getX(), 0.001);
 assertEquals(expectedPoint.getY(), actualPoint.getY(), 0.001);

 final int numberOfFoxesBefore = this.frame.fox.size();
 final TrigPoint expectedFox = FoxCalc.calcFox(expectedPoint, (TrigPoint) this.frame.points.elementAt(this.frame.points.size() - 2));
 final TrigPoint actualFox = (TrigPoint) this.frame.fox.lastElement();
 assertEquals(expectedFox.getX(), actualFox.getX(), 0.001);
 assertEquals(expectedFox.getY(), actualFox.getY(), 0.001);

 final int numberOfRowsInTableBefore = ((JTable) this.frame.pos).getRowCount();
 final int numberOfColumnsInTable = ((JTable) this.frame.pos).getColumnCount();
 final Object[] expectedData = { expectedPoint.getY(), expectedFox.getX() };
 final Object[] actualData = ((JTable) this.frame.pos).getValueAt(numberOfRowsInTableBefore, 0);
 assertTrue(Arrays.deepEquals(expectedData, actualData));
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidTheta() {
 this.frame.theta.setText("InvalidTheta");
 this.frame.theta.requestFocus();
 this.frame.theta.dispatchEvent(new FocusEvent(this.frame, FocusEvent.FOCUS_GAINED, false));
 this.frame.theta.dispatchEvent(new ActionEvent(this.frame.theta, 0, "textChanged"));
 this.frame.add.requestFocus();
 this.frame.add.dispatchEvent(new ActionEvent(this.frame.add, 0, "ActionCommand"));
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLat() {
 this.frame.theta.setText("45");
 this.frame.lat.setText("InvalidLatitude");
 this.frame.lat.requestFocus();
 this.frame.lat.dispatchEvent(new FocusEvent(this.frame, FocusEvent.FOCUS_GAINED, false));
 this.frame.lat.dispatchEvent(new ActionEvent(this.frame.lat, 0, "textChanged"));
 this.frame.lon.requestFocus();
 this.frame.add.requestFocus();
 this.frame.add.dispatchEvent(new ActionEvent(this.frame.add, 0, "ActionCommand"));
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLon() {
 this.frame.theta.setText("45");
 this.frame.lat.setText("51.507411");
 this.frame.lon.setText("InvalidLongitude");
 this.frame.lon.requestFocus();
 this.frame.lon.dispatchEvent(new FocusEvent(this.frame, FocusEvent.FOCUS_GAINED, false));
 this.frame.lon.dispatchEvent(new ActionEvent(this.frame.lon, 0, "textChanged"));
 this.frame.add.requestFocus();
 this.frame.add.dispatchEvent(new ActionEvent(this.frame.add, 0, "ActionCommand"));
 }

 @Test
 public void testLoadLandmarks() {
 try {
 this.frame.loadLandmarks();
 final int numberOfLandmarksBefore = this.frame.display.size();
 final Landmark expectedLandmark = new Landmark(51.517017, -0.088306, new ImageIcon("icon.gif"));
 final Landmark actualLandmark = this.frame.display.elementAt(this.frame.display.size() - 1);
 assertEquals(expectedLandmark.getX(), actualLandmark.getX(), 0.001);
 assertEquals(expectedLandmark.getY(), actualLandmark.getY(), 0.001);
 assertEquals(expectedLandmark.getIcon(), actualLandmark.getIcon());
 }
 catch (FileNotFoundException e) {
 fail("Expected no exception but got " + e.getMessage());
 }
 catch (IOException e) {
 fail("Expected no exception but got " + e.getMessage());
 }
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksWithMissingFile() {
 this.frame.loadLandmarks();
 }

 @Test(expected = IOException.class)
 public void testLoadLandmarksWithInvalidFile() {
 this.frame.loadLandmarks();
 this.frame.display.removeAll();
 final String invalidContent = "INVALID_CONTENT";
 try {
 final File file = new File("./landmarks.cfg");
 final FileInputStream inFile = new FileInputStream(file);
 final InputStreamReader in = new InputStreamReader(inFile);
 in.readLine();
 in.readLine();
 in.write(invalidContent.getBytes());
 in.close();
 inFile.close();
 }
 catch (FileNotFoundException e) {
 fail("Expected no exception but got " + e.getMessage());
 }
 this.frame.loadLandmarks();
 }

 @Test
 public void testFoxTableModel() {
 final FoxTableModel model = new FoxTableModel();
 assertEquals(2, model.getColumnCount());
 assertEquals(0, model.getRowCount());
 }
}