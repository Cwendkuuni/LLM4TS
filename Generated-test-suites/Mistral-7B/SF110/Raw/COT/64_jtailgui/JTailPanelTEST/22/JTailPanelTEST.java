package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.JTailSizeController;
import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.gui.view.JTailPanelTest;
import fr.pingtimeout.jtail.util.JTailLogger;
import fr.pingtimeout.jtail.util.LoggerLevel;

public class JTailPanelTest {

 private JTailPanel panel;
 private JTailModel model;

 @Before
 public void setUp() {
 model = mock(JTailModel.class);
 panel = new JTailPanel(model);
 }

 @Test
 public void testJTailPanelConstructor() {
 JTailLogger mockLogger = mock(JTailLogger.class);
 when(model.addObserver(any())).thenReturn(this);
 JScrollPane mockScrollPane = mock(JScrollPane.class);
 JScrollBar mockVerticalScrollBar = mock(JScrollBar.class);
 JTable mockDataTable = mock(JTable.class);
 DefaultTableModel mockTableModel = mock(DefaultTableModel.class);
 int expectedCharacterHeight = 10;
 int expectedMaxCharacterWidth = 20;
 when(mockDataTable.getFontMetrics(any())).thenReturn(mockFontMetrics());
 when(mockFontMetrics().getHeight()).thenReturn(expectedCharacterHeight);
 when(mockFontMetrics().stringWidth(anyString())).thenReturn(expectedMaxCharacterWidth);
 when(mockScrollPane.setVerticalScrollBarPolicy(anyInt())).thenReturn(panel);
 when(mockScrollPane.setHorizontalScrollBarPolicy(anyInt())).thenReturn(panel);
 when(mockScrollPane.getViewport()).thenReturn(panel.dataTable);
 when(mockScrollPane.setViewportView(any())).thenReturn(panel);
 when(mockScrollPane.getComponentZOrder(any())).thenReturn(0);
 when(mockScrollPane.getParent()).thenReturn(panel);
 when(mockScrollPane.getBounds()).thenReturn(new Rectangle(0, 0, 100, 100));
 when(mockVerticalScrollBar.addAdjustmentListener(any())).thenReturn(panel);
 when(mockTableModel.getRowCount()).thenReturn(0);
 when(mockTableModel.addRow(any(Object[].class))).thenReturn(true);
 when(mockTableModel.getColumnModel()).thenReturn(panel.tableModel.getColumnModel());
 when(mockTableModel.getColumn(anyInt())).thenReturn(panel.tableModel.getColumnModel().getColumn(0));
 when(mockTableModel.getColumn(anyInt()).getPreferredWidth()).thenReturn(expectedMaxCharacterWidth * 10);
 when(mockTableModel.getColumn(anyInt()).getWidth()).thenReturn(expectedMaxCharacterWidth * 11);
 when(panel.getWidth()).thenReturn(100);

 // Test the constructor
 JTailPanel testPanel = new JTailPanel(model);
 assertSame(panel, testPanel);
 assertEquals(model, testPanel.model);
 assertSame(mockScrollPane, testPanel.scrollPane);
 assertSame(mockDataTable, testPanel.dataTable);
 assertSame(mockTableModel, testPanel.tableModel);
 assertEquals(expectedCharacterHeight, testPanel.characterHeight);
 assertEquals(expectedMaxCharacterWidth, testPanel.maxCharacterWidth);
 }

 @Test
 public void testGetCharacterHeight() {
 int expectedCharacterHeight = 15;
 when(mockDataTable.getFontMetrics(any())).thenReturn(mockFontMetrics());
 when(mockFontMetrics().getHeight()).thenReturn(expectedCharacterHeight);
 assertEquals(expectedCharacterHeight, panel.getCharacterHeight());
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertSame(panel.verticalScrollBar, panel.getVerticalScrollBar());
 }

 @Test
 public void testUpdateObserver() {
 JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
 panel.update(model, event);
 verify(model).addObserver(any());
 }

