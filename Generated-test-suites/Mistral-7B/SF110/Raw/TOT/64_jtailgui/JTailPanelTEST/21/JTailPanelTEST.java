package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

import java.awt.event.AdjustmentEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.gui.view.JTailSizeController;

public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel jTailPanel;

 @Before
 public void setup() {
 model = new JTailModel();
 jTailPanel = new JTailPanel(model);
 }

 @Test
 public void testInitialization() {
 assertEquals("The number of columns should be 2", 2, jTailPanel.getDataTable().getColumnModel().getColumnCount());
 assertNotNull("The data table should not be null", jTailPanel.getDataTable());
 assertNotNull("The vertical scroll bar should not be null", jTailPanel.getVerticalScrollBar());
 }

 @Test
 public void testUpdateModelWhenWindowIsResized() {
 final int lines = 10;
 final String content = Arrays.toString(new String[lines]).replace("[", "").replace("]", "").replace(" ", "");
 model.setContent(content);
 model.setSize(100, 100);
 jTailPanel.updateUI();
 final int expectedFirstLine = 0;
 final int expectedLastLine = 2;
 final int expectedNbDisplayedLines = 2;
 final int expectedMinLine = 0;
 final int expectedMaxLine = 3;
 model.setSize(300, 600);
 jTailPanel.updateUI();
 ScrollBarModelAssertions.assertScrollBarValues(jTailPanel.getVerticalScrollBar(), expectedFirstLine, expectedLastLine, expectedNbDisplayedLines, expectedMinLine, expectedMaxLine);
 }

 @Test
 public void testUpdateModelWhenContentIsChanged() {
 final int lines = 10;
 model.setSize(300, 600);
 model.setContent(Arrays.toString(new String[lines]).replace("[", "").replace("]", "").replace(" ", ""));
 jTailPanel.updateUI();
 final int expectedFirstLine = 0;
 final int expectedLastLine = 2;
 final int expectedNbDisplayedLines = 2;
 final int expectedMinLine = 0;
 final int expectedMaxLine = 3;
 ScrollBarModelAssertions.assertScrollBarValues(jTailPanel.getVerticalScrollBar(), expectedFirstLine, expectedLastLine, expectedNbDisplayedLines, expectedMinLine, expectedMaxLine);
 model.setContent(Arrays.toString(new String[lines + 1]).replace("[", "").replace("]", "").replace(" ", ""));
 jTailPanel.updateUI();
 final int newExpectedLastLine = 3;
 ScrollBarModelAssertions.assertScrollBarValues(jTailPanel.getVerticalScrollBar(), expectedFirstLine, newExpectedLastLine, expectedNbDisplayedLines, expectedMinLine, expectedMaxLine);
 }

 @Test
 public void testUpdateTableWhenModelIsUpdated() {
 model.setContent("Line 1\nLine 2\nLine 3\nLine 4\nLine 5");
 jTailPanel.updateUI();
 final int expectedNumberOfRows = 5;
 assertEquals("The number of rows should be equal to the number of lines", expectedNumberOfRows, jTailPanel.getDataTable().getRowCount());
 }

 @Test
 public void testScrollBarAdjustmentController() {
 final int lines = 10;
 model.setContent(Arrays.toString(new String[lines]).replace("[", "").replace("]", "").replace(" ", ""));
 model.setSize(300, 600);
 jTailPanel.updateUI();
 final int firstLine = 0;
 final int lastLine = 2;
 final int nbDisplayedLines = 2;
 final int minLine = 0;
 final int maxLine = 3;
 ScrollBarModelAssertions.assertScrollBarValues(jTailPanel.getVerticalScrollBar(), firstLine, lastLine, nbDisplayedLines, minLine, maxLine);
 final int newLines = 5;
 model.setContent(Arrays.toString(new String[newLines]).replace("[", "").replace("]", "").replace(" ", ""));
 jTailPanel.updateUI();
 final int newLastLine = 3;
 ScrollBarModelAssertions.assertScrollBarValues(jTailPanel.getVerticalScrollBar(), firstLine, newLastLine, nbDisplayedLines, minLine, maxLine);
 final int newMinLine = 1;
 jTailPanel.getVerticalScrollBar().setValue(ScrollBarAdjustmentController.getThumbPosition(nbDisplayedLines, newLines, firstLine, newLastLine));
 ScrollBarModelAssertions.assertScrollBarValues(jTailPanel.getVerticalScrollBar(), newMinLine, newLastLine, nbDisplayedLines, minLine, maxLine);
 }

 @Test
 public void testJTailSizeController() {
 final int width = 300;
 final int height = 600;
 model.setSize(width, height);
 jTailPanel.updateUI();
 final int expectedWidth = width;
 final int expectedHeight = height;
 assertEquals("The width and height of the panel should match the size of the model", expectedWidth, jTailPanel.getWidth());
 assertEquals("The width and height of the panel should match the size of the model", expectedHeight, jTailPanel.getHeight());
 }

 @Test
 public void testTableColumnWidths() {
 final int width = 300;
 final int height = 600;
 model.setSize(width, height);
 model.setContent("Line 1\nLine 2\nLine 3\nLine 4\nLine 5");
 jTailPanel.updateUI();
 final int expectedNumbersColumnWidth = width / 2;
 final int expectedLinesColumnWidth = width - expectedNumbersColumnWidth;
 TableColumn numbersColumn = jTailPanel.getDataTable().getColumnModel().getColumn(0);
 TableColumn linesColumn = jTailPanel.getDataTable().getColumnModel().getColumn(1);
 assertEquals("The width of the numbers column should be equal to half the width of the panel", expectedNumbersColumnWidth, numbersColumn.getWidth());
 assertEquals("The width of the lines column should be equal to the width of the panel minus the width of the numbers column", expectedLinesColumnWidth, linesColumn.getWidth());
 }

 @Test
 public void testScrolling() {
 final int lines = 10;
 model.setContent(Arrays.toString(new String[lines]).replace("[", "").replace("]", "").replace(" ", ""));
 model.setSize(300, 600);
 jTailPanel.updateUI();
 final int firstLine = 0;
 final int lastLine = 2;
 final int nbDisplayedLines = 2;
 final int minLine = 0;
 final int maxLine = 3;
 ScrollBarModelAssertions.assertScrollBarValues(jTailPanel.getVerticalScrollBar(), firstLine, lastLine, nbDisplayedLines, minLine, maxLine);
 jTailPanel.scrollDown();
 final int newLastLine = 3;
 ScrollBarModelAssertions.assertScrollBarValues(jTailPanel.getVerticalScrollBar(), firstLine, newLastLine, nbDisplayedLines, minLine, maxLine);
 }

 @Test
 public void testScrollDownToLastLine() {
 final int lines = 10;
 model.setContent(Arrays.toString(new String[lines]).replace("[", "").replace("]", "").replace(" ", ""));
 model.setSize(300, 600);
 jTailPanel.updateUI();
 final int firstLine = 0;
 final int lastLine = 2;
 final int nbDisplayedLines = 2;
 final int minLine = 0;
 final int maxLine = 3;
 ScrollBarModelAssertions.assertScrollBarValues(jTailPanel.getVerticalScrollBar(), firstLine, lastLine, nbDisplayedLines, minLine, maxLine);
 jTailPanel.scrollDownToLastLine();
 final int newLastLine = 9;
 ScrollBarModelAssertions.assertScrollBarValues(jTailPanel.getVerticalScrollBar(), firstLine, newLastLine, nbDisplayedLines, minLine, maxLine);
 }

 @Test
 public void testScrollUpToFirstLine() {
 final int lines = 10;
 model.setContent(Arrays.toString(new String[lines]).replace("[", "").replace("]", "").replace(" ", ""));
 model.setSize(300, 600);
 jTailPanel.updateUI();
 final int firstLine = 0;
 final int lastLine = 2;
 final int nbDisplayedLines = 2;
 final int minLine = 0;
 final int maxLine = 3;
 ScrollBarModelAssertions.assertScrollBarValues(jTailPanel.getVerticalScrollBar(), firstLine, lastLine, nbDisplayedLines, minLine, maxLine);
 jTailPanel.scrollUpToFirstLine();
 final int newFirstLine = 0;
 ScrollBarModelAssertions.assertScrollBarValues(jTailPanel.getVerticalScrollBar(), newFirstLine, lastLine, nbDisplayedLines, minLine, maxLine);
 }

 @Test
 public void testScrollingWithMouseEvent() {
 final int lines = 10;
 model.setContent(Arrays.toString(new String[lines]).replace("[", "").replace("]", "").replace(" ", ""));
 model.setSize(300, 600);
 jTailPanel.updateUI();
 final int firstLine = 0;
 final int lastLine = 2;
 final int nbDisplayedLines = 2;
 final int minLine = 0;
 final int maxLine = 3;
 ScrollBarModelAssertions.assertScrollBarValues(jTailPanel.getVerticalScrollBar(), firstLine, lastLine, nbDisplayedLines, minLine, maxLine);
 final int newLastLine = 9;
 final MouseEvent mouseEvent = new MouseEvent(jTailPanel, 1, 0, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis());
 jTailPanel.scrolling(mouseEvent);
 ScrollBarModelAssertions.assertScrollBarValues(jTailPanel.getVerticalScrollBar(), firstLine, newLastLine, nbDisplayedLines, minLine, maxLine);
 }

 @Test
 public void testScrollingWithAdjustmentEvent() {
 final int lines = 10;
 model.setContent(Arrays.toString(new String[lines]).replace("[", "").replace("]", "").replace(" ", ""));
 model.setSize(300, 600);
 jTailPanel.updateUI();
 final int firstLine = 0;
 final int lastLine = 2;
 final int nbDisplayedLines = 2;
 final int minLine = 0;
 final int maxLine = 3;
 ScrollBarModelAssertions.assertScrollBarValues(jTailPanel.getVerticalScrollBar(), firstLine, lastLine, nbDisplayedLines, minLine, maxLine);
 final int newLastLine = 9;
 final AdjustmentEvent adjustmentEvent = new AdjustmentEvent(jTailPanel.getVerticalScrollBar(), AdjustmentEvent.ADJUSTING, 0, 0, 0);
 jTailPanel.scrolling(adjustmentEvent);
 ScrollBarModelAssertions.assertScrollBarValues(jTailPanel.getVerticalScrollBar(), firstLine, newLastLine, nbDisplayedLines, minLine, maxLine);
 }
}

class ScrollBarModelAssertions {

 public static void assertScrollBarValues(JScrollBar scrollBar, int firstLine, int lastLine, int nbDisplayedLines, int minLine, int maxLine) {
 assertEquals("The first line displayed should match the value of the firstLine variable", firstLine, scrollBar.getMinimum());
 assertEquals("The last line displayed should match the value of the lastLine variable", lastLine, scrollBar.getValue());
 assertEquals("The number of lines displayed should match the value of the nbDisplayedLines variable", nbDisplayedLines, scrollBar.getExtent());
 assertEquals("The minimum line should match the value of the minLine variable", minLine, scrollBar.getModel().getMinimum());
 assertEquals("The maximum line should match the value of the maxLine variable", maxLine, scrollBar.getModel().getMaximum());
 }
}