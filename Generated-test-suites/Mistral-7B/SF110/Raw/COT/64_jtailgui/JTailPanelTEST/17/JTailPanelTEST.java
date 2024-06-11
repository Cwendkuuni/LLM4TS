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
 JTailLogger.reset(); // Reset logger for testing
 JTailPanel jtp = new JTailPanel(model);
 assertTrue(jtp instanceof JPanel);
 assertTrue(jtp instanceof Observer);
 assertNotNull(jtp.getVerticalScrollBar());
 assertNotNull(jtp.getDataTable());
 assertNotNull(jtp.getTableModel());
 JTailLogger.assertTrue(JTailLogger.getDebugLog().isEmpty());
 }

 @Test
 public void testGetCharacterHeight() {
 final int expectedCharacterHeight = 15; // Assuming default font height is 15 pixels
 assertEquals(expectedCharacterHeight, jTailPanel.getCharacterHeight());
 }

 @Test
 public void testGetVerticalScrollBar() {
 final JScrollBar expectedVerticalScrollBar = jTailPanel.getVerticalScrollBar();
 assertSame(expectedVerticalScrollBar, jTailPanel.getVerticalScrollBar());
 }

 @Test
 public void testUpdate() {
 model.notifyObservers(new JTailModelEvent(JTailModelEvent.Type.RESIZED, 10, 10, 20));
 JTailLogger.reset();
 jTailPanel.update(model, new JTailModelEvent(JTailModelEvent.Type.RESIZED, 10, 10, 20));
 JTailLogger.assertTrue(JTailLogger.getDebugLog().size() > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateWithNullModel() {
 jTailPanel.update(null, new JTailModelEvent(JTailModelEvent.Type.RESIZED, 10, 10, 20));
 }

 @Test
 public void testUpdateWithNullEvent() {
 jTailPanel.update(model, null);
 }

 @Test
 public void testUpdateWithInvalidEventType() {
 jTailPanel.update(model, new JTailModelEvent(JTailModelEvent.Type.INVALID, 10, 10, 20));
 }

 @Test
 public void testUpdateDataTable() {
 final String expectedLineNumbers = "1\n2\n3\n4\n5";
 final String expectedLines = "line1\nline2\nline3\nline4\nline5";
 String lineNumbers = "1\n2\n3\n4\n5";
 String lines = "line1\nline2\nline3\nline4\nline5";
 JTailLogger.reset();
 jTailPanel.updateDataTable(lineNumbers, lines);
 JTailLogger.assertTrue(JTailLogger.getDebugLog().size() > 0);
 assertEquals(expectedLineNumbers, jTailPanel.getModel().getLineNumbers());
 assertEquals(expectedLines, jTailPanel.getDataTable().getValueAt(0, 1).toString());
 }

 @Test
 public void testUpdateDataTableWithNullLineNumbers() {
 String lineNumbers = null;
 String lines = "line1\nline2\nline3\nline4\nline5";
 jTailPanel.updateDataTable(lineNumbers, lines);
 assertTrue(jTailPanel.getDataTable().getRowCount() == 0);
 }

 @Test
 public void testUpdateDataTableWithNullLines() {
 String lineNumbers = "1\n2\n3\n4\n5";
 String lines = null;
 jTailPanel.updateDataTable(lineNumbers, lines);
 assertTrue(jTailPanel.getDataTable().getRowCount() == 0);
 }
}