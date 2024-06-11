package fr.pingtimeout.jtail.gui.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.model.JTailModelFactory;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.gui.view.JTailSizeController;

public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel jTailPanel;

 @Before
 public void setUp() {
 model = JTailModelFactory.createInstance();
 jTailPanel = new JTailPanel(model);
 }

 @Test
 public void testJTailPanelConstruction() {
 assertNotNull(jTailPanel);
 }

 @Test
 public void testGetCharacterHeight() {
 final int expectedCharacterHeight = 15; // Assuming the font size is 15 in your application
 assertEquals(expectedCharacterHeight, jTailPanel.getCharacterHeight());
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertNotNull(jTailPanel.getVerticalScrollBar());
 }

 @Test
 public void testUpdateMethodWithResizedEvent() {
 model.firePropertyChange(JTailModel.PROPERTY_WIDTH, 0, 500);
 model.firePropertyChange(JTailModel.PROPERTY_HEIGHT, 0, 500);
 model.firePropertyChange(JTailModel.PROPERTY_LINES, null, new String[]{"Line 1", "Line 2", "Line 3"});

 final BoundedRangeModel verticalScrollBarModel = jTailPanel.getVerticalScrollBar().getModel();
 assertEquals(0, verticalScrollBarModel.getMinimum());
 assertEquals(0, verticalScrollBarModel.getValue());
 assertEquals(3, verticalScrollBarModel.getExtent());
 assertEquals(3, verticalScrollBarModel.getMaximum());
 assertFalse(verticalScrollBarModel.isEnabled());
 }

 @Test
 public void testUpdateMethodWithScrolledEvent() {
 model.firePropertyChange(JTailModel.PROPERTY_LINES, null, new String[]{"Line 1", "Line 2", "Line 3", "Line 4", "Line 5"});
 model.firePropertyChange(JTailModel.PROPERTY_CURRENT_LINE, 3, 4);

 final BoundedRangeModel verticalScrollBarModel = jTailPanel.getVerticalScrollBar().getModel();
 assertEquals(1, verticalScrollBarModel.getMinimum());
 assertEquals(1, verticalScrollBarModel.getValue());
 assertEquals(3, verticalScrollBarModel.getExtent());
 assertEquals(5, verticalScrollBarModel.getMaximum());
 assertTrue(verticalScrollBarModel.isEnabled());
 }

 @Test
 public void testUpdateDataTable() {
 model.firePropertyChange(JTailModel.PROPERTY_LINES, null, new String[]{"Line 1", "Line 2", "Line 3", "Line 4", "Line 5"});
 model.firePropertyChange(JTailModel.PROPERTY_WIDTH, 0, 500);
 model.firePropertyChange(JTailModel.PROPERTY_HEIGHT, 0, 500);

 jTailPanel.update(model, new JTailModelEvent(JTailModelEvent.Type.RESIZED, null));

 final int expectedNumberOfRows = 5;
 assertEquals(expectedNumberOfRows, jTailPanel.tableModel.getRowCount());
 }
}