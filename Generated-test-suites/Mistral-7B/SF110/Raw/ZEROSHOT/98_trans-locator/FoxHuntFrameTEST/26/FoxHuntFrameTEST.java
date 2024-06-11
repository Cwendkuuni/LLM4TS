package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTable;

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
 frame = new FoxHuntFrame();
 }

 @Test
 public void testFoxHuntFrameConstructor() {
 assertEquals("Triangulation", frame.getTitle());
 assertNotNull(frame.getContentPane());
 assertTrue(frame.isVisible());
 assertEquals(3, frame.getDefaultCloseOperation());
 }

 @Test
 public void testActionPerformed() {
 ActionEvent event = new ActionEvent(new Object(), 0, "ActionPerformed");
 frame.actionPerformed(event);
 assertEquals(2, frame.getPoints().size());
 assertEquals(2, frame.getFox().size());
 }

 @Test
 public void testAddPoint() {
 JTextField theta = new JTextField("10");
 JTextField lat = new JTextField("40.744130");
 JTextField lon = new JTextField("-73.992880");
 frame.theta = theta;
 frame.lat = lat;
 frame.lon = lon;
 ActionEvent event = new ActionEvent(new Object(), 0, "ActionPerformed");
 frame.actionPerformed(event);
 TrigPoint expectedPoint = new TrigPoint(40.74413, -73.99288, 10);
 assertEquals(expectedPoint, frame.getPoints().lastElement());
 }

 @Test
 public void testLoadLandmarks() {
 assertFalse(frame.getDisplay().getLandmarks().isEmpty());
 }

 @Test
 public void testTableDataChanged() {
 FoxTableModel model = frame.getPosModel();
 JTable table = frame.getPos();
 table.getModel().fireTableDataChanged();
 assertTrue(model.getRowCount() > 0);
 }

 @Test
 public void testGettersAndSetters() {
 JTextField zoom = new JTextField("1");
 frame.setZoom(zoom);
 assertEquals(zoom, frame.getZoom());
 }

 @Test
 public void testFinalPrep() {
 assertTrue(frame.getPanel3() instanceof JPanel);
 assertTrue(frame.getPanel3().getComponentCount() > 0);
 }

 @Test
 public void testTextPrep() {
 assertTrue(frame.getPanel1() instanceof JPanel);
 assertTrue(frame.getPanel1().getComponentCount() > 0);
 }

 @Test
 public void testTablePrep() {
 assertTrue(frame.getPosModel() instanceof FoxTableModel);
 assertTrue(frame.getPos() instanceof JTable);
 }

 @Test
 public void testDisplayPrep() {
 assertTrue(frame.getDisplay() instanceof HuntDisplay);
 }
}