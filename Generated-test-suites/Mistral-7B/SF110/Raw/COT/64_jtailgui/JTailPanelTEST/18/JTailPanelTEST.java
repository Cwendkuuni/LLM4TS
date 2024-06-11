package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.util.*;
import fr.pingtimeout.jtail.gui.view.*;

public class JTailPanelTest {

 private JTailPanel jTailPanel;
 private JTailModel mockModel;

 @Before
 public void setUp() {
 mockModel = Mockito.mock(JTailModel.class);
 jTailPanel = new JTailPanel(mockModel);
 }

 @Test
 public void testJTailPanelConstructor() {
 // Basic functionality test for JTailPanel constructor
 assertEquals("JTailPanel should be an instance of JPanel", true, jTailPanel instanceof JPanel);
 }

 @Test
 public void testGetCharacterHeight() {
 // Basic functionality test for getCharacterHeight() method
 int characterHeight = jTailPanel.getCharacterHeight();
 assertNotNull("getCharacterHeight() should return a valid value", characterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 // Basic functionality test for getVerticalScrollBar() method
 JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 assertNotNull("getVerticalScrollBar() should return a valid value", verticalScrollBar);
 }

 @Test
 public void testUpdateMethodWithResizeEvent() {
 // Test for update() method when receiving a RESIZED event
 mockModel.notifyObservers(new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 10, 20, 30, 40, 50));
 jTailPanel.update(mockModel, new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 10, 20, 30, 40, 50));
 // Check if scrollbar values have been updated
 JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 BoundedRangeModel rangeModel = verticalScrollBar.getModel();
 assertEquals("First line should be set to the correct value", 1, rangeModel.getValue());
 assertEquals("Maximum line should be set to the correct value", 50, rangeModel.getMaximum());
 // Check if scrollbar is enabled or disabled based on the number of lines
 assertTrue("Scrollbar should be enabled if there are more lines than displayed", jTailPanel.verticalScrollBar.isEnabled());
 // Test for resizing columns
 TableColumn numbersColumn = jTailPanel.dataTable.getColumnModel().getColumn(0);
 TableColumn linesColumn = jTailPanel.dataTable.getColumnModel().getColumn(1);
 int numbersColumnWidth = numbersColumn.getPreferredWidth();
 int linesColumnWidth = linesColumn.getPreferredWidth();
 assertEquals("Numbers column width should be correct", 26 * 7, numbersColumnWidth); // Assuming maxCharacterWidth = 7
 assertEquals("Lines column width should be correct", jTailPanel.dataTable.getWidth() - numbersColumnWidth, linesColumnWidth);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateMethodWithInvalidEvent() {
 // Test for update() method with an invalid event
 mockModel.notifyObservers(new JTailModelEvent("InvalidEventType", 1, 1, 10, 20, 30, 40, 50));
 jTailPanel.update(mockModel, new JTailModelEvent("InvalidEventType", 1, 1, 10, 20, 30, 40, 50));
 }

 @Test
 public void testUpdateMethodWithScrollEvent() {
 // Test for update() method when receiving a SCROLLED event
 mockModel.notifyObservers(new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1, 1, 10, 20, 30, 40, 50));
 jTailPanel.update(mockModel, new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1, 1, 10, 20, 30, 40, 50));
 // Check if data table is updated
 String[] lineNumbers = jTailPanel.model.getLineNumbers().split("\n");
 String[] lines = jTailPanel.model.getContent().split("\n");
 int numberOfLines = lineNumbers.length;
 int numberOfDisplayedLines = jTailPanel.dataTable.getRowCount();
 assertFalse("Data table should not have the same number of rows as the model", numberOfLines == numberOfDisplayedLines);
 assertEquals("Number of displayed lines should be correct", numberOfDisplayedLines, 10);
 // Check if scrollbar values have been updated
 JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 BoundedRangeModel rangeModel = verticalScrollBar.getModel();
 assertEquals("First line should be set to the correct value", 1, rangeModel.getValue());
 assertEquals("Maximum line should be set to the correct value", numberOfLines, rangeModel.getMaximum());
 }

}