package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.controller.TailSizeController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.util.JTailLogger;

public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel jTailPanel;
 private JScrollBar verticalScrollBar;
 private JTable dataTable;
 private DefaultTableModel tableModel;

 @Before
 public void setUp() {
 model = new JTailModel();
 jTailPanel = new JTailPanel(model);
 verticalScrollBar = jTailPanel.getVerticalScrollBar();
 dataTable = jTailPanel.getDataTable();
 tableModel = (DefaultTableModel) dataTable.getModel();
 }

 @Test
 public void testJTailPanelConstructor() {
 JTailLogger.initLogging();
 assertEquals(model, jTailPanel.getModel());
 assertNotNull(verticalScrollBar);
 assertNotNull(dataTable);
 assertNotNull(tableModel);
 }

 @Test
 public void testGetCharacterHeight() {
 int characterHeight = jTailPanel.getCharacterHeight();
 assertTrue(characterHeight > 0);
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertNotNull(jTailPanel.getVerticalScrollBar());
 }

 @Test
 public void testUpdate() {
 JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, model, 1, 1, 1, 1, 1);
 jTailPanel.update(model, event);
 JScrollBar adjustedScrollBar = verticalScrollBar;
 assertEquals(event.firstLine, adjustedScrollBar.getValue());
 assertEquals(event.nbDisplayedLines, adjustedScrollBar.getExtent());
 assertEquals(event.maxLine, adjustedScrollBar.getMaximum());
 assertFalse(adjustedScrollBar.isEnabled());

 event = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, model, 1, 1, 1, 1, 1);
 jTailPanel.update(model, event);
 assertNull(event);
 }

 @Test
 public void testUpdateDataTable() {
 String lineNumbers = "1\n2\n3\n4\n5\n6";
 String lines = "line1\nline2\nline3\nline4\nline5\nline6";
 jTailPanel.updateDataTable(lineNumbers, lines);
 assertEquals(6, tableModel.getRowCount());
 for (int i = 0; i < 6; ++i) {
 String lineNumber = (String) tableModel.getValueAt(i, 0);
 String line = (String) tableModel.getValueAt(i, 1);
 assertEquals(Integer.toString(i + 1), lineNumber);
 assertEquals("line" + (i + 1), line);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testUpdateDataTableWithNullModel() {
 JTailPanel jTailPanelWithNullModel = new JTailPanel(null);
 jTailPanelWithNullModel.updateDataTable("1\n2\n3\n4\n5\n6", "line1\nline2\nline3\nline4\nline5\nline6");
 }

 @Test(expected = NullPointerException.class)
 public void testUpdateDataTableWithNullLines() {
 JTailPanel jTailPanelWithNullLines = new JTailPanel(model);
 jTailPanelWithNullLines.updateDataTable("1\n2\n3\n4\n5\n6", null);
 }
}