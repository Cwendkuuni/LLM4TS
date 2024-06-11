package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.controller.JTailSizeController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.util.JTailLogger;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.junit.Before;
import org.junit.Test;

public class JTailPanelTest {

 private JTailPanel jTailPanel;
 private JScrollBar scrollBar;
 private DefaultTableModel tableModel;
 private JTable dataTable;
 private JScrollPane scrollPane;
 private JTailModel model;
 private JFrame frame;

 @Before
 public void setup() {
 model = mock(JTailModel.class);
 tableModel = new DefaultTableModel(new String[]{"#", ""}, 0);
 dataTable = new JTable(tableModel);
 scrollPane = new JScrollPane(dataTable);
 scrollBar = mock(JScrollBar.class);
 jTailPanel = new JTailPanel(model);
 JTailLogger mockLogger = mock(JTailLogger.class);
 when(model.getLogger()).thenReturn(mockLogger);
 jTailPanel.setLogger(mockLogger);
 frame = new JFrame();
 }

 @Test
 public void testBuildingJTailPanel() {
 // Testing the construction of JTailPanel
 assertNotNull(jTailPanel);
 assertNotNull(jTailPanel.model);
 assertNotNull(jTailPanel.scrollPane);
 assertNotNull(jTailPanel.verticalScrollBar);
 assertNotNull(jTailPanel.dataTable);
 assertNotNull(jTailPanel.tableModel);
 }

 @Test
 public void testGetCharacterHeight() {
 int characterHeight = jTailPanel.getCharacterHeight();
 assertTrue(characterHeight > 0);
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertNotNull(jTailPanel.getVerticalScrollBar());
 assertSame(jTailPanel.verticalScrollBar, scrollBar);
 }

 @Test
 public void testUpdateWhenModelIsResized() {
 // Setting up the model for testing model resized event
 when(model.getLineNumbers()).thenReturn("1\n2\n3\n4\n5");
 when(model.getContent()).thenReturn("Line 1\nLine 2\nLine 3\nLine 4\nLine 5");
 when(model.getType()).thenReturn(JTailModelEvent.Type.RESIZED);

 // Triggering the update method
 jTailPanel.update(model, new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, 1, 5, 0));

 // Testing the scrollbar values after the update
 TableModel tableModel = jTailPanel.dataTable.getModel();
 TableModelEvent tableModelEvent = new TableModelEvent(tableModel, TableModelEvent.HEAD_ROW);
 int firstLine = jTailPanel.model.getFirstLine();
 int maxLine = jTailPanel.model.getMaxLine();
 int nbDisplayedLines = jTailPanel.model.getNbDisplayedLines();

 assertEquals("Checking the first line value", firstLine, 1);
 assertEquals("Checking the max line value", maxLine, 5);
 assertEquals("Checking the number of displayed lines", nbDisplayedLines, 5);
 assertEquals("Checking the number of rows in the table model", tableModel.getRowCount(), 5);

 // Testing the scrollbar values after the update
 assertEquals("Checking the minimum value", jTailPanel.verticalScrollBar.getModel().getMinimum(), 0);
 assertEquals("Checking the value", jTailPanel.verticalScrollBar.getModel().getValue(), 1);
 assertEquals("Checking the extent", jTailPanel.verticalScrollBar.getModel().getExtent(), 1);
 assertEquals("Checking the maximum value", jTailPanel.verticalScrollBar.getModel().getMaximum(), 4);
 }

 @Test
 public void testUpdateWhenModelIsScrolled() {
 // Setting up the model for testing model scrolled event
 when(model.getLineNumbers()).thenReturn("1\n2\n3\n4\n5");
 when(model.getContent()).thenReturn("Line 1\nLine 2\nLine 3\nLine 4\nLine 5");
 when(model.getType()).thenReturn(JTailModelEvent.Type.SCROLLED);

 // Triggering the update method
 jTailPanel.update(model, new JTailModelEvent(model, JTailModelEvent.Type.SCROLLED, 2, 5, 1));

 // Testing the table content after the update
 assertEquals("Checking the table content", jTailPanel.dataTable.getValueAt(0, 1).toString(), "Line 2");
 assertEquals("Checking the table content", jTailPanel.dataTable.getValueAt(1, 1).toString(), "Line 3");
 }

 @Test
 public void testUpdateDataTable() {
 String lineNumbers = "1\n2\n3\n4\n5\n6";
 String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6";
 jTailPanel.updateDataTable(lineNumbers, lines);
 int nbRows = jTailPanel.dataTable.getRowCount();

 assertEquals("Checking the number of rows in the table model", nbRows, 6);
 }
}