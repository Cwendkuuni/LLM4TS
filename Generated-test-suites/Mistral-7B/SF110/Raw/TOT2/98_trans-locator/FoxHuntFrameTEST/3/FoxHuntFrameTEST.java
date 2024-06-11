import org.junit.Test;
import static org.junit.Assert.*;
import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.FoxTableModel;
import javax.swing.table.DefaultTableModel;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Test
 public void testFoxHuntFrameConstructor() {
 frame = new FoxHuntFrame();
 assertNotNull(frame);
 assertEquals("Triangulation", frame.getTitle());
 }

 @Test
 public void testActionPerformed() {
 frame = new FoxHuntFrame();
 frame.theta.setText("45");
 frame.lat.setText("51.5074");
 frame.lon.setText("0.1278");
 frame.add.doClick();
 assertFalse(frame.points.isEmpty());
 assertFalse(frame.fox.isEmpty());
 }

 @Test
 public void testAddPointWithInvalidTheta() {
 frame = new FoxHuntFrame();
 frame.theta.setText("A");
 frame.lat.setText("51.5074");
 frame.lon.setText("0.1278");
 frame.add.doClick();
 assertTrue(frame.points.isEmpty());
 assertFalse(frame.fox.isEmpty());
 }

 @Test
 public void testAddPointWithInvalidLat() {
 frame = new FoxHuntFrame();
 frame.theta.setText("45");
 frame.lat.setText("A");
 frame.lon.setText("0.1278");
 frame.add.doClick();
 assertTrue(frame.points.isEmpty());
 assertFalse(frame.fox.isEmpty());
 }

 @Test
 public void testAddPointWithInvalidLon() {
 frame = new FoxHuntFrame();
 frame.theta.setText("45");
 frame.lat.setText("51.5074");
 frame.lon.setText("A");
 frame.add.doClick();
 assertTrue(frame.points.isEmpty());
 assertFalse(frame.fox.isEmpty());
 }

 @Test
 public void testAddTwoPoints() {
 frame = new FoxHuntFrame();
 frame.theta.setText("45");
 frame.lat.setText("51.5074");
 frame.lon.setText("0.1278");
 frame.add.doClick();
 frame.theta.setText("35");
 frame.lat.setText("52.0123");
 frame.lon.setText("0.1417");
 frame.add.doClick();
 assertTrue(!frame.points.isEmpty());
 assertTrue(!frame.fox.isEmpty());
 }

 @Test
 public void testLoadLandmarks() {
 frame = new FoxHuntFrame();
 frame.loadLandmarks();
 assertFalse(frame.display.getLandmarks().isEmpty());
 }

 @Test
 public void testError() {
 frame = new FoxHuntFrame();
 frame.error("Test Error");
 }

 @Test
 public void testFoxTableModel() {
 FoxTableModel model = new FoxTableModel();
 Object[][] data = { { 51.5074, 0.1278 }, { 52.0123, 0.1417 } };
 DefaultTableModel dtm = new DefaultTableModel(data, FoxTableModel.colNames);
 assertEquals(dtm.getColumnCount(), FoxTableModel.colNames.length);
 assertEquals(dtm.getRowCount(), 2);
 }

}