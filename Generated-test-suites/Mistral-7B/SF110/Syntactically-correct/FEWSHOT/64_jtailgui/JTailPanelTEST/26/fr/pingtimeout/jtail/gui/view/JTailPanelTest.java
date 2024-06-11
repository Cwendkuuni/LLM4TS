package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.event.AdjustmentEvent;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.util.JTailLogger;

public class JTailPanelTest {

    private JTailModel model;
    private JTailPanel jTailPanel;
    private ScrollBarAdjustmentController scrollBarAdjustmentController;
    private JTailSizeController jTailSizeController;

    @Before
    public void setup() {
        model = new JTailModel();
        jTailPanel = new JTailPanel(model);
        scrollBarAdjustmentController = new ScrollBarAdjustmentController(jTailPanel, model);
        jTailSizeController = new JTailSizeController(jTailPanel, model);
        JTailLogger.setDebugEnabled(true);
    }

    @Test
    public void testJTailPanelConstruction() {
        assertEquals("JTailPanel is null", jTailPanel, jTailPanel);
        assertEquals("model is null", model, jTailPanel.model);
        assertEquals("scrollPane is null", jTailPanel.scrollPane, jTailPanel.add(jTailPanel.scrollPane, "Center"));
        assertEquals("dataTable is null", jTailPanel.dataTable, jTailPanel.scrollPane.getViewport().getComponent(0));
        assertEquals("verticalScrollBar is null", jTailPanel.verticalScrollBar, jTailPanel.add(jTailPanel.verticalScrollBar, "East"));
        assertEquals("tableModel is null", jTailPanel.tableModel, jTailPanel.dataTable.getModel());
        assertEquals("characterHeight is not set", jTailPanel.characterHeight > 0, true);
        assertEquals("maxCharacterWidth is not set", jTailPanel.maxCharacterWidth > 0, true);
        assertTrue("verticalScrollBar is an instance of JScrollBar", jTailPanel.verticalScrollBar instanceof JScrollBar);
    }

    @Test
    public void testGetCharacterHeight() {
        assertEquals("characterHeight is not set", jTailPanel.getCharacterHeight(), jTailPanel.characterHeight);
    }

    @Test
    public void testGetVerticalScrollBar() {
        assertEquals("verticalScrollBar is not set", jTailPanel.getVerticalScrollBar(), jTailPanel.verticalScrollBar);
    }

    @Test
    public void testUpdateWhenModelIsResized() {
        model.fireEvent(new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 1, 1, 1));
        JTailModelEvent event = (JTailModelEvent) model.getLastEvent();
        assertEquals("event type is not RESIZED", event.type, JTailModelEvent.Type.RESIZED);
        assertEquals("minLine is not set", jTailPanel.verticalScrollBar.getModel().getMinimum(), event.minLine);
        assertEquals("firstLine is not set", jTailPanel.verticalScrollBar.getModel().getValue(), event.firstLine);
        assertEquals("nbDisplayedLines is not set", jTailPanel.verticalScrollBar.getModel().getExtent(), event.nbDisplayedLines);
        assertEquals("maxLine is not set", jTailPanel.verticalScrollBar.getModel().getMaximum(), event.maxLine);
        assertFalse("scrollbar is enabled", jTailPanel.verticalScrollBar.isEnabled());
    }

    @Test
    public void testUpdateWhenModelIsScrolled() {
        model.fireEvent(new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1, 1, 1, 1, 1));
        JTailModelEvent event = (JTailModelEvent) model.getLastEvent();
        assertEquals("event type is not SCROLLED", event.type, JTailModelEvent.Type.SCROLLED);
        assertNull("lineNumbers is null", jTailPanel.tableModel.getDataVector());
    }

    @Test
    public void testUpdateDataTable() {
        String lineNumbers = "1\n2\n3\n4\n5\n6\n7\n8";
        String lines = "line 1\nline 2\nline 3\nline 4\nline 5\nline 6\nline 7\nline 8";
        model.fireEvent(new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 1, 1, lineNumbers.length() + lines.length()));
        jTailPanel.updateDataTable(lineNumbers, lines);
        Object[] rows = jTailPanel.tableModel.getDataVector().toArray();
        assertEquals("number of rows is not correct", rows.length, 8);
        for (int i = 0; i < rows.length; i++) {
            String[] row = (String[]) rows[i];
            assertEquals("line number is not correct", Integer.parseInt(row[0]), i + 1);
            assertEquals("line is not correct", row[1], lines.split("\n")[i]);
        }
    }

    @Test
    public void testScrollBarAdjustmentController() {
        model.fireEvent(new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 1, 1, 10));
        jTailPanel.updateDataTable(model.getLineNumbers(), model.getContent());
        AdjustmentEvent adjustmentEvent = new AdjustmentEvent(scrollBarAdjustmentController, AdjustmentEvent.ADJUSTING, 5, 5, 5, 5, 5, 5);
        scrollBarAdjustmentController.adjustmentValueChanged(adjustmentEvent);
        assertEquals("value is not set", jTailPanel.verticalScrollBar.getModel().getValue(), 6);
    }

    @Test
    public void testJTailSizeController() {
        jTailSizeController.componentResized(jTailPanel, 200, 200);
        assertEquals("numbersColumnWidth is not set", jTailPanel.tableModel.getColumn(0).getPreferredWidth(), 20 * jTailPanel.maxCharacterWidth);
        assertEquals("linesColumnWidth is not set", jTailPanel.tableModel.getColumn(1).getPreferredWidth(), 200 - 20 * jTailPanel.maxCharacterWidth);
    }
}