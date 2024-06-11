package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import Joshua.FoxHunt.*;
import org.junit.*;

import java.util.*;

public class FoxHuntFrameTest {

 private FoxHuntFrame fhf;

 @Before
 public void setUp() {
 fhf = new FoxHuntFrame();
 }

 @Test
 public void testFoxHuntFrameConstructor() {
 FoxHuntFrame fhf = new FoxHuntFrame();
 assertNotNull(fhf);
 }

 @Test
 public void testAddPointMethod() {
 // Basic test case
 String thetaText = "10";
 String latText = "32.123456";
 String lonText = "112.123456";
 fhf.theta.setText(thetaText);
 fhf.lat.setText(latText);
 fhf.lon.setText(lonText);
 fhf.add.doClick();

 // Check if point has been added to points vector
 assertEquals(1, fhf.points.size());

 // Check if point has been added to fox vector
 assertEquals(1, fhf.fox.size());

 // Check if table has been updated
 int numRows = fhf.pos.getRowCount();
 assertEquals(1, numRows);
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointMethodWithInvalidThetaText() {
 String thetaText = "";
 String latText = "32.123456";
 String lonText = "112.123456";
 fhf.theta.setText(thetaText);
 fhf.lat.setText(latText);
 fhf.lon.setText(lonText);
 fhf.add.doClick();
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointMethodWithInvalidLatText() {
 String thetaText = "10";
 String latText = "";
 String lonText = "112.123456";
 fhf.theta.setText(thetaText);
 fhf.lat.setText(latText);
 fhf.lon.setText(lonText);
 fhf.add.doClick();
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointMethodWithInvalidLonText() {
 String thetaText = "10";
 String latText = "32.123456";
 String lonText = "";
 fhf.theta.setText(thetaText);
 fhf.lat.setText(latText);
 fhf.lon.setText(lonText);
 fhf.add.doClick();
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointMethodWithInvalidTheta() {
 String thetaText = "abc";
 String latText = "32.123456";
 String lonText = "112.123456";
 fhf.theta.setText(thetaText);
 fhf.lat.setText(latText);
 fhf.lon.setText(lonText);
 fhf.add.doClick();
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointMethodWithInvalidLat() {
 String thetaText = "10";
 String latText = "abc";
 String lonText = "112.123456";
 fhf.theta.setText(thetaText);
 fhf.lat.setText(latText);
 fhf.lon.setText(lonText);
 fhf.add.doClick();
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointMethodWithInvalidLon() {
 String thetaText = "10";
 String latText = "32.123456";
 String lonText = "abc";
 fhf.theta.setText(thetaText);
 fhf.lat.setText(latText);
 fhf.lon.setText(lonText);
 fhf.add.doClick();
 }

 @Test
 public void testAddPointMethodWithPointsVectorSizeGreaterThanOne() {
 String thetaText = "20";
 String latText = "33.123456";
 String lonText = "113.123456";
 fhf.theta.setText(thetaText);
 fhf.lat.setText(latText);
 fhf.lon.setText(lonText);
 fhf.add.doClick();

 // Check if points vector has size 2
 assertEquals(2, fhf.points.size());

 // Check if fox vector has size 2
 assertEquals(2, fhf.fox.size());

 // Check if table has been updated with 2 rows
 int numRows = fhf.pos.getRowCount();
 assertEquals(2, numRows);
 }

 @Test
 public void testLoadLandmarksMethod() {
 // Stub the loadLandmarks method as it uses external file.
 fhf.loadLandmarks = (file) -> {};

 // Call loadLandmarks method
 fhf.loadLandmarks(null);
 }
}