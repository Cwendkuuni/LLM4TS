package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.gui.view.*;
import fr.pingtimeout.jtail.util.*;

public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel jTailPanel;

 @Before
 public void setUp() {
 JTailLogger.init();
 model = new JTailModel();
 jTailPanel = new JTailPanel(model);
 }

 @Test
 public void testJTailPanelConstruction() {
 JTailPanel panel = new JTailPanel(model);
 assertNotNull(panel);
 }

 @Test
 public void testGetCharacterHeight() {
 int characterHeight = jTailPanel.getCharacterHeight();
 assertNotNull(characterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 assertNotNull(verticalScrollBar);
 }

 @Test
 public void testUpdateMethodWithResizedEvent() {
 model.firePropertyChange(JTailModel.LINE_NUMBERS, null, "");
 model.firePropertyChange(JTailModel.CONTENT, null, "");
 model.firePropertyChange(JTailModel.Type.RESIZED, null, new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, 0, 0, 0, ""));
 assertEquals(0, jTailPanel.verticalScrollBar.getValue());
 model.firePropertyChange(JTailModel.Type.RESIZED, null, new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, 10, 10, 10, ""));
 assertFalse(jTailPanel.verticalScrollBar.isEnabled());
 model.firePropertyChange(JTailModel.Type.RESIZED, null, new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, 5, 5, 5, ""));
 assertFalse(jTailPanel.verticalScrollBar.isEnabled());
 model.firePropertyChange(JTailModel.Type.RESIZED, null, new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, 15, 15, 15, ""));
 assertTrue(jTailPanel.verticalScrollBar.isEnabled());
 }

 @Test(expected = NullPointerException.class)
 public void testUpdateMethodWithNullModel() {
 model = null;
 JTailPanel panel = new JTailPanel(model);
 model.firePropertyChange(JTailModel.LINE_NUMBERS, null, "");
 model.firePropertyChange(JTailModel.CONTENT, null, "");
 model.firePropertyChange(JTailModel.Type.RESIZED, null, new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, 0, 0, 0, ""));
 }

 @Test
 public void testUpdateMethodWithScrolledEvent() {
 model.firePropertyChange(JTailModel.LINE_NUMBERS, null, "");
 model.firePropertyChange(JTailModel.CONTENT, null, "");
 model.firePropertyChange(JTailModel.Type.SCROLLED, null, new JTailModelEvent(model, JTailModelEvent.Type.SCROLLED, 0, 0, 0, ""));
 }

 @Test
 public void testUpdateDataTableMethodWithEmptyLineNumbersAndContent() {
 String[] lineNumbers = {};
 String[] lines = {};
 jTailPanel.updateDataTable(lineNumbers, lines);
 assertEquals(0, jTailPanel.dataTable.getRowCount());
 }

 @Test
 public void testUpdateDataTableMethodWithNonEmptyLineNumbersAndContent() {
 String[] lineNumbers = {"1", "2", "3"};
 String[] lines = {"line1", "line2", "line3"};
 jTailPanel.updateDataTable(lineNumbers, lines);
 assertEquals(3, jTailPanel.dataTable.getRowCount());
 }

 @Test
 public void testUpdateDataTableMethodWithNullLineNumbersOrContent() {
 String[] lineNumbers = null;
 String[] lines = {"line1", "line2", "line3"};
 jTailPanel.updateDataTable(lineNumbers, lines);
 assertEquals(0, jTailPanel.dataTable.getRowCount());
 lines = null;
 jTailPanel.updateDataTable(lineNumbers, lines);
 assertEquals(0, jTailPanel.dataTable.getRowCount());
 }
}