package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

@RunWith(org.junit.runner.JUnit4.class)
public class JTailPanelTest {

 private JTailModel modelMock;
 private JTailPanel jTailPanel;
 private DefaultTableModel tableModelMock;
 private JTable dataTableMock;
 private JScrollPane scrollPaneMock;
 private JScrollBar verticalScrollBarMock;
 private SwingUtilities swingUtilities;
 private TableColumn numbersColumnMock;
 private TableColumn linesColumnMock;

 @Before
 public void setUp() {
 JTailLogger.init();
 modelMock = Mockito.mock(JTailModel.class);
 tableModelMock = Mockito.mock(DefaultTableModel.class);
 dataTableMock = Mockito.mock(JTable.class);
 scrollPaneMock = Mockito.mock(JScrollPane.class);
 verticalScrollBarMock = Mockito.mock(JScrollBar.class);
 swingUtilities = SwingUtilities.getWindowAncestor(this);
 numbersColumnMock = Mockito.mock(TableColumn.class);
 linesColumnMock = Mockito.mock(TableColumn.class);

 jTailPanel = new JTailPanel(modelMock);
 }

 @Test
 public void testJTailPanelConstructor() {
 assertEquals("Building a new JTailPanel", JTailLogger.getLastLogMessage());
 assertNotNull(jTailPanel.getModel());
 assertNotNull(jTailPanel.getVerticalScrollBar());
 assertNotNull(jTailPanel.getDataTable());
 assertNotNull(jTailPanel.getTableModel());
 assertSame(jTailPanel.getCharacterHeight(), swingUtilities.computeStringHeight(jTailPanel.getDataTable().getFont(), ""));
 assertFalse(jTailPanel.isEnabled());
 }

 @Test
 public void testGetCharacterHeight() {
 final int characterHeightExpected = 15; // Assuming a font height of 15 pixels
 assertEquals("Character height should be " + characterHeightExpected, characterHeightExpected, jTailPanel.getCharacterHeight());
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertSame("Vertical scroll bar", jTailPanel.getVerticalScrollBar(), verticalScrollBarMock);
 }

 @Test
 public void testUpdateMethod() {
 final int minLine = 1;
 final int firstLine = 10;
 final int nbDisplayedLines = 5;
 final int maxLine = 20;
 final JTailModelEvent event = new JTailModelEvent(modelMock, JTailModelEvent.Type.RESIZED, minLine, firstLine, nbDisplayedLines, maxLine);

 // Setting up expectations for model notifications
 Mockito.when(modelMock.getLineNumbers()).thenReturn(getLineNumbersMock());
 Mockito.when(modelMock.getContent()).thenReturn(getLinesMock());
 Mockito.when(modelMock.getMinLine()).thenReturn(minLine);
 Mockito.when(modelMock.getFirstLine()).thenReturn(firstLine);
 Mockito.when(modelMock.getNbDisplayedLines()).thenReturn(nbDisplayedLines);
 Mockito.when(modelMock.getMaxLine()).thenReturn(maxLine);
 Mockito.when(modelMock.getEventType()).thenReturn(event.getType());

 // Calling the update method
 jTailPanel.update(modelMock, event);

 // Verifying the scroll bar properties
 final BoundedRangeModel rangeModel = Mockito.mock(BoundedRangeModel.class);
 Mockito.when(verticalScrollBarMock.getModel()).thenReturn(rangeModel);
 Mockito.when(rangeModel.getMinimum()).thenReturn(minLine);
 Mockito.when(rangeModel.getValue()).thenReturn(firstLine);
 Mockito.when(rangeModel.getExtent()).thenReturn(nbDisplayedLines);
 Mockito.when(rangeModel.getMaximum()).thenReturn(maxLine);
 Mockito.when(rangeModel.isEnabled()).thenReturn(true);
 Mockito.verify(verticalScrollBarMock, Mockito.times(1)).setEnabled(true);
 Mockito.verify(verticalScrollBarMock, Mockito.times(1)).setValueIsAdjusting(true);
 Mockito.verify(verticalScrollBarMock, Mockito.times(1)).setValue(firstLine);
 Mockito.verify(verticalScrollBarMock, Mockito.times(1)).setMinimum(minLine);
 Mockito.verify(verticalScrollBarMock, Mockito.times(1)).setExtent(nbDisplayedLines);
 Mockito.verify(verticalScrollBarMock, Mockito.times(1)).setMaximum(maxLine);
 Mockito.verify(verticalScrollBarMock, Mockito.times(1)).setValueIsAdjusting(false);

 // Verifying the table update
 Mockito.verify(jTailPanel, Mockito.times(1)).updateDataTable(Arrays.asList(getLineNumbersMock()), getLinesMock());
 }

