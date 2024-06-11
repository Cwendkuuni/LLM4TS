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

 private JTailModel model;
 private JTailPanel jTailPanel;
 private ScrollBarAdjustmentController scrollBarController;
 private JTailSizeController sizeController;

 @Before
 public void setUp() {
 model = mock(JTailModel.class);
 jTailPanel = new JTailPanel(model);
 scrollBarController = mock(ScrollBarAdjustmentController.class);
 sizeController = mock(JTailSizeController.class);
 }

 @Test
 public void testJTailPanelInitialization() {
    assertEquals("Building a new JTailPanel", JTailLogger.getLastLogMessage());
    assertSame(model, jTailPanel.getModel());
    assertTrue(jTailPanel.getVerticalScrollBar() instanceof JScrollBar);
    assertEquals(new DefaultTableModel(new String[]{"#", ""}, 0), jTailPanel.getTableModel());
    assertTrue(jTailPanel.getDataTable() instanceof JTable);
    assertTrue(jTailPanel.getScrollPane() instanceof JScrollPane);
    assertSame(scrollBarController, jTailPanel.getVerticalScrollBar().getAdjustmentListener());
    assertSame(sizeController, jTailPanel.addComponentListener(mock(ComponentListener.class)));
  }

 @Test
 public void testUpdateWhenModelIsResized() {
    final int minLine = 10;
    final int maxLine = 20;
    final int nbDisplayedLines = 5;
    final int firstLine = 15;

    when(model.getLineNumbers()).thenReturn("10\n11\n12\n13\n14\n15\n16\n17\n18\n19\n20");
    when(model.getContent()).thenReturn("Line 10\nLine 11\nLine 12\nLine 13\nLine 14\nLine 15\nLine 16\nLine 17\nLine 18\nLine 19\nLine 20");
    when(model.getEventType()).thenReturn(JTailModelEvent.Type.RESIZED);
    when(model.getMinLine()).thenReturn(minLine);
    when(model.getMaxLine()).thenReturn(maxLine);
    when(model.getFirstLine()).thenReturn(firstLine);
    when(model.getNbDisplayedLines()).thenReturn(nbDisplayedLines);

    jTailPanel.update(model, mock(JTailModelEvent.class));

    final int expectedMinimum = minLine;
    final int expectedValue = firstLine;
    final int expectedExtent = nbDisplayedLines;
    final int expectedMaximum = maxLine;

    final BoundedRangeModel rangeModel = jTailPanel.getVerticalScrollBar().getModel();

    assertEquals(expectedMinimum, rangeModel.getMinimum());
    assertEquals(expectedValue, rangeModel.getValue());
    assertEquals(expectedExtent, rangeModel.getExtent());
    assertEquals(expectedMaximum, rangeModel.getMaximum());
    assertEquals(false, jTailPanel.getVerticalScrollBar().isEnabled());
  }

 @Test
 public void testUpdateWhenModelIsScrolled() {
    final int minLine = 10;
    final int maxLine = 20;
    final int firstLine = 15;
    final int lastLine = 18;
    final int nbDisplayedLines = 5;

    when(model.getLineNumbers()).thenReturn("10\n11\n12\n13\n14\n15\n16\n17\n18\n19\n20");
    when(model.getContent()).thenReturn("Line 10\nLine 11\nLine 12\nLine 13\nLine 14\nLine 15\nLine 16\nLine 17\nLine 18\nLine 19\nLine 20");
    when(model.getEventType()).thenReturn(JTailModelEvent.Type.SCROLLED);

    jTailPanel.update(model, mock(JTailModelEvent.class));

    final String[] lineNumbers = jTailPanel.getModel().getLineNumbers().split("\n");
    final String[] lines = jTailPanel.getModel().getContent().split("\n");

    assertEquals(lineNumbers[lastLine], jTailPanel.getModel().getLineNumbers().split("\n")[jTailPanel.getModel().getTableModel().getRowCount() - 1]);
    assertEquals(lines[lastLine], jTailPanel.getModel().getContent().split("\n")[jTailPanel.getModel().getTableModel().getRowCount() - 1]);
  }

 @Test
 public void testUpdateDataTable() {
    final String lineNumbers = "10\n11\n12\n13\n14\n15\n16\n17\n18\n19\n20";
    final String lines = "Line 10\nLine 11\nLine 12\nLine 13\nLine 14\nLine 15\nLine 16\nLine 17\nLine 18\nLine 19\nLine 20";

    jTailPanel.updateDataTable(lineNumbers, lines);

    final int numbersColumnWidth = 18 * 12;
    final int linesColumnWidth = jTailPanel.getWidth() - numbersColumnWidth;

    final TableColumn numbersColumn = jTailPanel.getDataTable().getColumnModel().getColumn(0);
    final TableColumn linesColumn = jTailPanel.getDataTable().getColumnModel().getColumn(1);

    assertEquals(numbersColumnWidth, numbersColumn.getPreferredWidth());
    assertEquals(linesColumnWidth, linesColumn.getPreferredWidth());
  }

 @Test
 public void testSetEnabledVerticalScrollBar() {
    final int minLine = 10;
    final int maxLine = 20;
    final int firstLine = 15;
    final int nbDisplayedLines = 5;

    when(model.getLineNumbers()).thenReturn("10\n11\n12\n13\n14\n15\n16\n17\n18\n19\n20");
    when(model.getContent()).thenReturn("Line 10\nLine 11\nLine 12\nLine 13\nLine 14\nLine 15\nLine 16\nLine 17\nLine 18\nLine 19\nLine 20");
    when(model.getEventType()).thenReturn(JTailModelEvent.Type.RESIZED);
    when(model.getMinLine()).thenReturn(minLine);
    when(model.getMaxLine()).thenReturn(maxLine);
    when(model.getFirstLine()).thenReturn(firstLine);
    when(model.getNbDisplayedLines()).thenReturn(nbDisplayedLines);

    jTailPanel.update(model, mock(JTailModelEvent.class));

    final boolean expectedDisabled = (minLine == firstLine && maxLine == nbDisplayedLines);
    final boolean actualDisabled = jTailPanel.getVerticalScrollBar().isEnabled();

    assertEquals(expectedDisabled, actualDisabled);
  }
}