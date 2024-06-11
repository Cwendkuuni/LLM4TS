package fr.pingtimeout.jtail.gui.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.util.JTailLogger;

public class JTailPanelTest {

 private JTailModel mockModel;
 private JTailPanel jTailPanel;
 private ChangeListener mockScrollBarAdjustmentController;
 private ChangeEvent mockChangeEvent;

 @Before
 public void setUp() {
 mockModel = Mockito.mock(JTailModel.class);
 mockScrollBarAdjustmentController = Mockito.mock(ScrollBarAdjustmentController.class);
 mockChangeEvent = Mockito.mock(ChangeEvent.class);
 jTailPanel = new JTailPanel(mockModel);
 }

 @Test
 public void testJTailPanelConstruction() {
 assertEquals("JTailPanel should extend JPanel", JPanel.class, jTailPanel.getClass());
 assertNotNull("model should not be null", jTailPanel.model);
 assertNotNull("scrollPane should not be null", jTailPanel.scrollPane);
 assertNotNull("dataTable should not be null", jTailPanel.dataTable);
 assertNotNull("tableModel should not be null", jTailPanel.tableModel);
 assertNotNull("verticalScrollBar should not be null", jTailPanel.verticalScrollBar);
 assertFalse("verticalScrollBar should not be enabled by default", jTailPanel.verticalScrollBar.isEnabled());
 }

