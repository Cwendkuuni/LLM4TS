package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;

import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.controller.JTailSizeController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.view.JTailPanel;

public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel jtailPanel;
 private JTable dataTable;
 private DefaultTableModel tableModel;
 private JScrollBar verticalScrollBar;
 private JScrollPane scrollPane;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 model = mock(JTailModel.class);
 jtailPanel = new JTailPanel(model);
 dataTable = jtailPanel.dataTable;
 tableModel = jtailPanel.tableModel;
 verticalScrollBar = jtailPanel.verticalScrollBar;
 scrollPane = jtailPanel.scrollPane;
 }

 @Test
 public void testInitialization() {
 assertNotNull(jtailPanel);
 assertNotNull(jtailPanel.model);
 assertNotNull(jtailPanel.scrollPane);
 assertNotNull(jtailPanel.verticalScrollBar);
 assertNotNull(jtailPanel.dataTable);
 assertNotNull(jtailPanel.tableModel);
 }

 @Test
 public void testUpdateDataTableWithValidData() {
 when(model.getLineNumbers()).thenReturn("1\n2\n3\n4");
 when(model.getContent()).thenReturn("Line 1\nLine 2\nLine 3\nLine 4");
 jtailPanel.updateDataTable(model.getLineNumbers(), model.getContent());

 assertEquals(4, tableModel.getRowCount());
 List<String> lineNumbers = Arrays.asList("1", "2", "3", "4");
 List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3", "Line 4");
 for (int i = 0; i < lineNumbers.size(); ++i) {
 assertEquals(lineNumbers.get(i), tableModel.getValueAt(i, 0).toString());
 assertEquals(lines.get(i), tableModel.getValueAt(i, 1).toString());
 }
 }

 @Test
 public void testUpdateDataTableWithEmptyContent() {
 when(model.getLineNumbers()).thenReturn("1\n2\n3");
 when(model.getContent()).thenReturn("");
 jtailPanel.updateDataTable(model.getLineNumbers(), model.getContent());

 assertEquals(3, tableModel.getRowCount());
 }

 @Test
 public void testUpdateDataTableWithNullContent() {
 when(model.getLineNumbers()).thenReturn("1\n2\n3");
 when(model.getContent()).thenReturn(null);
 jtailPanel.updateDataTable(model.getLineNumbers(), model.getContent());

 assertEquals(0, tableModel.getRowCount());
 }

 @Test
 public void testUpdateScrollBarWhenModelIsResized() {
 final int maxLine = 10;
 final int nbDisplayedLines = 5;
 when(model.getMaxLine()).thenReturn(maxLine);
 when(model.getFirstLine()).thenReturn(0);
 when(model.getNbDisplayedLines()).thenReturn(nbDisplayedLines);
 when(model.getMinLine()).thenReturn(0);

 final int expectedMin = 0;
 final int expectedMax = maxLine;
 final int expectedValue = 0;
 final int expectedExtent = nbDisplayedLines;

 verify(verticalScrollBar, never()).setValueIsAdjusting();
 jtailPanel.update(model, new JTailModelEvent(JTailModelEvent.Type.RESIZED));
 verify(verticalScrollBar).setValueIsAdjusting();
 verify(verticalScrollBar).setValue(expectedValue);
 verify(verticalScrollBar).setMinimum(expectedMin);
 verify(verticalScrollBar).setMaximum(expectedMax);
 verify(verticalScrollBar).setExtent(expectedExtent);
 }

 @Test
 public void testUpdateScrollBarWhenUserScrolls() {
 final int firstLine = 5;
 final int maxLine = 10;
 when(model.getFirstLine()).thenReturn(firstLine);
 when(model.getMaxLine()).thenReturn(maxLine);
 when(model.getNbDisplayedLines()).thenReturn(5);

 final int expectedValue = firstLine;

 verify(verticalScrollBar, never()).setValueIsAdjusting();
 jtailPanel.update(model, new JTailModelEvent(JTailModelEvent.Type.SCROLLED));
 verify(verticalScrollBar).setValueIsAdjusting();
 verify(verticalScrollBar).setValue(expectedValue);
 }

 @Test
 public void testUpdateDataTableColumnsWidths() {
 final TableColumn numbersColumn = dataTable.getColumnModel().getColumn(0);
 final TableColumn linesColumn = dataTable.getColumnModel().getColumn(1);
 final int width = 400;
 final Font font = new Font("Arial", Font.PLAIN, 14);
 final FontMetrics metrics = dataTable.getFontMetrics(font);
 final int maxCharacterWidth = metrics.stringWidth("0") + 2;
 final int numbersColumnWidth = 200;
 final int linesColumnWidth = width - numbersColumnWidth;

 jtailPanel.setPreferredSize(new Dimension(width, 200));
 jtailPanel.updateDataTable(model.getLineNumbers(), model.getContent());

 assertEquals(numbersColumnWidth, numbersColumn.getPreferredWidth());
 assertEquals(linesColumnWidth, linesColumn.getPreferredWidth());
 }

 @Test
 public void testDisableVerticalScrollBarWhenAllLinesFitsOnScreen() {
 final int maxLine = 10;
 final int nbDisplayedLines = maxLine;
 when(model.getMaxLine()).thenReturn(maxLine);
 when(model.getFirstLine()).thenReturn(0);
 when(model.getNbDisplayedLines()).thenReturn(nbDisplayedLines);
 when(model.getMinLine()).thenReturn(0);

 jtailPanel.update(model, new JTailModelEvent(JTailModelEvent.Type.RESIZED));
 verify(verticalScrollBar, never()).setEnabled(true);
 }

 @Test
 public void testEnableVerticalScrollBarWhenNotAllLinesFitsOnScreen() {
 final int maxLine = 15;
 final int nbDisplayedLines = 10;
 when(model.getMaxLine()).thenReturn(maxLine);
 when(model.getFirstLine()).thenReturn(5);
 when(model.getNbDisplayedLines()).thenReturn(nbDisplayedLines);
 when(model.getMinLine()).thenReturn(0);

 jtailPanel.update(model, new JTailModelEvent(JTailModelEvent.Type.RESIZED));
 verify(verticalScrollBar).setEnabled(true);
 }
}