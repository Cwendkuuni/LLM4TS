package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.AdjustmentEvent;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.controller.SizeController;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent.Type;
import fr.pingtimeout.jtail.gui.view.JTailPanel;

public class JTailPanelTest {

 private JTailModel mockModel;
 private JTailPanel jTailPanel;
 private FontMetrics mockFontMetrics;
 private JScrollBar mockVerticalScrollBar;
 private JTable mockDataTable;
 private DefaultTableModel mockTableModel;

 @Before
 public void setUp() {
 mockModel = mock(JTailModel.class);
 mockVerticalScrollBar = mock(JScrollBar.class);
 mockDataTable = mock(JTable.class);
 mockTableModel = mock(DefaultTableModel.class);
 jTailPanel = new JTailPanel(mockModel);
 mockFontMetrics = mock(FontMetrics.class);
 }

 @Test
 public void testJTailPanelConstruction() {
 // check that the model is initialized
 assertEquals(mockModel, jTailPanel.model);

 // check that the scroll pane and data table are initialized
 assertEquals(mockScrollPane, jTailPanel.scrollPane);
 assertEquals(mockDataTable, jTailPanel.dataTable);

 // check that the vertical scroll bar and table model are initialized
 assertEquals(mockVerticalScrollBar, jTailPanel.verticalScrollBar);
 assertEquals(mockTableModel, jTailPanel.tableModel);

 // check that the character height is initialized
 assertEquals(1, jTailPanel.getCharacterHeight());
 }

 @Test
 public void testUpdateWhenModelIsResized() {
 final JTailModelEvent event = new JTailModelEvent(mockModel, Type.RESIZED, 1, 1, 1, 1, 1, 1, 1, 1, 1, "test");

 // simulate the model event
 jTailPanel.update(mockModel, event);

 // check that the vertical scroll bar was updated with the new minimum, value, extent and maximum
 ArgumentCaptor<AdjustmentEvent> adjustmentEventCaptor = ArgumentCaptor.forClass(AdjustmentEvent.class);
 verify(mockVerticalScrollBar).setValueChanged(adjustmentEventCaptor.capture());
 AdjustmentEvent adjustmentEvent = adjustmentEventCaptor.getValue();
 assertEquals(event.minLine, adjustmentEvent.getValue());
 assertEquals(event.firstLine, adjustmentEvent.getSource().getValue());
 assertEquals(event.nbDisplayedLines, adjustmentEvent.getSource().getExtent());
 assertEquals(event.maxLine, adjustmentEvent.getSource().getMaximum());

 // check that the data table is updated
 assertEquals(event.lineNumbers.split("\n").length, jTailPanel.dataTable.getRowCount());
 }

 @Test
 public void testUpdateWhenModelIsScrolled() {
 final JTailModelEvent event = new JTailModelEvent(mockModel, Type.SCROLLED, 1, 1, 1, 1, 1, 1, 1, 1, 1, "test");

 // simulate the model event
 jTailPanel.update(mockModel, event);

 // check that the data table is updated
 assertEquals(event.lineNumbers.split("\n").length, jTailPanel.dataTable.getRowCount());
 }

 @Test
 public void testUpdateDataTableWithLineNumbersAndLines() {
 final String lineNumbers = "1\n2\n3\n4";
 final String lines = "one\ntwo\nthree\nfour";

 jTailPanel.updateDataTable(lineNumbers, lines);

 // check that the table model contains the correct data
 assertEquals(lineNumbers.split("\n").length, jTailPanel.tableModel.getRowCount());
 for (int i = 0; i < lineNumbers.split("\n").length; ++i) {
 String lineNumber = lineNumbers.split("\n")[i];
 String line = (i < lines.split("\n").length) ? lines.split("\n")[i] : "";
 assertEquals(lineNumber, jTailPanel.tableModel.getValueAt(i, 0).toString());
 assertEquals(line, jTailPanel.tableModel.getValueAt(i, 1).toString());
 }
 }

 @Test
 public void testUpdateDataTableWidths() {
 final String lineNumbers = "1234567890\n1234567890\n1234567890\n1234567890";
 final String lines = "abcdefghijklmnopqrstuvwxyz\nabcdefghijklmnopqrstuvwxyz\nabcdefghijklmnopqrstuvwxyz\nabcdefghijklmnopqrstuvwxyz";
 final int columnWidth = 10;
 final int expectedNumbersColumnWidth = 53;
 final int expectedLinesColumnWidth = jTailPanel.dataTable.getWidth() - expectedNumbersColumnWidth;

 jTailPanel.updateDataTable(lineNumbers, lines);

 // check that the numbers and lines columns have the expected widths
 TableColumn numbersColumn = jTailPanel.dataTable.getColumnModel().getColumn(0);
 TableColumn linesColumn = jTailPanel.dataTable.getColumnModel().getColumn(1);
 assertEquals(expectedNumbersColumnWidth, numbersColumn.getPreferredWidth());
 assertEquals(expectedLinesColumnWidth, linesColumn.getPreferredWidth());
 }

 @Test
 public void testDisableVerticalScrollBarWhenOnlyOneLineIsDisplayed() {
 final String lineNumbers = "1";
 final String lines = "one";

 jTailPanel.updateDataTable(lineNumbers, lines);

 assertTrue(jTailPanel.verticalScrollBar.isEnabled());
 jTailPanel.updateDataTable(lineNumbers, lines);

 assertFalse(jTailPanel.verticalScrollBar.isEnabled());
 }

 @Test
 public void testEnableVerticalScrollBarWhenMoreThanOneLineIsDisplayed() {
 final String lineNumbers = "1\n2\n3";
 final String lines = "one\ntwo\nthree";

 jTailPanel.updateDataTable(lineNumbers, lines);

 assertTrue(jTailPanel.verticalScrollBar.isEnabled());
 jTailPanel.updateDataTable(lineNumbers + "\n", lines);

 assertTrue(jTailPanel.verticalScrollBar.isEnabled());
 }
}