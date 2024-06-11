package fr.pingtimeout.jtail.gui.view.test;

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

 private JTailModel model;
 private JTailPanel panel;

 @Before
 public void setUp() {
 JTailLogger.initLogging();
 model = mock(JTailModel.class);
 panel = new JTailPanel(model);
 }

 @Test
 public void testJTailPanelConstruction() {
 assertEquals("Expected JTailPanel instance", true, panel instanceof JTailPanel);
 }

 @Test
 public void testGetCharacterHeight() {
 final FontMetrics metrics = mock(FontMetrics.class);
 when(panel.dataTable.getFontMetrics(any(Font.class))).thenReturn(metrics);
 when(metrics.getHeight()).thenReturn(10);
 final int result = panel.getCharacterHeight();
 assertEquals("Expected correct character height", 9, result);
 }

 @Test
 public void testGetVerticalScrollBar() {
 final JScrollBar verticalScrollBar = panel.getVerticalScrollBar();
 assertEquals("Expected JScrollBar instance", true, verticalScrollBar instanceof JScrollBar);
 }

 @Test
 public void testUpdateWithEmptyModelData() {
 final String lineNumbers = "";
 final String lines = "";
 panel.updateDataTable(lineNumbers, lines);
 final int rowCount = panel.dataTable.getRowCount();
 assertEquals("Expected no rows in data table", 0, rowCount);
 }

 @Test
 public void testUpdateWithValidModelData() {
 final String lineNumbers = "1\n2\n3\n4";
 final String lines = "This is line 1\nThis is line 2\nThis is line 3\nThis is line 4";
 panel.updateDataTable(lineNumbers, lines);
 final int rowCount = panel.dataTable.getRowCount();
 assertEquals("Expected 4 rows in data table", 4, rowCount);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateWithInvalidModelData() {
 final String lineNumbers = "1\n2\n";
 final String lines = "This is line 1\nThis is line 2\n";
 panel.updateDataTable(lineNumbers, lines);
 }

 @Test
 public void testUpdateWithInvalidModelLineNumberSize() {
 final String lineNumbers = "1\n1\n1\n1";
 final String lines = "This is line 1\nThis is line 2\nThis is line 3\nThis is line 4";
 panel.updateDataTable(lineNumbers, lines);
 final int rowCount = panel.dataTable.getRowCount();
 assertEquals("Expected 4 rows in data table", 4, rowCount);
 }

 @Test
 public void testUpdateWithInvalidModelContentSize() {
 final String lineNumbers = "1\n2\n3\n4";
 final String lines = "This is line 1\nThis is line 2\n";
 panel.updateDataTable(lineNumbers, lines);
 final int rowCount = panel.dataTable.getRowCount();
 assertEquals("Expected 3 rows in data table", 3, rowCount);
 }

 @Test
 public void testUpdateDataTableWithModelResizeEvent() {
 final String lineNumbers = "1\n2\n3\n4";
 final String lines = "This is line 1\nThis is line 2\nThis is line 3\nThis is line 4";
 final int maxCharacterWidth = 20;
 final int minLine = 1;
 final int firstLine = 1;
 final int nbDisplayedLines = 3;
 final int maxLine = 4;
 when(model.getLineNumbers()).thenReturn(lineNumbers);
 when(model.getContent()).thenReturn(lines);
 when(model.getCharacterWidth()).thenReturn(maxCharacterWidth);
 when(model.getMinimumLinesToDisplay()).thenReturn(minLine);
 when(model.getFirstLine()).thenReturn(firstLine);
 when(model.getNbDisplayedLines()).thenReturn(nbDisplayedLines);
 when(model.getMaximumLines()).thenReturn(maxLine);
 panel.updateDataTable(lineNumbers, lines);
 final int rowCount = panel.dataTable.getRowCount();
 assertEquals("Expected 4 rows in data table", 4, rowCount);
 final int columnCount = panel.dataTable.getColumnCount();
 final TableColumn numbersColumn = panel.dataTable.getColumnModel().getColumn(0);
 final TableColumn linesColumn = panel.dataTable.getColumnModel().getColumn(1);
 final int numbersColumnWidth = lineNumbers.split("\n").length() * maxCharacterWidth;
 final int linesColumnWidth = panel.dataTable.getWidth() - numbersColumnWidth;
 assertEquals("Expected correct numbersColumnWidth", numbersColumnWidth, numbersColumn.getPreferredWidth());
 assertEquals("Expected correct linesColumnWidth", linesColumnWidth, linesColumn.getPreferredWidth());
 }
}