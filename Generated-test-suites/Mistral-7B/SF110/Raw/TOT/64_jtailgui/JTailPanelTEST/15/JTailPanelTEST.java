package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Dimension;
import java.awt.Font;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.gui.view.JTailSizeController;
import fr.pingtimeout.jtail.util.JTailLogger;

public class JTailPanelTest {

 private JTailModel mockModel;
 private JTailPanel panelUnderTest;

 @Before
 public void setup() {
 mockModel = mock(JTailModel.class);
 panelUnderTest = new JTailPanel(mockModel);
 }

 @Test
 public void testJTailPanelConstruction() {
 assertEquals("Unexpected model instance", mockModel, panelUnderTest.model);
 assertNotNull("Unexpected null scrollPane", panelUnderTest.scrollPane);
 assertNotNull("Unexpected null dataTable", panelUnderTest.dataTable);
 assertNotNull("Unexpected null tableModel", panelUnderTest.tableModel);
 assertNotNull("Unexpected null verticalScrollBar", panelUnderTest.verticalScrollBar);
 }

 @Test
 public void testGetCharacterHeightReturnsCorrectValue() {
 final int expectedCharacterHeight = 16; // Assuming a test font's line height is 16
 assertEquals("Unexpected character height", expectedCharacterHeight, panelUnderTest.getCharacterHeight());
 }

 @Test
 public void testGetVerticalScrollBarReturnsCorrectComponent() {
 assertEquals("Unexpected verticalScrollBar instance", panelUnderTest.verticalScrollBar, panelUnderTest.getVerticalScrollBar());
 }

 @Test
 public void testUpdateWithModelResizeEvent() {
 final int minLine = 10;
 final int firstLine = 20;
 final int nbDisplayedLines = 5;
 final int maxLine = 30;

 mockModel.addObserver(panelUnderTest);
 when(mockModel.getLineNumbers()).thenReturn("10\n20\n30\n");
 when(mockModel.getContent()).thenReturn("Line 10\nLine 20\nLine 30\n");

 panelUnderTest.update(mockModel, new JTailModelEvent(JTailModelEvent.Type.RESIZED, this, minLine, firstLine, nbDisplayedLines, maxLine));

 final ArgumentCaptor<BoundedRangeModel> rangeModelCaptor = ArgumentCaptor.forClass(BoundedRangeModel.class);
 verify(panelUnderTest.verticalScrollBar).getModel();
 verify(panelUnderTest.verticalScrollBar).setEnabled(false);
 verify(panelUnderTest.verticalScrollBar).setMinimum(minLine);
 verify(panelUnderTest.verticalScrollBar).setValue(firstLine);
 verify(panelUnderTest.verticalScrollBar).setExtent(nbDisplayedLines);
 verify(panelUnderTest.verticalScrollBar).setMaximum(maxLine);
 verify(panelUnderTest.verticalScrollBar).setEnabled(true);
 verify(panelUnderTest).updateDataTable(eq("10\n20\n30\n"), anyString());
 }

 @Test
 public void testUpdateWithModelScrolledEvent() {
 final int lineNumber = 20;
 final String contentBefore = "Line 10\nLine 20\nLine 30\n";
 final String contentAfter = "Line 11\nLine 20\nLine 31\n";

 when(mockModel.getLineNumbers()).thenReturn("10\n20\n30\n");
 when(mockModel.getContent()).thenReturn(contentBefore);

 panelUnderTest.update(mockModel, new JTailModelEvent(JTailModelEvent.Type.SCROLLED, this, null, null, null, null));

 assertEquals("Unexpected content after scrolling", contentAfter, mockModel.getContent());
 verify(panelUnderTest).updateDataTable(eq("11\n20\n31\n"), anyString());
 }

 @Test
 public void testUpdateDataTableSetsCorrectNumberOfRows() {
 final String lineNumbers = "1\n2\n3\n";
 final String lines = "Line 1\nLine 2\nLine 3\n";

 panelUnderTest.updateDataTable(lineNumbers, lines);
 assertEquals("Unexpected number of rows", 3, panelUnderTest.tableModel.getRowCount());
 }

 @Test
 public void testUpdateDataTableSetsCorrectCellValues() {
 final String lineNumbers = "1\n2\n3\n";
 final String lines = "Line 1\nLine 2\nLine 3\n";

 panelUnderTest.updateDataTable(lineNumbers, lines);

 final Object[] firstRow = panelUnderTest.tableModel.getRowData(0);
 assertEquals("Unexpected line number for first row", "1", firstRow[0]);
 assertEquals("Unexpected line content for first row", "Line 1", firstRow[1]);

 final Object[] lastRow = panelUnderTest.tableModel.getRowData(2);
 assertEquals("Unexpected line number for last row", "3", lastRow[0]);
 assertEquals("Unexpected line content for last row", "Line 3", lastRow[1]);
 }

 @Test
 public void testUpdateDataTableResizesColumnsToFitContent() {
 final int width = 400;
 final int height = 300;
 final Font font = new Font("Serif", Font.BOLD, 14);
 final Dimension preferredSize = new Dimension(width, height);
 panelUnderTest.setPreferredSize(preferredSize);

 panelUnderTest.updateDataTable("1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n", "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8\nLine 9\nLine 10\n");

 final int numbersColumnWidth = 60;
 final int linesColumnWidth = width - numbersColumnWidth;

 assertEquals("Unexpected numbers column width", numbersColumnWidth, panelUnderTest.dataTable.getColumnModel().getColumn(0).getPreferredWidth());
 assertEquals("Unexpected lines column width", linesColumnWidth, panelUnderTest.dataTable.getColumnModel().getColumn(1).getPreferredWidth());
 }
}