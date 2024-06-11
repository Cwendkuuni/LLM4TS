package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.util.JTailLogger;

public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel jTailPanel;

 @Before
 public void setUp() {
 model = new JTailModel();
 jTailPanel = new JTailPanel(model);
 }

 @Test
 public void testJTailPanelConstructor() {
 JTailLogger.reset(); // Reset the logger for testing purpose
 JTailPanel panel = new JTailPanel(model);
 assertTrue("JTailPanel was not added to the observer list", model.hasObserver(panel));
 }

 @Test
 public void testGetCharacterHeight() {
 final FontMetrics metrics = jTailPanel.dataTable.getFontMetrics(jTailPanel.dataTable.getFont());
 final int characterHeight = jTailPanel.getCharacterHeight();
 assertEquals("Character height should be equal to the metrics height minus one", metrics.getHeight() - 1, characterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertNotNull("Vertical scroll bar should not be null", jTailPanel.getVerticalScrollBar());
 }

 @Test
 public void testUpdate() {
 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 1, 1, 1, 1, ""));
 JTailLogger.reset(); // Reset the logger for testing purpose
 jTailPanel.update(model, new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 1, 1, 1, 1, ""));
 assertTrue("The logger should have logged a message", JTailLogger.getMessages().size() > 0);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testUpdateDataTableWithNullLinesArray() {
 String lineNumbers = "1\n2\n3\n";
 jTailPanel.updateDataTable(lineNumbers, null);
 }

 @Test
 public void testUpdateDataTable() {
 String lineNumbers = "1\n2\n3\n";
 String lines = "one\ntwo\nthree\n";
 jTailPanel.updateDataTable(lineNumbers, lines);
 final int numRows = jTailPanel.dataTable.getRowCount();
 assertEquals("The number of rows should be equal to the number of lines", 3, numRows);
 }

 @Test
 public void testScrollBarAdjustmentController() {
 final ScrollBarAdjustmentController controller = new ScrollBarAdjustmentController(jTailPanel, model);
 controller.valueChanged(new AdjustmentEvent());
 JTailLogger.reset(); // Reset the logger for testing purpose
 jTailPanel.update(model, new JTailModelEvent(JTailModelEvent.Type.SCROLLED));
 assertTrue("The logger should have logged a message", JTailLogger.getMessages().size() > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateDataTableWithNullLineNumbers() {
 jTailPanel.updateDataTable(null, lines);
 }
}