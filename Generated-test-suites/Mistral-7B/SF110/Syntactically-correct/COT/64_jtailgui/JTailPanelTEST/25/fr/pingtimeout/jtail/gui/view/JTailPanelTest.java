package fr.pingtimeout.jtail.gui.test.view;

import static org.junit.Assert.*;

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
 public void setup() {
 jTailModel = new JTailModel();
 jTailPanel = new JTailPanel(jTailModel);
 }

 @Test
 public void testJTailPanelConstructor() {
 // Check that a new JTailPanel is created
 assertNotNull(jTailPanel);
 }

 @Test
 public void testGetCharacterHeight() {
 // Check that characterHeight is correctly initialized
 int characterHeight = jTailPanel.getCharacterHeight();
 assertEquals("characterHeight should be initialized correctly", 1, characterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 // Check that verticalScrollBar is initialized
 assertNotNull(jTailPanel.getVerticalScrollBar());
 }

 @Test(expected = IllegalStateException.class)
 public void testUpdateWithNullModel() {
 jTailPanel = new JTailPanel(null);
 }

 @Test
 public void testUpdateWithModelResizedEvent() {
 // Simulate model resized event
 JTailModelEvent resizedEvent = new JTailModelEvent(jTailModel, JTailModelEvent.Type.RESIZED, 1, 10, 1, 10, "test");
 jTailPanel.update(jTailModel, resizedEvent);
 // Check that scrollbar values are updated
 JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 assertEquals("minimum should be set to 1", 1, verticalScrollBar.getValue());
 assertEquals("maximum should be set to 10", 10, verticalScrollBar.getMaximum());
 }

 @Test
 public void testUpdateWithModelScrolledEvent() {
 // Simulate model scrolled event
 JTailModelEvent scrolledEvent = new JTailModelEvent(jTailModel, JTailModelEvent.Type.SCROLLED, 1, "test");
 jTailPanel.update(jTailModel, scrolledEvent);
 // Check that datatable is updated
 TableModel tableModel = jTailPanel.getDataTable().getModel();
 String[] data = (String[])tableModel.getDataVector().toArray(new String[0]);
 assertEquals("data should be updated", "test\n", data[0]);
 }

 @Test
 public void testUpdateDataTable() {
 // Mock logger to avoid logging messages
 JTailLogger mockLogger = mock(JTailLogger.class);
 JTailPanel jTailPanelSpy = spy(new JTailPanel(jTailModel));
 doNothing().when(jTailPanelSpy).log(mockLogger, "DataTable width : {}", anyInt());
 doNothing().when(jTailPanelSpy).log(mockLogger, "Highest line number : {}", anyString());
 doNothing().when(jTailPanelSpy).log(mockLogger, "numbersColumnWidth : {}", anyInt());
 doNothing().when(jTailPanelSpy).log(mockLogger, "linesColumnWidth : {}", anyInt());

 // Call the method under test
 jTailPanelSpy.updateDataTable("1\n2\n3", "line1\nline2\nline3");

 // Check that datatable is updated
 TableModel tableModel = jTailPanelSpy.getDataTable().getModel();
 String[] data = (String[])tableModel.getDataVector().toArray(new String[0]);
 assertEquals("data should be updated", new String[]{"1", "line1"}, data);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateDataTableWithNullLineNumbers() {
 JTailPanel jTailPanel = new JTailPanel(jTailModel);
 jTailPanel.updateDataTable(null, jTailModel.getContent());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateDataTableWithNullContent() {
 JTailPanel jTailPanel = new JTailPanel(jTailModel);
 jTailPanel.updateDataTable(jTailModel.getLineNumbers(), null);
 }
}