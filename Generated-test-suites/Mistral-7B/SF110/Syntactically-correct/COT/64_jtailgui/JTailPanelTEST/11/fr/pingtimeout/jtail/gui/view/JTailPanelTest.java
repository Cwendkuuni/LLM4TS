package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.controller.JTailSizeController;
import fr.pingtimeout.jtail.gui.model.DefaultTableModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.util.JTailLogger;

public class JTailPanelTest {

 private JTailPanel jTailPanel;
 private JTailModel model;

 @Before
 public void setUp() {
 model = new JTailModel();
 jTailPanel = new JTailPanel(model);
 }

 @After
 public void tearDown() {
 if (jTailPanel != null) {
 jTailPanel.dispose();
 }
 }

 @Test
 public void testJTailPanelConstructor() {
 // Check that a new instance of JTailPanel has been created
 assertNotNull(jTailPanel);

 // Check that the instance has the correct components
 assertNotNull(jTailPanel.getVerticalScrollBar());
 assertNotNull(jTailPanel.getDataTable());
 assertNotNull(jTailPanel.getScrollPane());
 }

 @Test
 public void testGetCharacterHeight() {
 int characterHeight = jTailPanel.getCharacterHeight();
 assertTrue(characterHeight > 0);
 }

 @Test
 public void testUpdateWhenModelIsResized() {
 final int initialSize = model.getContent().length();
 model.fireEvent(new JTailModelEvent(JTailModelEvent.Type.RESIZED, initialSize, initialSize + 100));

 // Check that the dataTable has been updated
 final int rowCount = jTailPanel.getDataTable().getRowCount();
 assertTrue(rowCount > 0);

 // Check that the verticalScrollBar has been updated
 final int verticalScrollBarValue = jTailPanel.getVerticalScrollBar().getValue();
 assertTrue(verticalScrollBarValue > 0);
 }

 @Test
 public void testUpdateWhenModelIsScrolled() {
 model.fireEvent(new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1, 2));

 // Check that the dataTable has been updated
 final int rowCount = jTailPanel.getDataTable().getRowCount();
 assertTrue(rowCount > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateWhenModelIsNull() {
 model = null;
 jTailPanel = new JTailPanel(model);
 }

 @Test
 public void testDisableVerticalScrollBarWhenNoMoreLinesToDisplay() {
 model.fireEvent(new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 1));
 assertTrue(jTailPanel.getVerticalScrollBar().isEnabled());

 model.fireEvent(new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1));
 assertFalse(jTailPanel.getVerticalScrollBar().isEnabled());
 }

 @Test
 public void testDisableVerticalScrollBarWhenAlreadyDisabled() {
 model.fireEvent(new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 0));
 assertFalse(jTailPanel.getVerticalScrollBar().isEnabled());

 model.fireEvent(new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1));
 assertFalse(jTailPanel.getVerticalScrollBar().isEnabled());
 }

 @Test
 public void testUpdateDataTableWithEmptyContent() {
 jTailPanel.updateDataTable(new String[0], "");
 assertTrue(jTailPanel.getDataTable().getRowCount() == 0);
 }

 @Test
 public void testUpdateDataTableWithNullContent() {
 jTailPanel.updateDataTable(new String[0], null);
 assertTrue(jTailPanel.getDataTable().getRowCount() == 0);
 }

 @Test
 public void testUpdateDataTableWithOneLineContent() {
 String content = "Line 1";
 jTailPanel.updateDataTable(new String[] { "1" }, content);
 assertTrue(jTailPanel.getDataTable().getRowCount() == 1);
 assertTrue(jTailPanel.getDataTable().getValueAt(0, 1).toString().equals(content));
 }

 @Test
 public void testUpdateDataTableWithMultipleLinesContent() {
 String content = "Line 1\nLine 2\nLine 3";
 jTailPanel.updateDataTable(new String[] { "1", "2", "3" }, content);
 assertTrue(jTailPanel.getDataTable().getRowCount() == 3);
 assertTrue(jTailPanel.getDataTable().getValueAt(0, 1).toString().equals("Line 1"));
 assertTrue(jTailPanel.getDataTable().getValueAt(1, 1).toString().equals("Line 2"));
 assertTrue(jTailPanel.getDataTable().getValueAt(2, 1).toString().equals("Line 3"));
 }

 @Test
 public void testResizingColumns() {
 jTailPanel.updateDataTable(new String[] { "1", "Line 1", "Line 2", "Line 3" }, "Line 1\nLine 2\nLine 3");

 int numbersColumnWidth = jTailPanel.getDataTable().getColumnModel().getColumn(0).getPreferredWidth();
 int linesColumnWidth = jTailPanel.getDataTable().getColumnModel().getColumn(1).getPreferredWidth();

 assertTrue(numbersColumnWidth > 0);
 assertTrue(linesColumnWidth > 0);
 }
}