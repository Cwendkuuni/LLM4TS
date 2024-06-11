package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.util.JTailLogger;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;

public class JTailPanelTest {

 private JTailPanel jTailPanel;
 private JTailModel jTailModel;

 @org.junit.Before
 public void setUp() {
 jTailModel = new JTailModel();
 jTailPanel = new JTailPanel(jTailModel);
 }

 @org.junit.Test
 public void testJTailPanelConstruction() {
 assertEquals("Unexpected model", jTailModel, jTailPanel.getModel());
 assertNotNull("ScrollPane not initialized", jTailPanel.getScrollPane());
 assertNotNull("DataTable not initialized", jTailPanel.getDataTable());
 }

 @org.junit.Test
 public void testGetCharacterHeight() {
 final int characterHeight = jTailPanel.getCharacterHeight();
 assertNotEquals("Unexpected character height", 0, characterHeight);
 }

 @org.junit.Test
 public void testGetVerticalScrollBar() {
 assertNotNull("VerticalScrollBar not initialized", jTailPanel.getVerticalScrollBar());
 }

 @org.junit.Test
 public void testUpdateMethodWithResizeEvent() {
 final int minLine = 1;
 final int firstLine = 5;
 final int nbDisplayedLines = 3;
 final int maxLine = 10;

 jTailModel.addResizeEvent(new JTailModelEvent(JTailModelEvent.Type.RESIZED, minLine, firstLine, nbDisplayedLines, maxLine));

 final BoundedRangeModel rangeModel = jTailPanel.getVerticalScrollBar().getModel();
 assertEquals("Unexpected minimum", minLine, rangeModel.getMinimum());
 assertEquals("Unexpected first line", firstLine, rangeModel.getValue());
 assertEquals("Unexpected extent", nbDisplayedLines, rangeModel.getExtent());
 assertEquals("Unexpected maximum", maxLine, rangeModel.getMaximum());
 }

 @org.junit.Test
 public void testUpdateMethodWithScrollEvent() {
 final String lineNumbers = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10";
 final String lines = "line1\nline2\nline3\nline4\nline5\nline6\nline7\nline8\nline9\nline10";

 jTailModel.addScrollEvent(new JTailModelEvent(JTailModelEvent.Type.SCROLLED));

 jTailPanel.updateDataTable(lineNumbers, lines);

 final int numRows = jTailPanel.getDataTable().getRowCount();
 assertEquals("Unexpected number of rows", 10, numRows);
 }

 @org.junit.Test
 public void testUpdateDataTableMethod() {
 final String lineNumbers = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10";
 final String lines = "line1\nline2\nline3\nline4\nline5\nline6\nline7\nline8\nline9\nline10";

 jTailPanel.updateDataTable(lineNumbers, lines);

 final int numRows = jTailPanel.getDataTable().getRowCount();
 assertEquals("Unexpected number of rows", 10, numRows);

 final int columnCount = jTailPanel.getDataTable().getColumnCount();
 assertEquals("Unexpected number of columns", 2, columnCount);
 }

 @org.junit.Test
 public void testUpdateDataTableMethodWithEmptyLines() {
 final String lineNumbers = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11";
 final String lines = "line1\nline2\nline3\nline4\nline5\nline6\nline7\nline8\nline9\nline10\n";

 jTailPanel.updateDataTable(lineNumbers, lines);

 final int numRows = jTailPanel.getDataTable().getRowCount();
 assertEquals("Unexpected number of rows", 11, numRows);
 }

 @org.junit.Test
 public void testUpdateDataTableMethodWithTruncatedLines() {
 final String lineNumbers = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11";
 final String lines = "line1\nline2\nline3\nline4\nline5\nline6\nline7\nline8\nline9\nline10line11";

 jTailPanel.updateDataTable(lineNumbers, lines);

 final int numRows = jTailPanel.getDataTable().getRowCount();
 assertEquals("Unexpected number of rows", 11, numRows);

 final int columnCount = jTailPanel.getDataTable().getColumnCount();
 assertEquals("Unexpected number of columns", 2, columnCount);
 }