 @Test
 public void testGetCharacterHeight() {
 final int expectedCharacterHeight = 15;
 Mockito.when(jTailPanel.dataTable.getFontMetrics(Mockito.any(Font.class)))
 .thenReturn(Mockito.mock(FontMetrics.class, Mockito.withSettings().retries(3)));
 Mockito.when(jTailPanel.dataTable.getFontMetrics(Mockito.any(Font.class)).getHeight())
 .thenReturn(expectedCharacterHeight);
 final int actualCharacterHeight = jTailPanel.getCharacterHeight();
 assertEquals("Character height should match expected value", expectedCharacterHeight, actualCharacterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertSame("JTailPanel and verticalScrollBar should be the same instance", jTailPanel.verticalScrollBar, jTailPanel.getVerticalScrollBar());
 }

 @Test
 public void testUpdateObserverMethod() {
 Mockito.when(mockModel.getEventType()).thenReturn(JTailModelEvent.Type.RESIZED);
 Mockito.when(mockModel.getMinLine()).thenReturn(0);
 Mockito.when(mockModel.getFirstLine()).thenReturn(1);
 Mockito.when(mockModel.getNbDisplayedLines()).thenReturn(5);
 Mockito.when(mockModel.getLineNumbers()).thenReturn("1\n2\n3\n4\n5\n6\n7\n8\n9\n10");
 Mockito.when(mockModel.getContent()).thenReturn("one\ntwo\nthree\nfour\nfive\nsix\nseven\neight\nnine\nten");
 Mockito.when(mockChangeEvent.getSource()).thenReturn(mockModel);

 jTailPanel.update(mockModel, mockChangeEvent);

 final JScrollBar verticalScrollBar = jTailPanel.verticalScrollBar;
 final BoundedRangeModel rangeModel = verticalScrollBar.getModel();
 assertEquals("Minimum should match expected value", 0, rangeModel.getMinimum());
 assertEquals("FirstLine should match expected value", 1, rangeModel.getValue());
 assertEquals("NbDisplayedLines should match expected value", 5, rangeModel.getExtent());
 assertEquals("Maximum should match expected value", 10, rangeModel.getMaximum());
 assertTrue("ScrollBar should be enabled", verticalScrollBar.isEnabled());
 }

 @Test
 public void testUpdateObserverMethod_Scrolling() {
 Mockito.when(mockModel.getEventType()).thenReturn(JTailModelEvent.Type.SCROLLED);
 Mockito.when(mockModel.getContent()).thenReturn("one\ntwo\nthree\nfour\nsix\nseven\neight\nine\nten");
 Mockito.when(mockChangeEvent.getSource()).thenReturn(mockModel);

 jTailPanel.update(mockModel, mockChangeEvent);

 final String expectedLines = "one\ntwo\nthree\nfour\nsix\nseven\neight\nine\nten";
 final int numberOfRows = 9;
 final TableModel tableModel = jTailPanel.tableModel;
 assertEquals("Number of rows should match expected value", numberOfRows, tableModel.getRowCount());
 String actualLines = "";
 for (int i = 0; i < numberOfRows; ++i) {
 actualLines += tableModel.getValueAt(i, 1).toString();
 }
 assertEquals("Lines should match expected value", expectedLines, actualLines);
 }

 @Test
 public void testUpdateDataTable() {
 final String lineNumbers = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10";
 final String lines = "one\ntwo\nthree\nfour\nsix\nseven\neight\nine\nten";
 jTailPanel.updateDataTable(lineNumbers, lines);

 final int numberOfRows = 10;
 final TableModel tableModel = jTailPanel.tableModel;
 assertEquals("Number of rows should match expected value", numberOfRows, tableModel.getRowCount());
 final TableColumn numbersColumn = tableModel.getColumnModel().getColumn(0);
 final TableColumn linesColumn = tableModel.getColumnModel().getColumn(1);
 final int numbersColumnWidth = lineNumbers.split("\n")[lineNumbers.split("\n").length - 1].length() * jTailPanel.maxCharacterWidth;
 final int linesColumnWidth = jTailPanel.dataTable.getWidth() - numbersColumnWidth;
 assertEquals("Widths of columns should match expected values", numbersColumnWidth, numbersColumn.getPreferredWidth());
 assertEquals("Widths of columns should match expected values", linesColumnWidth, linesColumn.getPreferredWidth());
 }

 @Test
 public void testUpdateDataTable_Scrolling() {
 final String lineNumbers = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10";
 final String lines = "one\ntwo\nthree\nfour\nsix\nseven\neight\nine\nten";
 jTailPanel.updateDataTable(lineNumbers, lines);
 jTailPanel.update(mockModel, mockChangeEvent);

 final int numberOfRows = 10;
 final TableModel tableModel = jTailPanel.tableModel;
 assertEquals("Number of rows should match expected value", numberOfRows, tableModel.getRowCount());
 final TableColumn numbersColumn = tableModel.getColumnModel().getColumn(0);
 final TableColumn linesColumn = tableModel.getColumnModel().getColumn(1);
 final int numbersColumnWidth = lineNumbers.split("\n")[lineNumbers.split("\n").length - 1].length() * jTailPanel.maxCharacterWidth;
 final int linesColumnWidth = jTailPanel.dataTable.getWidth() - numbersColumnWidth;
 assertEquals("Widths of columns should match expected values", numbersColumnWidth, numbersColumn.getPreferredWidth());
 assertEquals("Widths of columns should match expected values", linesColumnWidth, linesColumn.getPreferredWidth());
 }

 @Test
 public void testScrollBarAdjustmentController() {
 mockScrollBarAdjustmentController = new ScrollBarAdjustmentController(jTailPanel, mockModel);
 mockScrollBarAdjustmentController.valueChanged(mockChangeEvent);

 final BoundedRangeModel rangeModel = jTailPanel.verticalScrollBar.getModel();
 assertEquals("Minimum should match the minimum value of the model", 0, rangeModel.getMinimum());
 assertEquals("Value should match the value of the model", 0, rangeModel.getValue());
 assertEquals("Extent should match the number of lines to display", 5, rangeModel.getExtent());
 assertEquals("Maximum should match the maximum value of the model", 10, rangeModel.getMaximum());
 }

 @Test
 public void testJTailSizeController() {
 jTailPanel = new JTailPanel(mockModel);
 jTailPanel.setSize(500, 500);
 jTailPanel.update(mockModel, mockChangeEvent);
 final int minLine = 0;
 final int firstLine = 1;
 final int nbDisplayedLines = 5;
 final int maxLine = 10;
 Mockito.when(mockModel.getEventType()).thenReturn(JTailModelEvent.Type.RESIZED);
 Mockito.when(mockModel.getMinLine()).thenReturn(minLine);
 Mockito.when(mockModel.getFirstLine()).thenReturn(firstLine);
 Mockito.when(mockModel.getNbDisplayedLines()).thenReturn(nbDisplayedLines);
 Mockito.when(mockModel.getMaxLine()).thenReturn(maxLine);
 Mockito.when(mockModel.getLineNumbers()).thenReturn(lineNumbers);
 Mockito.when(mockModel.getContent()).thenReturn(lines);
 Mockito.when(mockChangeEvent.getSource()).thenReturn(mockModel);

 jTailPanel.update(mockModel, mockChangeEvent);
 final JScrollBar verticalScrollBar = jTailPanel.verticalScrollBar;
 final BoundedRangeModel rangeModel = verticalScrollBar.getModel();
 assertEquals("Minimum should match expected value", minLine, rangeModel.getMinimum());
 assertEquals("FirstLine should match expected value", firstLine, rangeModel.getValue());
 assertEquals("NbDisplayedLines should match expected value", nbDisplayedLines, rangeModel.getExtent());
 assertEquals("Maximum should match expected value", maxLine, rangeModel.getMaximum());
 JTailLogger.debug("Resizing columns");
 jTailPanel.updateDataTable(lineNumbers, lines);
 final TableColumn numbersColumn = jTailPanel.dataTable.getColumnModel().getColumn(0);
 final TableColumn linesColumn = jTailPanel.dataTable.getColumnModel().getColumn(1);
 final int numbersColumnWidth = lineNumbers.split("\n")[lineNumbers.split("\n").length - 1].length() * jTailPanel.maxCharacterWidth;
 final int linesColumnWidth = jTailPanel.dataTable.getWidth() - numbersColumnWidth;
 assertEquals("Numbers column width should match expected value", numbersColumnWidth, numbersColumn.getPreferredWidth());
 assertEquals("Lines column width should match expected value", linesColumnWidth, linesColumn.getPreferredWidth());
 assertTrue("VerticalScrollBar should be enabled if there are more lines than displayed", maxLine > nbDisplayedLines);
 }
}