package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.gui.view.*;
import org.junit.*;

public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel panel;

 @Before
 public void setUp() {
 model = new JTailModel();
 panel = new JTailPanel(model);
 }

 @Test
 public void testConstructor() {
 // Testing if the constructor is called correctly and the components are initialized
 assertNotNull(panel.getModel());
 assertNotNull(panel.getScrollPane());
 assertNotNull(panel.getDataTable());
 assertNotNull(panel.getTableModel());
 assertNotNull(panel.getVerticalScrollBar());
 }

 @Test
 public void testUpdateWithEmptyModel() {
 // Testing if the panel updates correctly when the model is empty
 model.clear();
 panel.update(model, new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, 0, 0, 0, 0));
 assertEquals(0, panel.getDataTable().getRowCount());
 }

 @Test
 public void testUpdateWithSmallModel() {
 // Testing if the panel updates correctly with a small model
 model.setContent("Line 1\nLine 2\nLine 3");
 panel.update(model, new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, 0, 0, 0, 0));
 assertEquals(3, panel.getDataTable().getRowCount());
 String[] expectedLineNumbers = {"1", "2", "3"};
 String[] expectedLines = {"Line 1", "Line 2", "Line 3"};
 for (int i = 0; i < 3; i++) {
 String lineNumber = (String) panel.getDataTable().getValueAt(i, 0);
 String line = (String) panel.getDataTable().getValueAt(i, 1);
 assertArrayEquals(expectedLineNumbers, new String[] {lineNumber});
 assertArrayEquals(expectedLines, new String[] {line});
 }
 }

 @Test
 public void testUpdateWithLargeModel() {
 // Testing if the panel updates correctly with a large model
 model.setContent("Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8\nLine 9\nLine 10\nLine 11\nLine 12\nLine 13\nLine 14\nLine 15\nLine 16\nLine 17\nLine 18\nLine 19\nLine 20");
 panel.update(model, new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, 0, 0, 0, 0));
 assertEquals(20, panel.getDataTable().getRowCount());
 }

 @Test
 public void testUpdateWithScrolling() {
 // Testing if the panel updates correctly when the user scrolls the file
 model.setContent("Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8\nLine 9\nLine 10\nLine 11\nLine 12\nLine 13\nLine 14\nLine 15\nLine 16\nLine 17\nLine 18\nLine 19\nLine 20");
 model.setFirstLine(5);
 model.setNbDisplayedLines(5);
 model.setMaxLine(20);
 panel.update(model, new JTailModelEvent(model, JTailModelEvent.Type.SCROLLED, 5, 5, 5, 5));
 assertEquals(5, panel.getDataTable().getSelectedRow());
 String[] expectedLines = {"5", "6", "7", "8", "9"};
 for (int i = 0; i < 5; i++) {
 String line = (String) panel.getDataTable().getValueAt(i, 1);
 assertArrayEquals(expectedLines, new String[] {line});
 }
 }

 @Test
 public void testUpdateWithDisableScrolling() {
 // Testing if the panel disables the scrollbar correctly when there's only one line displayed
 model.setContent("Line 1");
 model.setFirstLine(0);
 model.setNbDisplayedLines(1);
 model.setMaxLine(1);
 panel.update(model, new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, 0, 0, 0, 0));
 assertFalse(panel.isVerticalScrollBarEnabled());
 }
}