package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.util.JTailLogger;

public class JTailPanelTest {

 private JTailPanel jTailPanel;
 private JTailModel jTailModel;

 @Before
 public void setUp() {
 jTailModel = new JTailModel();
 jTailPanel = new JTailPanel(jTailModel);
 }

 @Test
 public void testJTailPanelConstructor() {
 // Check if the constructor is called
 assertNotNull(jTailPanel);

 // Check if the observer is registered to the model
 assertEquals(1, jTailModel.getObserverCount());
 }

 @Test
 public void testGetCharacterHeight() {
 // Call the method
 final int result = jTailPanel.getCharacterHeight();

 // Check the result
 assertEquals(result, jTailPanel.characterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 // Call the method
 final JScrollBar result = jTailPanel.getVerticalScrollBar();

 // Check the result
 assertNotNull(result);
 }

 @Test
 public void testUpdateObserver() {
 // Set up the model
 final String lineNumbers = "1\n2\n3\n4\n5";
 final String lines = "Hello World\nThis is a test\nLine 3\nLine 4\nLine 5";
 jTailModel.setLineNumbers(lineNumbers);
 jTailModel.setContent(lines);

 // Call the update method
 jTailModel.fireNotification(new JTailModelEvent(JTailModelEvent.Type.RESIZED, lineNumbers, lines));

 // Check the data table
 final int rows = jTailPanel.dataTable.getRowCount();
 final String[] expectedData = new String[]{"1", "Hello World", "2", "This is a test", "3", "Line 3", "4", "Line 4", "5", "Line 5"};
 for (int i = 0; i < rows; ++i) {
 String[] data = jTailPanel.dataTable.getModel().getDataVector().get(i);
 assertArrayEquals(expectedData[i * 2], data);
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testUpdateObserverWithInvalidEventType() {
 jTailModel.fireNotification(new JTailModelEvent(JTailModelEvent.Type.INVALID, "lineNumbers", "content"));
 }

 @Test
 public void testUpdateObserverWithDisabledScrollbar() {
 // Set up the model
 final String lineNumbers = "1\n2\n3\n4\n5";
 final String lines = "Hello World\nThis is a test\nLine 3\nLine 4\nLine 5";
 jTailModel.setLineNumbers(lineNumbers);
 jTailModel.setContent(lines);
 jTailModel.fireNotification(new JTailModelEvent(JTailModelEvent.Type.RESIZED, lineNumbers, lines));
 jTailModel.fireNotification(new JTailModelEvent(JTailModelEvent.Type.RESIZED, lineNumbers, lines));
 jTailModel.fireNotification(new JTailModelEvent(JTailModelEvent.Type.RESIZED, lineNumbers, lines));

 // Check the scrollbar is disabled
 assertFalse(jTailPanel.isVerticalScrollBarEnabled());
 }

 @Test
 public void testUpdateObserverWithEnabledScrollbar() {
 // Set up the model
 final String lineNumbers = "1\n2\n3\n4\n5";
 final String lines = "Hello World\nThis is a test\nLine 3\nLine 4\nLine 5";
 jTailModel.setLineNumbers(lineNumbers);
 jTailModel.setContent(lines);
 jTailModel.fireNotification(new JTailModelEvent(JTailModelEvent.Type.RESIZED, lineNumbers, lines));
 jTailPanel.updateDataTable(lineNumbers, lines);
 jTailModel.fireNotification(new JTailModelEvent(JTailModelEvent.Type.RESIZED, lineNumbers, lines));
 jTailPanel.updateDataTable(lineNumbers, lines);

 // Check the scrollbar is enabled
 assertTrue(jTailPanel.isVerticalScrollBarEnabled());
 }

 @Test
 public void testUpdateDataTableWithEmptyModel() {
 // Set up the model
 jTailModel.setLineNumbers("");
 jTailModel.setContent("");

 // Call the update method
 jTailPanel.updateDataTable(jTailModel.getLineNumbers(), jTailModel.getContent());

 // Check the data table
 assertEquals(0, jTailPanel.dataTable.getRowCount());
 }

 @Test
 public void testUpdateDataTableWithNullModel() {
 // Call the update method
 jTailPanel.updateDataTable(null, jTailModel.getContent());

 // Check the data table
 assertEquals(0, jTailPanel.dataTable.getRowCount());
 }

 @Test
 public void testUpdateDataTableWithInvalidContent() {
 // Set up the model
 final String lineNumbers = "1\n2\n3\n4\n5";
 jTailModel.setLineNumbers(lineNumbers);
 final String invalidContent = null;
 jTailModel.setContent(invalidContent);

 // Call the update method
 jTailPanel.updateDataTable(lineNumbers, invalidContent);

 // Check the data table
 assertEquals(0, jTailPanel.dataTable.getRowCount());
 }

 @Test
 public void testUpdateDataTableWithEmptyContent() {
 // Set up the model
 final String lineNumbers = "1\n2\n3\n4\n5";
 jTailModel.setLineNumbers(lineNumbers);
 final String emptyContent = "";
 jTailModel.setContent(emptyContent);

 // Call the update method
 jTailPanel.updateDataTable(lineNumbers, emptyContent);

 // Check the data table
 assertEquals(0, jTailPanel.dataTable.getRowCount());
 }

 @Test
 public void testAutoResizeMode() {
 // Call the method
 jTailPanel.dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

 // Check the result
 assertEquals(JTable.AUTO_RESIZE_OFF, jTailPanel.dataTable.getAutoResizeMode());
 }

 @Test
 public void testResizingColumnWidths() {
 // Call the method
 final String lineNumbers = "1\n2\n3\n4\n5";
 final String lines = "Hello World\nThis is a test\nLine 3\nLine 4\nLine 5";
 jTailModel.setLineNumbers(lineNumbers);
 jTailModel.setContent(lines);
 jTailPanel.updateDataTable(lineNumbers, lines);

 // Check the columns widths
 final TableColumn numbersColumn = jTailPanel.dataTable.getColumnModel().getColumn(0);
 final TableColumn linesColumn = jTailPanel.dataTable.getColumnModel().getColumn(1);
 final int numbersColumnWidth = numbersColumn.getPreferredWidth();
 final int linesColumnWidth = linesColumn.getPreferredWidth();
 assertEquals(numbersColumnWidth, 15 * jTailPanel.maxCharacterWidth);
 assertEquals(linesColumnWidth, jTailPanel.dataTable.getWidth() - numbersColumnWidth);
 }

}