 @Test
 public void testUpdateDataTableMethod() {
 final String lineNumbers = "1\n2\n3\n4\n5";
 final String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";

 // Setting up expectations for model notifications
 Mockito.when(modelMock.getLineNumbers()).thenReturn(lineNumbers);
 Mockito.when(modelMock.getContent()).thenReturn(lines);

 // Calling the updateDataTable method
 jTailPanel.updateDataTable(lineNumbers, lines);

 // Verifying the table contents
 final int numberOfRowsBefore = jTailPanel.getDataTable().getRowCount();
 final int numberOfRowsAfter = jTailPanel.getDataTable().getRowCount();
 assertEquals("Number of rows in the table should have increased by 5", numberOfRowsBefore + 5, numberOfRowsAfter);

 // Verifying the table cells
 for (int i = 0; i < 5; i++) {
 final int rowIndex = i;
 final String expectedLineNumber = "Line " + (i + 1);
 final String expectedLine = "Line " + (i + 1);
 final TableCellRenderer cellRenderer = jTailPanel.getDataTable().getDefaultRenderer(Object.class);
 final Object[] expectedCellValues = new Object[] { (i + 1), expectedLine };
 final Object[] actualCellValues = jTailPanel.getDataTable().getModel().getRow(rowIndex).getValues();
 assertEquals("Cells in the table should have the correct values", expectedCellValues, actualCellValues);
 assertEquals("Cell renderer should return the correct text for line number", expectedLineNumber, cellRenderer.getTableCellRendererComponent(jTailPanel.getDataTable(), expectedCellValues[0], rowIndex, false, false).getText());
 assertEquals("Cell renderer should return the correct text for line", expectedLine, cellRenderer.getTableCellRendererComponent(jTailPanel.getDataTable(), expectedCellValues[1], rowIndex, false, false).getText());
 }
 }

 @Test
 public void testSizeChangeListenerMethod() {
 final AdjustmentEvent adjustmentEvent = Mockito.mock(AdjustmentEvent.class);
 final JTailSizeController sizeControllerMock = Mockito.mock(JTailSizeController.class);

 // Setting up expectations for size change events
 Mockito.when(jTailPanel.getVerticalScrollBar()).thenReturn(verticalScrollBarMock);
 Mockito.when(verticalScrollBarMock.getModel()).thenReturn(Mockito.mock(BoundedRangeModel.class));
 Mockito.when(sizeControllerMock.getJTailPanel()).thenReturn(jTailPanel);
 Mockito.when(sizeControllerMock.getJTailModel()).thenReturn(modelMock);
 Mockito.when(jTailPanel.getSize()).thenReturn(new Dimension(500, 500));
 Mockito.when(modelMock.getEventType()).thenReturn(JTailModelEvent.Type.RESIZED);
 Mockito.when(modelMock.getMinLine()).thenReturn(0);
 Mockito.when(modelMock.getFirstLine()).thenReturn(0);
 Mockito.when(modelMock.getNbDisplayedLines()).thenReturn(5);
 Mockito.when(modelMock.getMaxLine()).thenReturn(10);

 // Calling the sizeChangeListener method
 jTailPanel.addComponentListener(sizeControllerMock);
 jTailPanel.componentResized(adjustmentEvent);

 // Verifying the scroll bar properties
 Mockito.verify(verticalScrollBarMock, Mockito.times(1)).setEnabled(false);

 // Verifying the table update
 Mockito.verify(jTailPanel, Mockito.times(1)).updateDataTable(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"), getLinesMock());
 }

 private String getLineNumbersMock() {
 return "1\n2\n3\n4\n5\n6\n7\n8\n9\n10";
 }

 private String getLinesMock() {
 return "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8\nLine 9\nLine 10";
 }
}