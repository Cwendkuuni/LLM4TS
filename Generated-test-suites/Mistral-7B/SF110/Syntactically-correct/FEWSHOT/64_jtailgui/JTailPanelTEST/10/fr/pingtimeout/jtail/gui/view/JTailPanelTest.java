package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.util.Arrays;

import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.JTailModelEvent;
import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.controller.JTailSizeController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.util.JTailLogger;

public class JTailPanelTest {

    private JTailModel model;
    private JTailPanel panel;

    @Before
    public void setUp() {
        JTailLogger.setLevel(JTailLogger.DEBUG);
        model = new JTailModel();
        panel = new JTailPanel(model);
    }

    @Test
    public void testJTailPanelConstruction() {
        assertEquals("JTailPanel should be an instance of JPanel", true, panel instanceof JPanel);
        assertEquals("JTailPanel should have a JScrollPane as a Center component", true, panel.getComponentCount() > 1
                && panel.getComponent(0) instanceof JScrollPane);
        assertEquals("JTailPanel should have a JScrollBar as an East component", true, panel.getComponentCount() > 2
                && panel.getComponent(1) instanceof JScrollBar);
        assertEquals("JTailPanel should have a JTable as a Center component of JScrollPane", true, panel.getComponentInBounds(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE) instanceof JTable);
    }

    @Test
    public void testGetCharacterHeight() {
        final FontMetrics metrics = panel.dataTable.getFontMetrics(panel.dataTable.getFont());
        final int characterHeight = metrics.getHeight() - 1;
        assertEquals("Character Height should be correct", characterHeight, panel.getCharacterHeight());
    }

    @Test
    public void testGetVerticalScrollBar() {
        assertEquals("JTailPanel should have a JScrollBar as a verticalScrollBar", true, panel.getVerticalScrollBar() instanceof JScrollBar);
    }

    @Test
    public void testUpdateObserver() {
        model.firePropertyChange(JTailModel.Type.RESIZED.toString(), null, new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 2, 3, 4, 5, 6));
        verifyUpdate(JTailModelEvent.Type.RESIZED);

