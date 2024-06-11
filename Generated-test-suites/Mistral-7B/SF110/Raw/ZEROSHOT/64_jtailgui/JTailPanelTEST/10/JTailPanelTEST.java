package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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
 public void testBuildingJTailPanel() {
 JTailLogger.debug("Testing JTailPanel construction");
 assertNotNull(jTailPanel);
 }

 @Test
 public void testGetCharacterHeight() {
 JTailLogger.debug("Testing getting character height");
 int characterHeight = jTailPanel.getCharacterHeight();
 assertNotNull(characterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 JTailLogger.debug("Testing getting vertical scroll bar");
 JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 assertNotNull(verticalScrollBar);
 }

 @Test
 public void testUpdateWithResizedEvent() {
 JTailLogger.debug("Testing update with resized event");
 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.RESIZED, 10, 20, 30, 40, 50));
 assertEquals(10, jTailPanel.verticalScrollBar.getModel().getMinimum());
 assertEquals(20, jTailPanel.verticalScrollBar.getModel().getValue());
 assertEquals(30, jTailPanel.verticalScrollBar.getModel().getExtent());
 assertEquals(50, jTailPanel.verticalScrollBar.getModel().getMaximum());
 }

 @Test
 public void testUpdateWithScrolledEvent() {
 JTailLogger.debug("Testing update with scrolled event");
 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 10, 20));
 assertNotNull(jTailPanel.dataTable.getModel());
 assertFalse(jTailPanel.verticalScrollBar.isEnabled());
 }

 @Test
 public void testUpdateDataTable() {
 JTailLogger.debug("Testing updateDataTable method");
 DefaultTableModel tableModel = (DefaultTableModel) jTailPanel.dataTable.getModel();
 model.setContent("line1\nline2\nline3\nline4\nline5");
 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.RESIZED, 10, 20, 30, 40, 50));
 assertEquals(5, tableModel.getRowCount());
 }
}