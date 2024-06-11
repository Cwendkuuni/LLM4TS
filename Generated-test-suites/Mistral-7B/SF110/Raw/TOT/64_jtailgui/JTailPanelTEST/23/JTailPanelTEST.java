package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.controller.JTailModelEvent;
import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent.Type;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.gui.view.JTailSizeController;

public class JTailPanelTest {

 private JTailModel mockModel;
 private JTailPanel jTailPanel;

 @Before
 public void setUp() {
 mockModel = Mockito.mock(JTailModel.class);
 jTailPanel = new JTailPanel(mockModel);
 }

 @Test
 public void testInitialization() {
 // Test if the JTailPanel is initialized correctly
 assertEquals("The number of components in the JTailPanel should be 3", 3, jTailPanel.getComponentCount());
 }

 @Test
 public void testGetCharacterHeight() {
 // Test the getCharacterHeight method
 int characterHeight = jTailPanel.getCharacterHeight();
 assertEquals("The character height should be a positive number", characterHeight, characterHeight > 0 ? characterHeight : 0);
 }

 @Test
 public void testGetVerticalScrollBar() {
 // Test the getVerticalScrollBar method
 JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 assertNotNull("The vertical scroll bar should not be null", verticalScrollBar);
 }

 @Test
 public void testUpdateWithModelEvent_Resized() {
 // Test the update method with a RESIZED event
 JTailModelEvent resizedEvent = new JTailModelEvent(mockModel, Type.RESIZED, 1, 10, 5, 20);
 jTailPanel.update(mockModel, resizedEvent);

 // Test if the vertical scroll bar values have been updated
 JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 Mockito.verify(verticalScrollBar, Mockito.times(1)).setValueIsAdjusting(true);
 Mockito.verify(verticalScrollBar, Mockito.times(1)).setValue(Mockito.eq(10));
 Mockito.verify(verticalScrollBar, Mockito.times(1)).setMinimum(Mockito.eq(1));
 Mockito.verify(verticalScrollBar, Mockito.times(1)).setExtent(Mockito.eq(5));
 Mockito.verify(verticalScrollBar, Mockito.times(1)).setMaximum(Mockito.eq(20));
 Mockito.verify(verticalScrollBar, Mockito.times(1)).setValueIsAdjusting(false);

 // Test if the vertical scroll bar is enabled or disabled based on the event
 Mockito.verify(verticalScrollBar, Mockito.atLeast(1)).setEnabled(eq(false));
 Mockito.verify(verticalScrollPanel, Mockito.atLeast(1)).setEnabled(eq(false));
 }

 @Test
 public void testUpdateWithModelEvent_Scrolled() {
 // Test the update method with a SCROLLED event
 JTailModelEvent scrolledEvent = new JTailModelEvent(mockModel, Type.SCROLLED, 10, 20, 30, 40);
 jTailPanel.update(mockModel, scrolledEvent);

 // Test if the data table has been updated
 String[] lineNumbers = jTailPanel.model.getLineNumbers().split("\n");
 String[] lines = jTailPanel.model.getContent().split("\n");
 String[] data = jTailPanel.tableModel.getDataVector().toArray(new String[0][2]);

 assertEquals("The number of rows in the data table should be equal to the number of lines in the model", lines.length, data.length);
 for (int i = 0; i < lines.length; i++) {
 String lineNumber = lineNumbers[i];
 String line = lines[i];
 assertEquals("The line number and content in the data table should be equal to the line number and content in the model", lineNumber, data[i][0]);
 assertEquals("The line number and content in the data table should be equal to the line number and content in the model", line, data[i][1]);
 }
 }

 @Test
 public void testUpdateDataTable_ValidParameters() {
 // Test the updateDataTable method with valid parameters
 String lineNumbers = "1\n2\n3\n";
 String lines = "Line 1\nLine 2\nLine 3\n";
 jTailPanel.updateDataTable(lineNumbers, lines);

 // Test if the number of rows in the data table is equal to the number of lines in the model
 int numberOfRows = jTailPanel.dataTable.getRowCount();
 assertEquals("The number of rows in the data table should be equal to the number of lines in the model", numberOfRows, 3);

 // Test if the content of the data table matches the content of the model
 String[] lineNumbersArray = lineNumbers.split("\n");
 String[] linesArray = lines.split("\n");
 String[] data = jTailPanel.tableModel.getDataVector().toArray(new String[0][2]);
 for (int i = 0; i < lineNumbersArray.length; i++) {
 String lineNumber = lineNumbersArray[i];
 String line = linesArray[i];
 assertEquals("The line number and content in the data table should be equal to the line number and content in the model", lineNumber, data[i][0]);
 assertEquals("The line number and content in the data table should be equal to the line number and content in the model", line, data[i][1]);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateDataTable_InvalidParameters() {
 // Test the updateDataTable method with invalid parameters
 jTailPanel.updateDataTable(null, "Line 1\nLine 2\nLine 3\n");
 }

 @Test
 public void testAutoResizeDataTable() {
 // Test the auto resize feature of the data table
 jTailPanel.dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
 jTailPanel.dataTable.setPreferredSize(new Dimension(800, 600));
 jTailPanel.updateDataTable("1\n1000\n1001\n1002\n1003", "Line 1\nA very very very very very very very very very very very very very very very very very very very very very long long long long long line 2\nLine 3\nLine 4");

 // Test if the columns have been resized correctly
 TableColumn numbersColumn = jTailPanel.dataTable.getColumnModel().getColumn(0);
 TableColumn linesColumn = jTailPanel.dataTable.getColumnModel().getColumn(1);

 int numbersColumnWidth = numbersColumn.getWidth();
 int linesColumnWidth = linesColumn.getWidth();

 assertEquals("The width of the first column should be greater than zero", numbersColumnWidth, numbersColumnWidth > 0 ? numbersColumnWidth : 0);
 assertEquals("The width of the second column should be greater than zero", linesColumnWidth, linesColumnWidth > 0 ? linesColumnWidth : 0);

 int highestLineNumberLength = Arrays.stream(lineNumbers.split("\n")).map(s -> s.length()).max().get();
 int dataTableWidth = jTailPanel.dataTable.getWidth();

 int expectedNumbersColumnWidth = highestLineNumberLength * jTailPanel.maxCharacterWidth;
 int expectedLinesColumnWidth = dataTableWidth - expectedNumbersColumnWidth;

 assertEquals("The width of the first column should be equal to the calculated width", numbersColumnWidth, expectedNumbersColumnWidth);
 assertEquals("The width of the second column should be equal to the calculated width", linesColumnWidth, expectedLinesColumnWidth);
 }
}