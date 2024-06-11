import org.junit.Test;
import static org.junit.Assert.*;
import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.FoxTableModel;

public class FoxHuntFrameTest {

 @Test
 public void testFoxHuntFrameConstructor() {
 FoxHuntFrame frame = new FoxHuntFrame();
 assertNotNull(frame);
 }

 @Test
 public void testTextPrep() {
 FoxHuntFrame frame = new FoxHuntFrame();
 assertEquals(3, frame.theta.getText().length());
 assertEquals(9, frame.lat.getText().length());
 assertEquals(10, frame.lon.getText().length());
 }

 @Test
 public void testTablePrep() {
 FoxHuntFrame frame = new FoxHuntFrame();
 assertNotNull(frame.posModel);
 assertNotNull(frame.pos);
 }

 @Test
 public void testAddPointMethodNormalScenario() {
 FoxHuntFrame frame = new FoxHuntFrame();
 frame.theta.setText("45");
 frame.lat.setText("51.502149,-0.086147");
 frame.lon.setText("-0.123145,52.405016");
 frame.add.doClick();
 assertEquals(1, frame.points.size());
 assertEquals(1, frame.fox.size());
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointMethodInvalidTheta() {
 FoxHuntFrame frame = new FoxHuntFrame();
 frame.theta.setText("abc");
 frame.lat.setText("51.502149,-0.086147");
 frame.lon.setText("2.34,52.405016");
 frame.add.doClick();
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointMethodInvalidLat() {
 FoxHuntFrame frame = new FoxHuntFrame();
 frame.theta.setText("45");
 frame.lat.setText("abcd");
 frame.lon.setText("2.34,52.405016");
 frame.add.doClick();
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPointMethodInvalidLon() {
 FoxHuntFrame frame = new FoxHuntFrame();
 frame.theta.setText("45");
 frame.lat.setText("51.502149,-0.086147");
 frame.lon.setText("2.34,abcd");
 frame.add.doClick();
 }

 @Test
 public void testAddPointMethodEdgeCase() {
 FoxHuntFrame frame = new FoxHuntFrame();
 frame.theta.setText("180");
 frame.lat.setText("51.502149,-0.086147");
 frame.lon.setText("-180,52.405016");
 frame.add.doClick();
 assertEquals(1, frame.points.size());
 assertEquals(1, frame.fox.size());
 }

 @Test
 public void testDisplayPrep() {
 FoxHuntFrame frame = new FoxHuntFrame();
 frame.loadLandmarks();
 assertNotNull(frame.display);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksMethodNoFile() {
 FoxHuntFrame frame = new FoxHuntFrame();
 frame.loadLandmarks();
 }

 @Test(expected = IOException.class)
 public void testLoadLandmarksMethodIOError() {
 FoxHuntFrame frame = new FoxHuntFrame();
 frame = mock(FoxHuntFrame.class);
 when(frame.loadLandmarks()).thenThrow(new IOException());
 }

 @Test
 public void testLoadLandmarksMethodValidFile() {
 FoxHuntFrame frame = new FoxHuntFrame();
 frame = spy(FoxHuntFrame.class);
 doNothing().when(frame).error("Can't Find the file");
 doNothing().when(frame).error("Error communicating with the file");
 frame.loadLandmarks();
 verify(frame, times(2)).error(anyString());
 }
}