package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.swing.table.*;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.util.*;

import org.junit.Before;
import org.junit.Test;

public class JTailPanelTest {

    private JTailModel mockModel;
    private JTailPanel panelUnderTest;

    @Before
    public void setUp() {
        this.mockModel = mock(JTailModel.class);
        this.panelUnderTest = new JTailPanel(this.mockModel);
    }

    @Test
    public void testJTailPanelConstructor() {
        // Check that the constructor correctly initializes the components
        // and sets the correct observer for the model
        verify(this.mockModel, times(1)).addObserver(this.panelUnderTest);

        // Check that the initial state of the panel is correct
        assertEquals("The initial number of rows in the table should be zero", 0, this.panelUnderTest.getDataTable().getRowCount());
        assertNull("The scrollbar should be null in the initial state", this.panelUnderTest.getVerticalScrollBar());
    }

    @Test
    public void testUpdateMethodWithResizedEvent() {
        // Set up the mock model to return some test data
        when(this.mockModel.getLineNumbers()).thenReturn("1\n2\n3\n4\n5");
        when(this.mockModel.getContent()).thenReturn("Line 1\nLine 2\nLine 3\nLine 4\nLine 5");

        // Simulate a resized event and check that the panel updates correctly
        this.panelUnderTest.update(this.mockModel, new JTailModelEvent(this.mockModel, JTailModelEvent.Type.RESIZED, 100, 100, 500, 500));

        // Check that the table has been updated with the correct data
        List<String> lineNumbers = Arrays.asList("1", "2", "3", "4", "5");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3", "Line 4", "Line 5");
        assertEquals("The number of rows in the table should match the number of lines in the model", lines.size(), this.panelUnderTest.getDataTable().getRowCount());
        for (int i = 0; i < lines.size(); ++i) {
            assertEquals("The line numbers in the table should match the line numbers in the model", lineNumbers.get(i), this.panelUnderTest.getDataTable().getValueAt(i, 0).toString());
            assertEquals("The lines in the table should match the lines in the model", lines.get(i), this.panelUnderTest.getDataTable().getValueAt(i, 1).toString());
        }

        // Check that the scrollbar is enabled and has the correct minimum, maximum and value
        JScrollBar scrollbar = this.panelUnderTest.getVerticalScrollBar();
        assertEquals("The minimum value of the scrollbar should be the first line number", 1, scrollbar.getMinimum());
        assertEquals("The maximum value of the scrollbar should be the last line number", 5, scrollbar.getMaximum());
        assertEquals("The value of the scrollbar should be the first displayed line number", 1, scrollbar.getValue());
    }

    @Test
    public void testUpdateMethodWithScrolledEvent() {
        // Set up the mock model to return some test data
        when(this.mockModel.getLineNumbers()).thenReturn("1\n2\n3\n4\n5");
        when(this.mockModel.getContent()).thenReturn("Line 1\nLine 2\nLine 3\nLine 4\nLine 5");

        // Simulate a scrolled event and check that the panel updates correctly
        this.panelUnderTest.update(this.mockModel, new JTailModelEvent(this.mockModel, JTailModelEvent.Type.SCROLLED, 3));

        // Check that the table has been updated with the correct data
        List<String> lineNumbers = Arrays.asList("1", "2", "3", "4", "5");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3", "Line 4", "Line 5");
        assertEquals("The number of rows in the table should match the number of lines in the model", lines.size(), this.panelUnderTest.getDataTable().getRowCount());
        for (int i = 0; i < lines.size(); ++i) {
            assertEquals("The line numbers in the table should match the line numbers in the model", lineNumbers.get(i), this.panelUnderTest.getDataTable().getValueAt(i, 0).toString());
            assertEquals("The lines in the table should match the lines in the model", lines.get(i), this.panelUnderTest.getDataTable().getValueAt(i, 1).toString());
        }

        // Check that the scrollbar is enabled and has the correct minimum, maximum and value
        JScrollBar scrollbar = this.panelUnderTest.getVerticalScrollBar();
        assertEquals("The minimum value of the scrollbar should be the first line number", 1, scrollbar.getMinimum());
        assertEquals("The maximum value of the scrollbar should be the last line number", 5, scrollbar.getMaximum());
        assertEquals("The value of the scrollbar should be the line number that was scrolled to", 3, scrollbar.getValue());
    }

