package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.util.*;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class JTailPanelTest {

    @Test
    public void testJTailPanelConstruction() {
        JTailModel mockModel = Mockito.mock(JTailModel.class);
        JTailPanel panelUnderTest = new JTailPanel(mockModel);
        assertEquals("JTailPanel", panelUnderTest.getClass().getName());
        assertEquals(mockModel, panelUnderTest.model);
        assertFalse(panelUnderTest.isShowing());
        assertEquals(false, panelUnderTest.isEnabled());
    }

    @Test
    public void testGetCharacterHeight() {
        JTailPanel panelUnderTest = new JTailPanel(new JTailModel());
        int characterHeight = panelUnderTest.getCharacterHeight();
        assertTrue(characterHeight > 0);
    }

    @Test
    public void testGetVerticalScrollBar() {
        JTailModel mockModel = Mockito.mock(JTailModel.class);
        JTailPanel panelUnderTest = new JTailPanel(mockModel);
        JScrollBar verticalScrollBar = panelUnderTest.getVerticalScrollBar();
        assertSame(verticalScrollBar, panelUnderTest.verticalScrollBar);
    }

    @Test
    public void testUpdate() {
        JTailModel mockModel = Mockito.mock(JTailModel.class);
        JTailPanel panelUnderTest = new JTailPanel(mockModel);
        JTailModelEvent event = Mockito.mock(JTailModelEvent.class);

        panelUnderTest.update(mockModel, event);

        Mockito.verify(mockModel).addObserver(panelUnderTest);
    }

    @Test
    public void testTableModelUpdate() {
        JTailModel mockModel = Mockito.mock(JTailModel.class);
        JTailPanel panelUnderTest = new JTailPanel(mockModel);
        JTailModelEvent event = Mockito.mock(JTailModelEvent.class);
        String lineNumbers = "1\n2\n3\n4";
        String lines = "one\ntwo\nthree\nfour";

        Mockito.when(mockModel.getLineNumbers()).thenReturn(lineNumbers);
        Mockito.when(mockModel.getContent()).thenReturn(lines);

        panelUnderTest.update(mockModel, event);

        TableModel tableModel = panelUnderTest.tableModel;
        int rows = tableModel.getRowCount();
        assertEquals(4, rows);

        for (int i = 0; i < rows; ++i) {
            String lineNumber = (String) tableModel.getValueAt(i, 0);
            String line = (String) tableModel.getValueAt(i, 1);

            if (i == 0) {
                assertEquals("1", lineNumber);
                assertEquals("one", line);
            }
            else if (i == 1) {
                assertEquals("2", lineNumber);
                assertEquals("two", line);
            }
            else if (i == 2) {
                assertEquals("3", lineNumber);
                assertEquals("three", line);
            }
            else if (i == 3) {
                assertEquals("4", lineNumber);
                assertEquals("four", line);
            }
        }
    }

    @Test
    public void testResizeColumns() {
        JTailModel mockModel = Mockito.mock(JTailModel.class);
        JTailPanel panelUnderTest = new JTailPanel(mockModel);
        String lineNumbers = "1\n2\n3\n4\n5\n6\n7\n8";
        String lines = "one\ntwo\nthree\nfour\nfive\nsix\nseven\neight";

        Mockito.when(mockModel.getLineNumbers()).thenReturn(lineNumbers);
        Mockito.when(mockModel.getContent()).thenReturn(lines);

        panelUnderTest.update(mockModel, Mockito.mock(JTailModelEvent.class));

        TableColumn numbersColumn = panelUnderTest.dataTable.getColumnModel().getColumn(0);
        TableColumn linesColumn = panelUnderTest.dataTable.getColumnModel().getColumn(1);

        int numbersColumnWidth = numbersColumn.getPreferredWidth();
        int linesColumnWidth = linesColumn.getPreferredWidth();

        int totalColumnWidth = numbersColumnWidth + linesColumnWidth;
        int dataTableWidth = panelUnderTest.dataTable.getWidth();

        assertEquals(13 * maxCharacterWidth, numbersColumnWidth);
        assertEquals(dataTableWidth - numbersColumnWidth, linesColumnWidth);
    }
}