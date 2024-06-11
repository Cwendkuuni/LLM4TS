package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class JTailPanelTest {

 JTailModel mockModel;
 JScrollPane mockScrollPane;
 JScrollBar mockVerticalScrollBar;
 JTable mockDataTable;
 DefaultTableModel mockTableModel;
 int mockCharacterHeight;
 int mockMaxCharacterWidth;

 @Before
 public void setUp() {
 mockModel = mock(JTailModel.class);
 mockScrollPane = mock(JScrollPane.class);
 mockVerticalScrollBar = mock(JScrollBar.class);
 mockDataTable = mock(JTable.class);
 mockTableModel = mock(DefaultTableModel.class);
 mockCharacterHeight = 1;
 mockMaxCharacterWidth = 2;
 }

 @Test
 public void testJTailPanelConstruction() {
 JTailPanel jTailPanel = new JTailPanel(mockModel);
 assertSame(mockModel, jTailPanel.model);
 assertSame(mockScrollPane, jTailPanel.scrollPane);
 assertSame(mockVerticalScrollBar, jTailPanel.verticalScrollBar);
 assertSame(mockDataTable, jTailPanel.dataTable);
 assertSame(mockTableModel, jTailPanel.tableModel);
 assertEquals(mockCharacterHeight, jTailPanel.characterHeight);
 }

 @Test
 public void testGetCharacterHeight() {
 JTailPanel jTailPanel = new JTailPanel(mockModel);
 assertEquals(mockCharacterHeight, jTailPanel.getCharacterHeight());
 }

 @Test
 public void testGetVerticalScrollBar() {
 JTailPanel jTailPanel = new JTailPanel(mockModel);
 assertSame(mockVerticalScrollBar, jTailPanel.getVerticalScrollBar());
 }

 @Test
 public void testUpdateWithResizedEvent() {
 when(mockModel.getLineNumbers()).thenReturn("1\n2\n3\n4");
 when(mockModel.getContent()).thenReturn("Line 1\nLine 2\nLine 3\nLine 4");
 JTailPanel jTailPanel = new JTailPanel(mockModel);

 JTailModelEvent event = new JTailModelEvent(mockModel, JTailModelEvent.Type.RESIZED, 1, 1, 1, 1, 1, 1);
 jTailPanel.update(mockModel, event);

 verify(mockVerticalScrollBar).setEnabled(true);
 verify(mockVerticalScrollBar, times(1)).setValue(eq(1));
 }

 @Test
 public void testUpdateWithScrolledEvent() {
 JTailPanel jTailPanel = new JTailPanel(mockModel);

 JTailModelEvent event = new JTailModelEvent(mockModel, JTailModelEvent.Type.SCROLLED, 1, 1, 1, 1, 1, 1);
 jTailPanel.update(mockModel, event);

 verify(mockDataTable, times(1)).setModel(eq(mockTableModel));
 }

 @Test
 public void testUpdateDataTable() {
 String lineNumbers = "1\n2\n3\n4\n5";
 String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
 JTailPanel jTailPanel = new JTailPanel(mockModel);

 jTailPanel.updateDataTable(lineNumbers, lines);

 TableModel actualTableModel = jTailPanel.dataTable.getModel();
 DefaultTableModel expectedTableModel = (DefaultTableModel)mockTableModel;

 assertEquals(expectedTableModel.getRowCount(), actualTableModel.getRowCount());
 for (int i = 0; i < expectedTableModel.getRowCount(); i++) {
 String expectedLineNumber = expectedTableModel.getValueAt(i, 0).toString();
 String expectedLine = expectedTableModel.getValueAt(i, 1).toString();
 String actualLineNumber = actualTableModel.getValueAt(i, 0).toString();
 String actualLine = actualTableModel.getValueAt(i, 1).toString();
 assertEquals(expectedLineNumber, actualLineNumber);
 assertEquals(expectedLine, actualLine);
 }
 }

 @Test
 public void testResizeColumns() {
 String lineNumbers = "1\n2\n3\n4\n5";
 String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
 JTailPanel jTailPanel = new JTailPanel(mockModel);
 jTailPanel.updateDataTable(lineNumbers, lines);

 int numbersColumnWidth = 10;
 int linesColumnWidth = 10;
 jTailPanel.dataTable.setAutoResizeMode(3);
 jTailPanel.dataTable.setSize(300, 300);

 TableColumn numbersColumn = jTailPanel.dataTable.getColumnModel().getColumn(0);
 TableColumn linesColumn = jTailPanel.dataTable.getColumnModel().getColumn(1);

 jTailPanel.updateDataTable(lineNumbers, lines);

 assertEquals(numbersColumnWidth, numbersColumn.getPreferredWidth());
 assertEquals(linesColumnWidth, linesColumn.getPreferredWidth());
 }
}