package fr.pingtimeout.jtail.gui.test;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.util.Arrays;

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
 public void setUp() {
 jTailModel = new JTailModel();
 jTailPanel = new JTailPanel(jTailModel);
 }

 @Test
 public void testJTailPanelConstructor() {
 JTailLogger.initLogger(); // Initialize logger to avoid warnings in test
 JTailLogger.debug("Testing JTailPanel constructor");
 assertNotNull(jTailPanel);
 }

 @Test
 public void testGetCharacterHeight() {
 JTailLogger.debug("Testing getCharacterHeight() method");
 assertEquals(17, jTailPanel.getCharacterHeight());
 }

 @Test
 public void testGetVerticalScrollBar() {
 JTailLogger.debug("Testing getVerticalScrollBar() method");
 assertNotNull(jTailPanel.getVerticalScrollBar());
 }

 @Test
 public void testUpdateMethodWithResizeEvent() {
 JTailLogger.debug("Testing update() method with RESIZED event");
 jTailModel.fireEvent(new JTailModelEvent(JTailModelEvent.Type.RESIZED, new Dimension(100, 100), 5, 10, 20, 30));
 assertTrue(jTailPanel.isEnabled());
 assertEquals(5, jTailPanel.verticalScrollBar.getValue());
 }

 @Test
 public void testUpdateMethodWithScrollEvent() {
 JTailLogger.debug("Testing update() method with SCROLLED event");
 jTailModel.fireEvent(new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 10, 15));
 assertTrue(jTailPanel.isEnabled());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateMethodWithWrongEventType() {
 JTailLogger.debug("Testing update() method with wrong event type");
 jTailModel.fireEvent(new JTailModelEvent("WRONG_TYPE", new Object[] {}));
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test
 public void testUpdateDataTableMethodEmptyData() {
 JTailLogger.debug("Testing updateDataTable() method with empty data");
 jTailPanel.updateDataTable("", "");
 assertEquals(0, jTailPanel.dataTable.getRowCount());
 }

 @Test
 public void testUpdateDataTableMethodNormalData() {
 JTailLogger.debug("Testing updateDataTable() method with normal data");
 String lineNumbers = "1\n2\n3\n4\n5\n";
 String lines = "One\nTwo\nThree\nFour\nFive\n";
 jTailPanel.updateDataTable(lineNumbers, lines);
 assertEquals(5, jTailPanel.dataTable.getRowCount());
 }

 @Test
 public void testUpdateDataTableMethodWrongDataLength() {
 JTailLogger.debug("Testing updateDataTable() method with wrong data length");
 String lineNumbers = "1\n2\n3\n4\n";
 String lines = "One\nTwo\nThree\nFour\nFive\nSix\n";
 jTailPanel.updateDataTable(lineNumbers, lines);
 assertEquals(5, jTailPanel.dataTable.getRowCount());
 }

 @Test
 public void testUpdateDataTableMethodAutoResize() {
 JTailLogger.debug("Testing updateDataTable() method with auto resize");
 jTailPanel.updateDataTable(Arrays.asList(new String[] { "1", "ABCDEFGHIJKLMNOPQRSTUVWXYZ" }).toString(), "");
 int expectedWidth = 16 * 22 + 2; // Calculated width of columns
 assertEquals(expectedWidth, jTailPanel.dataTable.getPreferredSize().width);
 }

 @Test
 public void testScrollBarAdjustmentController() {
 JTailLogger.debug("Testing ScrollBarAdjustmentController");
 ScrollBarAdjustmentController controller = new ScrollBarAdjustmentController(jTailPanel, jTailModel);
 controller.valueChanged(new AdjustmentEvent(jTailPanel.verticalScrollBar, AdjustmentEvent.TYPE_VALUE_CHANGED, 10, 10, 10));
 assertEquals(10, jTailPanel.verticalScrollBar.getValue());
 }

}