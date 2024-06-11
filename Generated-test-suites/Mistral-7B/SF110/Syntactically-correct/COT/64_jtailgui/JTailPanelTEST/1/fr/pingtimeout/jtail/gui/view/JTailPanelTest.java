package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.model.JTailModelType;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.util.JTailLogger;

public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel jTailPanel;

 @Before
 public void setUp() {
 JTailLogger.init();
 model = new JTailModel(new String[] { "line1", "line2", "line3" }, Arrays.asList(Arrays.asList(1, 2, 3)));
 jTailPanel = new JTailPanel(model);
 }

 @Test
 public void testJTailPanelConstruction() {
 assertEquals(model, jTailPanel.getModel());
 assertNotNull(jTailPanel.getVerticalScrollBar());
 assertNotNull(jTailPanel.getDataTable());
 assertNotNull(jTailPanel.getTableModel());
 }

 @Test
 public void testGetCharacterHeight() {
 int characterHeight = jTailPanel.getCharacterHeight();
 assertEquals("Incorrect character height", 16, characterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 assertNotNull("Vertical scroll bar should not be null", verticalScrollBar);
 }

 @Test
 public void testUpdateOnModelEventResized() {
 JTailModelEvent event = new JTailModelEvent(model, JTailModelType.RESIZED, null);
 jTailPanel.update(model, event);
 JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 int lineNumber = jTailPanel.getDataTable().getRowCount();
 assertEquals("Number of lines should be correct", 3, lineNumber);
 int maxLine = jTailPanel.getDataTable().getModel().getRowCount();
 assertEquals("Maximum line number should be correct", 3, maxLine);
 int firstLine = verticalScrollBar.getValue();
 assertEquals("First line should be correct", 0, firstLine);
 int nbDisplayedLines = verticalScrollBar.getExtent();
 assertEquals("Number of displayed lines should be correct", 3, nbDisplayedLines);
 assertTrue("Vertical scroll bar should be enabled", verticalScrollBar.isEnabled());
 }

 @Test
 public void testUpdateOnModelEventScrolled() {
 JTailModelEvent event = new JTailModelEvent(model, JTailModelType.SCROLLED, null);
 jTailPanel.update(model, event);
 int lineNumber = jTailPanel.getDataTable().getRowCount();
 assertEquals("Number of lines should be correct", 3, lineNumber);
 }

 @Test
 public void testUpdateDataTable() {
 model.setContent(new String[] { "line1", "line2", "line3", "line4", "line5" });
 jTailPanel.update(model, new JTailModelEvent(model, JTailModelType.RESIZED, null));
 int lineNumbersSize = jTailPanel.getDataTable().getModel().getColumnCount();
 assertEquals("Number of columns should be correct", 2, lineNumbersSize);
 int lineNumber = jTailPanel.getDataTable().getRowCount();
 assertEquals("Number of lines should be correct", 5, lineNumber);
 }

 @Test
 public void testUpdateDataTableWithEmptyContent() {
 model.setContent(new String[0]);
 jTailPanel.update(model, new JTailModelEvent(model, JTailModelType.RESIZED, null));
 int lineNumbersSize = jTailPanel.getDataTable().getModel().getColumnCount();
 assertEquals("Number of columns should be correct", 2, lineNumbersSize);
 int lineNumber = jTailPanel.getDataTable().getRowCount();
 assertEquals("Number of lines should be empty", 0, lineNumber);
 }

 @Test
 public void testScrollBarAdjustmentController() {
 ScrollBarAdjustmentController controller = new ScrollBarAdjustmentController(jTailPanel, model);
 controller.valueChanged(new AdjustmentEvent(5, 7, 1, 2, 3, 4));
 int lineNumber = jTailPanel.getDataTable().getModel().getValueAt(0, 0).toString().parseInt();
 assertEquals("Incorrect line number", 6, lineNumber);
 }

 @Test(expected = NumberFormatException.class)
 public void testScrollBarAdjustmentControllerWithInvalidInput() {
 ScrollBarAdjustmentController controller = new ScrollBarAdjustmentController(jTailPanel, model);
 controller.valueChanged(new AdjustmentEvent("A", 7, 1, 2, 3, 4));
 }
}