        model.firePropertyChange(JTailModelEvent.Type.SCROLLED.toString(), null, new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1, 2));
        verifyUpdate(JTailModelEvent.Type.SCROLLED);
    }

    @Test
    public void testUpdateDataTable() {
        model.setLineNumbers("1\n2\n3\n4\n5");
        model.setContent("foo\nbar\nqux\nquux\nquw");

        panel.updateDataTable(model.getLineNumbers(), model.getContent());

        final JTable dataTable = panel.dataTable;
        final TableColumn numbersColumn = dataTable.getColumnModel().getColumn(0);
        final TableColumn linesColumn = dataTable.getColumnModel().getColumn(1);

        assertEquals("Number of rows should be equal to number of lines", 5, dataTable.getRowCount());
        assertEquals("Line 1 content should be equal to 'foo'", "foo", dataTable.getValueAt(0, 1).toString());
        assertEquals("Line 4 content should be equal to 'quux'", "quux", dataTable.getValueAt(3, 1).toString());

        final int numbersColumnWidth = numbersColumn.getPreferredWidth();
        final int linesColumnWidth = linesColumn.getPreferredWidth();

        assertEquals("Width of numbersColumn should be equal to maxCharacterWidth * number of line numbers", numbersColumnWidth, 5 * maxCharacterWidth);
        assertEquals("Width of linesColumn should be equal to DataTable width - numbersColumnWidth", linesColumnWidth, panel.dataTable.getWidth() - numbersColumnWidth);
    }

    @Test
    public void testResizingColumnsWidth() {
        model.setLineNumbers("1\n2\n3\n4\n5");
        model.setContent("foo\nbar\nqux\nquux\nquw");

        panel.updateDataTable(model.getLineNumbers(), model.getContent());

        final JTable dataTable = panel.dataTable;
        final TableColumn numbersColumn = dataTable.getColumnModel().getColumn(0);
        final TableColumn linesColumn = dataTable.getColumnModel().getColumn(1);

        final int numbersColumnWidth = numbersColumn.getPreferredWidth();
        final int linesColumnWidth = linesColumn.getPreferredWidth();

        assertEquals("Width of numbersColumn should be equal to maxCharacterWidth * number of line numbers", numbersColumnWidth, 5 * maxCharacterWidth);
        assertEquals("Width of linesColumn should be equal to DataTable width - numbersColumnWidth", linesColumnWidth, panel.dataTable.getWidth() - numbersColumnWidth);

        // Force resize
        panel.updateDataTable(model.getLineNumbers(), model.getContent());
    }

    @Test
    public void testDisableVerticalScrollBar() {
        model.setLineNumbers("1\n2\n3\n4\n5");
        model.setContent("foo\nbar\nqux\nquux\nquw");

        panel.updateDataTable(model.getLineNumbers(), model.getContent());

        final JScrollBar verticalScrollBar = panel.verticalScrollBar;
        final BoundedRangeModel rangeModel = verticalScrollBar.getModel();

        assertEquals("VerticalScrollBar should be disabled initially", false, verticalScrollBar.isEnabled());
        assertEquals("Minimum value of VerticalScrollBar should be equal to first line", 1, rangeModel.getMinimum());
        assertEquals("Value of VerticalScrollBar should be equal to first line", 1, rangeModel.getValue());
        assertEquals("Extent of VerticalScrollBar should be equal to number of displayed lines", 5, rangeModel.getExtent());
        assertEquals("Maximum value of VerticalScrollBar should be equal to last line", 5, rangeModel.getMaximum());

        model.firePropertyChange(JTailModelEvent.Type.RESIZED.toString(), null, new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 2, 3, 4, 5, 6));

        assertEquals("VerticalScrollBar should be disabled after resize if first line == last line and number of displayed lines == total number of lines", true, verticalScrollBar.isEnabled());

        model.firePropertyChange(JTailModelEvent.Type.SCROLLED.toString(), null, new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1, 1));

        assertEquals("VerticalScrollBar should be enabled after scrolling if it was previously disabled", true, verticalScrollBar.isEnabled());
    }

    @Test
    public void testEnableVerticalScrollBar() {
        model.setLineNumbers("1\n2\n3\n4\n5");
        model.setContent("foo\nbar\nqux\nquux\nquw");

        panel.updateDataTable(model.getLineNumbers(), model.getContent());

        final JScrollBar verticalScrollBar = panel.verticalScrollBar;
        final BoundedRangeModel rangeModel = verticalScrollBar.getModel();

        assertEquals("VerticalScrollBar should be disabled initially", false, verticalScrollBar.isEnabled());
        assertEquals("Minimum value of VerticalScrollBar should be equal to first line", 1, rangeModel.getMinimum());
        assertEquals("Value of VerticalScrollBar should be equal to first line", 1, rangeModel.getValue());
        assertEquals("Extent of VerticalScrollBar should be equal to number of displayed lines", 5, rangeModel.getExtent());
        assertEquals("Maximum value of VerticalScrollBar should be equal to last line", 5, rangeModel.getMaximum());

        model.firePropertyChange(JTailModelEvent.Type.RESIZED.toString(), null, new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 2, 3, 4, 5, 6));

        assertEquals("VerticalScrollBar should be disabled after resize if first line == last line and number of displayed lines == total number of lines", true, verticalScrollBar.isEnabled());

        model.firePropertyChange(JTailModelEvent.Type.SCROLLED.toString(), null, new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1, 1));

        assertEquals("VerticalScrollBar should be enabled after scrolling if it was previously disabled", true, verticalScrollBar.isEnabled());

        model.setLineNumbers("1\n2\n3\n4\n5\n6");

        assertEquals("VerticalScrollBar should be enabled if number of displayed lines < total number of lines", true, verticalScrollBar.isEnabled());
    }

    @Test
    public void testScrollBarAdjustmentController() {
        model.setLineNumbers("1\n2\n3\n4\n5");
        model.setContent("foo\nbar\nqux\nquux\nquw");

        panel.updateDataTable(model.getLineNumbers(), model.getContent());

        final ScrollBarAdjustmentController controller = new ScrollBarAdjustmentController(panel, model);
        final AdjustmentEvent adjustmentEvent = new AdjustmentEvent(controller, 1, 1, 1, 2);

        panel.verticalScrollBar.addAdjustmentListener(controller);

        controller.adjust(adjustmentEvent);

        final JScrollBar verticalScrollBar = panel.verticalScrollBar;
        final BoundedRangeModel rangeModel = verticalScrollBar.getModel();

        assertEquals("Value of VerticalScrollBar should be equal to new value", 2, rangeModel.getValue());
    }

    @Test
    public void testJTailSizeController() {
        model.setLineNumbers("1\n2\n3\n4\n5");
        model.setContent("foo\nbar\nqux\nquux\nquw");

        panel.updateDataTable(model.getLineNumbers(), model.getContent());

        final JTailSizeController controller = new JTailSizeController(panel, model);

        panel.addComponentListener(controller);

        final Component component = new JLabel();
        panel.add(component);

        panel.setSize(new Dimension(10, 10));
        panel.validate();

        controller.resized();

        final JScrollBar verticalScrollBar = panel.verticalScrollBar;
        final BoundedRangeModel rangeModel = verticalScrollBar.getModel();

        assertEquals("Value of VerticalScrollBar should be equal to first line", 1, rangeModel.getValue());
    }

    private void verifyUpdate(JTailModelEvent.Type type) {
        final JScrollBar verticalScrollBar = panel.verticalScrollBar;
        final BoundedRangeModel rangeModel = verticalScrollBar.getModel();

        assertTrue("VerticalScrollBar should be updated", verticalScrollBar.isEnabled());

        switch (type) {
            case RESIZED:
                assertEquals("Minimum value of VerticalScrollBar should be equal to first line", 1, rangeModel.getMinimum());
                assertEquals("Value of VerticalScrollBar should be equal to first line", 1, rangeModel.getValue());
                assertEquals("Extent of VerticalScrollBar should be equal to number of displayed lines", 5, rangeModel.getExtent());
                assertEquals("Maximum value of VerticalScrollBar should be equal to last line", 5, rangeModel.getMaximum());
                break;
            case SCROLLED:
                assertEquals("Value of VerticalScrollBar should be equal to new value", 1, rangeModel.getValue());
                break;
            default:
                fail("Unexpected event type");
        }
    }

}