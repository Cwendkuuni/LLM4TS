package fr.pingtimeout.jtail.gui.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.gui.view.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel jTailPanel;

 @Before
 public void setUp() {
 model = new JTailModel();
 jTailPanel = new JTailPanel(model);
 }

 @After
 public void tearDown() {
 model = null;
 jTailPanel = null;
 }

 @Test
 public void testJTailPanelConstruction() {
 JTailLogger mockLogger = new JTailLogger() {
 @Override
 public void debug(String message) {
 // Do nothing
 }
 };

 assertNotNull("JTailPanel should not be null", jTailPanel);
 assertEquals("Model should be the same instance", model, jTailPanel.getModel());
 }

 @Test
 public void testGetCharacterHeight() {
 FontMetrics metrics = jTailPanel.dataTable.getFontMetrics(jTailPanel.dataTable.getFont());
 int characterHeight = metrics.getHeight() - 1;
 assertEquals("Character height should match", jTailPanel.getCharacterHeight(), characterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertNotNull("Vertical scroll bar should not be null", jTailPanel.getVerticalScrollBar());
 }

 @Test
 public void testUpdate() {
 JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, this.model, 10, 10, 5, 10, 5, 15);
 jTailPanel.update(jTailPanel.getModel(), event);

 JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 BoundedRangeModel rangeModel = verticalScrollBar.getModel();
 assertEquals("Minimum value should match", 10, rangeModel.getMinimum());
 assertEquals("Value should match", 10, rangeModel.getValue());
 assertEquals("Extent should match", 5, rangeModel.getExtent());
 assertEquals("Maximum value should match", 15, rangeModel.getMaximum());
 }

 @Test
 public void testUpdateDataTable() {
 model.setLineNumbers("1\n2\n3\n4\n5");
 model.setContent("This is line 1\nThis is line 2\nThis is line 3\nThis is line 4\nThis is line 5");
 jTailPanel.update(model, null);

 assertEquals("Number of rows should match", 5, jTailPanel.dataTable.getRowCount());
 for (int i = 0; i < 5; ++i) {
 String lineNumber = (String)jTailPanel.dataTable.getValueAt(i, 0);
 String line = (String)jTailPanel.dataTable.getValueAt(i, 1);
 assertEquals("Line number should match", (i + 1) + "", lineNumber);
 assertEquals("Line should match", "This is line " + (i + 1), line);
 }
 }
}