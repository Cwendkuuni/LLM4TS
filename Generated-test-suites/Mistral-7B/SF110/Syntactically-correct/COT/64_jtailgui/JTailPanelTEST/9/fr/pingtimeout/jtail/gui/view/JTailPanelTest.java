import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.controller.JTailSizeController;
import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.util.JTailLogger;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.util.Random;

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
 // Check that the JTailPanel is initialized correctly
 assertEquals("JTailPanel should have a JTailModel as an attribute", jTailModel, jTailPanel.model);
 assertNotNull("JScrollPane should not be null", jTailPanel.scrollPane);
 assertNotNull("JScrollBar should not be null", jTailPanel.verticalScrollBar);
 assertNotNull("JTable should not be null", jTailPanel.dataTable);
 assertNotNull("DefaultTableModel should not be null", jTailPanel.tableModel);
 // Check the initial state of the scrollBar
 assertEquals("The scrollBar minimum value should be 0", 0, jTailPanel.verticalScrollBar.getMinimum());
 assertEquals("The scrollBar maximum value should be greater than the minimum value", 10, jTailPanel.verticalScrollBar.getMaximum());
 }

 @Test
 public void testGetCharacterHeight() {
 final int expectedCharacterHeight = new FontMetrics(jTailPanel.dataTable.getFont()).getHeight() - 1;
 final int actualCharacterHeight = jTailPanel.getCharacterHeight();
 assertEquals("getCharacterHeight should return the expected character height", expectedCharacterHeight, actualCharacterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 final JScrollBar expectedVerticalScrollBar = jTailPanel.verticalScrollBar;
 final JScrollBar actualVerticalScrollBar = jTailPanel.getVerticalScrollBar();
 assertEquals("getVerticalScrollBar should return the same instance as the one initialized in the constructor", expectedVerticalScrollBar, actualVerticalScrollBar);
 }

 @Test
 public void testUpdateWithResizedEvent() {
 // Initialize a test model with some data
 jTailModel.setContent("Line 1\nLine 2\nLine 3\nLine 4\nLine 5");
 jTailModel.setNbDisplayedLines(3);
 jTailModel.setFirstLine(1);
 jTailModel.setMinLine(1);
 jTailModel.setMaxLine(5);

 // Simulate a resized event
 jTailPanel.update(jTailModel, new JTailModelEvent(JTailModelEvent.Type.RESIZED, jTailModel));

 // Check that the scrollBar values have been updated
 final BoundedRangeModel rangeModel = jTailPanel.verticalScrollBar.getModel();
 assertEquals("The scrollBar minimum value should be the minimum line number", 1, rangeModel.getMinimum());
 assertEquals("The scrollBar value should be the first line number", 1, rangeModel.getValue());
 assertEquals("The scrollBar extent should be the number of displayed lines", 3, rangeModel.getExtent());
 assertEquals("The scrollBar maximum value should be the maximum line number", 5, rangeModel.getMaximum());

 // Check that the data table has been updated
 final String[] lines = jTailModel.getContent().split("\n");
 final int linesLength = lines.length;
 final String[] expectedData = new String[linesLength];
 System.arraycopy(lines, 0, expectedData, 0, linesLength);
 final Object[][] data = jTailPanel.tableModel.getDataVector().toArray();
 for (int i = 0; i < linesLength; ++i) {
 assertEquals("The data table should contain the expected line number and content", expectedData[i], data[i][0]);
 assertEquals("The data table should contain the expected line content", lines[i], data[i][1]);
 }
 }

 @Test
 public void testUpdateWithScrolledEvent() {
 // Initialize a test model with some data
 jTailModel.setContent("Line 1\nLine 2\nLine 3\nLine 4\nLine 5");
 jTailModel.setNbDisplayedLines(3);
 jTailModel.setFirstLine(1);
 jTailModel.setMinLine(1);
 jTailModel.setMaxLine(5);

 // Simulate a scroll event
 jTailPanel.update(jTailModel, new JTailModelEvent(JTailModelEvent.Type.SCROLLED, jTailModel));

 // Check that the data table has been updated
 final String[] lines = jTailModel.getContent().split("\n");
 final int linesLength = lines.length;
 final String[] expectedData = new String[linesLength];
 System.arraycopy(lines, 0, expectedData, 0, linesLength);
 final Object[][] data = jTailPanel.tableModel.getDataVector().toArray();
 for (int i = 0; i < linesLength; ++i) {
 assertEquals("The data table should contain the expected line number and content", expectedData[i], data[i][0]);
 assertEquals("The data table should contain the expected line content", lines[i], data[i][1]);
 }
 }

 @Test
 public void testUpdateDataTableWithEmptyContent() {
 // Initialize a test model with an empty content
 jTailModel.setContent("");
 jTailModel.setNbDisplayedLines(3);
 jTailModel.setFirstLine(1);
 jTailModel.setMinLine(1);
 jTailModel.setMaxLine(1);

 // Update the data table
 jTailPanel.updateDataTable(jTailModel.getLineNumbers(), jTailModel.getContent());

 // Check that the data table is empty
 final int rowCountBefore = jTailPanel.tableModel.getRowCount();
 jTailPanel.tableModel.fireTableDataChanged();
 final int rowCountAfter = jTailPanel.tableModel.getRowCount();
 assertEquals("The data table should have one less row after updating it with an empty content", rowCountBefore - 1, rowCountAfter);
 }

 @Test
 public void testUpdateDataTableWithNullContent() {
 // Initialize a test model with a null content
 jTailModel.setContent(null);
 jTailModel.setNbDisplayedLines(3);
 jTailModel.setFirstLine(1);
 jTailModel.setMinLine(1);
 jTailModel.setMaxLine(1);

 // Update the data table
 jTailPanel.updateDataTable(jTailModel.getLineNumbers(), jTailModel.getContent());

 // Check that the data table is empty
 final int rowCountBefore = jTailPanel.tableModel.getRowCount();
 jTailPanel.tableModel.fireTableDataChanged();
 final int rowCountAfter = jTailPanel.tableModel.getRowCount();
 assertEquals("The data table should have no rows after updating it with a null content", 0, rowCountAfter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateDataTableWithInvalidLineNumbers() {
 // Initialize a test model with some data
 jTailModel.setContent("Line 1\nLine 2\nLine 3\nLine 4\nLine 5");
 jTailModel.setNbDisplayedLines(3);
 jTailModel.setFirstLine(1);
 jTailModel.setMinLine(1);
 jTailModel.setMaxLine(5);

 // Update the data table with invalid line numbers
 String[] invalidLineNumbers = new String[]{"", "Line 6"};
 jTailPanel.updateDataTable(invalidLineNumbers, jTailModel.getContent());
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testUpdateDataTableWithLessLinesThanDisplayed() {
 // Initialize a test model with some data
 jTailModel.setContent("Line 1\nLine 2\nLine 3");
 jTailModel.setNbDisplayedLines(4);
 jTailModel.setFirstLine(1);
 jTailModel.setMinLine(1);
 jTailModel.setMaxLine(3);

 // Update the data table
 jTailPanel.updateDataTable(jTailModel.getLineNumbers(), jTailModel.getContent());
 }

 @Test
 public void testUpdateDataTableWithContentLongerThanScreenWidth() {
 // Initialize a test model with some data
 jTailModel.setContent("Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8\nLine 9\nLine 10");
 jTailModel.setNbDisplayedLines(4);
 jTailModel.setFirstLine(1);
 jTailModel.setMinLine(1);
 jTailModel.setMaxLine(10);

 // Update the data table
 jTailPanel.updateDataTable(jTailModel.getLineNumbers(), jTailModel.getContent());

 // Check that the columns widths have been updated
 final TableColumn numbersColumn = jTailPanel.dataTable.getColumnModel().getColumn(0);
 final TableColumn linesColumn = jTailPanel.dataTable.getColumnModel().getColumn(1);
 final int screenWidth = new Random().nextInt(1000) + 50; // Random width between 50 and 1000
 final int expectedNumbersColumnWidth = (int) Math.ceil((Math.log10(jTailModel.getLineNumbers().split("\n").length) + 1) * 5); // Calculate the expected width based on the number of lines
 final int expectedLinesColumnWidth = screenWidth - expectedNumbersColumnWidth;
 final Component component = jTailPanel;
 final Dimension preferredSize = component.getPreferredSize();
 component.setPreferredSize(new Dimension(screenWidth, preferredSize.height));
 jTailPanel.updateDataTable(jTailModel.getLineNumbers(), jTailModel.getContent());
 component.setPreferredSize(preferredSize);

 final int actualNumbersColumnWidth = numbersColumn.getPreferredWidth();
 final int actualLinesColumnWidth = linesColumn.getPreferredWidth();
 assertEquals("The numbers column width should be the expected width", expectedNumbersColumnWidth, actualNumbersColumnWidth);
 assertEquals("The lines column width should be the expected width", expectedLinesColumnWidth, actualLinesColumnWidth);
 }
}