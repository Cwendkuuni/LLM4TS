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

 private JTailModel mockModel;
 private JTailPanel jTailPanel;

 @Before
 public void setup() {
 mockModel = mock(JTailModel.class);
 jTailPanel = new JTailPanel(mockModel);
 }

 @Test
 public void testJTailPanelConstruction() {
 assertEquals("Building a new JTailPanel", JTailLogger.DEBUG_LOGS.get(0));
 assertNotNull(jTailPanel.getVerticalScrollBar());
 assertNotNull(jTailPanel.getDataTable());
 }

 @Test
 public void testGetCharacterHeight() {
 final FontMetrics fontMetrics = mock(FontMetrics.class);
 when(jTailPanel.getDataTable().getFontMetrics(any(Font.class))).thenReturn(fontMetrics);
 final int characterHeight = jTailPanel.getCharacterHeight();
 final int expectedCharacterHeight = fontMetrics.getHeight() - 1;
 assertEquals(expectedCharacterHeight, characterHeight);
 }

 @Test
 public void testUpdateWithResizedEvent() {
 final int maxLine = 10;
 final int firstLine = 5;
 final int nbDisplayedLines = 3;
 final int minLine = 2;
 final int width = 100;
 final int height = 200;
 final int expectedMinLine = 2;
 final int expectedFirstLine = 5;
 final int expectedMaxLine = 10;
 final int expectedNbDisplayedLines = 3;
 final JTailModelEvent event = new JTailModelEvent(mockModel, JTailModelEvent.Type.RESIZED, width, height, maxLine, firstLine, nbDisplayedLines);

 jTailPanel.update(mockModel, event);

 final BoundedRangeModel rangeModel = jTailPanel.getVerticalScrollBar().getModel();
 assertEquals(expectedMinLine, rangeModel.getMinimum());
 assertEquals(expectedFirstLine, rangeModel.getValue());
 assertEquals(expectedMaxLine, rangeModel.getMaximum());
 assertEquals(expectedNbDisplayedLines, rangeModel.getExtent());

 final String lineNumbers = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10";
 verify(mockModel, times(1)).getLineNumbers();
 verify(mockModel, times(1)).getContent();
 verifyNoMoreInteractions(mockModel);
 }

 @Test
 public void testUpdateWithScrolledEvent() {
 final int maxLine = 10;
 final int firstLine = 5;
 final int nbDisplayedLines = 3;
 final int lineNumber = 7;
 final String lines = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10";
 final JTailModelEvent event = new JTailModelEvent(mockModel, JTailModelEvent.Type.SCROLLED, lineNumber, lines);

 jTailPanel.update(mockModel, event);

 final String[] lineNumbersArray = lines.split("\n");
 final int length = lineNumbersArray.length;
 assertEquals(length, jTailPanel.getDataTable().getRowCount());
 assertEquals("7", jTailPanel.getDataTable().getValueAt(length - 1, 0).toString());
 assertEquals("", jTailPanel.getDataTable().getValueAt(length - 1, 1).toString());

 final TableColumn numbersColumn = jTailPanel.getDataTable().getColumnModel().getColumn(0);
 final TableColumn linesColumn = jTailPanel.getDataTable().getColumnModel().getColumn(1);
 final int numbersColumnWidth = length * jTailPanel.getCharacterWidth() * JTailPanel.MAX_CHARACTER_WIDTH;
 final int linesColumnWidth = jTailPanel.getDataTable().getWidth() - numbersColumnWidth;

 assertEquals(numbersColumnWidth, numbersColumn.getPreferredWidth());
 assertEquals(linesColumnWidth, linesColumn.getPreferredWidth());
 }
}