 @Test
 public void testUpdateObserverWithResizedEvent() {
 JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
 panel.update(model, event);
 JScrollBar verticalScrollBar = panel.verticalScrollBar;
 TableColumnModel tableColumnModel = panel.dataTable.getColumnModel();
 int expectedMinLine = 0;
 int expectedFirstLine = 0;
 int expectedNbDisplayedLines = 7;
 int expectedMaxLine = 10;
 verify(model).getLineNumbers().equals("0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n10");
 verify(model).getContent().equals("line 0\nline 1\nline 2\nline 3\nline 4\nline 5\nline 6\nline 7\nline 8\nline 9\nline 10");
 assertEquals(expectedMinLine, verticalScrollBar.getModel().getMinimum());
 assertEquals(expectedFirstLine, verticalScrollBar.getModel().getValue());
 assertEquals(expectedNbDisplayedLines, verticalScrollBar.getModel().getExtent());
 assertEquals(expectedMaxLine, verticalScrollBar.getModel().getMaximum());

 // Test table model size
 assertEquals(11, tableColumnModel.getColumnCount());
 assertEquals("", tableColumnModel.getColumnName(0));
 assertEquals("#", tableColumnModel.getColumnName(1));

 // Test table data
 assertEquals(11, tableModel.getRowCount());
 List<Object[]> rows = Arrays.asList(tableModel.getDataVector().toArray());
 for (int i = 0; i < 11; i++) {
 Object[] row = rows.get(i);
 assertEquals(String.valueOf(i), row[0]);
 assertEquals(String.format("line %d", i), row[1]);
 }
 }

 @Test
 public void testUpdateObserverWithScrolledEvent() {
 JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
 panel.update(model, event);
 verify(model).getLineNumbers().equals("0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n10");
 verify(model).getContent().equals("line 0\nline 1\nline 2\nline 3\nline 4\nline 5\nline 6\nline 7\nline 8\nline 9\nline 10");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateObserverWithNullObserver() {
 panel.update(model, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateObserverWithNullEvent() {
 panel.update(model, (JTailModelEvent)null);
 }

 @Test
 public void testUpdateDataTable() {
 String expectedLineNumbers = "0\n1\n2\n3\n4\n5\n6";
 String expectedLines = "line 0\nline 1\nline 2\nline 3\nline 4\nline 5\nline 6";
 panel.updateDataTable(expectedLineNumbers, expectedLines);
 assertEquals(expectedLineNumbers, panel.model.getLineNumbers());
 assertEquals(expectedLines, panel.model.getContent());
 }

 @Test
 public void testUpdateDataTableWithEmptyLines() {
 String expectedLineNumbers = "0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n10";
 String expectedLines = "";
 panel.updateDataTable(expectedLineNumbers, expectedLines);
 assertEquals(expectedLineNumbers, panel.model.getLineNumbers());
 assertEquals(0, panel.dataTable.getRowCount());
 }

 @Test
 public void testUpdateDataTableWithInvalidLinesNumber() {
 String expectedLineNumbers = "0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n10";
 String expectedLines = "line 0\nline 1\nline 2\nline 3\nline 4\nline 5\nline 6\nline 7\nline 8\nline 11\nline 12";
 panel.updateDataTable(expectedLineNumbers, expectedLines);
 assertEquals(expectedLineNumbers, panel.model.getLineNumbers());
 assertEquals(12, panel.dataTable.getRowCount());
 }

 @Test
 public void testUpdateDataTableWithNullLines() {
 String expectedLineNumbers = "0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n10";
 String expectedLines = null;
 panel.updateDataTable(expectedLineNumbers, expectedLines);
 assertEquals(expectedLineNumbers, panel.model.getLineNumbers());
 assertEquals(0, panel.dataTable.getRowCount());
 }

 @Test
 public void testSetEnabledVerticalScrollBar() {
 when(model.getLineNumbers()).thenReturn("0\n1\n2\n3\n4\n5\n6");
 when(model.getContent()).thenReturn("line 0\nline 1\nline 2\nline 3\nline 4\nline 5\nline 6");
 panel.updateDataTable(panel.model.getLineNumbers(), panel.model.getContent());
 panel.verticalScrollBar.getModel().setValue(3);
 panel.setEnabledVerticalScrollBar(false);
 assertEquals(false, panel.verticalScrollBar.isEnabled());
 panel.verticalScrollBar.getModel().setValue(5);
 panel.setEnabledVerticalScrollBar(true);
 assertEquals(true, panel.verticalScrollBar.isEnabled());
 }
}