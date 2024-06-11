package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.event.AdjustmentEvent;
import java.awt.event.ComponentEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.controller.JTailSizeController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.util.JTailLogger;

public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel jTailPanel;

 @Before
 public void setup() {
 model = mock(JTailModel.class);
 jTailPanel = new JTailPanel(model);
 JTailLogger.init(); // Init logger for test
 }

 @Test
 public void testJTailPanelInitialization() {
 // Test that a new JTailPanel is properly initialized
 assertEquals(model, jTailPanel.getModel());
 assertNotNull(jTailPanel.getVerticalScrollBar());
 assertNotNull(jTailPanel.getDataTable());
 assertNotNull(jTailPanel.getTableModel());
 assertNotNull(jTailPanel.getScrollPane());
 assertNotNull(jTailPanel.getCharacterHeight());
 assertNull(jTailPanel.getMaxCharacterWidth());
 }

 @Test
 public void testGetCharacterHeight() {
 // Test getCharacterHeight method
 int characterHeight = jTailPanel.getCharacterHeight();
 assertEquals(characterHeight, jTailPanel.getCharacterHeight());
 }

 @Test
 public void testGetVerticalScrollBar() {
 // Test getVerticalScrollBar method
 JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 assertEquals(verticalScrollBar, jTailPanel.getVerticalScrollBar());
 }

 @Test
 public void testUpdateObserver() {
 // Test update method with RESIZED event
 JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 1, 1, 1, 1, 1, "test");
 when(model.getEvent()).thenReturn(event);

 jTailPanel.update(model, event);

 final BoundedRangeModel rangeModel = jTailPanel.getVerticalScrollBar().getModel();
 assertEquals(1, rangeModel.getMinimum());
 assertEquals(1, rangeModel.getValue());
 assertEquals(1, rangeModel.getExtent());
 assertEquals(1, rangeModel.getMaximum());
 assertFalse(rangeModel.isValueIsAdjusting());

 // Test update method with SCROLLED event
 event = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1, 1, 1, 1, 1, 1, 1, "test");
 when(model.getEvent()).thenReturn(event);

 jTailPanel.update(model, event);

 assertTrue(jTailPanel.updateDataTableCalled); // Assuming updateDataTable has a flag to check if it was called
 }

 @Test
 public void testUpdateDataTable() {
 // Test updateDataTable method
 String lineNumbers = "1\n2\n3\n4\n5";
 String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
 List<String> lineNumbersList = Arrays.asList(lineNumbers.split("\n"));
 List<String> linesList = Arrays.asList(lines.split("\n"));

 jTailPanel.updateDataTable(lineNumbers, lines);

 int rowCountBefore = jTailPanel.getDataTable().getRowCount();
 int rowCountAfter = jTailPanel.getDataTable().getRowCount();

 assertEquals(lineNumbersList.size(), rowCountBefore);
 assertEquals(lineNumbersList.size(), rowCountAfter);

 for (int i = 0; i < lineNumbersList.size(); i++) {
 String lineNumber = lineNumbersList.get(i);
 String line = linesList.get(i);
 String cellValue = (i < jTailPanel.getDataTable().getModel().getRowCount()) ?
 jTailPanel.getDataTable().getValueAt(i, 0).toString() : "";

 assertEquals(lineNumber, cellValue);
 assertEquals(line, jTailPanel.getDataTable().getValueAt(i, 1).toString());
 }
 }

 @Test
 public void testUpdateTableColumnsWidth() {
 // Test updateDataTable method with columns width resizing
 String lineNumbers = "1\n2\n3\n4\n5";
 String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
 List<String> lineNumbersList = Arrays.asList(lineNumbers.split("\n"));
 List<String> linesList = Arrays.asList(lines.split("\n"));

 jTailPanel.updateDataTable(lineNumbers, lines);

 TableColumn numbersColumn = jTailPanel.getDataTable().getColumnModel().getColumn(0);
 TableColumn linesColumn = jTailPanel.getDataTable().getColumnModel().getColumn(1);

 int numbersColumnWidth = numbersColumn.getPreferredWidth();
 int linesColumnWidth = linesColumn.getPreferredWidth();

 assertEquals(numbersColumnWidth, lineNumbersList.get(lineNumbersList.size() - 1).length() * jTailPanel.getMaxCharacterWidth());
 assertEquals(linesColumnWidth, jTailPanel.getDataTable().getWidth() - numbersColumnWidth);
 }

 @Test
 public void testSizeController() {
 // Test JTailSizeController
 JTailSizeController sizeController = new JTailSizeController(jTailPanel, model);
 sizeController.componentResized(new ComponentEvent(jTailPanel, 0));

 int lineNumber = 1;
 int maxLine = 5;
 int firstLine = 1;
 int nbDisplayedLines = 3;
 int minLine = 1;

 JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, lineNumber, maxLine, firstLine, minLine, nbDisplayedLines, nbDisplayedLines, nbDisplayedLines, "test");
 when(model.getEvent()).thenReturn(event);

 sizeController.update(model, event);

 assertTrue(jTailPanel.updateDataTableCalled);
 }

 @Test
 public void testScrollBarController() {
 // Test ScrollBarAdjustmentController
 ScrollBarAdjustmentController scrollBarController = new ScrollBarAdjustmentController(jTailPanel, model);
 AdjustmentEvent adjustmentEvent = new AdjustmentEvent(scrollBarController, AdjustmentEvent.ADJUSTING, 1, 1, 1);

 scrollBarController.adjustmentValueChanged(adjustmentEvent);

 final BoundedRangeModel rangeModel = jTailPanel.getVerticalScrollBar().getModel();
 assertEquals(1, rangeModel.getValue());
 }
}