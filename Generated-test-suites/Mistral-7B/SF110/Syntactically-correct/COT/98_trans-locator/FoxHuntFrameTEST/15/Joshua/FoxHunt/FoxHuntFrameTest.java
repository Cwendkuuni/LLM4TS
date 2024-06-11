package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 this.frame = new FoxHuntFrame();
 }

 @Test
 public void testFoxHuntFrameConstructor() {
 assertEquals("Triangulation", this.frame.getTitle());
 assertNotNull(this.frame.points);
 assertNotNull(this.frame.fox);
 }

 @Test
 public void testActionPerformed() {
 final ActionEvent event = new ActionEvent(new Object(), 0, "ActionEvent");
 this.frame.actionPerformed(event);
 assertTrue(this.frame.points.size() > 0);
 assertTrue(this.frame.fox.size() > 0);
}

 @Test
 public void testAddPointWithValidData() {
 this.frame.theta.setText("45");
 this.frame.lat.setText("51.505555");
 this.frame.lon.setText("-0.123456");
 this.frame.add.doClick();

 final TrigPoint expectedPoint = new TrigPoint(51.505555, -0.123456, 45);
 assertTrue(this.frame.points.contains(expectedPoint));
 assertNotNull(this.frame.display.getComponentAt(0, 0));
}

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLatData() {
 this.frame.theta.setText("45");
 this.frame.lat.setText("dddd");
 this.frame.lon.setText("-0.123456");
 this.frame.add.doClick();
}

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLonData() {
 this.frame.theta.setText("45");
 this.frame.lat.setText("51.505555");
 this.frame.lon.setText("ddddd");
 this.frame.add.doClick();
}

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidThetaData() {
 this.frame.theta.setText("dddd");
 this.frame.lat.setText("51.505555");
 this.frame.lon.setText("-0.123456");
 this.frame.add.doClick();
}

 @Test
 public void testLoadLandmarks() {
 this.frame.loadLandmarks();
 assertTrue(this.frame.display.getLandmarkCount() > 0);
}

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksWithFileNotFoundException() {
 this.frame.loadLandmarks();
 this.frame.error("Can't Find the file");
}

 @Test(expected = IOException.class)
 public void testLoadLandmarksWithIOException() {
 this.frame.loadLandmarks();
 this.frame.error("Error communicating with the file");
}

 @Test
 public void testGetColumnCount() {
 final FoxTableModel model = this.frame.posModel;
 assertEquals(2, model.getColumnCount());
}

 @Test
 public void testGetRowCount() {
 final FoxTableModel model = this.frame.posModel;
 assertEquals(this.frame.fox.size(), model.getRowCount());
}

 @Test
 public void testGetColumnName() {
 final FoxTableModel model = this.frame.posModel;
 assertEquals("Lattitude", model.getColumnName(0));
 assertEquals("Longitude", model.getColumnName(1));
}

 @Test
 public void testGetValueAt() {
 final FoxTableModel model = this.frame.posModel;
 final TrigPoint point = (TrigPoint) this.frame.fox.lastElement();
 final DecimalFormat formatter = new DecimalFormat("###.##############");
 assertEquals(formatter.format(point.getY()), model.getValueAt(model.getRowCount() - 1, 0));
 assertEquals(formatter.format(point.getX()), model.getValueAt(model.getRowCount() - 1, 1));
}

 @Test
 public void testGetColumnClass() {
 final FoxTableModel model = this.frame.posModel;
 assertEquals(String.class, model.getColumnClass(0));
 assertEquals(String.class, model.getColumnClass(1));
}

 @Test
 public void testIsCellEditable() {
 final FoxTableModel model = this.frame.posModel;
 assertFalse(model.isCellEditable(0, 0));
 assertFalse(model.isCellEditable(0, 1));
}

 @Test
 public void testErrorDialog() {
 this.frame.error("Error Message");
}
}