package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 }

 @Test
 public void testConstructor() {
 assertEquals("Triangulation", frame.getTitle());
 assertNotNull(frame.getTable());
 assertNotNull(frame.getDisplay());
 }

 @Test
 public void testAddPoint() {
 frame.theta.setText("45");
 frame.lat.setText("40.1234");
 frame.lon.setText("-74.5678");
 frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));

 TrigPoint expectedPoint = new TrigPoint(40.1234, -74.5678, 45);
 TrigPoint actualPoint = (TrigPoint) frame.points.lastElement();
 assertEquals(expectedPoint.getX(), actualPoint.getX(), 0.01);
 assertEquals(expectedPoint.getY(), actualPoint.getY(), 0.01);

 TrigPoint expectedFox = FoxCalc.calcFox(expectedPoint, (TrigPoint) frame.points.elementAt(frame.points.size() - 2));
 TrigPoint actualFox = (TrigPoint) frame.fox.lastElement();
 assertEquals(expectedFox.getX(), actualFox.getX(), 0.01);
 assertEquals(expectedFox.getY(), actualFox.getY(), 0.01);
 }

 @Test
 public void testTableDataChanged() {
 FoxTableModel model = frame.posModel;
 assertFalse(model.fireTableDataChanged(0, 0, 1, 0));
 assertFalse(model.fireTableDataChanged(0, 1, 1, 0));
 assertFalse(model.fireTableDataChanged(0, 0, 0, 1));
 assertFalse(model.fireTableDataChanged(0, 1, 0, 1));

 frame.addPoint();
 model.fireTableDataChanged(frame.points.size() - 1, 0, 1, 0);
 model.fireTableDataChanged(frame.points.size() - 1, 1, 1, 0);
 model.fireTableDataChanged(frame.points.size() - 1, 0, 0, 1);
 model.fireTableDataChanged(frame.points.size() - 1, 1, 0, 1);

 TrigPoint point = (TrigPoint) frame.points.lastElement();
 TrigPoint fox = (TrigPoint) frame.fox.lastElement();
 assertEquals(point.getX(), model.getValueAt(frame.points.size() - 1, 0), 0.01);
 assertEquals(point.getY(), model.getValueAt(frame.points.size() - 1, 1), 0.01);
 assertEquals(fox.getX(), model.getValueAt(frame.points.size() - 1, 2), 0.01);
 assertEquals(fox.getY(), model.getValueAt(frame.points.size() - 1, 3), 0.01);
 }
}