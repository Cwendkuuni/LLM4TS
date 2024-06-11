package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.controller.JTailSizeController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.util.JTailLogger;
import fr.pingtimeout.jtail.util.LineNumbersAndContentProvider;

public class JTailPanelTest
{
    private JTailModel mockModel;
    private JTailPanel jTailPanel;

    @Before
    public void setUp()
    {
        mockModel = mock(JTailModel.class);
        JTailLogger.setDefaultLogHandler(mock(JTailLogger.class));

        jTailPanel = new JTailPanel(mockModel);
    }

    @Test
    public void testJTailPanelConstruction()
    {
        // Test that the JTailPanel is created with the correct components and properties
        assertNotNull(jTailPanel.model);
        assertNotNull(jTailPanel.scrollPane);
        assertNotNull(jTailPanel.verticalScrollBar);
        assertNotNull(jTailPanel.dataTable);
        assertNotNull(jTailPanel.tableModel);
        assertEquals(BorderLayout.CENTER, jTailPanel.getLayout());

        verify(mockModel, times(1)).addObserver(jTailPanel);
    }

    @Test
    public void testGetCharacterHeight()
    {
        // Test that the getCharacterHeight method returns the correct height
        Font font = new Font("SansSerif", Font.PLAIN, 14);
        FontMetrics fontMetrics = mock(FontMetrics.class);
        when(jTailPanel.dataTable.getFontMetrics(font)).thenReturn(fontMetrics);
        int expectedHeight = fontMetrics.getHeight() - 1;
        int actualHeight = jTailPanel.getCharacterHeight();

        assertEquals(expectedHeight, actualHeight);
    }

    @Test
    public void testGetVerticalScrollBar()
    {
        // Test that the getVerticalScrollBar method returns the correct scroll bar instance
        assertSame(jTailPanel.verticalScrollBar, jTailPanel.getVerticalScrollBar());
    }

    @Test
    public void testUpdateWithResizedEvent()
    {
        // Test that the update method correctly updates the vertical scroll bar when the model is resized

        when(mockModel.getEventType()).thenReturn(JTailModelEvent.Type.RESIZED);
        when(mockModel.getMinLine()).thenReturn(0);
        when(mockModel.getFirstLine()).thenReturn(0);
        when(mockModel.getNbDisplayedLines()).thenReturn(20);
        when(mockModel.getMaxLine()).thenReturn(100);

        JTailSizeController sizeController = mock(JTailSizeController.class);
        when(sizeController.getWidth()).thenReturn(800);
        when(sizeController.getHeight()).thenReturn(600);

        jTailPanel.addComponentListener(sizeController);

        JScrollBarAdjustmentController scrollBarController = mock(ScrollBarAdjustmentController.class);
        jTailPanel.verticalScrollBar.addAdjustmentListener(scrollBarController);

        jTailPanel.update(mockModel, new JTailModelEvent(mockModel));

        // Verify that the scroll bar's model properties were updated correctly
        TableColumnModel columnModel = jTailPanel.dataTable.getColumnModel();
        JTable table = jTailPanel.dataTable;
        int expectedMinimum = 0;
        int expectedFirstLine = 0;
        int expectedNbDisplayedLines = 20;
        int expectedMaximum = 100;

        TableColumn numbersColumn = columnModel.getColumn(0);
        TableColumn linesColumn = columnModel.getColumn(1);

        int expectedNumbersColumnWidth = 55 * 10; // assuming max character width is 10 pixels
        int expectedLinesColumnWidth = table.getWidth() - expectedNumbersColumnWidth;

        verify(scrollBarController, times(1)).adjust(expectedMinimum, expectedFirstLine, expectedNbDisplayedLines, expectedMaximum);
        verify(numbersColumn, times(1)).setPreferredWidth(expectedNumbersColumnWidth);
        verify(linesColumn, times(1)).setPreferredWidth(expectedLinesColumnWidth);

        // Verify that the vertical scroll bar is enabled or disabled based on the number of lines
        int[] linesNumbers = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 21, 30, 31, 40, 41, 50, 51, 60, 61, 70, 71, 80, 81, 90, 91, 99 };
        String[] lines = new String[linesNumbers.length];
        Arrays.fill(lines, "Line " + linesNumbers[i]);
        when(mockModel.getLineNumbers()).thenReturn(new LineNumbersAndContentProvider(linesNumbers, lines).getLineNumbers());
        when(mockModel.getContent()).thenReturn(new LineNumbersAndContentProvider(linesNumbers, lines).getContent());

        assertEquals(linesNumbers.length, jTailPanel.model.getLineCount());
        assertEquals(lines[linesNumbers[0]], jTailPanel.model.getLine(0));
        assertEquals(lines[linesNumbers[linesNumbers.length - 1]], jTailPanel.model.getLine(linesNumbers.length - 1));

        jTailPanel.updateDataTable(jTailPanel.model.getLineNumbers(), jTailPanel.model.getContent());

        TableModel model = jTailPanel.dataTable.getModel();
        assertEquals(linesNumbers.length, model.getRowCount());

        for (int i = 0; i < linesNumbers.length; i++) {
            assertEquals(lines[i], model.getValueAt(i, 1).toString());
        }

       verify(mockModel, times(2)).getLineNumbers();
        verify(mockModel, times(2)).getContent();
    }

    @Test
    public void testUpdateWithScrolledEvent()
    {
        // Test that the update method correctly updates the data table when the user scrolls the file

        when(mockModel.getEventType()).thenReturn(JTailModelEvent.Type.SCROLLED);
        when(mockModel.getLineNumbers()).thenReturn(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" });
        when(mockModel.getContent()).thenReturn(new String[] { "Line 1", "Line 2", "Line 3", "Line 4", "Line 5", "Line 6", "Line 7", "Line 8", "Line 9" });

        jTailPanel.update(mockModel, new JTailModelEvent(mockModel));

        verify(mockModel, times(2)).getLineNumbers();
        verify(mockModel, times(2)).getContent();
    }
}