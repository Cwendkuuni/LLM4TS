package Joshua.FoxHunt.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import org.junit.Test;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Test
 public void testFoxHuntFrameConstruction() {
 frame = new FoxHuntFrame();
 assertNotNull(frame.getContentPane());
 assertNotNull(frame.getJTable());
 assertNotNull(frame.getJTextField_theta());
 assertNotNull(frame.getJTextField_lat());
 assertNotNull(frame.getJTextField_lon());
 }

 @Test
 public void testAddPointMethod() {
 frame = new FoxHuntFrame();
 frame.getJTextField_theta().setText("45");
 frame.getJTextField_lat().setText("51.5072");
 frame.getJTextField_lon().setText("0.1278");
 frame.actionPerformed(new ActionEvent(frame.getJButton_add(), ActionEvent.ACTION_PERFORMED, null));

 Vector<TrigPoint> points = frame.points;
 Vector<TrigPoint> fox = frame.fox;
 TrigPoint expectedPoint = new TrigPoint(51.5072, 0.1278, 45);
 assertEquals(1, points.size());
 assertEquals(expectedPoint, points.get(0));
 TrigPoint expectedFox = FoxCalc.calcFox(expectedPoint, frame.points.get(0));
 assertEquals(1, fox.size());
 assertEquals(expectedFox, fox.get(0));
 }

 @Test
 public void testDisplayAddPointMethod() {
 frame = new FoxHuntFrame();
 frame.getJTextField_theta().setText("45");
 frame.getJTextField_lat().setText("51.5072");
 frame.getJTextField_lon().setText("0.1278");
 frame.actionPerformed(new ActionEvent(frame.getJButton_add(), ActionEvent.ACTION_PERFORMED, null));

 HuntDisplay display = frame.display;
 assertEquals(1, display.getPoints().size());
 TrigPoint expectedPoint = new TrigPoint(51.5072, 0.1278, 45);
 assertEquals(expectedPoint, display.getPoints().get(0));
 }

 @Test
 public void testTableDataChangedMethod() {
 frame = new FoxHuntFrame();
 FoxTableModel tableModel = (FoxTableModel) frame.getJTable().getModel();

 frame.getJTextField_theta().setText("45");
 frame.getJTextField_lat().setText("51.5072");
 frame.getJTextField_lon().setText("0.1278");
 frame.actionPerformed(new ActionEvent(frame.getJButton_add(), ActionEvent.ACTION_PERFORMED, null));

 int initialRowCount = tableModel.getRowCount();
 frame.getJTextField_theta().setText("30");
 frame.getJTextField_lat().setText("51.6072");
 frame.getJTextField_lon().setText("0.1378");
 frame.actionPerformed(new ActionEvent(frame.getJButton_add(), ActionEvent.ACTION_PERFORMED, null));

 assertEquals(2, tableModel.getRowCount());
 }

 @Test
 public void testLoadLandmarksMethod() {
 frame = new FoxHuntFrame();
 frame.loadLandmarks();

 HuntDisplay display = frame.display;
 assertEquals(5, display.getLandmarks().size());
 }
}