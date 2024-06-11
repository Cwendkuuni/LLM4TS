package fr.pingtimeout.jtail.gui.test;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.controller.SizeController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.util.JTailLogger;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@RunWith(org.junit.runner.JUnit4.class)
public class JTailPanelTest {

 private JTailPanel jtailPanel;
 private JScrollBar scrollBar;
 private JTable table;
 private JFrame frame;
 private JTailModel model;

 @Before
 public void setup() {
 model = Mockito.spy(new JTailModel());
 jtailPanel = new JTailPanel(model);
 scrollBar = jtailPanel.getVerticalScrollBar();
 table = jtailPanel.getDataTable();
 frame = new JFrame();
 frame.setContentPane(jtailPanel);
 frame.setPreferredSize(new Dimension(1000, 800));
 frame.pack();
 frame.setVisible(true);
 }

 @Test
 public void testInitialization() {
 assertEquals("The model is correctly set", model, jtailPanel.getModel());
 assertNotNull("The verticalScrollBar is not null", jtailPanel.getVerticalScrollBar());
 assertNotNull("The dataTable is not null", jtailPanel.getDataTable());
 assertNotNull("The tableModel is not null", jtailPanel.getTableModel());
 assertEquals("The characterHeight is correctly initialized", jtailPanel.getCharacterHeight(), 15);
 }

 @Test
 public void testUpdateWhenModelIsResized() {
 final int maxLine = 10;
 final int nbDisplayedLines = 5;
 final int firstLine = 3;
 final int minLine = 1;

 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.RESIZED, maxLine, nbDisplayedLines, firstLine, minLine));

 final int expectedMinLine = minLine;
 final int expectedFirstLine = firstLine;
 final int expectedNbDisplayedLines = nbDisplayedLines;
 final int expectedMaxLine = maxLine;

 final BoundedRangeModel rangeModel = scrollBar.getModel();
 assertEquals("The minimum is correctly set", expectedMinLine, rangeModel.getMinimum());
 assertEquals("The first line is correctly set", expectedFirstLine, rangeModel.getValue());
 assertEquals("The number of displayed lines is correctly set", expectedNbDisplayedLines, rangeModel.getExtent());
 assertEquals("The maximum is correctly set", expectedMaxLine, rangeModel.getMaximum());
 }

 @Test
 public void testUpdateWhenModelIsScrolled() {
 final int maxLine = 10;
 final int nbDisplayedLines = 5;
 final int firstLine = 3;
 final int minLine = 1;
 final int lineToScroll = 7;

 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.SCROLLED, lineToScroll));

 final String[] expectedLines = {"1", "2", "3", "4", "7", "8", "9"};
 final Object[][] expectedData = {{"1", "foo"}, {"2", "bar"}, {"3", "baz"}, {"4", "qux"}, {"7", "quux"}, {"8", "corge"}, {"9", "grault"}};

 int[] columnWidths = new int[2];
 table.getColumnModel().getColumn(0).getSizeWidth(columnWidths);
 int numbersColumnWidth = columnWidths[0];
 int linesColumnWidth = table.getWidth() - numbersColumnWidth;

 assertEquals("The number of rows is correctly updated", 7, table.getRowCount());
 assertArrayEquals("The lines are correctly displayed", Arrays.copyOfRange(expectedLines, 0, 7), table.getValueAt(0, 1).toString().split("\\s+"));
 assertArrayEquals("The data model is correctly updated", expectedData, table.getModel().toArray());
 assertEquals("The width of the first column is correctly updated", numbersColumnWidth, jtailPanel.getMaxCharacterWidth() * 7);
 assertEquals("The width of the second column is correctly updated", linesColumnWidth, table.getWidth() - numbersColumnWidth);
 }

 @Test
 public void testDisableScrollBarWhenAllLinesAreDisplayed() {
 final int maxLine = 10;
 final int nbDisplayedLines = 10;
 final int firstLine = 1;
 final int minLine = 1;

 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.RESIZED, maxLine, nbDisplayedLines, firstLine, minLine));
 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1));
 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1));
 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1));
 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1));
 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1));
 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1));
 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1));
 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1));
 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1));

 assertTrue("The scrollbar should be disabled", !scrollBar.isEnabled());
 }

 @Test
 public void testEnableScrollBarWhenNotAllLinesAreDisplayed() {
 final int maxLine = 10;
 final int nbDisplayedLines = 5;
 final int firstLine = 3;
 final int minLine = 1;

 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.RESIZED, maxLine, nbDisplayedLines, firstLine, minLine));
 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1));
 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1));
 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1));
 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1));
 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1));

 assertFalse("The scrollbar should be enabled", !scrollBar.isEnabled());
 }

 @Test
 public void testDisableScrollBarWhenFirstLineIsDisplayed() {
 final int maxLine = 10;
 final int nbDisplayedLines = 1;
 final int firstLine = 1;
 final int minLine = 1;

 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.RESIZED, maxLine, nbDisplayedLines, firstLine, minLine));

 assertTrue("The scrollbar should be disabled", !scrollBar.isEnabled());
 }

 @Test
 public void testScrollBarDoesNotScrollWhenIncorrectEventIsReceived() {
 final int maxLine = 10;
 final int nbDisplayedLines = 5;
 final int firstLine = 3;
 final int minLine = 1;

 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.RESIZED, maxLine, nbDisplayedLines, firstLine, minLine));
 model.fireNotification(new AdjustmentEvent(scrollBar, 1, 0, 0, 0));

 final int value = scrollBar.getValue();
 assertEquals("The scrollbar value should not have changed", firstLine, value);
 }
}