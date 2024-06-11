package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

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
 private JTailModel mockModel;

 @Before
 public void setUp() {
 JTailLogger.init();
 this.mockModel = new JTailModel();
 this.jTailPanel = new JTailPanel(this.mockModel);
 }

 @Test
 public void testGetCharacterHeight() {
 final int expected = 15; // Assuming a default font size in Java Swing is 15
 final int actual = this.jTailPanel.getCharacterHeight();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertNotNull(this.jTailPanel.getVerticalScrollBar());
 }

 @Test
 public void testUpdateWithModelResizedEvent() {
 final int firstLine = 10;
 final int maxLine = 20;
 final int minLine = 5;
 final int nbDisplayedLines = 5;

 this.mockModel.setLineNumbersAndContent("line1\nline2\nline3\nline4\nline5\nline6\nline7\nline8\nline9\nline10", Arrays.asList(firstLine, maxLine), Arrays.asList(minLine, nbDisplayedLines));

 final BoundedRangeModel rangeModel = this.jTailPanel.verticalScrollBar.getModel();

 rangeModel.setValueIsAdjusting(true);
 rangeModel.setMinimum(firstLine);
 rangeModel.setValue(firstLine);
 rangeModel.setExtent(nbDisplayedLines);
 rangeModel.setMaximum(maxLine);
 rangeModel.setValueIsAdjusting(false);

 final int[] lineNumbers = this.jTailPanel.model.getLineNumbers().stream().mapToInt(Integer::intValue).toArray();
 final String[] lines = this.jTailPanel.model.getContent().split("\n");

 assertEquals(Arrays.asList(lineNumbers), Arrays.asList(new Integer[] { firstLine, firstLine + nbDisplayedLines - 1 }));
 assertEquals(Arrays.asList(lines), Arrays.asList(Arrays.copyOfRange(lines, firstLine, firstLine + nbDisplayedLines)));
 }

 @Test
 public void testUpdateWithModelScrolledEvent() {
 final int firstLine = 10;
 final int maxLine = 20;
 final int minLine = 5;
 final int nbDisplayedLines = 5;

 this.mockModel.setLineNumbersAndContent("line1\nline2\nline3\nline4\nline5\nline6\nline7\nline8\nline9\nline10", Arrays.asList(firstLine, maxLine), Arrays.asList(minLine, nbDisplayedLines));
 this.jTailPanel.update(this.mockModel, new JTailModelEvent(JTailModelEvent.Type.SCROLLED, null));

 final int[] lineNumbers = this.jTailPanel.model.getLineNumbers().stream().mapToInt(Integer::intValue).toArray();
 final String[] lines = this.jTailPanel.model.getContent().split("\n");

 assertEquals(Arrays.asList(lineNumbers), Arrays.asList(Arrays.copyOfRange(lineNumbers, 0, nbDisplayedLines + firstLine)));
 assertEquals(Arrays.asList(lines), Arrays.asList(Arrays.copyOfRange(lines, firstLine, firstLine + nbDisplayedLines)));
 }

 @Test
 public void testUpdateDataTableWithNewContent() {
 final String lineNumbers = "lineNumber1\nlineNumber2\nlineNumber3\nlineNumber4\nlineNumber5";
 final String lines = "line1\nline2\nline3\nline4\nline5";

 this.jTailPanel.updateDataTable(lineNumbers, lines);

 final int rowCountBefore = this.jTailPanel.dataTable.getRowCount();
 final int columnCountBefore = this.jTailPanel.dataTable.getColumnCount();
 final Object[] dataBefore = this.jTailPanel.dataTable.getModel().getDataVector().toArray();

 this.jTailPanel.update(this.mockModel, new JTailModelEvent(JTailModelEvent.Type.RESIZED, null));

 final int rowCountAfter = this.jTailPanel.dataTable.getRowCount();
 final Object[] dataAfter = this.jTailPanel.dataTable.getModel().getDataVector().toArray();

 assertEquals(rowCountBefore, 0);
 assertEquals(columnCountBefore, 2);
 assertArrayEquals(new Object[] { null, null }, dataBefore);

 assertEquals(rowCountAfter, lineNumbers.split("\n").length);
 assertArrayEquals(new String[] { "lineNumber1", "lineNumber2", "lineNumber3", "lineNumber4", "lineNumber5" }, this.jTailPanel.dataTable.getColumnNames());
 assertArrayEquals(dataAfter, Arrays.stream(this.jTailPanel.dataTable.getDataVector().toArray()).map(o -> Arrays.copyOf((Object[]) o, 2)).toArray());
 }

 @Test
 public void testUpdateDataTableWithNewSize() {
 final String lineNumbers = "lineNumber1\nlineNumber2\nlineNumber3\nlineNumber4\nlineNumber5";
 final String lines = "line1\nline2\nline3\nline4\nline5";

 this.jTailPanel.updateDataTable(lineNumbers, lines);
 this.jTailPanel.update(this.mockModel, new JTailModelEvent(JTailModelEvent.Type.RESIZED, null));

 final TableColumn numbersColumn = this.jTailPanel.dataTable.getColumnModel().getColumn(0);
 final TableColumn linesColumn = this.jTailPanel.dataTable.getColumnModel().getColumn(1);

 final int numbersColumnWidthBefore = numbersColumn.getPreferredWidth();
 final int linesColumnWidthBefore = linesColumn.getPreferredWidth();

 this.jTailPanel.dataTable.doLayout();

 final int numbersColumnWidthAfter = numbersColumn.getPreferredWidth();
 final int linesColumnWidthAfter = linesColumn.getPreferredWidth();

 assertEquals(numbersColumnWidthBefore, this.jTailPanel.characterHeight * lineNumbers.split("\n").length);
 assertEquals(linesColumnWidthBefore, this.jTailPanel.dataTable.getWidth() - numbersColumnWidthBefore);

 assertEquals(numbersColumnWidthAfter, this.jTailPanel.characterHeight * lineNumbers.split("\n").length);
 assertEquals(linesColumnWidthAfter, this.jTailPanel.dataTable.getWidth() - numbersColumnWidthAfter);
 }
}