package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.model.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class JTailPanelTest {

 private JTailModel mockModel;
 private JTailPanel panelUnderTest;

 @Before
 public void setUp() {
 mockModel = mock(JTailModel.class);
 panelUnderTest = new JTailPanel(mockModel);
 }

 @Test
 public void testConstructor() {
 assertEquals("Building a new JTailPanel", JTailLogger.debugCalls.get(0));
 assertNotNull(panelUnderTest.model);
 assertNotNull(panelUnderTest.scrollPane);
 assertNotNull(panelUnderTest.dataTable);
 assertNotNull(panelUnderTest.tableModel);
 assertNotNull(panelUnderTest.verticalScrollBar);
 assertNotNull(panelUnderTest.characterHeight);
 assertNotNull(panelUnderTest.maxCharacterWidth);
 }

 @Test
 public void getCharacterHeightTest() {
 int characterHeight = panelUnderTest.getCharacterHeight();
 assertEquals("Expected character height to be set correctly", panelUnderTest.characterHeight, characterHeight);
 }

 @Test
 public void getVerticalScrollBarTest() {
 JScrollBar verticalScrollBar = panelUnderTest.getVerticalScrollBar();
 assertSame("Expected the same scroll bar to be returned", panelUnderTest.verticalScrollBar, verticalScrollBar);
 }

 @Test
 public void updateTest() {
 // Mock the Observer's update method
 doNothing().when(panelUnderTest).update(mockModel, any());

 // Trigger the update method
 panelUnderTest.update(mockModel, new JTailModelEvent(JTailModelEvent.Type.RESIZED, null));

 // Verify the update method was called
 verify(mockModel, times(1)).addObserver(any());
 verify(mockModel, times(1)).addObserver(panelUnderTest);
 verify(mockModel, times(1)).removeObserver(panelUnderTest);
 }

 @Test
 public void testResize() {
 // Set up the mock model to return some line numbers and content
 when(mockModel.getLineNumbers()).thenReturn("1\n2\n3\n4\n5");
 when(mockModel.getContent()).thenReturn("Line 1\nLine 2\nLine 3\nLine 4\nLine 5");

 // Trigger the update method
 panelUnderTest.update(mockModel, new JTailModelEvent(JTailModelEvent.Type.RESIZED, null));

 // Verify the data table was updated
 TableModel dataTableModel = panelUnderTest.dataTable.getModel();
 assertEquals("Expected 5 rows in the DataTable", 5, dataTableModel.getRowCount());
 String[] lineNumbers = {"1", "2", "3", "4", "5"};
 String[] lines = {"Line 1", "Line 2", "Line 3", "Line 4", "Line 5"};
 for (int i = 0; i < lineNumbers.length; ++i) {
 String lineNumber = lineNumbers[i];
 String line = lines[i];
 assertEquals("Expected the correct line number", lineNumber, dataTableModel.getValueAt(i, 0).toString());
 assertEquals("Expected the correct line content", line, dataTableModel.getValueAt(i, 1).toString());
 }

 // Verify the column widths were updated
 TableColumn numbersColumn = panelUnderTest.dataTable.getColumnModel().getColumn(0);
 TableColumn linesColumn = panelUnderTest.dataTable.getColumnModel().getColumn(1);
 int numbersColumnWidth = 11 * panelUnderTest.maxCharacterWidth;
 int linesColumnWidth = panelUnderTest.dataTable.getWidth() - numbersColumnWidth;
 assertEquals("Expected the correct width for the numbers column", numbersColumnWidth, numbersColumn.getPreferredWidth());
 assertEquals("Expected the correct width for the lines column", linesColumnWidth, linesColumn.getPreferredWidth());
 }

 @Test
 public void testScrolling() {
 // Set up the mock model to return some line numbers and content
 when(mockModel.getLineNumbers()).thenReturn("1\n2\n3\n4\n5");
 when(mockModel.getContent()).thenReturn("Line 1\nLine 2\nLine 3\nLine 4\nLine 5");

 // Trigger the update method with a scroll event
 panelUnderTest.update(mockModel, new JTailModelEvent(JTailModelEvent.Type.SCROLLED, null));

 // Verify the data table was updated
 TableModel dataTableModel = panelUnderTest.dataTable.getModel();
 assertEquals("Expected 5 rows in the DataTable", 5, dataTableModel.getRowCount());
 String[] lineNumbers = {"1", "2", "3", "4", "5"};
 String[] lines = {"Line 1", "Line 2", "Line 3", "Line 4", "Line 5"};
 for (int i = 0; i < lineNumbers.length; ++i) {
 String lineNumber = lineNumbers[i];
 String line = lines[i];
 assertEquals("Expected the correct line number", lineNumber, dataTableModel.getValueAt(i, 0).toString());
 assertEquals("Expected the correct line content", line, dataTableModel.getValueAt(i, 1).toString());
 }
}
}