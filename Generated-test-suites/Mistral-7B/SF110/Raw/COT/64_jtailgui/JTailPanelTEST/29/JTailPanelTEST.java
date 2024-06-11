package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.util.JTailLogger;

public class JTailPanelTest {

 private JTailPanel panel;
 private JTailModel model;

 @Before
 public void setUp() {
 model = new JTailModel();
 panel = new JTailPanel(model);
 }

 @Test
 public void testJTailPanelConstruction() {
 JTailPanel expectedPanel = new JTailPanel(model);
 assertEquals("JTailPanel should be an instance of JTailPanel", expectedPanel.getClass(), panel.getClass());
 }

 @Test
 public void testGetCharacterHeight() {
 int characterHeight = panel.getCharacterHeight();
 int expectedCharacterHeight = 15; // Assuming a hardcoded font height
 assertEquals("Character height should be equal to the expected value", expectedCharacterHeight, characterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 JScrollBar expectedScrollBar = new JScrollBar();
 JScrollBar actualScrollBar = panel.getVerticalScrollBar();
 assertEquals("Vertical scrollbar should be an instance of JScrollBar", expectedScrollBar.getClass(), actualScrollBar.getClass());
 }

 @Test(expected = NullPointerException.class)
 public void testUpdateWithNullModel() {
 JTailPanel nullPanel = new JTailPanel(null);
 nullPanel.update(model, new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, 0, 0, 0, 0, 0, 0, null));
 }

 @Test
 public void testUpdateWithValidModel() {
 JTailModelEvent event = new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, 0, 0, 10, 15, 20, 25);
 panel.update(model, event);
 JScrollBar scrollBar = panel.getVerticalScrollBar();
 assertTrue("Scrollbar value should be greater than 0", scrollBar.getValue() > 0);
 }

 @Test
 public void testUpdateDataTableWithValidArguments() {
 String lineNumbers = "1\n2\n3\n4\n5\n";
 String lines = "Line1\nLine2\nLine3\nLine4\nLine5\n";
 panel.updateDataTable(lineNumbers, lines);
 int rowCount = panel.getDataTable().getRowCount();
 assertEquals("Number of rows should be equal to the number of lines", 5, rowCount);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateDataTableWithNullLines() {
 String lineNumbers = "1\n2\n3\n4\n5\n";
 panel.updateDataTable(lineNumbers, null);
 }
}