    @Test
    public void testUpdateDataTableMethod() {
        // Set up the mock model to return some test data
        when(this.mockModel.getLineNumbers()).thenReturn("1\n2\n3\n4\n5");
        when(this.mockModel.getContent()).thenReturn("Line 1\nLine 2\nLine 3\nLine 4\nLine 5");

        // Simulate a call to updateDataTable with test data
        this.panelUnderTest.updateDataTable(this.mockModel.getLineNumbers(), this.mockModel.getContent());

        // Check that the table has been updated with the correct data
        List<String> lineNumbers = Arrays.asList("1", "2", "3", "4", "5");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3", "Line 4", "Line 5");
        assertEquals("The number of rows in the table should match the number of lines in the model", lines.size(), this.panelUnderTest.getDataTable().getRowCount());
        for (int i = 0; i < lines.size(); ++i) {
            assertEquals("The line numbers in the table should match the line numbers in the model", lineNumbers.get(i), this.panelUnderTest.getDataTable().getValueAt(i, 0).toString());
            assertEquals("The lines in the table should match the lines in the model", lines.get(i), this.panelUnderTest.getDataTable().getValueAt(i, 1).toString());
        }

        // Check that the columns have been resized correctly
        TableColumn numbersColumn = this.panelUnderTest.getDataTable().getColumnModel().getColumn(0);
        TableColumn linesColumn = this.panelUnderTest.getDataTable().getColumnModel().getColumn(1);
        int numbersColumnWidth = 12 * this.panelUnderTest.getCharacterWidth();
        int linesColumnWidth = this.panelUnderTest.getDataTable().getWidth() - numbersColumnWidth;
        assertEquals("The width of the number column should match the calculated width", numbersColumnWidth, numbersColumn.getPreferredWidth());
        assertEquals("The width of the line column should match the calculated width", linesColumnWidth, linesColumn.getPreferredWidth());
    }

    @Test
    public void testComponentResizedMethod() {
        // Set up the mock model to return some test data
        when(this.mockModel.getLineNumbers()).thenReturn("1\n2\n3\n4\n5");
        when(this.mockModel.getContent()).thenReturn("Line 1\nLine 2\nLine 3\nLine 4\nLine 5");

        // Simulate a component resized event and check that the panel updates correctly
        this.panelUnderTest.componentResized(new ComponentEvent(this.panelUnderTest, ComponentEvent.COMPONENT_RESIZED));

        // Check that the table has been updated with the correct data
        List<String> lineNumbers = Arrays.asList("1", "2", "3", "4", "5");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3", "Line 4", "Line 5");
        assertEquals("The number of rows in the table should match the number of lines in the model", lines.size(), this.panelUnderTest.getDataTable().getRowCount());
        for (int i = 0; i < lines.size(); ++i) {
            assertEquals("The line numbers in the table should match the line numbers in the model", lineNumbers.get(i), this.panelUnderTest.getDataTable().getValueAt(i, 0).toString());
            assertEquals("The lines in the table should match the lines in the model", lines.get(i), this.panelUnderTest.getDataTable().getValueAt(i, 1).toString());
        }

        // Check that the columns have been resized correctly
        TableColumn numbersColumn = this.panelUnderTest.getDataTable().getColumnModel().getColumn(0);
        TableColumn linesColumn = this.panelUnderTest.getDataTable().getColumnModel().getColumn(1);
        int numbersColumnWidth = 12 * this.panelUnderTest.getCharacterWidth();
        int linesColumnWidth = this.panelUnderTest.getDataTable().getWidth() - numbersColumnWidth;
        assertEquals("The width of the number column should match the calculated width", numbersColumnWidth, numbersColumn.getPreferredWidth());
        assertEquals("The width of the line column should match the calculated width", linesColumnWidth, linesColumn.getPreferredWidth());
    }
}