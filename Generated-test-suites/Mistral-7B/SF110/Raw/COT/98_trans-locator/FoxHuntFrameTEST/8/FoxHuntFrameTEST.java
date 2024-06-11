package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;
 private FoxTableModel tableModel;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 tableModel = (FoxTableModel) frame.posModel;
 }

 @Test
 public void testConstructor() {
 assertEquals("Triangulation", frame.getTitle());
 assertNotNull(frame.panel);
 assertNotNull(frame.pos);
 assertNotNull(frame.display);
 }

 @Test
 public void testActionPerformed() {
 ActionEvent event = new ActionEvent(new Object(), 0, "ActionEvent");
 frame.actionPerformed(event);
 assertEquals(1, frame.points.size());
 assertEquals(1, frame.fox.size());
}

 @Test
 public void testAddPointWithValidValues() {
 theta.setText("45");
 lat.setText("51.5069");
 lon.setText("-0.1278");
 frame.actionPerformed(new ActionEvent(new Object(), 0, "ActionEvent"));
 assertEquals(2, frame.points.size());
 assertEquals(1, frame.fox.size());
}

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidTheta() {
 theta.setText("ABC");
 lat.setText("51.5069");
 lon.setText("-0.1278");
 frame.actionPerformed(new ActionEvent(new Object(), 0, "ActionEvent"));
 fail("NumberFormatException expected but not thrown");
}

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLat() {
 theta.setText("45");
 lat.setText("ABC.DEF.GHI");
 lon.setText("-0.1278");
 frame.actionPerformed(new ActionEvent(new Object(), 0, "ActionEvent"));
 fail("NumberFormatException expected but not thrown");
}

 @Test(expected = NumberFormatException.class)
 public void testAddPointWithInvalidLon() {
 theta.setText("45");
 lat.setText("51.5069");
 lon.setText("ABC.DEF.GHI");
 frame.actionPerformed(new ActionEvent(new Object(), 0, "ActionEvent"));
 fail("NumberFormatException expected but not thrown");
}

 @Test
 public void testFinalPrep() {
 assertNotNull(frame.panel3);
 assertNotNull(frame.display);
 assertNotNull(frame.panel);
 assertNotNull(frame.pos);
 assertNotNull(frame.posModel);
}

 @Test
 public void testLoadLandmarks() {
 assertEquals(10, tableModel.getRowCount());
 for (int i = 0; i < 10; i++) {
 Landmark landmark = frame.display.getLandmarks().elementAt(i);
 assertEquals("landmark" + (i + 1) + ".png", landmark.getIcon().toString());
 }
}

 @Test(expected = FileNotFoundException.class)
 public void testLoadLandmarksWithMissingFile() {
 frame.loadLandmarks();
 fail("FileNotFoundException expected but not thrown");
}

 @Test(expected = IOException.class)
 public void testLoadLandmarksWithIOException() {
 frame.loadLandmarks();
 fail("IOException expected but not thrown");
}
}