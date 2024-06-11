package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class JTailPanelTest {

 private JTailModel mockModel;
 private JTailPanel testSubject;

 @Before
 public void setup() {
 mockModel = mock(JTailModel.class);
 testSubject = new JTailPanel(mockModel);
 }

 @Test
 public void testJTailPanelConstruction() {
 assertEquals("Building a new JTailPanel", JTailLogger.DEBUG_LOGS.get(0));
 assertNotNull(testSubject.getVerticalScrollBar());
 assertNotNull(testSubject.getDataTable());
 assertNotNull(testSubject.getTableModel());
 }

 @Test
 public void testUpdateWhenModelEventIsResized() {
 when(mockModel.getLineNumbers()).thenReturn("1\n2\n3\n4\n5");
 when(mockModel.getContent()).thenReturn("line1\nline2\nline3\nline4\nline5");
 testSubject.update(mockModel, new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 5, 1, 1, 5, 1));
 List<Integer> lineNumbers = Arrays.asList(1, 2, 3, 4, 5);
 String[] lines = new String[]{"line1", "line2", "line3", "line4", "line5"};
 verify(mockModel).getLineNumbers();
 verify(mockModel).getContent();
 assertEquals("The window has beed resized", JTailLogger.DEBUG_LOGS.get(1));
 assertEquals(5, testSubject.getTableModel().getRowCount());
 assertArrayEquals(lines, testSubject.getTableModel().getDataVector().toArray());
 }

 @Test
 public void testUpdateWhenModelEventIsScrolled() {
 when(mockModel.getLineNumbers()).thenReturn("1\n2\n3\n4\n5");
 when(mockModel.getContent()).thenReturn("line1\nline2\nline3\nline4\nline5");
 testSubject.update(mockModel, new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1, 1, 1, 2));
 assertEquals("The user scrolled the file", JTailLogger.DEBUG_LOGS.get(1));
 }

 @Test
 public void testUpdateWhenModelEventIsNotJTailModelEvent() {
 testSubject.update(mockModel, new Object());
 assertTrue(JTailLogger.DEBUG_LOGS.isEmpty());
 }

 @Test
 public void testUpdateDataTable() {
 String lineNumbers = "1\n2\n3\n4\n5\n6";
 String lines = "line1\nline2\nline3\nline4\nline5\nline6";
 testSubject.updateDataTable(lineNumbers, lines);
 List<Integer> lineNumbersList = Arrays.asList(1, 2, 3, 4, 5, 6);
 String[] linesArray = new String[]{"line1", "line2", "line3", "line4", "line5", "line6"};
 assertEquals(linesArray.length, testSubject.getTableModel().getRowCount());
 assertArrayEquals(linesArray, testSubject.getTableModel().getDataVector().toArray());
 }

 @Test
 public void testSetPreferredWidthOfDataTableColumns() {
 JTable dataTable = testSubject.getDataTable();
 TableColumn numbersColumn = dataTable.getColumnModel().getColumn(0);
 TableColumn linesColumn = dataTable.getColumnModel().getColumn(1);
 int numbersColumnWidth = 20;
 int linesColumnWidth = 200;
 testSubject.setPreferredWidthOfDataTableColumns(numbersColumnWidth, linesColumnWidth);
 assertEquals(numbersColumnWidth, numbersColumn.getPreferredWidth());
 assertEquals(linesColumnWidth, linesColumn.getPreferredWidth());
 }

 @Test
 public void testSetPreferredWidthOfDataTableColumnsWithNegativeWidth() {
 JTable dataTable = testSubject.getDataTable();
 TableColumn numbersColumn = dataTable.getColumnModel().getColumn(0);
 TableColumn linesColumn = dataTable.getColumnModel().getColumn(1);
 int numbersColumnWidth = 20;
 int linesColumnWidth = -20;
 testSubject.setPreferredWidthOfDataTableColumns(numbersColumnWidth, linesColumnWidth);
 assertEquals(numbersColumnWidth, numbersColumn.getPreferredWidth());
 assertEquals(linesColumnWidth, linesColumn.getPreferredWidth());
 assertEquals(-1, numbersColumn.getWidth());
 assertEquals(-1, linesColumn.getWidth());
 }

 @After
 public void teardown() {
 JTailLogger.DEBUG_LOGS.clear();
 }

}