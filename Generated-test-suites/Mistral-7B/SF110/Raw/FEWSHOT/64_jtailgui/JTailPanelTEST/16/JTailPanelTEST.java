package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.util.*;

public class JTailPanelTest {

 private JTailModel mockModel;
 private JTailPanel jTailPanel;
 private JScrollPane mockScrollPane;
 private JScrollBar mockVerticalScrollBar;
 private JTable mockDataTable;
 private DefaultTableModel mockTableModel;
 private BorderLayout mockLayout;
 private JTailSizeController mockSizeController;
 private ScrollBarAdjustmentController mockAdjustmentController;

 @Before
 public void setUp() {
 mockModel = mock(JTailModel.class);
 jTailPanel = new JTailPanel(mockModel);
 mockScrollPane = mock(JScrollPane.class);
 mockVerticalScrollBar = mock(JScrollBar.class);
 mockDataTable = mock(JTable.class);
 mockTableModel = mock(DefaultTableModel.class);
 mockLayout = mock(BorderLayout.class);
 mockSizeController = mock(JTailSizeController.class);
 mockAdjustmentController = mock(ScrollBarAdjustmentController.class);
 when(mockScrollPane.getViewport()).thenReturn(mockDataTable);
 when(mockDataTable.getModel()).thenReturn(mockTableModel);
 when(mockTableModel.getColumnModel()).thenReturn(mockTableModel.getColumnModel());
 when(mockLayout.add(any(Component.class), eq("Center"))).thenReturn(jTailPanel);
 when(mockLayout.add(any(Component.class), eq("East"))).thenReturn(jTailPanel);
 }

 @Test
 public void testJTailPanelConstruction() {
 assertEquals("JPanel", jTailPanel.getClass().getName());
 assertSame(mockModel, jTailPanel.model);
 assertNotNull(jTailPanel.verticalScrollBar);
 assertInstanceOf(DefaultTableModel.class, jTailPanel.tableModel);
 assertInstanceOf(JScrollPane.class, jTailPanel.scrollPane);
 assertSame(mockScrollPane, jTailPanel.add(mockScrollPane, "Center"));
 assertSame(mockScrollBar, jTailPanel.add(mockScrollBar, "East"));
 }

