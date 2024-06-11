package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.controller.JTailSizeController;
import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.util.JTailLogger;
import fr.pingtimeout.jtail.util.LoggerWrapper;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class JTailPanelTest {

    @Test
    public void testJTailPanelConstruction() {
        JTailModel mockModel = Mockito.mock(JTailModel.class);
        JTailPanel jTailPanel = new JTailPanel(mockModel);
        assertNotNull(jTailPanel);
        JTailLogger logger = new LoggerWrapper(JTailLogger.class);
        Mockito.verify(logger).debug("Building a new JTailPanel");
    }

    @Test
    public void testGetCharacterHeight() {
        JTailPanel jTailPanel = new JTailPanel(new JTailModel());
        int characterHeight = jTailPanel.getCharacterHeight();
        assertNotNull(characterHeight);
    }

    @Test
    public void testGetVerticalScrollBar() {
        JTailPanel jTailPanel = new JTailPanel(new JTailModel());
        JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
        assertNotNull(verticalScrollBar);
    }

    @Test
    public void testUpdate() {
        JTailModel mockModel = Mockito.mock(JTailModel.class);
        JTailPanel jTailPanel = new JTailPanel(mockModel);
        Observable observable = jTailPanel;
        JTailModelEvent event = new JTailModelEvent(mockModel, JTailModelEvent.Type.RESIZED, 0, 0, 10, 10, 10, 10);
        observable.notifyObservers(event);

        JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
        Mockito.verify(verticalScrollBar).setEnabled(true);
    }

    @Test
    public void testUpdateDataTable() {
        JTailPanel jTailPanel = new JTailPanel(new JTailModel());
        JTailModel mockModel = Mockito.mock(JTailModel.class);
        String lineNumbers = "1\n2\n3\n4\n5";
        String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
        jTailPanel.updateDataTable(lineNumbers, lines);

        JTable dataTable = jTailPanel.getDataTable();
        int numRows = dataTable.getRowCount();
        int numColumns = dataTable.getColumnCount();
        assertEquals(numRows, 5);
        assertEquals(numColumns, 2);
        String[] columnNames = dataTable.getColumnNames();
        assertEquals(columnNames[0], "#");
        assertEquals(columnNames[1], "");

        String[] data = new String[5];
        String[][] modelData = ((DefaultTableModel)dataTable.getModel()).getDataMatrix();
        for (int i = 0; i < 5; i++) {
            data[i] = modelData[i][0];
        }
        assertArrayEquals(data, new String[]{"1", "2", "3", "4", "5"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateDataTableNullModel() {
        JTailPanel jTailPanel = new JTailPanel(null);
        String lineNumbers = "1\n2\n3\n4\n5";
        String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
        jTailPanel.updateDataTable(lineNumbers, lines);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateDataTableNullLines() {
        JTailPanel jTailPanel = new JTailPanel(new JTailModel());
        String lineNumbers = "1\n2\n3\n4\n5";
        jTailPanel.updateDataTable(lineNumbers, null);
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateDataTableNullLineNumbers() {
        JTailPanel jTailPanel = new JTailPanel(new JTailModel());
        String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
        jTailPanel.updateDataTable(null, lines);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testUpdateDataTableEmptyLineNumbers() {
        JTailPanel jTailPanel = new JTailPanel(new JTailModel());
        String lineNumbers = "";
        String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
        jTailPanel.updateDataTable(lineNumbers, lines);
    }

    @Test
    public void testUpdateDataTableWithZeroLines() {
        JTailPanel jTailPanel = new JTailPanel(new JTailModel());
        String lineNumbers = "0\n";
        String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
        jTailPanel.updateDataTable(lineNumbers, lines);

        JTable dataTable = jTailPanel.getDataTable();
        int numRows = dataTable.getRowCount();
        assertEquals(numRows, 1);
    }

    @Test
    public void testUpdateDataTableWithOneLine() {
        JTailPanel jTailPanel = new JTailPanel(new JTailModel());
        String lineNumbers = "1\n";
        String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
        jTailPanel.updateDataTable(lineNumbers, lines);

        JTable dataTable = jTailPanel.getDataTable();
        int numRows = dataTable.getRowCount();
        assertEquals(numRows, 1);
        String[] columnNames = dataTable.getColumnNames();
        assertEquals(columnNames[0], "#");
        assertEquals(columnNames[1], "");

        String[] data = new String[1];
        String[][] modelData = ((DefaultTableModel)dataTable.getModel()).getDataMatrix();
        for (int i = 0; i < 1; i++) {
            data[i] = modelData[i][0];
        }
        assertArrayEquals(data, new String[]{"1"});
    }

    @Test
    public void testUpdateDataTableWithManyLines() {
        JTailPanel jTailPanel = new JTailPanel(new JTailModel());
        String lineNumbers = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11\n12\n13\n14\n15\n16\n17\n18\n19\n20\n";
        String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8\nLine 9\nLine 10\nLine 11\nLine 12\nLine 13\nLine 14\nLine 15\nLine 16\nLine 17\nLine 18\nLine 19\nLine 20\n";
        jTailPanel.updateDataTable(lineNumbers, lines);

        JTable dataTable = jTailPanel.getDataTable();
        int numRows = dataTable.getRowCount();
        assertEquals(numRows, 20);
    }

    @Test
    public void testSetAutoResizeMode() {
        JTailPanel jTailPanel = new JTailPanel(new JTailModel());
        JTable dataTable = jTailPanel.getDataTable();
        dataTable.setAutoResizeMode(3);
        assertEquals(dataTable.getAutoResizeMode(), JTable.AUTO_RESIZE_OFF);
    }

    @Test
    public void testSetMaxWidthForNumbersColumn() {
        JTailPanel jTailPanel = new JTailPanel(new JTailModel());
        JTable dataTable = jTailPanel.getDataTable();

        Font font = new Font("Serif", Font.BOLD, 14);
        FontMetrics metrics = dataTable.getFontMetrics(font);
        int maxCharacterWidth = metrics.stringWidth("0") + 2;

        TableColumn numbersColumn = dataTable.getColumnModel().getColumn(0);
        numbersColumn.setPreferredWidth(maxCharacterWidth * 10);

        String text = "12345678901234567890";
        int preferredWidth = numbersColumn.getPreferredWidth();
        Dimension size = new Dimension(preferredWidth, dataTable.getRowHeight());

        assertEquals(text.length() * maxCharacterWidth, preferredWidth);
        assertEquals(size.width, preferredWidth);

        List<Integer> lineNumbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        String[] lines = new String[10];
        for (int i = 0; i < 10; i++) {
            lines[i] = String.valueOf(lineNumbers.get(i));
        }
        jTailPanel.updateDataTable(String.join("\n", lineNumbers), String.join("\n", lines));

        int numRows = dataTable.getRowCount();
        int numColumns = dataTable.getColumnCount();
        assertEquals(numRows, 10);
        assertEquals(numColumns, 2);
        String[] columnNames = dataTable.getColumnNames();
        assertEquals(columnNames[0], "#");
        assertEquals(columnNames[1], "");

        String[] data = new String[10];
        String[][] modelData = ((DefaultTableModel)dataTable.getModel()).getDataMatrix();
        for (int i = 0; i < 10; i++) {
            data[i] = modelData[i][0];
        }
        assertArrayEquals(data, lineNumbers.toArray(new Integer[10]));

        int columnWidth = numbersColumn.getWidth();
        assertTrue(columnWidth >= maxCharacterWidth * lineNumbers.size());
    }

    @Test
    public void testSetWidthForLinesColumn() {
        JTailPanel jTailPanel = new JTailPanel(new JTailModel());
        JTable dataTable = jTailPanel.getDataTable();

        Font font = new Font("Serif", Font.PLAIN, 14);
        FontMetrics metrics = dataTable.getFontMetrics(font);
        int linesColumnWidth = dataTable.getWidth() - (metrics.stringWidth("0") + 2) * 10;

        TableColumn linesColumn = dataTable.getColumnModel().getColumn(1);
        linesColumn.setPreferredWidth(linesColumnWidth);

        String text = "Line 1 is a line of text\nLine 2 is a line of text\nLine 3 is a line of text\nLine 4 is a line of text\nLine 5 is a line of text\nLine 6 is a line of text\nLine 7 is a line of text\nLine 8 is a line of text\nLine 9 is a line of text\nLine 10 is a line of text";
        int preferredWidth = linesColumn.getPreferredWidth();
        Dimension size = new Dimension(preferredWidth, dataTable.getRowHeight());

        assertEquals(text.length(), preferredWidth / (metrics.getWidth("Line 1 is a line of text") + 1));
        assertEquals(size.width, preferredWidth);

        List<Integer> lineNumbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        String[] lines = new String[10];
        for (int i = 0; i < 10; i++) {
            lines[i] = "Line " + (i + 1);
        }
        jTailPanel.updateDataTable(String.join("\n", lineNumbers), String.join("\n", lines));

        int numRows = dataTable.getRowCount();
        int numColumns = dataTable.getColumnCount();
        assertEquals(numRows, 10);
        assertEquals(numColumns, 2);
        String[] columnNames = dataTable.getColumnNames();
        assertEquals(columnNames[0], "#");
        assertEquals(columnNames[1], "");

        String[] data = new String[10];
        String[][] modelData = ((DefaultTableModel)dataTable.getModel()).getDataMatrix();
        for (int i = 0; i < 10; i++) {
            data[i] = modelData[i][1];
        }
        assertArrayEquals(data, lines);

        int columnWidth = linesColumn.getWidth();
        assertTrue(columnWidth >= lines[0].length() * metrics.getWidth("Line 1") + 1);
    }

}