package fr.pingtimeout.jtail.gui.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.gui.view.*;
import fr.pingtimeout.jtail.util.*;

public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel jTailPanel;

 @Before
 public void setUp() {
 model = new JTailModel();
 jTailPanel = new JTailPanel(model);
 }

 @Test
 public void testJTailPanelConstruction() {
 JTailLogger.debug("Building a new JTailPanel");
 assertEquals("model is null", model, jTailPanel.model);
 assertNotNull("scrollPane is null", jTailPanel.scrollPane);
 assertNotNull("dataTable is null", jTailPanel.dataTable);
 assertNotNull("tableModel is null", jTailPanel.tableModel);
 assertNotNull("characterHeight is defined", jTailPanel.characterHeight);
 assertNull("verticalScrollBar is null when first created", jTailPanel.verticalScrollBar);
 assertFalse("horizontal scrollbar is not shown by default", jTailPanel.isHorizontalScrollBarShown());
 }

 @Test
 public void testGetCharacterHeight() {
 final int expectedCharacterHeight = 15; // Replace this with the actual value
 assertEquals("characterHeight is not as expected", expectedCharacterHeight, jTailPanel.getCharacterHeight());
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertNull("verticalScrollBar is null when first created", jTailPanel.getVerticalScrollBar());
}

 @Test
 public void testUpdate() {
 final int firstLine = 0;
 final int nbDisplayedLines = 20;
 final int maxLine = 100;
 model.setSize(100, 100);
 model.setFirstLine(firstLine);
 model.setLastLine(maxLine);
 model.setNbDisplayedLines(nbDisplayedLines);
 model.notifyObservers(new JTailModelEvent(model, JTailModelEvent.Type.RESIZED));

 final JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 final BoundedRangeModel rangeModel = (BoundedRangeModel) verticalScrollBar.getModel();

 assertEquals("minimum is not as expected", firstLine, rangeModel.getMinimum());
 assertEquals("value is not as expected", firstLine, rangeModel.getValue());
 assertEquals("extent is not as expected", nbDisplayedLines, rangeModel.getExtent());
 assertEquals("maximum is not as expected", maxLine, rangeModel.getMaximum());
 assertTrue("scrollbar is enabled after resize", verticalScrollBar.isEnabled());

 model.setFirstLine(firstLine + 1);
 model.notifyObservers(new JTailModelEvent(model, JTailModelEvent.Type.SCROLLED));

 assertEquals("minimum is not updated after scroll", firstLine + 1, rangeModel.getMinimum());
 assertEquals("value is not updated after scroll", firstLine + 1, rangeModel.getValue());
 assertFalse("scrollbar is disabled after scroll", verticalScrollBar.isEnabled());
}

 @Test
 public void testUpdateDataTable() {
 final String lineNumbers = "1\n2\n3\n4\n5";
 final String lines = "one\ntwo\nthree\nfour\nfive";
 model.setContent(lines);
 model.setLineNumbers(lineNumbers);
 model.notifyObservers(new JTailModelEvent(model, JTailModelEvent.Type.RESIZED));

 assertEquals("number of rows is not as expected", 5, jTailPanel.dataTable.getRowCount());
 for (int i = 0; i < 5; i++) {
 String expectedLineNumber = "1";
 String expectedLine = "one";
 if (i > 0) {
 expectedLineNumber = Integer.toString(i + 1);
 expectedLine = "two\nthree\nfour\nfive".split("\n")[i];
 }
 String lineNumber = (String) jTailPanel.tableModel.getValueAt(i, 0);
 String line = (String) jTailPanel.tableModel.getValueAt(i, 1);
 assertEquals("line number is not as expected", expectedLineNumber, lineNumber);
 assertEquals("line is not as expected", expectedLine, line);
 }
}

 @Test
 public void testAutoResizeColumns() {
 final int dataTableWidth = 300;
 final TableColumn numbersColumn = jTailPanel.dataTable.getColumnModel().getColumn(0);
 final TableColumn linesColumn = jTailPanel.dataTable.getColumnModel().getColumn(1);

 jTailPanel.dataTable.setPreferredSize(new Dimension(dataTableWidth, 300));
 jTailPanel.updateDataTable(model.getLineNumbers(), model.getContent());

 final int numbersColumnWidth = numbersColumn.getPreferredWidth();
 final int linesColumnWidth = linesColumn.getPreferredWidth();

 assertEquals("numbers column width is not as expected", 50, numbersColumnWidth);
 assertEquals("lines column width is not as expected", dataTableWidth - numbersColumnWidth, linesColumnWidth);
}
}