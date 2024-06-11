package fr.pingtimeout.jtail.gui.test;

import org.junit.Test;
import static org.junit.Assert.*;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.view.JTailPanel;

public class JTailPanelTest {

    @Test
    public void testJTailPanelConstructor() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        assertNotNull(panel);
    }

    @Test
    public void testGetCharacterHeight() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        int characterHeight = panel.getCharacterHeight();
        assertNotNull(characterHeight);
    }

    @Test
    public void testGetVerticalScrollBar() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        JScrollBar verticalScrollBar = panel.getVerticalScrollBar();
        assertNotNull(verticalScrollBar);
    }

    @Test
    public void testUpdateMethod() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        Observable observable = new Observable() {};
        Object arg = new JTailModelEvent(JTailModelEvent.Type.RESIZED, null);
        panel.update(observable, arg);
    }

    @Test
    public void testUpdateDataTableWithNullLineNumbers() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        panel.updateDataTable(null, "lines");
        assertTrue(panel.dataTable.getRowCount() == 0);
    }

    @Test
    public void testUpdateDataTableWithEmptyLines() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        panel.updateDataTable("", "");
        assertTrue(panel.dataTable.getRowCount() == 0);
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateDataTableWithNullLines() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        panel.updateDataTable("lineNumbers", null);
    }

    @Test
    public void testUpdateDataTableWithNegativeMinimum() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        final int minLine = -1;
        panel.updateDataTable("lineNumbers", "lines");
        JScrollBar verticalScrollBar = panel.getVerticalScrollBar();
        assertEquals(minLine, verticalScrollBar.getValue());
        assertTrue(verticalScrollBar.isEnabled());
    }

    @Test
    public void testUpdateDataTableWithNegativeFirstLine() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        final int firstLine = -1;
        panel.updateDataTable("lineNumbers", "lines");
        JScrollBar verticalScrollBar = panel.getVerticalScrollBar();
        assertEquals(firstLine, verticalScrollBar.getValue());
        assertTrue(verticalScrollBar.isEnabled());
    }

    @Test
    public void testUpdateDataTableWithNegativeNbDisplayedLines() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        final int nbDisplayedLines = -1;
        panel.updateDataTable("lineNumbers", "lines");
        JScrollBar verticalScrollBar = panel.getVerticalScrollBar();
        assertEquals(0, verticalScrollBar.getExtent());
        assertTrue(verticalScrollBar.isEnabled());
    }
}