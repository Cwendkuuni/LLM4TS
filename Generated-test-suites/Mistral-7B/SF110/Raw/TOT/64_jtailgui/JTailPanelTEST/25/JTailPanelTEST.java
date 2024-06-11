package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.controller.JTailSizeController;
import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import javax.swing.*;
import javax.swing.table.*;

public class JTailPanelTest {

 private JTailPanel jTailPanel;
 private JTailModel mockModel;

 @Before
 public void setUp() {
 mockModel = new JTailModel();
 jTailPanel = new JTailPanel(mockModel);
 }

 @Test
 public void testConstructor() {
 assertNotNull(jTailPanel);
 assertNotNull(jTailPanel.model);
 assertNotNull(jTailPanel.scrollPane);
 assertNotNull(jTailPanel.dataTable);
 assertNotNull(jTailPanel.tableModel);
 assertNotNull(jTailPanel.verticalScrollBar);
 }

 @Test
 public void testGetCharacterHeight() {
 final int expectedCharacterHeight = 15; // Assuming a font size of 15 pixels for the table
 final int actualCharacterHeight = jTailPanel.getCharacterHeight();
 assertEquals(expectedCharacterHeight, actualCharacterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 final JScrollBar expectedVerticalScrollBar = new JScrollBar();
 final JScrollBar actualVerticalScrollBar = jTailPanel.getVerticalScrollBar();
 assertEquals(expectedVerticalScrollBar, actualVerticalScrollBar);
 }

 @Test
 public void testUpdate_ModelResized() {
 // Prepare mock events
 final int firstLine = 0;
 final int maxLine = 10;
 final int nbDisplayedLines = 5;
 final int minLine = 4;
 final JTailModelEvent resizedEvent = new JTailModelEvent(mockModel, JTailModelEvent.Type.RESIZED, firstLine, maxLine, nbDisplayedLines, minLine);

 // Simulate update event
 jTailPanel.update(mockModel, resizedEvent);

 // Check vertical scroll bar values
 final JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 final BoundedRangeModel rangeModel = verticalScrollBar.getModel();
 assertEquals(firstLine, rangeModel.getMinimum());
 assertEquals(minLine, rangeModel.getValue());
 assertEquals(nbDisplayedLines, rangeModel.getExtent());
 assertEquals(maxLine, rangeModel.getMaximum());

 // Check dataTable contents
 final TableModel tableModel = jTailPanel.dataTable.getModel();
 final int numberOfRows = tableModel.getRowCount();
 assertEquals(maxLine + 1, numberOfRows); // One extra row for line numbers

 // Check line numbers and lines in the dataTable
 for (int i = 0; i < maxLine + 1; i++) {
 final int lineNumberIndex = i;
 final String expectedLineNumber = Integer.toString(lineNumberIndex + 1);
 final String expectedLine = (i < 5) ? "Line " + (lineNumberIndex + 1) : "";
 final Object[] expectedData = { expectedLineNumber, expectedLine };
 final Object[] actualData = tableModel.getDataVector().getElementAt(lineNumberIndex);
 assertArrayEquals(expectedData, actualData);
 }
 }

 @Test
 public void testUpdate_ModelScrolled() {
 // Prepare mock events
 final JTailModelEvent scrolledEvent = new JTailModelEvent(mockModel, JTailModelEvent.Type.SCROLLED);

 // Simulate scroll event
 jTailPanel.update(mockModel, scrolledEvent);

 // Check dataTable contents
 final TableModel tableModel = jTailPanel.dataTable.getModel();
 final int numberOfRows = tableModel.getRowCount();
 assertEquals(mockModel.getSize(), numberOfRows); // Size of the model should be equal to the number of rows in the dataTable
 }

 @Test
 public void testUpdate_SizeChangeController() {
 // Prepare mock components
 final JFrame frame = new JFrame();
 final JComponent component = new JComponent() {};

 // Set up size controller
 final JTailSizeController sizeController = new JTailSizeController(jTailPanel, mockModel);
 frame.addComponentListener(sizeController);

 // Set the frame size
 final int width = 300;
 final int height = 400;
 frame.setSize(width, height);

 // Simulate resize event
 frame.setSize(width + 10, height);

 // Check vertical scroll bar values
 final JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 final BoundedRangeModel rangeModel = verticalScrollBar.getModel();
 assertTrue(rangeModel.getValueIsAdjusting());
 }

 @Test
 public void testUpdate_ScrollBarAdjustmentController() {
 // Prepare mock events
 final JScrollBar mockScrollBar = new JScrollBar();
 final ScrollBarAdjustmentController scrollBarAdjustmentController = new ScrollBarAdjustmentController(jTailPanel, mockModel);
 mockScrollBar.addAdjustmentListener(scrollBarAdjustmentController);
 mockScrollBar.setValue(10);

 // Simulate scroll event
 mockScrollBar.setValue(20);

 // Check model line numbers
 final int expectedFirstLine = 11;
 final int expectedLastLine = 20;
 mockModel.firePropertyChange(JTailModel.PROPERTY_FIRST_LINE, 10, expectedFirstLine);
 mockModel.firePropertyChange(JTailModel.PROPERTY_MAX_LINE, 20, expectedLastLine);

 // Check dataTable contents
 final TableModel tableModel = jTailPanel.dataTable.getModel();
 final int numberOfRows = tableModel.getRowCount();
 assertEquals(expectedLastLine + 1, numberOfRows); // One extra row for line numbers

 // Check line numbers and lines in the dataTable
 for (int i = 0; i < expectedLastLine + 1; i++) {
 final int lineNumberIndex = i;
 final String expectedLineNumber = Integer.toString(lineNumberIndex + expectedFirstLine);
 final String expectedLine = (i < expectedLastLine) ? "Line " + (lineNumberIndex + expectedFirstLine + 1) : "";
 final Object[] expectedData = { expectedLineNumber, expectedLine };
 final Object[] actualData = tableModel.getDataVector().getElementAt(lineNumberIndex);
 assertArrayEquals(expectedData, actualData);
 }
 }
}