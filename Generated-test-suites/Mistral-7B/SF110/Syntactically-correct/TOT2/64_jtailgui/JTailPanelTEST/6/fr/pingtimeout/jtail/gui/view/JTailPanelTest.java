import org.junit.Test;
import static org.junit.Assert.*;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.view.JTailPanel;

public class JTailPanelTest {
    @Test
    public void testConstructor() {
        JTailModel model = new JTailModel(); // Assuming JTailModel is instantiated correctly
        JTailPanel jTailPanel = new JTailPanel(model);
        assertNotNull(jTailPanel.getVerticalScrollBar());
        assertNotNull(jTailPanel.getDataTable());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullModel() {
        JTailPanel jTailPanel = new JTailPanel(null);
    }

    @Test
    public void testGetCharacterHeight() {
        JTailModel model = new JTailModel();
        JTailPanel jTailPanel = new JTailPanel(model);
        int characterHeight = jTailPanel.getCharacterHeight();
        assertTrue(characterHeight > 0);
    }

    @Test
    public void testGetVerticalScrollBar() {
        JTailModel model = new JTailModel();
        JTailPanel jTailPanel = new JTailPanel(model);
        JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
        assertNotNull(verticalScrollBar);
    }

    @Test
    public void testUpdateResizedEvent() {
        JTailModel model = new JTailModel();
        JTailPanel jTailPanel = new JTailPanel(model);
        JTable table = jTailPanel.getDataTable();
        JScrollPane scrollPane = jTailPanel.getScrollPane();
        JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
        BoundedRangeModel rangeModel = verticalScrollBar.getModel();
        TableColumn numbersColumn = table.getColumnModel().getColumn(0);
        TableColumn linesColumn = table.getColumnModel().getColumn(1);

        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 1, 1, "1", new String[]{"1", "1"});
        jTailPanel.update(model, event);

        assertEquals(1, rangeModel.getMinimum());
        assertEquals(1, rangeModel.getValue());
        assertEquals(1, rangeModel.getExtent());
        assertEquals(1, rangeModel.getMaximum());
        assertEquals(false, rangeModel.isEnabled());

        numbersColumn.setPreferredWidth(10);
        linesColumn.setPreferredWidth(10);
        assertEquals(10, numbersColumn.getPreferredWidth());
        assertEquals(table.getWidth() - 10, linesColumn.getPreferredWidth());
    }

    @Test
    public void testUpdateScrolledEvent() {
        JTailModel model = new JTailModel();
        JTailPanel jTailPanel = new JTailPanel(model);
        JTable table = jTailPanel.getDataTable();
        JScrollPane scrollPane = jTailPanel.getScrollPane();
        JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
        BoundedRangeModel rangeModel = verticalScrollBar.getModel();

        String lineNumbers = "1\n2\n3\n4\n5";
        String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, lineNumbers, lines);
        jTailPanel.update(model, event);

        int[] lineNumbersArray = {1, 2, 3, 4, 5};
        String[] linesArray = {"Line 1", "Line 2", "Line 3", "Line 4", "Line 5"};
        for (int i = 0; i < lineNumbersArray.length; ++i) {
            assertEquals(lineNumbersArray[i], table.getValueAt(i, 0).toString());
            assertEquals(linesArray[i], table.getValueAt(i, 1).toString());
        }
    }

    @Test
    public void testEmptyUpdateResizedEvent() {
        JTailModel model = new JTailModel();
        JTailPanel jTailPanel = new JTailPanel(model);
        JTable table = jTailPanel.getDataTable();
        JScrollPane scrollPane = jTailPanel.getScrollPane();
        JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
        BoundedRangeModel rangeModel = verticalScrollBar.getModel();

        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, "", "");
        jTailPanel.update(model, event);

        assertEquals(0, table.getRowCount());
    }

    @Test
    public void testEmptyUpdateScrolledEvent() {
        JTailModel model = new JTailModel();
        JTailPanel jTailPanel = new JTailPanel(model);
        jTailPanel.update(model, new JTailModelEvent(JTailModelEvent.Type.SCROLLED, "", ""));

        assertEquals(0, jTailPanel.getDataTable().getRowCount());
    }

    @Test
    public void testUpdateWithDifferentSizesEvent() {
        JTailModel model = new JTailModel();
        JTailPanel jTailPanel = new JTailPanel(model);
        JTable table = jTailPanel.getDataTable();
        JScrollPane scrollPane = jTailPanel.getScrollPane();
        JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
        BoundedRangeModel rangeModel = verticalScrollBar.getModel();

        String lineNumbers = "1\n2\n3\n4\n5\n6\n7";
        String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7";
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, lineNumbers, lines);
        jTailPanel.update(model, event);

        int[] lineNumbersArray = {1, 2, 3, 4, 5, 6, 7};
        String[] linesArray = {"Line 1", "Line 2", "Line 3", "Line 4", "Line 5", "Line 6", "Line 7"};
        for (int i = 0; i < lineNumbersArray.length; ++i) {
            assertEquals(lineNumbersArray[i], table.getValueAt(i, 0).toString());
            assertEquals(linesArray[i], table.getValueAt(i, 1).toString());
        }

        TableColumn numbersColumn = table.getColumnModel().getColumn(0);
        TableColumn linesColumn = table.getColumnModel().getColumn(1);
        numbersColumn.setPreferredWidth(10);
        linesColumn.setPreferredWidth(20);

        assertNotEquals(10, numbersColumn.getPreferredWidth());
        assertNotEquals(20, linesColumn.getPreferredWidth());
    }
}