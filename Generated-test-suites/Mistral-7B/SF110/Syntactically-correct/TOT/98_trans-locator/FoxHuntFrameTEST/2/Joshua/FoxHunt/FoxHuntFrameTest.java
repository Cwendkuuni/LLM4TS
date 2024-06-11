import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.Landmark;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 this.frame = new FoxHuntFrame();
 }

 @Test
 public void testFoxHuntFrameConstruction() {
 // Test that the frame is constructed correctly and visible
 assertEquals("Triangulation", this.frame.getTitle());
 assertNotNull(this.frame.gettha());
 assertNotNull(this.frame.getlat());
 assertNotNull(this.frame.getlon());
 assertNotNull(this.frame.getzoom());
 assertNotNull(this.frame.getadd());
 assertNotNull(this.frame.getpos());
 assertNotNull(this.frame.getposModel());
 assertNotNull(this.frame.getdisplay());
 }

 @Test
 public void testAddPointWithValidInput() {
 // Test adding a point with valid input
 this.frame.theta.setText("45");
 this.frame.lat.setText("51.503985");
 this.frame.lon.setText("-0.088230");
 this.frame.add.doClick();

 // Check that the point was added to the list
 assertEquals(1, this.frame.points.size());
 // Check that the point was added to the display
 assertEquals(1, this.frame.display.getPoints().size());
 }

 @Test
 public void testAddPointWithInvalidInput() {
 // Test adding a point with invalid input
 this.frame.theta.setText("abc");
 this.frame.lat.setText("x.y.z");
 this.frame.lon.setText("ddd.mmm.mmm");
 this.frame.add.doClick();

 // Check that no point was added
 assertEquals(0, this.frame.points.size());
 // Check that no point was added to the display
 assertEquals(0, this.frame.display.getPoints().size());
 }

 @Test
 public void testAddPointWithDuplicateInput() {
 // Test adding a point with duplicate input
 this.frame.theta.setText("45");
 this.frame.lat.setText("51.503985");
 this.frame.lon.setText("-0.088230");
 this.frame.add.doClick();
 this.frame.theta.setText("45");
 this.frame.lat.setText("51.503985");
 this.frame.lon.setText("-0.088230");
 this.frame.add.doClick();

 // Check that only one point was added
 assertEquals(1, this.frame.points.size());
 // Check that only one point was added to the display
 assertEquals(1, this.frame.display.getPoints().size());
 }

 @Test
 public void testAddPointWithInvalidAngle() {
 // Test adding a point with an invalid angle
 this.frame.theta.setText("-180");
 this.frame.lat.setText("51.503985");
 this.frame.lon.setText("-0.088230");
 this.frame.add.doClick();

 // Check that no point was added
 assertEquals(0, this.frame.points.size());
 // Check that no point was added to the display
 assertEquals(0, this.frame.display.getPoints().size());
 }

 @Test
 public void testAddPointWithInvalidLatitude() {
 // Test adding a point with an invalid latitude
 this.frame.theta.setText("45");
 this.frame.lat.setText("x.y.z");
 this.frame.lon.setText("-0.088230");
 this.frame.add.doClick();

 // Check that no point was added
 assertEquals(0, this.frame.points.size());
 // Check that no point was added to the display
 assertEquals(0, this.frame.display.getPoints().size());
 }

 @Test
 public void testAddPointWithInvalidLongitude() {
 // Test adding a point with an invalid longitude
 this.frame.theta.setText("45");
 this.frame.lat.setText("51.503985");
 this.frame.lon.setText("ddd.mmm.mmm");
 this.frame.add.doClick();

 // Check that no point was added
 assertEquals(0, this.frame.points.size());
 // Check that no point was added to the display
 assertEquals(0, this.frame.display.getPoints().size());
 }

 @Test
 public void testDisplayTableModel() {
 // Test that the table model displays the correct data
 this.frame.addPoint();
 this.frame.addPoint();
 this.frame.theta.setText("45");
 this.frame.lat.setText("51.503985");
 this.frame.lon.setText("-0.088230");
 this.frame.add.doClick();
 this.frame.addPoint();
 this.frame.theta.setText("315");
 this.frame.lat.setText("51.503985");
 this.frame.lon.setText("-0.088230");
 this.frame.add.doClick();

 // Check that the table has the correct number of rows
 assertEquals(4, this.frame.pos.getRowCount());

 // Check that the table displays the correct data
 assertEquals("51.503985", this.frame.posModel.getValueAt(0, 0).toString());
 assertEquals("-0.088230", this.frame.posModel.getValueAt(0, 1).toString());
 assertEquals("51.503985", this.frame.posModel.getValueAt(1, 0).toString());
 assertEquals("-0.088230", this.frame.posModel.getValueAt(1, 1).toString());
 assertEquals("51.503985", this.frame.posModel.getValueAt(2, 0).toString());
 assertEquals("-0.088230", this.frame.posModel.getValueAt(2, 1).toString());
 assertEquals("51.503985", this.frame.posModel.getValueAt(3, 0).toString());
 assertEquals("-0.088230", this.frame.posModel.getValueAt(3, 1).toString());
 }

 @Test
 public void testLoadLandmarks() {
 // Test loading landmarks from a file
 this.frame.loadLandmarks();

 // Check that the number of landmarks loaded matches the number in the file
 assertEquals(5, this.frame.display.getLandmarks().size());
 }
}