 @Test
 public void testGetCharacterHeight() {
 int characterHeight = jTailPanel.getCharacterHeight();
 int expectedCharacterHeight = 15; // Assuming a font with a height of 15 pixels
 assertEquals(expectedCharacterHeight, characterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertEquals(mockVerticalScrollBar, jTailPanel.getVerticalScrollBar());
 }

 @Test
 public void testUpdateWhenModelIsResized() {
 JTailModelEvent resizedEvent = new JTailModelEvent(mockModel, JTailModelEvent.Type.RESIZED, 1, 10, 20, 30, 40);
 jTailPanel.update(mockModel, resizedEvent);
 verify(mockModel, times(1)).addObserver(eq(jTailPanel));

 JScrollBarModel verticalScrollBarModel = mockVerticalScrollBar.getModel();
 int minLine = resizedEvent.minLine;
 int firstLine = resizedEvent.firstLine;
 int maxLine = resizedEvent.maxLine;
 int nbDisplayedLines = resizedEvent.nbDisplayedLines;
 int expectedValue = firstLine;
 int expectedMinimum = minLine;
 int expectedMaximum = maxLine;

 verify(mockVerticalScrollBar, times(1)).setEnabled(eq(true));
 verify(verticalScrollBarModel, times(1)).setValueIsAdjusting(eq(true));
 verify(verticalScrollBarModel, times(1)).setValue(eq(expectedValue));
 verify(verticalScrollBarModel, times(1)).setMinimum(eq(expectedMinimum));
 verify(verticalScrollBarModel, times(1)).setMaximum(eq(expectedMaximum));
 verify(verticalScrollBarModel, times(1)).setValueIsAdjusting(eq(false));

 assertEquals("The window has beed resized", jTailPanel.getLog());
 }

 @Test
 public void testUpdateWhenModelIsScrolled() {
 JTailModelEvent scrolledEvent = new JTailModelEvent(mockModel, JTailModelEvent.Type.SCROLLED, 5);
 jTailPanel.update(mockModel, scrolledEvent);
 verify(mockModel, times(1)).addObserver(eq(jTailPanel));
 verify(mockDataTable, times(1)).setAutoResizeMode(eq(3));
 }

 @Test
 public void testUpdateDataTable() {
 String lineNumbers = "1\n2\n3\n4\n5\n";
 String lines = "one\ntwo\nthree\nfour\nfive\n";
 jTailPanel.updateDataTable(lineNumbers, lines);
 int numberOfRows = 5;

 verify(mockTableModel, times(1)).getRowCount();
 verify(mockTableModel, times(5)).addRow(any(Object[].class));

 assertEquals(numberOfRows, jTailPanel.dataTable.getRowCount());
 }

 @Test
 public void testResizingColumns() {
 String lineNumbers = "1\n2\n3\n4\n5\n";
 String lines = "one\ntwo\nthree\nfour\nfive\n";
 jTailPanel.updateDataTable(lineNumbers, lines);
 int numbersColumnWidth = 10;
 int linesColumnWidth = jTailPanel.dataTable.getWidth() - numbersColumnWidth;
 int maxCharacterWidth = 5;

 verify(mockTableModel.getColumnModel(), times(1)).getColumn(0).setPreferredWidth(eq(numbersColumnWidth * maxCharacterWidth));
 verify(mockTableModel.getColumnModel(), times(1)).getColumn(1).setPreferredWidth(eq(jTailPanel.dataTable.getWidth() - numbersColumnWidth));
 }

 @Test
 public void testDisableVerticalScrollBarWhenNoLinesToDisplay() {
 String lineNumbers = "1\n2\n3\n";
 String lines = "one\ntwo\nthree\n";
 JTailModelEvent resizedEvent = new JTailModelEvent(mockModel, JTailModelEvent.Type.RESIZED, 1, 1, 3, 3, 3);
 jTailPanel.update(mockModel, resizedEvent);

 verify(mockVerticalScrollBar, times(1)).setEnabled(eq(false));
 }

 @Test
 public void testEnableVerticalScrollBarWhenLinesToDisplay() {
 String lineNumbers = "1\n2\n3\n4\n5\n";
 String lines = "one\ntwo\nthree\nfour\nfive\n";
 JTailModelEvent resizedEvent = new JTailModelEvent(mockModel, JTailModelEvent.Type.RESIZED, 1, 1, 5, 5, 5);
 jTailPanel.update(mockModel, resizedEvent);

 verify(mockVerticalScrollBar, times(1)).setEnabled(eq(true));
 }

 @Test
 public void testGetLog() {
 String expectedLog = "Building a new JTailPanel\nJPanel\nJPanel[, 0, 0, 600, 400]\nJTailModel@1b6d358\nJScrollBar[, 0, 0, 20, 400]\nDefaultTableModel[, 0]\nJTable[JTable[TableModel: DefaultTableModel[, 0], defaultRenderer: javax.swing.table.DefaultTableCellRenderer, defaultEditor: null, selectionModel: javax.swing.table.DefaultTableSelectionModel@180e392, rowSorter: null, tableHeader: JTableHeader[JTable[TableModel: DefaultTableModel[, 0], defaultRenderer: javax.swing.table.DefaultTableCellRenderer, defaultEditor: null, selectionModel: javax.swing.table.DefaultTableSelectionModel@180e392, rowSorter: null], headerPane: javax.swing.JTableHeaderUI$HeaderRow[, 0, 148, 600, 20], header: javax.swing.JTableHeader[, 0, 0, 600, 20], table: JTable[TableModel: DefaultTableModel[, 0], defaultRenderer: javax.swing.table.DefaultTableCellRenderer, defaultEditor: null, selectionModel: javax.swing.table.DefaultTableSelectionModel@180e392, rowSorter: null], columnModel: javax.swing.table.DefaultTableColumnModel@1f8d931]\nJScrollPane[JViewport[JTable[TableModel: DefaultTableModel[, 0], defaultRenderer: javax.swing.table.DefaultTableCellRenderer, defaultEditor: null, selectionModel: javax.swing.table.DefaultTableSelectionModel@180e392, rowSorter: null], JScrollBar[, 0, 0, 20, 400], null]]\n";
 assertEquals(expectedLog, jTailPanel.getLog());
 }
}