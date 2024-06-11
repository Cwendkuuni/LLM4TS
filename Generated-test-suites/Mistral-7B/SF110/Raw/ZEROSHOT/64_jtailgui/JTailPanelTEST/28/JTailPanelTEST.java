package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class JTailPanelTest {

    private JTailModel model;
    private JTailPanel jTailPanel;

    @Before
    public void setUp() {
        model = Mockito.spy(new JTailModel());
        jTailPanel = new JTailPanel(model);
    }

    @Test
    public void testJTailPanelConstructor() {
        assertNotNull(jTailPanel);
        assertEquals(model, jTailPanel.model);
        assertTrue(jTailPanel.verticalScrollBar instanceof JScrollBar);
        assertTrue(jTailPanel.scrollPane instanceof JScrollPane);
        assertTrue(jTailPanel.dataTable instanceof JTable);
        assertTrue(jTailPanel.tableModel instanceof DefaultTableModel);
        assertEquals(2, jTailPanel.tableModel.getColumnCount());
    }

    @Test
    public void testGetCharacterHeight() {
        final FontMetrics metrics = Mockito.mock(FontMetrics.class);
        when(jTailPanel.dataTable.getFontMetrics(any(Font.class))).thenReturn(metrics);
        int characterHeight = jTailPanel.getCharacterHeight();
        verify(metrics).getHeight();
        assertEquals(characterHeight, jTailPanel.characterHeight);
    }

    @Test
    public void testGetVerticalScrollBar() {
        assertSame(jTailPanel.verticalScrollBar, jTailPanel.getVerticalScrollBar());
    }

    @Test
    public void testUpdateObserver() {
        final JTailModelEvent event = new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, 1, 1, 1, 1, 1);
        jTailPanel.update(model, event);
        verify(model).addObserver(jTailPanel);
        verify(model).notifyObservers(event);
    }

    @Test
    public void testUpdateWithResizedEvent() {
        final int minLine = 1;
        final int firstLine = 2;
        final int nbDisplayedLines = 3;
        final int maxLine = 4;
        final int width = 10;

        final BoundedRangeModel rangeModel = Mockito.mock(BoundedRangeModel.class);
        when(jTailPanel.verticalScrollBar.getModel()).thenReturn(rangeModel);

        final int[] lines = new int[maxLine];
        for (int i = 0; i < maxLine; i++) {
            lines[i] = i + 1;
        }

        jTailPanel.update(model, new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, minLine, firstLine, nbDisplayedLines, maxLine, lines));

        verify(rangeModel).setValueIsAdjusting(true);
        verify(rangeModel).setMinimum(minLine);
        verify(rangeModel).setValue(firstLine);
        verify(rangeModel).setExtent(nbDisplayedLines);
        verify(rangeModel).setMaximum(maxLine);
        verify(rangeModel).setValueIsAdjusting(false);
        verify(jTailPanel).updateDataTable(lineNumbers, linesAsString);

        assertTrue(jTailPanel.verticalScrollBar.isEnabled());
    }

    @Test
    public void testUpdateWithScrolledEvent() {
        final String lineNumbers = "1\n2\n3\n4\n5\n";
        final String linesAsString = "line 1\nline 2\nline 3\nline 4\nline 5\n";

        jTailPanel.update(model, new JTailModelEvent(model, JTailModelEvent.Type.SCROLLED, 2, 3));

        verify(jTailPanel).updateDataTable(lineNumbers, linesAsString);
    }

    @Test
    public void testUpdateDataTable() {
        final String lineNumbers = "1\n2\n3\n4\n5\n";
        final String linesAsString = "line 1\nline 2\nline 3\nline 4\nline 5\n";

        jTailPanel.updateDataTable(lineNumbers, linesAsString);

        final int numberOfRows = jTailPanel.dataTable.getRowCount();
        assertEquals(numberOfRows, 5);

        for (int i = 0; i < numberOfRows; i++) {
            final String lineNumber = (String) jTailPanel.dataTable.getValueAt(i, 0);
            final String line = (String) jTailPanel.dataTable.getValueAt(i, 1);
            assertEquals(String.valueOf(i + 1), lineNumber);
            assertEquals("line " + (i + 1), line);
        }
    }
}