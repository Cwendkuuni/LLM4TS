package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.util.JTailLogger;

/**
 * JUnit test file for JTailPanel class
 */
public class JTailPanelTest {

 private JTailPanel jTailPanel;
 private JTailModel jTailModel;

 @Before
 public void setUp() {
 jTailModel = new JTailModel();
 jTailPanel = new JTailPanel(jTailModel);
 JTailLogger.init();
 }

 @Test
 public void testJTailPanelConstructor() {
 JTailPanel jtp = new JTailPanel(jTailModel);
 assertNotNull(jtp.getVerticalScrollBar());
 assertNotNull(jtp.getDataTable());
 assertNotNull(jtp.getTableModel());
 }

 @Test
 public void testGetCharacterHeight() {
 int characterHeight = jTailPanel.getCharacterHeight();
 assertNotNull(characterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 JScrollBar vsb = jTailPanel.getVerticalScrollBar();
 assertNotNull(vsb);
 }

 @Test
 public void testUpdateMethodWithResizedEvent() {
 //Stub ScrollBarAdjustmentController and JTailSizeController for this test
 ScrollBarAdjustmentController scrollBarAdjustmentController = new ScrollBarAdjustmentController(jTailPanel, jTailModel);
 JTailSizeController jTailSizeController = new JTailSizeController(jTailPanel, jTailModel);

 //Set up initial state
 jTailModel.setContent("Line 1\nLine 2\nLine 3");
 jTailModel.setLineNumbers("1\n2\n3");
 jTailModel.addObserver(jTailPanel);
 jTailPanel.addComponentListener(jTailSizeController);
 jTailPanel.addAdjustmentListener(scrollBarAdjustmentController);
 jTailPanel.updateUI();

 //Mock resized event
 JTailModelEvent resizedEvent = new JTailModelEvent(jTailModel, JTailModelEvent.Type.RESIZED);
 jTailModel.notifyObservers(resizedEvent);

 //Verify scrollbar position
 JBoundedRangeModel rangeModel = jTailPanel.getVerticalScrollBar().getModel();
 assertEquals(0, rangeModel.getValue());
 assertEquals(3, rangeModel.getMaximum());
 assertTrue(rangeModel.isAdjusting());
 rangeModel.setValueIsAdjusting(false);

 //Verify data table content
 String[] lines = jTailPanel.getDataTable().getModel().getDataVector().toArray(new String[0])[0];
 assertEquals("1  Line 1\n2  Line 2\n3  Line 3", lines);
 }

 @Test(expected = NullPointerException.class)
 public void testUpdateMethodWithNullModel() {
 JTailPanel jtpNullModel = new JTailPanel(null);
 jtpNullModel.update(null, null);
 }

}