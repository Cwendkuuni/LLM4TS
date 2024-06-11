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
 private JScrollPane mockScrollPane;
 private JTable mockDataTable;
 private DefaultTableModel mockTableModel;
 private JScrollBar mockVerticalScrollBar;
 private JPanel mockJPanel;
 private JTailSizeController mockJTailSizeController;
 private ScrollBarAdjustmentController mockScrollBarAdjustmentController;

 @Before
 public void setUp() {
 mockModel = mock(JTailModel.class);
 jTailPanel = new JTailPanel(mockModel);
 mockScrollPane = mock(JScrollPane.class);
 mockDataTable = mock(JTable.class);
 mockTableModel = mock(DefaultTableModel.class);
 mockVerticalScrollBar = mock(JScrollBar.class);
 mockJPanel = mock(JPanel.class);
 mockJTailSizeController = mock(JTailSizeController.class);
 mockScrollBarAdjustmentController = mock(ScrollBarAdjustmentController.class);

 when(mockScrollPane.getViewport()).thenReturn(mockDataTable);
 when(mockDataTable.getModel()).thenReturn(mockTableModel);
 when(mockJPanel.getLayout()).thenReturn(new BorderLayout());
 when(mockJPanel.add(any(Component.class), anyString())).thenAnswer(invocation -> jTailPanel.add(invocation.getArgument(0), invocation.getArgument(1)));
 when(mockJPanel.getComponent(anyInt())).thenAnswer(invocation -> invocation.getArgument(0).getComponent(invocation.getArgument(1)));
 when(mockModel.getLineNumbers()).thenReturn("1\n2\n3\n4");
 when(mockModel.getContent()).thenReturn("line1\nline2\nline3\nline4");
 when(mockModel.getLineNumbers().split("\n").length).thenReturn(5);
 when(mockModel.getNbDisplayedLines()).thenReturn(3);
 when(mockModel.getMaxLine()).thenReturn(10);
 when(mockModel.getFirstLine()).thenReturn(1);
 when(mockModel.getMinLine()).thenReturn(1);
 when(mockModel.getType()).thenReturn(JTailModelEvent.Type.RESIZED);
 when(mockModel.getEventSource()).thenReturn(mockModel);

 jTailPanel.scrollPane = mockScrollPane;
 jTailPanel.dataTable = mockDataTable;
 jTailPanel.tableModel = mockTableModel;
 jTailPanel.verticalScrollBar = mockVerticalScrollBar;
 jTailPanel.addComponentListener(mockJTailSizeController);
 jTailPanel.addObserver(mockModel);
 jTailPanel.verticalScrollBar.addAdjustmentListener(mockScrollBarAdjustmentController);

 }

 @Test
 public void testConstructor() {
 assertEquals("JTailPanel", jTailPanel.getClass().getSimpleName());
 }

 @Test
 public void testGetCharacterHeight() {
 assertEquals(11, jTailPanel.getCharacterHeight());
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertEquals(mockVerticalScrollBar, jTailPanel.getVerticalScrollBar());
 }

 @Test
 public void testUpdate() {
 final JTailModelEvent event = new JTailModelEvent(mockModel, JTailModelEvent.Type.RESIZED, this);
 jTailPanel.update(mockModel, event);
 verify(mockModel, times(1)).addObserver(eq(jTailPanel));

 final BoundedRangeModel rangeModel = mockVerticalScrollBar.getModel();
 assertEquals(1, rangeModel.getMinimum());
 assertEquals(1, rangeModel.getValue());
 assertEquals(3, rangeModel.getExtent());
 assertEquals(10, rangeModel.getMaximum());
 assertEquals(false, rangeModel.isEnabled());
 }

 @Test
 public void testUpdateDataTable() {
 jTailPanel.updateDataTable(mockModel.getLineNumbers(), mockModel.getContent());
 verify(mockTableModel, times(5)).addRow(any(Object[].class));
 }

 @Test
 public void testScrollBarAdjustmentController() {
 final int newValue = 5;
 when(mockModel.getNbDisplayedLines()).thenReturn(newValue);
 jTailPanel.verticalScrollBar.setValue(newValue);
 jTailPanel.updateDataTable(mockModel.getLineNumbers(), mockModel.getContent());
 verify(mockScrollBarAdjustmentController, times(1)).adjustScrollBar(eq(jTailPanel), eq(newValue));
 }

 @Test
 public void testJTailSizeController() {
 final int width = 500;
 final int height = 500;
 jTailPanel.setSize(new Dimension(width, height));
 jTailPanel.update(mockModel, any(JTailModelEvent.class));
 assertEquals(false, jTailPanel.verticalScrollBar.isEnabled());
 }

 @Test
 public void testJTailSizeController_withScrolling() {
 final int width = 500;
 final int height = 500;
 jTailPanel.setSize(new Dimension(width, height));
 jTailPanel.update(mockModel, any(JTailModelEvent.class));
 jTailPanel.verticalScrollBar.setValue(5);
 jTailPanel.update(mockModel, any(JTailModelEvent.class));
 assertEquals(true, jTailPanel.verticalScrollBar.isEnabled());
 }
}