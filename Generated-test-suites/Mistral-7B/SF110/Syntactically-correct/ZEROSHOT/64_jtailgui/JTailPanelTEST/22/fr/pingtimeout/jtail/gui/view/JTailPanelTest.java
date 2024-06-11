package fr.pingtimeout.jtail.gui.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.table.*;

public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel jTailPanel;

 @Before
 public void setUp() {
 model = new JTailModel(); // Assuming JTailModel is initialized properly
 jTailPanel = new JTailPanel(model);
 }

 @Test
 public void testJTailPanelConstruction() {
 JPanel panel = jTailPanel;
 assertNotNull(panel);
 assertNotNull(jTailPanel.model);
 assertNotNull(jTailPanel.scrollPane);
 assertNotNull(jTailPanel.dataTable);
 assertNotNull(jTailPanel.tableModel);
 assertNotNull(jTailPanel.verticalScrollBar);
 }

 @Test
 public void testGetCharacterHeight() {
 int characterHeight = jTailPanel.getCharacterHeight();
 assertNotNull(characterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 assertNotNull(verticalScrollBar);
 }

 @Test
 public void testUpdateWhenModelIsResized() {
 // Assuming that there is a way to trigger the JTailModel event in the test
 model.notifyObservers(new JTailModelEvent(JTailModelEvent.Type.RESIZED));

 // Check the scrollbar values
 JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 JBoundedRangeModel rangeModel = verticalScrollBar.getModel();
 int minLine = rangeModel.getMinimum();
 int firstLine = rangeModel.getValue();
 int nbDisplayedLines = rangeModel.getExtent();
 int maxLine = rangeModel.getMaximum();

 assertEquals(0, minLine); // Assuming the first line is the first displayed line
 assertNotEquals(0, firstLine);
 assertEquals(10, nbDisplayedLines); // Assuming a constant number of lines to display
 assertEquals(model.getLineCount(), maxLine);

 // Check the table content
 TableModel tableModel = jTailPanel.dataTable.getModel();
 int numberOfRows = tableModel.getRowCount();
 String[] lineNumbers = new String[numberOfRows];
 String[] lines = new String[numberOfRows];

 for (int i = 0; i < numberOfRows; i++) {
 lineNumbers[i] = (String) tableModel.getValueAt(i, 0);
 lines[i] = (String) tableModel.getValueAt(i, 1);
 }

 // TODO: Add assertions to check the content of the lines
 }

 @Test
 public void testUpdateWhenModelIsScrolled() {
 // Assuming that there is a way to trigger the JTailModel event in the test
 model.notifyObservers(new JTailModelEvent(JTailModelEvent.Type.SCROLLED));

 // Check the table content
 TableModel tableModel = jTailPanel.dataTable.getModel();
 int numberOfRows = tableModel.getRowCount();
 String[] lineNumbers = new String[numberOfRows];
 String[] lines = new String[numberOfRows];

 for (int i = 0; i < numberOfRows; i++) {
 lineNumbers[i] = (String) tableModel.getValueAt(i, 0);
 lines[i] = (String) tableModel.getValueAt(i, 1);
 }

 // TODO: Add assertions to check the content of the lines
 }
}