package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.junit.Before;
import org.junit.Test;

public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel jTailPanel;
 private DefaultTableModel tableModel;
 private JTable dataTable;
 private JScrollPane scrollPane;
 private JScrollBar verticalScrollBar;
 private BoundedRangeModel rangeModel;

 @Before
 public void setUp() {
 model = mock(JTailModel.class);
 jTailPanel = new JTailPanel(model);
 tableModel = jTailPanel.tableModel;
 dataTable = jTailPanel.dataTable;
 scrollPane = jTailPanel.scrollPane;
 verticalScrollBar = jTailPanel.verticalScrollBar;
 rangeModel = verticalScrollBar.getModel();
 }

 @Test
 public void testJTailPanelConstruction() {
 assertEquals("JTailPanel should have added observer to JTailModel", 1, model.getObserverCount());
 }

 @Test
 public void testGetCharacterHeight() {
 FontMetrics metrics = mock(FontMetrics.class);
 when(dataTable.getFontMetrics(dataTable.getFont())).thenReturn(metrics);
 int characterHeight = jTailPanel.getCharacterHeight();
 int expectedCharacterHeight = metrics.getHeight() - 1;
 assertEquals("Character height should be correct", expectedCharacterHeight, characterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertSame("JTailPanel and verticalScrollBar should be the same instance", jTailPanel.verticalScrollBar, verticalScrollBar);
 }

 @Test
 public void testUpdateObserver() {
 JTailModelEvent event = new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, 1, "test", 10, 10, 10);
 jTailPanel.update(model, event);
 assertEquals("Observer should have received the correct notification", JTailModelEvent.Type.RESIZED, event.type);
 }

 @Test
 public void testUpdateObserverWithResizedEvent() {
 int minLine = 1;
 int firstLine = 1;
 int maxLine = 1;
 int nbDisplayedLines = 1;

 when(model.getMinLine()).thenReturn(minLine);
 when(model.getFirstLine()).thenReturn(firstLine);
 when(model.getMaxLine()).thenReturn(maxLine);
 when(model.getNbDisplayedLines()).thenReturn(nbDisplayedLines);

 jTailPanel.update(model, event);

 assertEquals("Vertical scrollbar minimum should be set to minLine", minLine, rangeModel.getMinimum());
 assertEquals("Vertical scrollbar value should be set to firstLine", firstLine, rangeModel.getValue());
 assertEquals("Vertical scrollbar extent should be set to nbDisplayedLines", nbDisplayedLines, rangeModel.getExtent());
 assertEquals("Vertical scrollbar maximum should be set to maxLine", maxLine, rangeModel.getMaximum());
 }

 @Test
 public void testUpdateObserverWithScrolledEvent() {
 String lineNumbers = "1\n2\n3\n4\n5";
 String lines = "line1\nline2\nline3\nline4\nline5";

 when(model.getLineNumbers()).thenReturn(lineNumbers);
 when(model.getContent()).thenReturn(lines);

 jTailPanel.update(model, event);

 assertEquals("Table model should have added 5 rows", 5, tableModel.getRowCount());
 assertEquals("Rows should contain line numbers and corresponding lines", "1", tableModel.getValueAt(0, 0).toString());
 assertEquals("Rows should contain line numbers and corresponding lines", "line1", tableModel.getValueAt(0, 1).toString());
 }

 @Test
 public void testUpdateDataTable() {
 String lineNumbers = "1\n2\n3\n4\n5\n6\n7\n8";
 String lines = "line1\nline2\nline3\nline4\nline5\nline6\nline7\nline8";

 when(model.getLineNumbers()).thenReturn(lineNumbers);
 when(model.getContent()).thenReturn(lines);

 jTailPanel.updateDataTable(lineNumbers, lines);

 assertEquals("Table model should have added 8 rows", 8, tableModel.getRowCount());
 }

 @Test
 public void testSetPreferredWidths() {
 int numbersColumnWidth = 12;
 int linesColumnWidth = 50;

 TableColumn numbersColumn = dataTable.getColumnModel().getColumn(0);
 TableColumn linesColumn = dataTable.getColumnModel().getColumn(1);

 jTailPanel.updateDataTable(lineNumbers, lines);
 jTailPanel.setPreferredWidths(numbersColumnWidth, linesColumnWidth);

 assertEquals("Numbers column width should be set to numbersColumnWidth", numbersColumnWidth, numbersColumn.getPreferredWidth());
 assertEquals("Lines column width should be set to linesColumnWidth", linesColumnWidth, linesColumn.getPreferredWidth());
 }

}