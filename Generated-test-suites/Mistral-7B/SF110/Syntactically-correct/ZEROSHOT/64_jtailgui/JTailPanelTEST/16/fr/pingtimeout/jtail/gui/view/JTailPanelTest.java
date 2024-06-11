package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

import org.junit.After;
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
 private JTailModel mockModel;

 @Before
 public void setUp() {
 JTailLogger.initLog(); // Initialize logger for debugging
 mockModel = new JTailModel();
 jTailPanel = new JTailPanel(mockModel);
 }

 @Test
 public void testJTailPanelConstruction() {
 // Test if JTailPanel is constructed correctly
 assertEquals("Expected JTailModel instance", mockModel, jTailPanel.model);
 assertNotNull("Expected scrollPane instance", jTailPanel.scrollPane);
 assertNotNull("Expected dataTable instance", jTailPanel.dataTable);
 assertNotNull("Expected tableModel instance", jTailPanel.tableModel);
 assertNotNull("Expected characterHeight instance", jTailPanel.characterHeight);
 assertNotNull("Expected verticalScrollBar instance", jTailPanel.verticalScrollBar);
 }

 @Test
 public void testGetCharacterHeight() {
 // Test getCharacterHeight method
 assertEquals("Expected characterHeight to be an integer", jTailPanel.getCharacterHeight(), jTailPanel.characterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 // Test getVerticalScrollBar method
 assertSame("Expected verticalScrollBar instance", jTailPanel.verticalScrollBar, jTailPanel.getVerticalScrollBar());
 }

 @Test
 public void testUpdateMethodWithResizeEvent() {
 // Prepare test data
 final int firstLine = 0;
 final int maxLine = 10;
 final int nbDisplayedLines = 5;
 final int minLine = 5;
 final JTailModelEvent resizedEvent = new JTailModelEvent(mockModel, JTailModelEvent.Type.RESIZED, "resized event", firstLine, maxLine, nbDisplayedLines, minLine);

 // Setup expectations
 mockModel.addObserver(jTailPanel);

 // Test method execution
 jTailPanel.update(mockModel, resizedEvent);

 // Verify results
 assertEquals("Expected firstLine to be the same", firstLine, jTailPanel.verticalScrollBar.getModel().getValue());
 assertEquals("Expected maxLine to be the same", maxLine, jTailPanel.verticalScrollBar.getModel().getMaximum());
 assertEquals("Expected nbDisplayedLines to be the same", nbDisplayedLines, jTailPanel.verticalScrollBar.getModel().getExtent());
 }

 @Test
 public void testUpdateMethodWithScrolledEvent() {
 // Prepare test data
 final String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6";
 final String lineNumbers = "1\n2\n3\n4\n5\n6";
 mockModel.setContent(lines);
 mockModel.setLineNumbers(lineNumbers);
 final JTailModelEvent scrolledEvent = new JTailModelEvent(mockModel, JTailModelEvent.Type.SCROLLED, "scrolled event", 2, 5);

 // Setup expectations
 mockModel.addObserver(jTailPanel);
 jTailPanel.updateDataTable(mockModel.getLineNumbers(), mockModel.getContent());

 // Test method execution
 jTailPanel.update(mockModel, scrolledEvent);

 // Verify results
 assertTrue("Expected updateDataTable method to be called", jTailPanel.tableModel.getRowCount() > 0);
 }

 @Test
 public void testUpdateDataTableMethod() {
 // Prepare test data
 final String lineNumbers = "1\n2\n3\n4\n5\n6";
 final String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6";

 // Test method execution
 jTailPanel.updateDataTable(lineNumbers, lines);

 // Verify results
 assertEquals("Expected lineNumbers to be the same", lineNumbers, jTailPanel.tableModel.getDataVector().get(0).toString());
 assertTrue("Expected lines to be the same", jTailPanel.dataTable.getValueAt(0, 1).toString().equals(lines.split("\n")[0]));
 }

 @After
 public void tearDown() {
 // Clean up
 mockModel.removeObserver(jTailPanel);
 }
}