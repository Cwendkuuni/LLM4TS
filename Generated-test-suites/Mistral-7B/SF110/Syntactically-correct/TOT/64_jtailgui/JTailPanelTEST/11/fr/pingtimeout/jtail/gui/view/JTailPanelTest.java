package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.junit.*;
import org.mockito.*;

public class JTailPanelTest {

 private JTailModel mockModel;
 private JTailController mockController;
 private JTailPanel panelUnderTest;

 @Before
 public void setUp() {
 mockModel = mock(JTailModel.class);
 mockController = mock(JTailController.class);

 panelUnderTest = new JTailPanel(mockModel) {
 @Override
 public void update(Observable observable, Object arg) {
 super.update(observable, arg);
 mockController.onModelChanged(arg);
 }
 };
 panelUnderTest.setController(mockController);
 }

 @Test
 public void testInitialization() {
 assertTrue(panelUnderTest instanceof JPanel);
 assertNotNull(panelUnderTest.model);
 assertNotNull(panelUnderTest.scrollPane);
 assertNotNull(panelUnderTest.dataTable);
 assertNotNull(panelUnderTest.tableModel);
 assertNotNull(panelUnderTest.verticalScrollBar);
 assertEquals(21, panelUnderTest.scrollPane.getVerticalScrollBarPolicy());
 assertEquals(30, panelUnderTest.scrollPane.getHorizontalScrollBarPolicy());
 }

 @Test
 public void testGetCharacterHeight() {
 final int expectedCharacterHeight = 15; // Assuming a test font metric
 final FontMetrics metrics = mock(FontMetrics.class);
 when(panelUnderTest.dataTable.getFontMetrics(any()))
 .thenReturn(metrics);
 when(metrics.getHeight()).thenReturn(expectedCharacterHeight);
 final int actualCharacterHeight = panelUnderTest.getCharacterHeight();
 assertEquals(expectedCharacterHeight, actualCharacterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertSame(panelUnderTest.verticalScrollBar, panelUnderTest.getVerticalScrollBar());
 }

 @Test
 public void testUpdateWithResizedEvent() {
 final int firstLine = 0;
 final int maxLine = 10;
 final int nbDisplayedLines = 5;
 final int minLine = 2;
 final int newWidth = 500;
 final BoundedRangeModel rangeModel = mock(BoundedRangeModel.class);
 when(rangeModel.getValueIsAdjusting()).thenReturn(false);
 when(rangeModel.setMinimum(anyInt())).thenReturn(true);
 when(rangeModel.setValue(anyInt())).thenReturn(true);
 when(rangeModel.setExtent(anyInt())).thenReturn(true);
 when(rangeModel.setMaximum(anyInt())).thenReturn(true);

 mockModel.addObserver(panelUnderTest);
 panelUnderTest.setWidth(newWidth);
 panelUnderTest.update(mockModel, new JTailModelEvent(mockModel, JTailModelEvent.Type.RESIZED, firstLine, maxLine, nbDisplayedLines, minLine));

 verify(mockModel).addObserver(panelUnderTest);
 verify(rangeModel).setValueIsAdjusting(true);
 verify(rangeModel).setMinimum(firstLine);
 verify(rangeModel).setValue(minLine);
 verify(rangeModel).setExtent(nbDisplayedLines);
 verify(rangeModel).setMaximum(maxLine);
 verify(rangeModel).setValueIsAdjusting(false);

 final int expectedFirstLine = minLine;
 final int expectedLastLine = Math.min(maxLine, minLine + nbDisplayedLines);
 verify(rangeModel).setValue(expectedFirstLine);
 verify(rangeModel).setMaximum(expectedLastLine);

 final boolean expectedVerticalScrollBarEnabled = (minLine == firstLine && maxLine == nbDisplayedLines);
 assertEquals(expectedVerticalScrollBarEnabled, panelUnderTest.verticalScrollBar.isEnabled());
 }

 @Test
 public void testUpdateWithScrolledEvent() {
 final int lineToScroll = 5;
 mockModel.addObserver(panelUnderTest);
 panelUnderTest.update(mockModel, new JTailModelEvent(mockModel, JTailModelEvent.Type.SCROLLED, lineToScroll));

 verify(mockController).onModelChanged(argThat(e -> e.type == JTailModelEvent.Type.SCROLLED));
 }
}