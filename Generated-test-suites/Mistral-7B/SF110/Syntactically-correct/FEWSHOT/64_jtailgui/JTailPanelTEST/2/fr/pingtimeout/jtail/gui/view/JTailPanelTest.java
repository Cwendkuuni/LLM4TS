package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.*;
import java.awt.*;
import java.util.*;
import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class JTailPanelTest {

 private JTailModel mockModel;
 private JTailPanel panelUnderTest;
 private JScrollPane mockScrollPane;
 private JScrollBar mockVerticalScrollBar;
 private JTable mockDataTable;
 private DefaultTableModel mockTableModel;
 private BoundedRangeModel mockRangeModel;
 private ScrollBarAdjustmentController mockScrollBarAdjustmentController;
 private JTailSizeController mockJTailSizeController;

 @Before
 public void setUp() {
 mockModel = mock(JTailModel.class);
 mockScrollPane = mock(JScrollPane.class);
 mockVerticalScrollBar = mock(JScrollBar.class);
 mockDataTable = mock(JTable.class);
 mockTableModel = mock(DefaultTableModel.class);
 mockRangeModel = mock(BoundedRangeModel.class);
 mockScrollBarAdjustmentController = mock(ScrollBarAdjustmentController.class);
 mockJTailSizeController = mock(JTailSizeController.class);

 panelUnderTest = new JTailPanel(mockModel);
 doReturn(panelUnderTest).when(mockScrollPane).getViewport();
 doReturn(mockScrollPane).when(panelUnderTest).getScrollPane();
 doReturn(mockDataTable).when(panelUnderTest).getDataTable();
 doReturn(mockTableModel).when(panelUnderTest).getTableModel();
 doReturn(mockVerticalScrollBar).when(panelUnderTest).getVerticalScrollBar();
 doReturn(mockScrollBarAdjustmentController).when(mockVerticalScrollBar).getAdjustmentListener();
 doReturn(mockJTailSizeController).when(panelUnderTest).getJTailSizeController();
 }

 @Test
 public void testJTailPanelConstructor() {
 assertEquals(mockModel, panelUnderTest.getModel());
 assertTrue(panelUnderTest.getLayout() instanceof BorderLayout);
 assertNotNull(panelUnderTest.getScrollPane());
 assertNotNull(panelUnderTest.getVerticalScrollBar());
 assertNotNull(panelUnderTest.getDataTable());
 assertNotNull(panelUnderTest.getTableModel());
 }

 @Test
 public void testGetCharacterHeight() {
 final FontMetrics metrics = mock(FontMetrics.class);
 when(mockDataTable.getFontMetrics(any())).thenReturn(metrics);
 when(metrics.getHeight()).thenReturn(15); // Set the height of the character
 int result = panelUnderTest.getCharacterHeight();
 assertEquals(14, result); // Subtract 1 from the height of the character
 }

 @Test
 public void testUpdateWithResizedEvent() {
 final int maxLine = 100;
 final int nbDisplayedLines = 20;
 final int firstLine = 10;
 final int minLine = 5;
 final int eventMinLine = minLine;
 final int eventFirstLine = firstLine;
 final int eventMaxLine = maxLine;
 final int eventNbDisplayedLines = nbDisplayedLines;

 mockModel.addObserver(panelUnderTest);
 final JTailModelEvent event = new JTailModelEvent(mockModel, JTailModelEvent.Type.RESIZED, eventMinLine, eventFirstLine, eventMaxLine, eventNbDisplayedLines);
 panelUnderTest.update(mockModel, event);

 verify(mockModel, times(1)).addObserver(panelUnderTest);
 verify(mockRangeModel, times(1)).setValueIsAdjusting(true);
 verify(mockRangeModel, times(1)).setMinimum(eventMinLine);
 verify(mockRangeModel, times(1)).setValue(eventFirstLine);
 verify(mockRangeModel, times(1)).setExtent(eventNbDisplayedLines);
 verify(mockRangeModel, times(1)).setMaximum(eventMaxLine);
 verify(mockRangeModel, times(1)).setValueIsAdjusting(false);
 verify(panelUnderTest, times(1)).updateDataTable(lineNumbers, lines);

 if (eventMinLine == eventFirstLine && eventMaxLine == eventNbDisplayedLines) {
 verify(panelUnderTest, times(1)).setVerticalScrollBarEnabled(false);
 }
 else {
 verify(panelUnderTest, times(1)).setVerticalScrollBarEnabled(true);
 }
 }

 @Test
 public void testUpdateWithScrolledEvent() {
 final String lineNumbers = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11\n12\n13\n14\n15\n16\n17\n18\n19\n20\n21\n22\n23\n24\n25\n26\n27\n28\n29\n30\n31\n32\n33\n34\n35\n36\n37\n38\n39\n40\n41\n42\n43\n44\n45\n46\n47\n48\n49\n50";
 final String lines = "LINE 1\nLINE 2\nLINE 3\nLINE 4\nLINE 5\nLINE 6\nLINE 7\nLINE 8\nLINE 9\nLINE 10\nLINE 11\nLINE 12\nLINE 13\nLINE 14\nLINE 15\nLINE 16\nLINE 17\nLINE 18\nLINE 19\nLINE 20\nLINE 21\nLINE 22\nLINE 23\nLINE 24\nLINE 25\nLINE 26\nLINE 27\nLINE 28\nLINE 29\nLINE 30\nLINE 31\nLINE 32\nLINE 33\nLINE 34\nLINE 35\nLINE 36\nLINE 37\nLINE 38\nLINE 39\nLINE 40\nLINE 41\nLINE 42\nLINE 43\nLINE 44\nLINE 45\nLINE 46\nLINE 47\nLINE 48\nLINE 49\nLINE 50";

 doReturn(lineNumbers).when(mockModel).getLineNumbers();
 doReturn(lines).when(mockModel).getContent();

 mockModel.addObserver(panelUnderTest);
 final JTailModelEvent event = new JTailModelEvent(mockModel, JTailModelEvent.Type.SCROLLED);
 panelUnderTest.update(mockModel, event);

 verify(panelUnderTest, times(1)).updateDataTable(lineNumbers, lines);
 }

 @Test
 public void testUpdateDataTable() {
 final String lineNumbers = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11\n12\n13\n14\n15\n16\n17\n18\n19\n20\n21\n22\n23\n24\n25\n26\n27\n28\n29\n30\n31\n32\n33\n34\n35\n36\n37\n38\n39\n40\n41\n42\n43\n44\n45\n46\n47\n48\n49\n50";
 final String lines = "LINE 1\nLINE 2\nLINE 3\nLINE 4\nLINE 5\nLINE 6\nLINE 7\nLINE 8\nLINE 9\nLINE 10\nLINE 11\nLINE 12\nLINE 13\nLINE 14\nLINE 15\nLINE 16\nLINE 17\nLINE 18\nLINE 19\nLINE 20\nLINE 21\nLINE 22\nLINE 23\nLINE 24\nLINE 25\nLINE 26\nLINE 27\nLINE 28\nLINE 29\nLINE 30\nLINE 31\nLINE 32\nLINE 33\nLINE 34\nLINE 35\nLINE 36\nLINE 37\nLINE 38\nLINE 39\nLINE 40\nLINE 41\nLINE 42\nLINE 43\nLINE 44\nLINE 45\nLINE 46\nLINE 47\nLINE 48\nLINE 49\nLINE 50";

 when(mockModel.getLineNumbers()).thenReturn(lineNumbers);
 when(mockModel.getContent()).thenReturn(lines);

 panelUnderTest.updateDataTable(lineNumbers, lines);

 final int numberOfRows = panelUnderTest.getDataTable().getRowCount();
 assertEquals(51, numberOfRows);
 }

 @Test
 public void testResizingColumnWidths() {
 final int highestLineNumberLength = 10;
 final int dataTableWidth = 400;
 final int numbersColumnWidth = highestLineNumberLength * 10;
 final int linesColumnWidth = dataTableWidth - numbersColumnWidth;

 when(mockDataTable.getWidth()).thenReturn(dataTableWidth);
 when(mockTableModel.getColumn(0).getPreferredWidth()).thenReturn(numbersColumnWidth);
 when(mockTableModel.getColumn(1).getPreferredWidth()).thenReturn(linesColumnWidth);

 panelUnderTest.updateDataTable("", "");

 final TableColumn numbersColumn = panelUnderTest.getDataTable().getColumnModel().getColumn(0);
 final TableColumn linesColumn = panelUnderTest.getDataTable().getColumnModel().getColumn(1);
 assertEquals(numbersColumnWidth, numbersColumn.getPreferredWidth());
 assertEquals(linesColumnWidth, linesColumn.getPreferredWidth());
 }

 @Test
 public void testScrollBarAdjustmentController() {
 final int min = 0;
 final int max = 100;
 final int value = 20;
 final int extent = 30;
 final int adjustmentValue = 40;

 doReturn(panelUnderTest).when(mockScrollBarAdjustmentController).getAdjustable();
 doReturn(mockRangeModel).when(mockVerticalScrollBar).getModel();

 mockRangeModel.addChangeListener(eq(mockScrollBarAdjustmentController));

 panelUnderTest.scrollBarAdjustmentController.valueChanged(new ChangeEvent(mockRangeModel));

 verify(mockRangeModel, times(1)).setValueIsAdjusting(true);
 verify(mockRangeModel, times(1)).setValue(adjustmentValue);
 verify(mockRangeModel, times(1)).setValueIsAdjusting(false);
 }

 @Test
 public void testJTailSizeController() {
 final int width = 400;
 final int height = 400;
 final int newWidth = 500;
 final int newHeight = 600;
 final int oldWidth = 300;
 final int oldHeight = 400;

 doReturn(oldWidth, oldHeight).when(panelUnderTest).getSize();
 doReturn(newWidth, newHeight).when(panelUnderTest).getPreferredSize();
 doReturn(mockModel).when(panelUnderTest).getModel();

 mockJTailSizeController.componentResized(new ComponentEvent(panelUnderTest, ComponentEvent.COMPONENT_RESIZED));

 verify(mockModel, times(1)).setSize(newWidth, newHeight);
 }
}