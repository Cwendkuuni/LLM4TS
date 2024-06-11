package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.controller.JTailSizeController;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import org.junit.Before;
import org.junit.Test;

public class JTailPanelTest {
    // Test construction of JTailPanel with valid model
    @Test
    public void testJTailPanelConstruction() {
        JTailModel mockModel = mock(JTailModel.class);
        JTailPanel panel = new JTailPanel(mockModel);
        assertNull(panel.getVerticalScrollBar());
    }

    // Test getCharacterHeight method returns correct height
    @Test
    public void testGetCharacterHeight() {
        JTailPanel panel = new JTailPanel(new JTailModel());
        assertEquals(panel.getCharacterHeight(), 16); // assume a default font height
    }

    // Test getVerticalScrollBar method returns correct scroll bar
    @Test
    public void testGetVerticalScrollBar() {
        JTailPanel panel = new JTailPanel(new JTailModel());
        assertNotNull(panel.getVerticalScrollBar());
    }

    // Test update method handles model resized event and updates scroll bar accordingly
    @Before
    public void setUp() {
        JTailModel mockModel = mock(JTailModel.class);
        JTailPanel panel = new JTailPanel(mockModel);
        ScrollBarAdjustmentController mockController = mock(ScrollBarAdjustmentController.class);
        JScrollBar verticalScrollBar = mock(JScrollBar.class);
        when(mockModel.getLineNumbers()).thenReturn("1\n2\n3");
        when(mockModel.getContent()).thenReturn("Line 1\nLine 2\nLine 3");
        when(mockController.getJTailPanel()).thenReturn(panel);
        when(mockController.getModel()).thenReturn(mockModel);
        panel.addComponentListener(mock(JTailSizeController.class));
        panel.addAdjustmentListener(mockController);
        panel.verticalScrollBar = verticalScrollBar;
    }

    @Test
    public void testUpdateMethodHandlesModelResizedEvent() {
        TableModelEvent tableModelEvent = new TableModelEvent(panel.tableModel, TableModelEvent.HEAD_ROW);
        panel.update(panel.model, tableModelEvent);
        JScrollBar scrollBar = panel.verticalScrollBar;
        assertEquals(scrollBar.getValue(), 0);
        assertEquals(scrollBar.getMaximum(), 3);
        assertEquals(scrollBar.isEnabled(), true);
    }

    @Test
    public void testUpdateMethodHandlesModelScrolledEvent() {
        TableModelEvent tableModelEvent = new TableModelEvent(panel.tableModel, TableModelEvent.ALL_COLUMNS, TableModelEvent.LAST_ROW);
        panel.update(panel.model, tableModelEvent);
        JScrollBar scrollBar = panel.verticalScrollBar;
        assertEquals(scrollBar.getValue(), 1);
        assertEquals(scrollBar.getMaximum(), 3);
        assertEquals(scrollBar.isEnabled(), true);
    }

    // Test updateDataTable method sets table data with correct line numbers and lines
    @Test
    public void testUpdateDataTableWithValidData() {
        JTailPanel panel = new JTailPanel(new JTailModel());
        panel.updateDataTable("1\n2\n3", "Line 1\nLine 2\nLine 3");
        assertEquals(panel.tableModel.getRowCount(), 3);
        String[][] data = ((DefaultTableModel) panel.tableModel).getDataMatrix();
        for (int i = 0; i < 3; ++i) {
            assertEquals(data[i][0], String.valueOf(i + 1));
            assertEquals(data[i][1], "Line " + (i + 1));
        }
    }

    // Test construction of JTailPanel with null model
    @Test(expected = NullPointerException.class)
    public void testJTailPanelConstructionWithNullModel() {
        new JTailPanel(null);
    }

    // Test updateDataTable method handles null or empty line numbers and lines
    @Test
    public void testUpdateDataTableWithEmptyData() {
        JTailPanel panel = new JTailPanel(new JTailModel());
        panel.updateDataTable("", "");
        assertEquals(panel.tableModel.getRowCount(), 0);
    }

    // Test update method handles model resized event when scroll bar is disabled
    @Test
    public void testUpdateMethodHandlesModelResizedEventWithDisabledScrollBar() {
        JTailModel mockModel = mock(JTailModel.class);
        JTailPanel panel = new JTailPanel(mockModel);
        panel.setEnabled(false);
        TableModelEvent tableModelEvent = new TableModelEvent(panel.tableModel, TableModelEvent.HEAD_ROW);
        panel.update(panel.model, tableModelEvent);
        JScrollBar scrollBar = panel.verticalScrollBar;
        assertEquals(scrollBar.getValue(), 0);
        assertEquals(scrollBar.getMaximum(), 0);
        assertEquals(scrollBar.isEnabled(), false);
    }

    // Test updateDataTable method handles resizing of columns when data table width changes
    @Test
    public void testUpdateDataTableWithChangedDataTableWidth() {
        JTailPanel panel = new JTailPanel(new JTailModel());
        panel.setSize(500, 500);
        String lineNumbers = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10";
        String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8\nLine 9\nLine 10";
        panel.updateDataTable(lineNumbers, lines);
        TableColumn numbersColumn = panel.tableModel.getColumnModel().getColumn(0);
        TableColumn linesColumn = panel.tableModel.getColumnModel().getColumn(1);
        int numbersColumnWidth = lineNumbers.split("\n")[lineNumbers.split("\n").length - 1].length() * panel.maxCharacterWidth;
        int linesColumnWidth = panel.dataTable.getWidth() - numbersColumnWidth;
        assertEquals(numbersColumn.getPreferredWidth(), numbersColumnWidth);
        assertEquals(linesColumn.getPreferredWidth(), linesColumnWidth);
    }
}