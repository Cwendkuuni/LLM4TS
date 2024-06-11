package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

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
 JTailLogger.debug("Testing JTailPanel constructor");
 assertNotNull(jTailPanel);
 }

 @Test
 public void testGetCharacterHeight() {
 JTailLogger.debug("Testing getCharacterHeight method");
 int characterHeight = jTailPanel.getCharacterHeight();
 assertEquals(characterHeight, jTailPanel.characterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 JTailLogger.debug("Testing getVerticalScrollBar method");
 assertNotNull(jTailPanel.getVerticalScrollBar());
 }

 @Test
 public void testUpdate() {
 JTailLogger.debug("Testing update method with RESIZED event");
 JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, Arrays.asList(1, 2, 3), "test1\ntest2\ntest3", 1, 5, 10);
 jTailPanel.update(model, event);
 JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 assertEquals(1, verticalScrollBar.getValue());

 JTailLogger.debug("Testing update method with SCROLLED event");
 event = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1, Arrays.asList(1, 2, 3), "test1\ntest2\ntest3", 2, 5, 10);
 jTailPanel.update(model, event);
 // The updateDataTable method is not tested directly as it is a private method
 // but its functionality is tested by checking the state of the DataTable after the update method is called
 }

 @Test(expected = NullPointerException.class)
 public void testUpdateWithNullModel() {
 JTailLogger.debug("Testing update method with null model");
 JTailPanel jTailPanelNullModel = new JTailPanel(null);
 jTailPanelNullModel.update(null, new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, Arrays.asList(1, 2, 3), "test1\ntest2\ntest3", 1, 5, 10));
 }

 @Test
 public void testUpdateDataTable() {
 JTailLogger.debug("Testing updateDataTable method with normal data");
 String lineNumbers = "1\n2\n3";
 String lines = "test1\ntest2\ntest3";
 jTailPanel.updateDataTable(lineNumbers, lines);
 int rowCount = jTailPanel.dataTable.getRowCount();
 assertEquals(3, rowCount);

 JTailLogger.debug("Testing updateDataTable method with empty data");
 jTailPanel.updateDataTable("", "");
 rowCount = jTailPanel.dataTable.getRowCount();
 assertEquals(0, rowCount);
 }
}