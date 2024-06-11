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

 private JTailModel mockModel;
 private JTailPanel jTailPanel;

 @Before
 public void setUp() {
 mockModel = new JTailModel();
 jTailPanel = new JTailPanel(mockModel);
 }

 // Test constructor method
 @Test
 public void testJTailPanelConstructor() {
 JTailPanel jtp = new JTailPanel(mockModel);
 assertNotNull(jtp);
 }

 // Test getCharacterHeight method
 @Test
 public void testGetCharacterHeight() {
 int characterHeight = jTailPanel.getCharacterHeight();
 assertNotNull(characterHeight);
 }

 // Test getVerticalScrollBar method
 @Test
 public void testGetVerticalScrollBar() {
 JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 assertNotNull(verticalScrollBar);
 }

 // Test update method
 @Test
 public void testUpdateMethod() {
 JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 1, null, null);
 jTailPanel.update(mockModel, event);
 JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 assertEquals(1, verticalScrollBar.getValue());
 }

 // Test updateDataTable method with valid data
 @Test
 public void testUpdateDataTableWithValidData() {
 String lineNumbers = "1\n2\n3\n4\n5";
 String lines = "line1\nline2\nline3\nline4\nline5";
 jTailPanel.updateDataTable(lineNumbers, lines);
 int rowCount = jTailPanel.dataTable.getRowCount();
 assertEquals(5, rowCount);
 }

 // Test updateDataTable method with empty data
 @Test
 public void testUpdateDataTableWithEmptyData() {
 String lineNumbers = "";
 String lines = "";
 jTailPanel.updateDataTable(lineNumbers, lines);
 int rowCount = jTailPanel.dataTable.getRowCount();
 assertEquals(0, rowCount);
 }

 // Test updateDataTable method with invalid data
 @Test(expected = IllegalArgumentException.class)
 public void testUpdateDataTableWithInvalidData() {
 String lineNumbers = "1\n2";
 String lines = "line1\nline2\nline3\nline4line5";
 jTailPanel.updateDataTable(lineNumbers, lines);
 }

 // Test resizeColumnWidths method with valid data
 @Test
 public void testResizeColumnWidthsWithValidData() {
 String lineNumbers = "1\n2\n3\n4\n5";
 String lines = "line1\nline2\nline3\nline4\nline5";
 jTailPanel.updateDataTable(lineNumbers, lines);
 jTailPanel.dataTable.setAutoResizeMode(3);
 TableColumn numbersColumn = jTailPanel.dataTable.getColumnModel().getColumn(0);
 TableColumn linesColumn = jTailPanel.dataTable.getColumnModel().getColumn(1);
 int numbersColumnWidth = 15;
 int linesColumnWidth = 200;
 numbersColumn.setPreferredWidth(numbersColumnWidth);
 linesColumn.setPreferredWidth(linesColumnWidth);
 int numbersColumnWidthActual = numbersColumn.getPreferredWidth();
 int linesColumnWidthActual = linesColumn.getPreferredWidth();
 assertEquals(numbersColumnWidth, numbersColumnWidthActual);
 assertEquals(linesColumnWidth, linesColumnWidthActual);
 }

 // Test resizeColumnWidths method with invalid data
 @Test(expected = IllegalArgumentException.class)
 public void testResizeColumnWidthsWithInvalidData() {
 String lineNumbers = "1\n2\n3\n4\n5";
 String lines = "line1\nline2\nline3\nline4\nline5";
 jTailPanel.updateDataTable(lineNumbers, lines);
 jTailPanel.dataTable.setAutoResizeMode(3);
 TableColumn numbersColumn = jTailPanel.dataTable.getColumnModel().getColumn(0);
 TableColumn linesColumn = jTailPanel.dataTable.getColumnModel().getColumn(1);
 numbersColumn.setPreferredWidth(-1);
 linesColumn.setPreferredWidth(-1);
 }
}