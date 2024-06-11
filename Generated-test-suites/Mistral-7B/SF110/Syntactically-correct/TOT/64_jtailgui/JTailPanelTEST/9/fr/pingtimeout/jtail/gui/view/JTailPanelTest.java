package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.junit.*;
import org.mockito.*;

public class JTailPanelTest {

 private JTailModel modelMock;
 private JTailPanel panel;

 @Before
 public void setup() {
 modelMock = mock(JTailModel.class);
 panel = new JTailPanel(modelMock);
 }

 @Test
 public void testInitialization() {
 // Test if the JTailPanel is correctly initialized
 assertNotNull(panel.getVerticalScrollBar());
 assertNotNull(panel.getDataTable());
 assertNotNull(panel.getScrollPane());
 }

 @Test
 public void testResize() {
 // Test if the JTailPanel resizes correctly when the model is resized
 when(modelMock.getResizedEvent()).thenReturn(new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 1, 1, 1, 1, 1));
 panel.modelChanged(modelMock, new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 1, 1, 1, 1, 1));
 TableRowSorter<DefaultTableModel> sorter = mock(TableRowSorter.class);
 doReturn(sorter).when(panel.getDataTable()).getRowSorter();
 TableColumnModel columnModel = panel.getDataTable().getColumnModel();
 int width = 500;
 panel.setPreferredSize(new Dimension(width, 200));
 panel.setSize(new Dimension(width, 200));
 panel.updateUI();
 int firstLine = 0;
 int lastLine = 10;
 int maxLine = 20;
 int nbDisplayedLines = 5;
 int minLine = 5;
 doReturn(new Rectangle(0, 0, width - 16, 200 - 22)).when(panel).getBounds();
 doReturn(new Dimension(width - 16, 16)).when(panel).getPreferredSize();
 doReturn(new Dimension(width - 16, 16)).when(panel).getSize();
 doReturn(new Dimension(width / 2, 20)).when(columnModel.getColumn(0)).getPreferredWidth();
 doReturn(new Dimension(width / 2, 20)).when(columnModel.getColumn(1)).getPreferredWidth();
 doReturn(1).when(panel.getDataTable()).getRowCount();
 doReturn(new String[][]{{"1", "line 1"}, {"2", "line 2"}, {"3", "line 3"}, {"4", "line 4"}, {"5", "line 5"}, {"6", "line 6"}, {"7", "line 7"}, {"8", "line 8"}, {"9", "line 9"}, {"10", "line 10"}}).when(panel.getTableModel()).getDataVector();
 assertEquals(firstLine, panel.getModel().getFirstLine());
 assertEquals(lastLine, panel.getModel().getLastLine());
 assertEquals(maxLine, panel.getModel().getMaxLine());
 assertEquals(nbDisplayedLines, panel.getModel().getNbDisplayedLines());
 assertEquals(minLine, panel.getModel().getMinLine());
 verify(sorter).setRowFilter(isA(RowFilter.class));
 }

 @Test
 public void testScrolling() {
 // Test if the JTailPanel scrolls correctly when the user scrolls
 when(modelMock.getScrolledEvent()).thenReturn(new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 5, 10));
 panel.modelChanged(modelMock, new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 5, 10));
 TableRowSorter<DefaultTableModel> sorter = mock(TableRowSorter.class);
 doReturn(sorter).when(panel.getDataTable()).getRowSorter();
 TableColumnModel columnModel = panel.getDataTable().getColumnModel();
 int width = 500;
 panel.setPreferredSize(new Dimension(width, 200));
 panel.setSize(new Dimension(width, 200));
 panel.updateUI();
 int firstLine = 0;
 int lastLine = 10;
 int maxLine = 20;
 int nbDisplayedLines = 5;
 int minLine = 5;
 doReturn(new Rectangle(0, 0, width - 16, 200 - 22)).when(panel).getBounds();
 doReturn(new Dimension(width - 16, 16)).when(panel).getPreferredSize();
 doReturn(new Dimension(width - 16, 16)).when(panel).getSize();
 doReturn(new Dimension(width / 2, 20)).when(columnModel.getColumn(0)).getPreferredWidth();
 doReturn(new Dimension(width / 2, 20)).when(columnModel.getColumn(1)).getPreferredWidth();
 doReturn(1).when(panel.getDataTable()).getRowCount();
 doReturn(new String[][]{{"1", "line 1"}, {"2", "line 2"}, {"3", "line 3"}, {"4", "line 4"}, {"5", "line 5"}, {"6", "line 6"}, {"7", "line 7"}, {"8", "line 8"}, {"9", "line 9"}, {"10", "line 10"}}).when(panel.getTableModel()).getDataVector();
 assertEquals(firstLine, panel.getModel().getFirstLine());
 assertEquals(lastLine, panel.getModel().getLastLine());
 assertEquals(maxLine, panel.getModel().getMaxLine());
 assertEquals(nbDisplayedLines, panel.getModel().getNbDisplayedLines());
 assertEquals(minLine, panel.getModel().getMinLine());
 verify(sorter).setRowFilter(isA(RowFilter.class));
 }
}