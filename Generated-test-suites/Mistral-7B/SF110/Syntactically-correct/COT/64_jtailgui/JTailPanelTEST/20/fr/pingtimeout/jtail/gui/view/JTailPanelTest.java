package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.util.JTailLogger;

public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel jTailPanel;

 @Before
 public void setUp() {
 model = new JTailModel();
 jTailPanel = new JTailPanel(model);
 }

 @Test
 public void testJTailPanelConstructor() {
 // Check if the JTailPanel is constructed correctly
 assertNotNull(jTailPanel);
 }

 @Test
 public void testGetCharacterHeight() {
 // Test the getCharacterHeight method
 int characterHeight = jTailPanel.getCharacterHeight();
 assertEquals("Incorrect character height", 1, characterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 // Test the getVerticalScrollBar method
 JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 assertNotNull("VerticalScrollBar should not be null", verticalScrollBar);
 }

 @Test
 public void testUpdateMethodWithResizeEvent() {
 // Test the update method with a resize event
 JTailModelEvent resizeEvent = new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, 0, 10, 10, 20, 30);
 jTailPanel.update(model, resizeEvent);
 assertFalse("VerticalScrollBar should be enabled after resize", jTailPanel.isVerticalScrollBarDisabled());
 }

 @Test
 public void testUpdateMethodWithScrollEvent() {
 // Test the update method with a scroll event
 JTailModelEvent scrollEvent = new JTailModelEvent(model, JTailModelEvent.Type.SCROLLED, 0, 10, 10, 20, 30);
 jTailPanel.update(model, scrollEvent);
 assertTrue("VerticalScrollBar should be disabled after scroll", jTailPanel.isVerticalScrollBarDisabled());
 }

 @Test(expected = NullPointerException.class)
 public void testUpdateMethodWithNullModel() {
 JTailPanel jTailPanelNullModel = new JTailPanel(null);
 jTailPanelNullModel.update(null, new JTailModelEvent(null, JTailModelEvent.Type.RESIZED, 0, 10, 10, 20, 30));
 }

 @Test
 public void testUpdateDataTableWithEmptyContent() {
 // Test the updateDataTable method with empty content
 jTailPanel.updateDataTable("", "");
 assertTrue("DataTable should be empty", jTailPanel.dataTable.getRowCount() == 0);
 }

 @Test
 public void testUpdateDataTableWithValidData() {
 // Test the updateDataTable method with valid data
 jTailPanel.updateDataTable("1\n2\n3", "Line 1\nLine 2\nLine 3");
 assertEquals("Number of rows in DataTable should be equal to the number of lines", 3, jTailPanel.dataTable.getRowCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateDataTableWithInvalidData() {
 jTailPanel.updateDataTable("1\n2", "Line 1\nLine 2");
 }

 @Test
 public void testUpdateDataTableWithInvalidLineNumberFormat() {
 jTailPanel.updateDataTable("1\n3a\n2", "Line 1\nLine 2");
 }
}