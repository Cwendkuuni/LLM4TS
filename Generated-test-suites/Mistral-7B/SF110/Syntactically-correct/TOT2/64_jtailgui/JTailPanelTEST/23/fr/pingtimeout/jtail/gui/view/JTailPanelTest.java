package fr.pingtimeout.jtail.gui.view.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class JTailPanelTest {

    @Test
    public void testJTailPanelConstruction() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        assertNotNull(panel);
    }

    @Test
    public void testGetCharacterHeight() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        int characterHeight = panel.getCharacterHeight();
        int expectedCharacterHeight = 1; // Assuming default font is used
        assertEquals(expectedCharacterHeight, characterHeight);
    }

    @Test
    public void testGetVerticalScrollBar() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        JScrollBar verticalScrollBar = panel.getVerticalScrollBar();
        assertNotNull(verticalScrollBar);
    }

    @Test
    public void testUpdateDataTableWithEmptyLineNumbers() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        String lineNumbers = "";
        String lines = "";
        panel.updateDataTable(lineNumbers, lines);
        assertEquals(0, panel.dataTable.getRowCount());
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateDataTableWithNullModel() {
        String lineNumbers = "Line numbers";
        String lines = "Lines";
        JTailPanel panel = new JTailPanel(null);
        panel.updateDataTable(lineNumbers, lines);
    }

    @Test
    public void testUpdateDataTableWithLargeLineNumbersAndLines() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        int lineCount = 1000;
        String lineNumbers = "";
        for (int i = 0; i < lineCount; i++) {
            lineNumbers += (i + 1) + "\n";
        }
        String lines = "";
        for (int i = 0; i < lineCount; i++) {
            lines += "Line " + (i + 1) + "\n";
        }
        panel.updateDataTable(lineNumbers, lines);
        int expectedRowCount = lineCount;
        assertEquals(expectedRowCount, panel.dataTable.getRowCount());
    }

    // Test update method with resized event

    // Test update method with scrolled event

    @Test
    public void testVerticalScrollBarEnabled() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        int lineCount = 100;
        JTailModelEvent event = new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, new Object[]{lineCount});
        panel.update(model, event);
        boolean expectedEnabled = lineCount > 1;
        assertEquals(expectedEnabled, panel.verticalScrollBar.isEnabled());
    }
}