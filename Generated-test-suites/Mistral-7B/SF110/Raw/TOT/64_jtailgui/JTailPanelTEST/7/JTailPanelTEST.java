import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.controller.JTailModelEvent;
import fr.pingtimeout.jtail.gui.controller.JTailSizeController;
import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;

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
 assertEquals("Expected JTailModel instance", model, jTailPanel.model);
 assertNotNull("Expected JScrollPane instance", jTailPanel.scrollPane);
 assertNotNull("Expected JTable instance", jTailPanel.dataTable);
 assertNotNull("Expected DefaultTableModel instance", jTailPanel.tableModel);
 assertNotNull("Expected JScrollBar instance", jTailPanel.verticalScrollBar);
 }

 @Test
 public void testUpdateWithResizedEvent() {
 // Setup
 final int firstLine = 5;
 final int nbDisplayedLines = 10;
 final int maxLine = 20;
 final JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, firstLine, nbDisplayedLines, maxLine);
 model.notifyObservers(event);

 // When
 jTailPanel.update(model, event);

 // Then
 final JScrollBar scrollBar = jTailPanel.verticalScrollBar;
 assertEquals("Expected the minimum value to be set correctly", firstLine, scrollBar.getMinimum());
 assertEquals("Expected the value to be set correctly", firstLine, scrollBar.getValue());
 assertEquals("Expected the extent to be set correctly", nbDisplayedLines, scrollBar.getExtent());
 assertEquals("Expected the maximum value to be set correctly", maxLine, scrollBar.getMaximum());
 assertTrue("Expected scrolling to be enabled after the event", scrollBar.isEnabled());
 }

 @Test
 public void testUpdateWithScrolledEvent() {
 // Setup
 final String lineNumbers = "5\n10\n15\n20";
 final String lines = "Line 5\nLine 10\nLine 15\nLine 20";
 model.setLineNumbers(lineNumbers);
 model.setContent(lines);

 // When
 final JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 5, 5, 10);
 model.notifyObservers(event);
 jTailPanel.update(model, event);

 // Then
 final String[] lineNumbersArray = jTailPanel.tableModel.getColumnNames();
 assertEquals("Expected column names to be set correctly", new String[] { "#", "" }, lineNumbersArray);
 final int numberOfRows = jTailPanel.tableModel.getRowCount();
 assertEquals("Expected number of rows to be equal to the number of lines", 5, numberOfRows);
 for (int i = 0; i < numberOfRows; ++i) {
 final Object[] rowData = jTailPanel.tableModel.getDataVector().getElementAt(i);
 assertEquals("Expected line number to be correct", "5", rowData[0]);
 assertEquals("Expected line content to be correct", "Line 5", rowData[1]);
 }
 }
}