 @org.junit.Test
 public void testUpdateDataTableMethodWithLongLines() {
 final String lineNumbers = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11";
 final String lines = "line1: a long long long long long line\nline2\nline3\nline4\nline5\nline6\nline7\nline8\nline9\nline10\nline11";

 jTailPanel.updateDataTable(lineNumbers, lines);

 final int numRows = jTailPanel.getDataTable().getRowCount();
 assertEquals("Unexpected number of rows", 11, numRows);

 final int columnCount = jTailPanel.getDataTable().getColumnCount();
 assertEquals("Unexpected number of columns", 2, columnCount);
 }

 @org.junit.Test
 public void testUpdateDataTableMethodWithNullLines() {
 final String lineNumbers = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11";
 final String lines = null;

 jTailPanel.updateDataTable(lineNumbers, lines);

 final int numRows = jTailPanel.getDataTable().getRowCount();
 assertEquals("Unexpected number of rows", 0, numRows);
 }

 @org.junit.Test
 public void testUpdateDataTableMethodWithEmptyLineNumbers() {
 final String lineNumbers = "";
 final String lines = "line1\nline2\nline3\nline4\nline5\nline6\nline7\nline8\nline9\nline10\nline11";

 jTailPanel.updateDataTable(lineNumbers, lines);

 final int numRows = jTailPanel.getDataTable().getRowCount();
 assertEquals("Unexpected number of rows", 11, numRows);
 }

 @org.junit.Test
 public void testUpdateSizeMethod() {
 final int width = 100;
 final int height = 200;

 jTailPanel.updateSize(width, height);

 final int componentWidth = jTailPanel.getWidth();
 assertEquals("Unexpected component width", width, componentWidth);
 final int componentHeight = jTailPanel.getHeight();
 assertEquals("Unexpected component height", height, componentHeight);
 }

 @org.junit.Test
 public void testUpdateDataTableMethodWithResizeEvent() {
 final String lineNumbers = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11";
 final String lines = "line1\nline2\nline3\nline4\nline5\nline6\nline7\nline8\nline9\nline10\nline11";
 final int width = 100;
 final int height = 200;

 jTailModel.addResizeEvent(new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 3, 10));
 jTailPanel.updateSize(width, height);
 jTailPanel.updateDataTable(lineNumbers, lines);

 final int numRows = jTailPanel.getDataTable().getRowCount();
 assertEquals("Unexpected number of rows", 11, numRows);

 final int columnCount = jTailPanel.getDataTable().getColumnCount();
 assertEquals("Unexpected number of columns", 2, columnCount);
 }

 @org.junit.Test
 public void testScrollBarAdjustmentController() {
 final JScrollBarAdjustmentController scrollBarAdjustmentController = new ScrollBarAdjustmentController(jTailPanel, jTailModel);
 final ChangeEvent changeEvent = new ChangeEvent(scrollBarAdjustmentController);

 jTailModel.addScrollEvent(new JTailModelEvent(JTailModelEvent.Type.SCROLLED));

 scrollBarAdjustmentController.stateChanged(changeEvent);

 final BoundedRangeModel rangeModel = jTailPanel.getVerticalScrollBar().getModel();
 assertEquals("Unexpected minimum", 0, rangeModel.getMinimum());
 assertEquals("Unexpected value", 0, rangeModel.getValue());
 assertEquals("Unexpected extent", 1, rangeModel.getExtent());
 assertEquals("Unexpected maximum", 1, rangeModel.getMaximum());
 }

 @org.junit.Test
 public void testJTailSizeController() {
 final JTailSizeController jTailSizeController = new JTailSizeController(jTailPanel, jTailModel);
 final ComponentEvent componentEvent = new ComponentEvent(jTailPanel, ComponentEvent.COMPONENT_RESIZED);

 jTailModel.addResizeEvent(new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 3, 10));

 jTailSizeController.componentResized(componentEvent);

 final BoundedRangeModel rangeModel = jTailPanel.getVerticalScrollBar().getModel();
 assertEquals("Unexpected minimum", 0, rangeModel.getMinimum());
 assertEquals("Unexpected value", 0, rangeModel.getValue());
 assertEquals("Unexpected extent", 1, rangeModel.getExtent());
 assertEquals("Unexpected maximum", 1, rangeModel.getMaximum());
 }
}