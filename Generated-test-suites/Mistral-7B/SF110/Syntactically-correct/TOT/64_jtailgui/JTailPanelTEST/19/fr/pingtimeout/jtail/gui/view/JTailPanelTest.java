package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.controller.JTailSizeController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.util.JTailLogger;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

public class JTailPanelTest {

 private JTailModel mockModel;
 private JTailPanel panelUnderTest;
 private JScrollPane mockScrollPane;
 private JScrollBar mockVerticalScrollBar;
 private JTable mockDataTable;
 private DefaultTableModel mockTableModel;
 private JFrame mockFrame;

 @Before
 public void setUp() {
 JTailLogger.init(); // Needed for debug logging in JTailPanel
 MockitoAnnotations.initMocks(this);

 mockModel = mock(JTailModel.class);
 panelUnderTest = new JTailPanel(mockModel);
 mockScrollPane = mock(JScrollPane.class);
 mockVerticalScrollBar = mock(JScrollBar.class);
 mockDataTable = mock(JTable.class);
 mockTableModel = mock(DefaultTableModel.class);

 panelUnderTest.scrollPane = mockScrollPane;
 panelUnderTest.verticalScrollBar = mockVerticalScrollBar;
 panelUnderTest.dataTable = mockDataTable;
 panelUnderTest.tableModel = mockTableModel;

 when(mockScrollPane.getViewport()).thenReturn(panelUnderTest);
 when(mockScrollPane.getVerticalScrollBar()).thenReturn(mockVerticalScrollBar);
 when(mockDataTable.getFontMetrics(any(Font.class))).thenReturn(mock(FontMetrics.class));

 panelUnderTest.addComponentListener(mock(JTailSizeController.class));
 panelUnderTest.verticalScrollBar.addAdjustmentListener(mock(ScrollBarAdjustmentController.class));
 }

 @Test
 public void testInitialization() {
 assertEquals(JTailModel.class, panelUnderTest.model.getClass());
 assertEquals(JScrollPane.class, panelUnderTest.scrollPane.getClass());
 assertEquals(JScrollBar.class, panelUnderTest.verticalScrollBar.getClass());
 assertEquals(JTable.class, panelUnderTest.dataTable.getClass());
 assertEquals(DefaultTableModel.class, panelUnderTest.tableModel.getClass());
 }

 @Test
 public void testGetCharacterHeight() {
 final FontMetrics fontMetrics = mock(FontMetrics.class);
 when(mockDataTable.getFontMetrics(any(Font.class))).thenReturn(fontMetrics);
 when(fontMetrics.getHeight()).thenReturn(20); // Arbitrary value
 int characterHeight = panelUnderTest.getCharacterHeight();
 assertEquals(19, characterHeight); // Subtract 1 for the line height adjustment
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertSame(mockVerticalScrollBar, panelUnderTest.getVerticalScrollBar());
 }

 @Test
 public void testUpdateWithNoChangedData() {
 final String lineNumbers = "1\n2\n3\n4";
 final String lines = "this is line 1\nthis is line 2\nthis is line 3\nthis is line 4";
 panelUnderTest.updateDataTable(lineNumbers, lines);
 verifyNoMoreInteractions(mockTableModel);
 }

 @Test
 public void testUpdateWithChangedData() {
 final String newLineNumbers = "1\n2\n3\n5\n6";
 final String newLines = "this is line 1\nthis is line 2\nthis is line 3\nthis is line 5\nthis is line 6";
 panelUnderTest.updateDataTable(newLineNumbers, newLines);

 verify(mockTableModel).addRow(argThat((String[] args) -> args[0].equals("5") && args[1].equals("this is line 5")));
 verify(mockTableModel).addRow(argThat((String[] args) -> args[0].equals("6") && args[1].equals("this is line 6")));
 verify(mockTableModel, times(2)).fireTableDataChanged(TableModelEvent.ALL_COLUMNS, 2, 2);
 }

 @Test
 public void testUpdateWithResizedModel() {
 final int newMaxWidth = 200;
 final int newMaxHeight = 300;
 final JTailModelEvent event = new JTailModelEvent(mockModel, JTailModelEvent.Type.RESIZED, new MaxSize(newMaxWidth, newMaxHeight));
 panelUnderTest.update(mockModel, event);

 final int expectedMinLine = 0;
 final int expectedFirstLine = 0;
 final int expectedNbDisplayedLines = 10;
 final int expectedMaxLine = 30;

 final ArgumentCaptor<ComponentEvent> componentEventCaptor = ArgumentCaptor.forClass(ComponentEvent.class);
 final ArgumentCaptor<JTailModelEvent> jTailModelEventCaptor = ArgumentCaptor.forClass(JTailModelEvent.class);

 verify(mockScrollPane).setBounds(anyInt(), anyInt(), eq(newMaxWidth), eq(newMaxHeight));
 verify(mockVerticalScrollBar).getModel().setValueIsAdjusting().wasCalledWith(true);
 verify(mockVerticalScrollBar).getModel().setMinimum(eq(expectedMinLine));
 verify(mockVerticalScrollBar).getModel().setValue(eq(expectedFirstLine));
 verify(mockVerticalScrollBar).getModel().setExtent(eq(expectedNbDisplayedLines));
 verify(mockVerticalScrollBar).getModel().setMaximum(eq(expectedMaxLine));
 verify(mockVerticalScrollBar).getModel().setValueIsAdjusting().wasCalledWith(false);
 verify(mockFrame).pack();
 verify(panelUnderTest).repaint();
 verify(mockModel).addObserver(any());
 verify(mockScrollPane).addComponentListener(eq(panelUnderTest.getSizeController()));
 verify(mockScrollPane).addAdjustmentListener(eq(panelUnderTest.getScrollBarController()));
 verify(mockModel).notifyObservers(jTailModelEventCaptor.capture());
 assertEquals(JTailModelEvent.Type.RESIZED, jTailModelEventCaptor.getValue().getType());
 }
}