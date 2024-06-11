package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.FoxHuntFrame;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Test
 public void testFoxHuntFrameConstruction() {
 frame = new FoxHuntFrame();
 assertNotNull(frame.getTheta());
 assertNotNull(frame.getLat());
 assertNotNull(frame.getLon());
 assertNotNull(frame.getZoom());
 assertNotNull(frame.getAdd());
 assertNotNull(frame.getPos());
 assertNotNull(frame.getDisplay());
 assertNotNull(frame.getPanel1());
 assertNotNull(frame.getPanel2());
 assertNotNull(frame.getPanel3());
 assertNotNull(frame.getPoints());
 assertNotNull(frame.getFox());
 }

 @Test
 public void testAddPoint() {
 frame = new FoxHuntFrame();
 frame.setTextPrep();
 frame.getText().setText("10");
 frame.getLat().setText("51.5074");
 frame.getLon().setText("-0.0898");
 frame.getActionEvent().setSource(new JButton("Add It!!!"));
 frame.actionPerformed(frame.getActionEvent());
 assertTrue(frame.getPoints().size() > 0);
 }

 @Test
 public void testAddPointWithExistingPoints() {
 frame = new FoxHuntFrame();
 frame.setTextPrep();
 frame.getText().setText("10");
 frame.getLat().setText("51.5074");
 frame.getLon().setText("-0.0898");
 frame.getActionEvent().setSource(new JButton("Add It!!!"));
 frame.actionPerformed(frame.getActionEvent());
 frame.getText().setText("20");
 frame.getLat().setText("52.0074");
 frame.getLon().setText("-1.0898");
 frame.getActionEvent().setSource(new JButton("Add It!!!"));
 frame.actionPerformed(frame.getActionEvent());
 assertTrue(frame.getPoints().size() > 1);
 }

 @Test
 public void testStripLeadingHyphensFromPointCoordinates() {
 FoxTableModel model = new FoxTableModel();
 FoxHuntFrame frame = new FoxHuntFrame();
 frame.setTextPrep();
 frame.getAdd().addActionListener(frame);
 frame.actionPerformed(frame.getActionEvent());
 TrigPoint point = (TrigPoint) frame.getPoints().lastElement();
 assertEquals("-51.5074", model.getValueAt(0, 0).toString());
 assertEquals("-0.0898", model.getValueAt(0, 1).toString());
 frame.getText().setText("--51.5074");
 frame.getLat().setText("--52.0074");
 frame.getLon().setText("--0.0898");
 frame.getActionEvent().setSource(new JButton("Add It!!!"));
 frame.actionPerformed(frame.getActionEvent());
 assertEquals("-51.5074", model.getValueAt(1, 0).toString());
 assertEquals("-0.0898", model.getValueAt(1, 1).toString());
 }

 @Test
 public void testParseLandmarksFile() {
 frame = new FoxHuntFrame();
 frame.displayPrep();
 try {
 frame.loadLandmarks();
 assertTrue(frame.getDisplay().getLandmarks().size() > 0);
 } catch (FileNotFoundException e) {
 fail("Expected file to be found, but it was not.");
 } catch (IOException e) {
 fail("Expected file to be readable, but it was not.");
 }
 }

}