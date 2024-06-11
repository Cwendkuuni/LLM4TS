package fr.pingtimeout.jtail.gui.test;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.gui.view.JTailSizeController;
import fr.pingtimeout.jtail.util.JTailLogger;

public class JTailPanelTest {

 private JTailPanel jTailPanel;
 private JTailModel jTailModel;

 @Before
 public void setUp() {
 jTailModel = new JTailModel();
 jTailPanel = new JTailPanel(jTailModel);
 jTailPanel.setPreferredSize(new Dimension(800, 600));
 }

 @Test
 public void testJTailPanelConstruction() {
 // Test that the JTailPanel is constructed correctly
 assertEquals("Expected a JPanel", true, jTailPanel instanceof JPanel);
 assertEquals("Expected a JScrollPane", true, jTailPanel.getComponentAt(0) instanceof JScrollPane);
 assertEquals("Expected a JTable", true, jTailPanel.getComponentAt(0).getComponentAt(0) instanceof JTable);
 assertEquals("Expected a DefaultTableModel", true, jTailPanel.getComponentAt(0).getComponentAt(0).getModel() instanceof DefaultTableModel);
 }

 @Test
 public void testGetCharacterHeight() {
 // Test that the character height can be retrieved correctly
 int characterHeight = jTailPanel.getCharacterHeight();
 assertEquals("Expected a positive integer", characterHeight > 0, true);
 }

 @Test
 public void testGetVerticalScrollBar() {
 // Test that the vertical scroll bar can be retrieved correctly
 JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 assertNotNull("Expected a JScrollBar", verticalScrollBar);
 }

 @Test
 public void testUpdateWhenModelResized() {
 // Test that the JTailPanel updates correctly when the model is resized
 jTailModel.firePropertyChange(JTailModel.Property.SIZE, 0, 100);
 waitForMessages(); // Wait for logging messages

 int minLine = jTailModel.getMinLine();
 int firstLine = jTailModel.getFirstLine();
 int nbDisplayedLines = jTailModel.getNbDisplayedLines();
 int maxLine = jTailModel.getMaxLine();

 assertEquals("Expected minLine to be set correctly", minLine, 0);
 assertEquals("Expected firstLine to be set correctly", firstLine, 0);
 assertEquals("Expected nbDisplayedLines to be set correctly", nbDisplayedLines, 100);
 assertEquals("Expected maxLine to be set correctly", maxLine, 100);

 JScrollBarAdjustmentController scrollBarAdjustmentController = (JScrollBarAdjustmentController)jTailPanel.verticalScrollBar.getComponent(0);
 ScrollBarAdjustmentController expectedScrollBarAdjustmentController = new ScrollBarAdjustmentController(jTailPanel, jTailModel);
 assertEquals("Expected the ScrollBarAdjustmentController to be set correctly", expectedScrollBarAdjustmentController, scrollBarAdjustmentController);
 }

 @Test
 public void testUpdateWhenModelScrolled() {
 // Test that the JTailPanel updates correctly when the model is scrolled
 jTailModel.setContent("Line 1\nLine 2\nLine 3\nLine 4\nLine 5");
 jTailModel.firePropertyChange(JTailModel.Property.SCROLLED, null, new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 2, 3, 2));
 waitForMessages(); // Wait for logging messages

 String[] lineNumbers = jTailModel.getLineNumbers().split("\n");
 String[] lines = jTailModel.getContent().split("\n");
 assertEquals("Expected the number of lines to be correct", lines.length, 5);
 assertEquals("Expected the line numbers to be correct", Arrays.toString(lineNumbers), Arrays.toString(new String[] { "1", "2", "3", "4", "5" }));
 assertEquals("Expected the content to be correct", Arrays.toString(lines), Arrays.toString(new String[] { "Line 1", "Line 2", "Line 3", "Line 4", "Line 5" }));
 }

 @Test
 public void testUpdateDataTable() {
 // Test that the JTailPanel updates the DataTable correctly
 jTailModel.setContent("Line 1\nLine 2\nLine 3\nLine 4\nLine 5");
 jTailModel.firePropertyChange(JTailModel.Property.RESIZED, null, null);
 waitForMessages(); // Wait for logging messages

 int numColumns = jTailPanel.dataTable.getColumnCount();
 int numRows = jTailPanel.dataTable.getRowCount();

 assertEquals("Expected 2 columns", 2, numColumns);
 assertEquals("Expected the number of rows to be correct", 5, numRows);

 String[] lineNumbers = jTailModel.getLineNumbers().split("\n");
 String[] lines = jTailModel.getContent().split("\n");
 for (int i = 0; i < numRows; i++) {
 String expectedLineNumber = lineNumbers[i];
 String expectedLineContent = lines[i];
 String actualLineNumber = (String)jTailPanel.dataTable.getValueAt(i, 0);
 String actualLineContent = (String)jTailPanel.dataTable.getValueAt(i, 1);
 assertEquals("Expected the line number to be correct", expectedLineNumber, actualLineNumber);
 assertEquals("Expected the line content to be correct", expectedLineContent, actualLineContent);
 }
 }

 private void waitForMessages() {
 while (JTailLogger.getLastMessage() == null) {
 try {
 Thread.sleep(10);
 }
 catch (InterruptedException e) {
 e.printStackTrace();
 }
 }
 }
}