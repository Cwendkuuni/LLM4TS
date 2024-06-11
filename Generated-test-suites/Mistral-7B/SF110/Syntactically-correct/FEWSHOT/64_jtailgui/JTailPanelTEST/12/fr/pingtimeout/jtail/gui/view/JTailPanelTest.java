package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.util.*;

public class JTailPanelTest {

 private JTailModel mockModel;
 private JTailPanel jTailPanel;
 private ScrollBarAdjustmentController mockScrollBarAdjustmentController;
 private JTailSizeController mockJTailSizeController;

 @Before
 public void setUp() {
 mockModel = mock(JTailModel.class);
 jTailPanel = new JTailPanel(mockModel);
 mockScrollBarAdjustmentController = mock(ScrollBarAdjustmentController.class);
 mockJTailSizeController = mock(JTailSizeController.class);
 jTailPanel.verticalScrollBar.addAdjustmentListener(mockScrollBarAdjustmentController);
 jTailPanel.addComponentListener(mockJTailSizeController);
 }

 @Test
 public void testJTailPanelConstruction() {
 assertNotNull(jTailPanel);
 assertEquals("JTailPanel", jTailPanel.getClass().getSimpleName());
 assertNotNull(jTailPanel.model);
 assertNotNull(jTailPanel.scrollPane);
 assertNotNull(jTailPanel.dataTable);
 assertNotNull(jTailPanel.tableModel);
 assertNotNull(jTailPanel.characterHeight);
 assertNotNull(jTailPanel.verticalScrollBar);
 }

 @Test
 public void testGetCharacterHeight() {
 assertEquals(jTailPanel.characterHeight, 15); // Assuming the characterHeight is initialized to 15
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertEquals(jTailPanel.verticalScrollBar, jTailPanel.getVerticalScrollBar());
 }

 @Test
 public void testUpdateObserver() {
 JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 10, 5, 10, 5);
 when(mockModel.getLineNumbers()).thenReturn("1\n2\n3\n4\n5");
 when(mockModel.getContent()).thenReturn("Line 1\nLine 2\nLine 3\nLine 4\nLine 5");
 jTailPanel.update(mockModel, event);

 verify(mockModel, times(1)).getLineNumbers();
 verify(mockModel, times(1)).getContent();

 // Test the update of the JTable with the new data
 TableModel tableModel = jTailPanel.tableModel;
 int linesNumber = tableModel.getRowCount();
 assertEquals(linesNumber, 5);

 String[] lineNumbersArray = jTailPanel.model.getLineNumbers().split("\n");
 String[] linesArray = jTailPanel.model.getContent().split("\n");

 for (int i = 0; i < linesNumber; i++) {
 String lineNumberTmp = lineNumbersArray[i];
 String lineTmp = linesArray[i];
 assertNotNull(tableModel.getValueAt(i, 0));
 assertEquals(lineNumberTmp, tableModel.getValueAt(i, 0));
 assertNotNull(tableModel.getValueAt(i, 1));
 assertEquals(lineTmp, tableModel.getValueAt(i, 1));
 }
 }

 @Test
 public void testUpdateObserver_ScrollEvent() {
 JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 0, 10, 5, 10, 5);
 jTailPanel.update(mockModel, event);

 verify(mockModel, times(1)).getLineNumbers();
 verify(mockModel, times(1)).getContent();

 TableModel tableModel = jTailPanel.tableModel;
 int linesNumber = tableModel.getRowCount();
 assertEquals(linesNumber, 5);
 }

 @Test
 public void testUpdateDataTable() {
 String lineNumbers = "1\n2\n3\n4\n5";
 String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
 jTailPanel.updateDataTable(lineNumbers, lines);

 TableModel tableModel = jTailPanel.tableModel;
 int linesNumber = tableModel.getRowCount();
 assertEquals(linesNumber, 5);

 String[] lineNumbersArray = lineNumbers.split("\n");
 String[] linesArray = lines.split("\n");

 for (int i = 0; i < linesNumber; i++) {
 String lineNumberTmp = lineNumbersArray[i];
 String lineTmp = linesArray[i];
 assertNotNull(tableModel.getValueAt(i, 0));
 assertEquals(lineNumberTmp, tableModel.getValueAt(i, 0));
 assertNotNull(tableModel.getValueAt(i, 1));
 assertEquals(lineTmp, tableModel.getValueAt(i, 1));
 }
 }

 @Test
 public void testSetAutoResizeMode() {
 jTailPanel.dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
 assertEquals(JTable.AUTO_RESIZE_OFF, jTailPanel.dataTable.getAutoResizeMode());
 }

 @Test
 public void testResizeColumns() {
 jTailPanel.updateDataTable("1\n2\n3\n4\n5", "Line 1\nLine 2\nLine 3\nLine 4\nLine 5");

 TableColumn numbersColumn = jTailPanel.dataTable.getColumnModel().getColumn(0);
 TableColumn linesColumn = jTailPanel.dataTable.getColumnModel().getColumn(1);

 // Set the preferred width for numbersColumn and linesColumn
 numbersColumn.setPreferredWidth(200);
 linesColumn.setPreferredWidth(500);

 jTailPanel.dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
 jTailPanel.dataTable.doLayout();

 int numbersColumnWidth = numbersColumn.getPreferredWidth();
 int linesColumnWidth = linesColumn.getPreferredWidth();

 assertEquals(numbersColumnWidth, 200);
 assertEquals(linesColumnWidth, 500);
 }
}