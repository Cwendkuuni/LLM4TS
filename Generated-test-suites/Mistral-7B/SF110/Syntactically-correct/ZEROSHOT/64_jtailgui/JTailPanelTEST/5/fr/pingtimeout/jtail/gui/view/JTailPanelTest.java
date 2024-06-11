package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.util.JTailLogger;

public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel jTailPanel;

 @Before
 public void setUp() {
 model = new JTailModel();
 jTailPanel = new JTailPanel(model);
 }

 @Test
 public void testInitialization() {
 // Test initial size
 assertEquals(new Dimension(0, 0), jTailPanel.getSize());

 // Test initial scrollbar value
 assertEquals(0, jTailPanel.getVerticalScrollBar().getValue());
 }

 @Test
 public void testUpdateWithModelEvent() {
 // Create model event
 final JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 5, 10, 20);

 // Call update method
 jTailPanel.update(model, event);

 // Test scrollbar value
 final int expectedFirstLine = 1;
 final int expectedNbDisplayedLines = 5;
 final int expectedMaxLine = 10;
 final JScrollBar scrollBar = jTailPanel.getVerticalScrollBar();
 assertEquals(expectedFirstLine, scrollBar.getValue());
 assertEquals(expectedNbDisplayedLines, scrollBar.getExtent());
 assertEquals(expectedMaxLine, scrollBar.getMaximum());

 // Test enabled status
 final boolean expectedEnabled = (expectedFirstLine == expectedNbDisplayedLines && expectedMaxLine == expectedNbDisplayedLines);
 assertEquals(expectedEnabled, scrollBar.isEnabled());
 }

 @Test
 public void testUpdateDataTable() {
 // Set up model with test data
 model.setContent("Line 1\nLine 2\nLine 3");
 model.setLineNumbers("1\n2\n3");

 // Update data table and check rows
 jTailPanel.update(model, new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 5, 10, 20));
 assertEquals(3, jTailPanel.getDataTable().getRowCount());

 String[] expectedLines = {"1", "Line 1", "2", "Line 2", "3", "Line 3"};
 for (int i = 0; i < 6; i++) {
 String cell = (String) jTailPanel.getDataTable().getValueAt(i, 1);
 assertEquals(expectedLines[i], cell);
 }

 // Set up model with more lines and check column widths
 model.setContent("Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6");
 model.setLineNumbers("1\n2\n3\n4\n5\n6");
 jTailPanel.update(model, new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 5, 10, 20));

 TableColumn numbersColumn = jTailPanel.getDataTable().getColumnModel().getColumn(0);
 TableColumn linesColumn = jTailPanel.getDataTable().getColumnModel().getColumn(1);
 int numbersColumnWidth = numbersColumn.getPreferredWidth();
 int linesColumnWidth = linesColumn.getPreferredWidth();

 int[] expectedColumnWidths = {2, 155};
 assertArrayEquals(expectedColumnWidths, new int[] {numbersColumnWidth, linesColumnWidth});
 }

 @Test
 public void testScrollBarAdjustmentController() {
 ScrollBarAdjustmentController controller = new ScrollBarAdjustmentController(jTailPanel, model);
 controller.valueChanged(new AdjustmentEvent(0, AdjustmentEvent.TYPE_VALUE_CHANGED, 10, 10, 10, 10));
 int expectedFirstLine = 11;
 int expectedNbDisplayedLines = 5;
 JScrollBar scrollBar = jTailPanel.getVerticalScrollBar();
 assertEquals(expectedFirstLine, scrollBar.getValue());
 assertEquals(expectedNbDisplayedLines, scrollBar.getExtent());
 }
}