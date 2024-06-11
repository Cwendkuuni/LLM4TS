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
import javax.swing.event.*;
import javax.swing.table.*;

import org.junit.*;
import org.mockito.*;

public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel panel;
 private JScrollBar verticalScrollBarMock;
 private JScrollPane scrollPaneMock;
 private JTable dataTableMock;
 private DefaultTableModel tableModelMock;

 @Before
 public void setUp() {
 JTailLogger.init("");
 model = Mockito.spy(new JTailModel());
 verticalScrollBarMock = Mockito.mock(JScrollBar.class);
 scrollPaneMock = Mockito.mock(JScrollPane.class);
 dataTableMock = Mockito.mock(JTable.class);
 tableModelMock = Mockito.mock(DefaultTableModel.class);

 panel = new JTailPanel(model);
 panel.verticalScrollBar = verticalScrollBarMock;
 panel.scrollPane = scrollPaneMock;
 panel.dataTable = dataTableMock;
 panel.tableModel = tableModelMock;
 }

 @Test
 public void testJTailPanelConstruction() {
 // Test if the constructor is called correctly
 verify(model, times(1)).addObserver(eq(panel));
 }

 @Test
 public void testGetCharacterHeight() {
 // Test if getCharacterHeight returns the correct value
 FontMetrics metrics = mock(FontMetrics.class);
 when(dataTableMock.getFontMetrics(any())).thenReturn(metrics);
 int characterHeight = panel.getCharacterHeight();
 int expectedCharacterHeight = metrics.getHeight() - 1;
 assertEquals(expectedCharacterHeight, characterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 // Test if getVerticalScrollBar returns the correct JScrollBar
 assertEquals(verticalScrollBarMock, panel.getVerticalScrollBar());
 }

 @Test
 public void testUpdateWhenModelIsResized() {
 // Test if update is called correctly when the model is resized
 JTailModelEvent resizedEvent = new JTailModelEvent(model, JTailModelEvent.Type.RESIZED);
 doCallRealMethod().when(panel).update(any(), any());
 panel.update(model, resizedEvent);

 // Test if the scroll bar is updated correctly
 verify(verticalScrollBarMock, times(1)).setValueIsAdjusting(true);
 verify(verticalScrollBarMock, times(1)).setValue(eq(resizedEvent.firstLine));
 verify(verticalScrollBarMock, times(1)).setMinimum(eq(resizedEvent.minLine));
 verify(verticalScrollBarMock, times(1)).setExtent(eq(resizedEvent.nbDisplayedLines));
 verify(verticalScrollBarMock, times(1)).setValueIsAdjusting(false);

 // Test if the data table is updated correctly
 verify(tableModelMock, times(1)).setRowCount(0);
 String[] lineNumbersArray = resizedEvent.lineNumbers.split("\n");
 String[] linesArray = resizedEvent.content.split("\n");
 for (int i = 0; i < lineNumbersArray.length; ++i) {
 verify(tableModelMock, times(1)).addRow(argThat(contains(lineNumbersArray[i], lineNumbersArray[lineNumbersArray.length - 1].length() * panel.maxCharacterWidth, lineNumbersArray[i].length(), linesArray[i])));
 }
 }

 @Test
 public void testUpdateWhenModelIsScrolled() {
 // Test if update is called correctly when the model is scrolled
 JTailModelEvent scrolledEvent = new JTailModelEvent(model, JTailModelEvent.Type.SCROLLED);
 doCallRealMethod().when(panel).update(any(), any());
 panel.update(model, scrolledEvent);

 // Test if the data table is updated correctly
 verify(tableModelMock, times(1)).setRowCount(0);
 String[] lineNumbersArray = model.getLineNumbers().split("\n");
 String[] linesArray = model.getContent().split("\n");
 for (int i = 0; i < lineNumbersArray.length; ++i) {
 verify(tableModelMock, times(1)).addRow(argThat(contains(lineNumbersArray[i], lineNumbersArray[lineNumbersArray.length - 1].length() * panel.maxCharacterWidth, lineNumbersArray[i].length(), linesArray[i])));
 }
 }

 @After
 public void tearDown() {
 // Reset mocks
 reset(model, verticalScrollBarMock, scrollPaneMock, dataTableMock, tableModelMock);
 }
}