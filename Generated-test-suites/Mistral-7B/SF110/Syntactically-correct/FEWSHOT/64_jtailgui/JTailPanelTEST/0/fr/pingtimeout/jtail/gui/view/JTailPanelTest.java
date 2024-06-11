package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.*;
import java.awt.*;
import java.util.*;
import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
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
 public void testJTailPanelConstruction() {
 assertEquals("Building a new JTailPanel", JTailLogger.getDebugLog().get(0));
 assertNotNull(panelUnderTest.getVerticalScrollBar());
 assertNotNull(panelUnderTest.getDataTable());
 assertNotNull(panelUnderTest.getScrollPane());
 }

 @Test
 public void testGetCharacterHeight() {
 final FontMetrics metrics = mock(FontMetrics.class);
 when(panelUnderTest.dataTable.getFontMetrics(any()))
 .thenReturn(metrics);
 final int expectedCharacterHeight = metrics.getHeight() - 1;
 assertEquals(expectedCharacterHeight, panelUnderTest.getCharacterHeight());
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertSame(panelUnderTest.verticalScrollBar, panelUnderTest.getVerticalScrollBar());
 }

 @Test
 public void testUpdateObserver_Type_RESIZED() {
 final int expectedMinLine = 0;
 final int expectedFirstLine = 0;
 final int expectedNbDisplayedLines = 10;
 final int expectedMaxLine = 20;
 final int expectedMaxCharacterWidth = 10;
 final int expectedCharacterHeight = 15;
 final BoundedRangeModel rangeModel = mock(BoundedRangeModel.class);
 final ScrollBarAdjustmentController scrollBarController = mock(ScrollBarAdjustmentController.class);
 final JTailSizeController sizeController = mock(JTailSizeController.class);

 when(mockModel.getMinLine()).thenReturn(expectedMinLine);
 when(mockModel.getFirstLine()).thenReturn(expectedFirstLine);
 when(mockModel.getNbDisplayedLines()).thenReturn(expectedNbDisplayedLines);
 when(mockModel.getMaxLine()).thenReturn(expectedMaxLine);
 when(mockModel.getMaxCharacterWidth()).thenReturn(expectedMaxCharacterWidth);
 when(mockModel.getCharacterHeight()).thenReturn(expectedCharacterHeight);
 when(rangeModel.setValueIsAdjusting()).thenAnswer(invocation -> {
 invocation.getArgument(0).setValueIsAdjusting(true);
 });
 when(rangeModel.setMinimum(anyInt())).then(invocation -> {
 invocation.getArgument(0).setMinimum(expectedMinLine);
 });
 when(rangeModel.setValue(anyInt())).then(invocation -> {
 invocation.getArgument(0).setValue(expectedFirstLine);
 });
 when(rangeModel.setExtent(anyInt())).then(invocation -> {
 invocation.getArgument(0).setExtent(expectedNbDisplayedLines);
 });
 when(rangeModel.setMaximum(anyInt())).then(invocation -> {
 invocation.getArgument(0).setMaximum(expectedMaxLine);
 });
 when(rangeModel.setValueIsAdjusting()).thenAnswer(invocation -> {
 invocation.getArgument(0).setValueIsAdjusting(false);
 });
 when(scrollBarController.updateScrollBar(panelUnderTest, mockModel)).thenReturn(rangeModel);
 when(sizeController.updateSize(panelUnderTest, mockModel)).thenReturn(null);

 panelUnderTest.update(mockModel, new JTailModelEvent(JTailModelEvent.Type.RESIZED));

 verify(mockModel, times(1)).getMinLine();
 verify(mockModel, times(1)).getFirstLine();
 verify(mockModel, times(1)).getNbDisplayedLines();
 verify(mockModel, times(1)).getMaxLine();
 verify(mockModel, times(1)).getMaxCharacterWidth();
 verify(mockModel, times(1)).getCharacterHeight();
 verify(panelUnderTest.verticalScrollBar, times(1)).addAdjustmentListener(eq(scrollBarController));
 verify(panelUnderTest, times(1)).addComponentListener(eq(sizeController));
 verify(rangeModel, times(1)).setValueIsAdjusting(true);
 verify(rangeModel, times(1)).setMinimum(expectedMinLine);
 verify(rangeModel, times(1)).setValue(expectedFirstLine);
 verify(rangeModel, times(1)).setExtent(expectedNbDisplayedLines);
 verify(rangeModel, times(1)).setMaximum(expectedMaxLine);
 verify(rangeModel, times(1)).setValueIsAdjusting(false);
 }

 @Test
 public void testUpdateObserver_Type_SCROLLED() {
 final String lineNumbers = "1\n2\n3\n4\n5";
 final String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";

 when(mockModel.getLineNumbers()).thenReturn(lineNumbers);
 when(mockModel.getContent()).thenReturn(lines);

 panelUnderTest.update(mockModel, new JTailModelEvent(JTailModelEvent.Type.SCROLLED));

 assertEquals(5, panelUnderTest.dataTable.getRowCount());
 }

 @Test
 public void testUpdateDataTable() {
 final String lineNumbers = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10";
 final String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8\nLine 9\nLine 10";

 panelUnderTest.updateDataTable(lineNumbers, lines);

 assertEquals(10, panelUnderTest.dataTable.getRowCount());
 for (int i = 0; i < 10; i++) {
 assertEquals(String.valueOf(i + 1), panelUnderTest.dataTable.getValueAt(i, 0).toString());
 assertEquals(lines.split("\n")[i], panelUnderTest.dataTable.getValueAt(i, 1).toString());
 }
 }

 @Test
 public void testResizeColumns() {
 final int dataTableWidth = 300;
 final int numbersColumnWidth = 50;
 final int linesColumnWidth = 250;

 panelUnderTest.updateDataTable(mockModel.getLineNumbers(), mockModel.getContent());
 panelUnderTest.dataTable.setAutoResizeMode(3);

 final TableColumn numbersColumn = panelUnderTest.dataTable.getColumnModel().getColumn(0);
 final TableColumn linesColumn = panelUnderTest.dataTable.getColumnModel().getColumn(1);

 panelUnderTest.dataTable.setSize(dataTableWidth, 200);

 assertEquals(numbersColumnWidth, numbersColumn.getPreferredWidth());
 assertEquals(linesColumnWidth, linesColumn.getPreferredWidth());
 }
}