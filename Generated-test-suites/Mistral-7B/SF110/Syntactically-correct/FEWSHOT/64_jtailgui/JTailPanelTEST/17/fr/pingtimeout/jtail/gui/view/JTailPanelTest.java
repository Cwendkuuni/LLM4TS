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

 JTailModel mockModel;
 JTailSizeController mockSizeController;
 ScrollBarAdjustmentController mockScrollBarController;
 JTailPanel panelUnderTest;

 @Before
 public void setup() {
 mockModel = mock(JTailModel.class);
 mockSizeController = mock(JTailSizeController.class);
 mockScrollBarController = mock(ScrollBarAdjustmentController.class);

 panelUnderTest = new JTailPanel(mockModel);
 panelUnderTest.addComponentListener(mockSizeController);
 panelUnderTest.getVerticalScrollBar().addAdjustmentListener(mockScrollBarController);
 }

 @Test
 public void testJTailPanelConstruction() {
 assertEquals("JTailPanel should be of type JPanel", true, panelUnderTest instanceof JPanel);
 }

 @Test
 public void testGetCharacterHeight() {
 final FontMetrics metrics = mock(FontMetrics.class);
 when(panelUnderTest.dataTable.getFontMetrics(any())).thenReturn(metrics);
 when(metrics.getHeight()).thenReturn(15);

 assertEquals("Character height should be 14", 14, panelUnderTest.getCharacterHeight());
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertSame("JTailPanel and JScrollBar should be the same object", panelUnderTest.verticalScrollBar, panelUnderTest.getVerticalScrollBar());
 }

 @Test
 public void testUpdateWithResizeEvent() {
 final int minLine = 10;
 final int firstLine = 20;
 final int maxLine = 30;
 final int nbDisplayedLines = 5;
 final BoundedRangeModel rangeModel = mock(BoundedRangeModel.class);
 when(mockModel.getMinLine()).thenReturn(minLine);
 when(mockModel.getFirstLine()).thenReturn(firstLine);
 when(mockModel.getMaxLine()).thenReturn(maxLine);
 when(mockModel.getNbDisplayedLines()).thenReturn(nbDisplayedLines);
 whenNew(JTailModelEvent.class).withArguments(mockModel, JTailModelEvent.Type.RESIZED, minLine, firstLine, maxLine, nbDisplayedLines).thenReturn(new JTailModelEvent(mockModel, JTailModelEvent.Type.RESIZED, minLine, firstLine, maxLine, nbDisplayedLines));

 doAnswer(i -> {
 rangeModel.setValueIsAdjusting(true);
 rangeModel.setMinimum(minLine);
 rangeModel.setValue(firstLine);
 rangeModel.setExtent(nbDisplayedLines);
 rangeModel.setMaximum(maxLine);
 rangeModel.setValueIsAdjusting(false);
 return null;
 }).when(mockScrollBarController).valueChanged(any());

 panelUnderTest.update(mockModel, new JTailModelEvent(mockModel, JTailModelEvent.Type.RESIZED, minLine, firstLine, maxLine, nbDisplayedLines));

 verify(mockScrollBarController, times(1)).valueChanged(any());
 verify(rangeModel, times(1)).setValueIsAdjusting(true);
 verify(rangeModel, times(1)).setMinimum(minLine);
 verify(rangeModel, times(1)).setValue(firstLine);
 verify(rangeModel, times(1)).setExtent(nbDisplayedLines);
 verify(rangeModel, times(1)).setMaximum(maxLine);
 verify(rangeModel, times(1)).setValueIsAdjusting(false);

 assertEquals("The scrollbar should be enabled if there are lines to scroll", true, panelUnderTest.verticalScrollBar.isEnabled());
 }

 @Test
 public void testUpdateWithScrollEvent() {
 final int firstLine = 20;
 final String lines = "Line 20: Some content\nLine 21: Some other content";
 final String[] lineNumbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
 final String[] linesArray = lines.split("\n");
 when(mockModel.getFirstLine()).thenReturn(firstLine);
 when(mockModel.getContent()).thenReturn(lines);
 whenNew(JTailModelEvent.class).withArguments(mockModel, JTailModelEvent.Type.SCROLLED, firstLine, lines).thenReturn(new JTailModelEvent(mockModel, JTailModelEvent.Type.SCROLLED, firstLine, lines));

 panelUnderTest.update(mockModel, new JTailModelEvent(mockModel, JTailModelEvent.Type.SCROLLED, firstLine, lines));

 final TableModel tableModel = panelUnderTest.dataTable.getModel();
 final int rowCount = tableModel.getRowCount();

 for (int i = 0; i < rowCount; i++) {
 String lineNumber = (String)tableModel.getValueAt(i, 0);
 String line = (String)tableModel.getValueAt(i, 1);
 assertEquals("Line number and line content should match", lineNumbers[i], lineNumber);
 assertEquals("Line number and line content should match", linesArray[i], line);
 }
 }
}