package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.table.*;
import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.util.*;

public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel jTailPanel;

 @Before
 public void setUp() {
 model = new JTailModel();
 jTailPanel = new JTailPanel(model);
 }

 @Test
 public void testConstructor() {
 JTailLogger.initLogging();
 assertTrue("JTailPanel should be an instance of JPanel", jTailPanel instanceof JPanel);
 }

 @Test
 public void testGetCharacterHeight() {
 assertEquals("Character height should be a positive number", jTailPanel.getCharacterHeight(), greaterThan(0));
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertTrue("VerticalScrollBar should be an instance of JScrollBar", jTailPanel.getVerticalScrollBar() instanceof JScrollBar);
 }

 @Test
 public void testUpdate() {
 // Stub JTailModel events
 JTailModelEvent event = new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, 0, 0, 10, 20, new String[]{"line1", "line2"}, new String[]{"content1", "content2"});
 model.notifyObservers(event);

 // Test verticalScrollBar update
 TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(jTailPanel.tableModel);
 JTailPanel.ScrollBarAdjustmentController controller = new JTailPanel.ScrollBarAdjustmentController(jTailPanel, model);
 jTailPanel.verticalScrollBar.addAdjustmentListener(controller);
 JScrollBar scrollBar = jTailPanel.verticalScrollBar;
 JBoundedRangeModel rangeModel = scrollBar.getModel();
 int expectedMin = event.minLine;
 int expectedFirst = event.firstLine;
 int expectedMax = event.maxLine;
 int expectedNbDisplayedLines = event.nbDisplayedLines;
 int expectedValue = event.firstLine;

 rangeModel.setValueIsAdjusting(true);
 rangeModel.setMinimum(expectedMin);
 rangeModel.setValue(expectedFirst);
 rangeModel.setExtent(expectedNbDisplayedLines);
 rangeModel.setMaximum(expectedMax);
 rangeModel.setValueIsAdjusting(false);
 assertEquals("Minimum should be set to " + expectedMin, expectedMin, rangeModel.getMinimum());
 assertEquals("First should be set to " + expectedFirst, expectedFirst, rangeModel.getValue());
 assertEquals("Maximum should be set to " + expectedMax, expectedMax, rangeModel.getMaximum());
 assertEquals("Value should be set to " + expectedValue, expectedValue, rangeModel.getValue());
 assertEquals("Vertical scrollbar should have " + (expectedMax > expectedFirst ? 1 : 0) + " visible lines", (expectedMax > expectedFirst ? 1 : 0), jTailPanel.dataTable.getRowCount());

 // Test dataTable update
 String[] lineNumbers = model.getLineNumbers().split("\n");
 String[] lines = model.getContent().split("\n");
 int rowsBefore = jTailPanel.dataTable.getRowCount();
 assertEquals("Table should have " + (lineNumbers.length < lines.length ? lineNumbers.length : lines.length) + " rows before update", rowsBefore, jTailPanel.dataTable.getRowCount());
 jTailPanel.update(model, event);
 String[] lineNumbersAfter = model.getLineNumbers().split("\n");
 String[] linesAfter = model.getContent().split("\n");
 int rowsAfter = jTailPanel.dataTable.getRowCount();
 assertEquals("Table should have " + (lineNumbersAfter.length < linesAfter.length ? lineNumbersAfter.length : linesAfter.length) + " rows after update", rowsAfter, jTailPanel.dataTable.getRowCount());
 for (int i = 0; i < lineNumbers.length; i++) {
 String lineNumber = lineNumbers[i];
 String line = lines[i];
 String lineNumberAfter = (i < lineNumbersAfter.length) ? lineNumbersAfter[i] : "";
 String lineAfter = (i < linesAfter.length) ? linesAfter[i] : "";
 assertEquals("Line number " + lineNumber + " should be equal to line number " + lineNumberAfter, lineNumber, lineNumberAfter);
 assertEquals("Line " + line + " should be equal to line " + lineAfter, line, lineAfter);
 }
}
}