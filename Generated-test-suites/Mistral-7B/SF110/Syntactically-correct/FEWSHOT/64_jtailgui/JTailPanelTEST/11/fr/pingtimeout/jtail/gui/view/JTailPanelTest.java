package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.*;
import java.awt.*;
import java.util.*;
import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import javax.swing.*;
import javax.swing.table.*;

public class JTailPanelTest {

 JTailModel mockModel;
 JScrollBar mockVerticalScrollBar;
 JTable mockDataTable;
 DefaultTableModel mockTableModel;
 JPanel mockJPanel;
 BorderLayout mockLayout;
 JScrollPane mockScrollPane;
 JTailSizeController mockJTailSizeController;
 ScrollBarAdjustmentController mockScrollBarAdjustmentController;

 @Before
 public void setUp() {
 mockModel = mock(JTailModel.class);
 mockVerticalScrollBar = mock(JScrollBar.class);
 mockDataTable = mock(JTable.class);
 mockTableModel = mock(DefaultTableModel.class);
 mockJPanel = mock(JPanel.class);
 mockLayout = mock(BorderLayout.class);
 mockScrollPane = mock(JScrollPane.class);
 mockJTailSizeController = mock(JTailSizeController.class);
 mockScrollBarAdjustmentController = mock(ScrollBarAdjustmentController.class);
 }

 @Test
 public void testJTailPanelConstruction() {
 JTailPanel jTailPanel = new JTailPanel(mockModel);
 verify(mockModel, times(1)).addObserver(jTailPanel);
 assertNotNull(jTailPanel.getVerticalScrollBar());
 assertNotNull(jTailPanel.getTableModel());
 assertNotNull(jTailPanel.getDataTable());
 assertNotNull(jTailPanel.getScrollPane());
 assertNotNull(jTailPanel.getVerticalScrollBar());
 assertEquals(new BorderLayout(), jTailPanel.getLayout());
 }

 @Test
 public void testGetCharacterHeight() {
 JTailPanel jTailPanel = new JTailPanel(mockModel);
 FontMetrics metrics = mock(FontMetrics.class);
 when(mockDataTable.getFontMetrics(eq(mockDataTable.getFont()))).thenReturn(metrics);
 int characterHeight = jTailPanel.getCharacterHeight();
 int expectedCharacterHeight = metrics.getHeight() - 1;
 assertEquals(expectedCharacterHeight, characterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 JTailPanel jTailPanel = new JTailPanel(mockModel);
 assertEquals(mockVerticalScrollBar, jTailPanel.getVerticalScrollBar());
 }

 @Test
 public void testUpdateObserver() {
 JTailModelEvent mockEvent = mock(JTailModelEvent.class);
 when(mockModel.getLineNumbers()).thenReturn("1\n2\n3\n4");
 when(mockModel.getContent()).thenReturn("Line 1\nLine 2\nLine 3\nLine 4");
 JTailPanel jTailPanel = new JTailPanel(mockModel);
 jTailPanel.update(mockModel, mockEvent);
 verify(mockEvent, times(1)).getType();
 verify(mockEvent, times(1)).getModel();
 verify(mockModel, times(1)).getLineNumbers();
 verify(mockModel, times(1)).getContent();
 }

 @Test
 public void testUpdateDataTable() {
 JTailPanel jTailPanel = new JTailPanel(mockModel);
 JTailModelEvent mockEvent = mock(JTailModelEvent.class);
 String lineNumbers = "1\n2\n3\n4";
 String lines = "Line 1\nLine 2\nLine 3\nLine 4";
 when(mockModel.getLineNumbers()).thenReturn(lineNumbers);
 when(mockModel.getContent()).thenReturn(lines);
 jTailPanel.update(mockModel, mockEvent);
 String[] lineNumbersArray = lineNumbers.split("\n");
 String[] linesArray = lines.split("\n");
 int expectedTableRows = lineNumbersArray.length;
 int actualTableRows = jTailPanel.getDataTable().getRowCount();
 assertEquals(expectedTableRows, actualTableRows);
 }

 @Test
 public void testResizeColumns() {
 JTailPanel jTailPanel = new JTailPanel(mockModel);
 when(mockDataTable.getWidth()).thenReturn(600);
 when(mockDataTable.getColumnModel().getColumn(0).getWidth()).thenReturn(200);
 when(mockDataTable.getColumnModel().getColumn(1).getWidth()).thenReturn(400);
 int numbersColumnWidth = 10 * 15 + 2;
 int linesColumnWidth = 600 - numbersColumnWidth;
 TableColumn numbersColumn = jTailPanel.getDataTable().getColumnModel().getColumn(0);
 TableColumn linesColumn = jTailPanel.getDataTable().getColumnModel().getColumn(1);
 jTailPanel.updateDataTable(mockModel.getLineNumbers(), mockModel.getContent());
 assertEquals(numbersColumnWidth, numbersColumn.getPreferredWidth());
 assertEquals(linesColumnWidth, linesColumn.getPreferredWidth());
 }

 @Test
 public void testDisableScrollBar() {
 JTailPanel jTailPanel = new JTailPanel(mockModel);
 jTailPanel.update(mockModel, mock(JTailModelEvent.class));
 jTailPanel.update(mockModel, mock(JTailModelEvent.class));
 jTailPanel.update(mockModel, mock(JTailModelEvent.class));
 assertTrue(jTailPanel.getVerticalScrollBar().isEnabled());
 jTailPanel.update(mockModel, mock(JTailModelEvent.class));
 assertFalse(jTailPanel.getVerticalScrollBar().isEnabled());
 }

 @Test
 public void testScrollBarAdjustmentListener() {
 JTailPanel jTailPanel = new JTailPanel(mockModel);
 JScrollBarAdjustmentEvent mockAdjustmentEvent = mock(JScrollBarAdjustmentEvent.class);
 ScrollBarAdjustmentController scrollBarAdjustmentController = new ScrollBarAdjustmentController(jTailPanel, mockModel);
 scrollBarAdjustmentController.valueChanged(mockAdjustmentEvent);
 verify(jTailPanel, times(1)).update(mockModel, eq(mockAdjustmentEvent.getSource()));
 }

 @Test
 public void testComponentListener() {
 JTailPanel jTailPanel = new JTailPanel(mockModel);
 ComponentEvent componentEvent = mock(ComponentEvent.class);
 JTailSizeController jTailSizeController = new JTailSizeController(jTailPanel, mockModel);
 jTailSizeController.componentResized(componentEvent);
 verify(jTailPanel, times(1)).update(mockModel, eq(componentEvent.getSource()));
 }

}