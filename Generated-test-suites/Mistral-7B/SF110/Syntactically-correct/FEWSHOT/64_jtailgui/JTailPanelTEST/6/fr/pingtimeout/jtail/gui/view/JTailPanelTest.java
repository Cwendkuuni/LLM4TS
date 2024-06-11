package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class JTailPanelTest {

 JTailModel mockModel;
 JScrollBar mockVerticalScrollBar;
 JTable mockDataTable;
 DefaultTableModel mockTableModel;
 int mockCharacterHeight;
 int mockMaxCharacterWidth;

 @Before
 public void setUp() {
 mockModel = mock(JTailModel.class);
 mockVerticalScrollBar = mock(JScrollBar.class);
 mockDataTable = mock(JTable.class);
 mockTableModel = mock(DefaultTableModel.class);
 mockCharacterHeight = 1;
 mockMaxCharacterWidth = 1;
 }

 @Test
 public void testJTailPanelConstruction() {
 JTailPanel panel = new JTailPanel(mockModel);
 assertEquals("Built a new JTailPanel", JTailLogger.debugStatementCaptor.getValue());
 }

 @Test
 public void testGetCharacterHeight() {
 JTailPanel panel = new JTailPanel(mockModel);
 assertEquals(mockCharacterHeight, panel.getCharacterHeight());
 }

 @Test
 public void testGetVerticalScrollBar() {
 JTailPanel panel = new JTailPanel(mockModel);
 assertEquals(mockVerticalScrollBar, panel.getVerticalScrollBar());
 }

 @Test
 public void testUpdateObserver() {
 String lineNumbers = "1\n2\n3";
 String lines = "line1\nline2\nline3";
 String[] lineNumbersArray = lineNumbers.split("\n");
 String[] linesArray = lines.split("\n");

 when(mockModel.getLineNumbers()).thenReturn(lineNumbers);
 when(mockModel.getContent()).thenReturn(lines);

 JTailPanel panel = new JTailPanel(mockModel);
 panel.update(mockModel, new JTailModelEvent(mockModel, JTailModelEvent.Type.RESIZED, 1, 2, 3, 4, 5, 6));

 verify(mockModel, times(1)).addObserver(any(Observer.class));

 TableModel tableModel = panel.tableModel;
 assertEquals(2, tableModel.getRowCount());
 assertEquals("1", tableModel.getValueAt(0, 0).toString());
 assertEquals("line1", tableModel.getValueAt(0, 1).toString());
 assertEquals("2", tableModel.getValueAt(1, 0).toString());
 assertEquals("line2", tableModel.getValueAt(1, 1).toString());
 }

 @Test
 public void testUpdateObserverScrollEvent() {
 String lineNumbers = "1\n2\n3";
 String lines = "line1\nline2\nline3";
 String[] lineNumbersArray = lineNumbers.split("\n");
 String[] linesArray = lines.split("\n");

 when(mockModel.getLineNumbers()).thenReturn(lineNumbers);
 when(mockModel.getContent()).thenReturn(lines);

 JTailPanel panel = new JTailPanel(mockModel);
 panel.update(mockModel, new JTailModelEvent(mockModel, JTailModelEvent.Type.SCROLLED, 1, 2, 3, 4, 5, 6));

 TableModel tableModel = panel.tableModel;
 assertEquals(2, tableModel.getRowCount());
 assertEquals("1", tableModel.getValueAt(0, 0).toString());
 assertEquals("line1", tableModel.getValueAt(0, 1).toString());
 assertEquals("2", tableModel.getValueAt(1, 0).toString());
 assertEquals("line2", tableModel.getValueAt(1, 1).toString());
 }

 @Test
 public void testUpdateDataTable() {
 JTailPanel panel = new JTailPanel(mockModel);

 String lineNumbers = "1\n2\n3\n4\n5";
 String lines = "line1\nline2\nline3\nline4\nline5";

 when(mockModel.getLineNumbers()).thenReturn(lineNumbers);
 when(mockModel.getContent()).thenReturn(lines);

 panel.update(mockModel, new JTailModelEvent(mockModel, JTailModelEvent.Type.RESIZED, 1, 2, 3, 4, 5, 6));

 TableModel tableModel = panel.tableModel;
 assertEquals(5, tableModel.getRowCount());
 assertEquals("1", tableModel.getValueAt(0, 0).toString());
 assertEquals("line1", tableModel.getValueAt(0, 1).toString());
 assertEquals("2", tableModel.getValueAt(1, 0).toString());
 assertEquals("line2", tableModel.getValueAt(1, 1).toString());
 assertEquals("3", tableModel.getValueAt(2, 0).toString());
 assertEquals("line3", tableModel.getValueAt(2, 1).toString());
 assertEquals("4", tableModel.getValueAt(3, 0).toString());
 assertEquals("line4", tableModel.getValueAt(3, 1).toString());
 assertEquals("5", tableModel.getValueAt(4, 0).toString());
 assertEquals("line5", tableModel.getValueAt(4, 1).toString());
 }

 @Test
 public void testResizeColumns() {
 JTailPanel panel = new JTailPanel(mockModel);

 panel.update(mockModel, new JTailModelEvent(mockModel, JTailModelEvent.Type.RESIZED, 1, 2, 3, 4, 5, 6));

 TableColumn numbersColumn = panel.dataTable.getColumnModel().getColumn(0);
 TableColumn linesColumn = panel.dataTable.getColumnModel().getColumn(1);

 int numbersColumnWidth = 20;
 int linesColumnWidth = 100;

 panel.updateDataTable(lineNumbers, lines);

 assertEquals(numbersColumnWidth, numbersColumn.getPreferredWidth());
 assertEquals(linesColumnWidth, linesColumn.getPreferredWidth());
 }
}