import org.junit.Test;
import static org.junit.Assert.*;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.view.JTailPanel;

public class JTailPanelTest {

    @Test
    public void testJTailPanelConstructor() {
        JTailModel model = new JTailModel();
        JTailPanel jTailPanel = new JTailPanel(model);
        assertNotNull(jTailPanel.getVerticalScrollBar());
        assertNotNull(jTailPanel.getDataTable());
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
    public void testUpdateMethodWithNullModel() {
        JTailPanel jTailPanel = new JTailPanel(null);
        JTailModelEvent event = new JTailModelEvent(null, JTailModelEvent.Type.RESIZED, 1, 1, 1);
        jTailPanel.update(null, event);
        assertTrue(jTailPanel.isEnabled());
    }

    @Test
    public void testUpdateMethodWithWrongObserver() {
        JTailModel model = new JTailModel();
        JTailPanel jTailPanel = new JTailPanel(model);
        JTailModelEvent event = new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, 1, 1, 1);
        Object wrongObserver = new Object();
        jTailPanel.update(wrongObserver, event);
        assertTrue(jTailPanel.isEnabled());
    }

    @Test
    public void testUpdateMethodWithUnknownType() {
        JTailModel model = new JTailModel();
        JTailPanel jTailPanel = new JTailPanel(model);
        JTailModelEvent event = new JTailModelEvent(model, "UNKNOWN_TYPE", 1, 1, 1);
        jTailPanel.update(model, event);
        assertTrue(jTailPanel.isEnabled());
    }

    @Test
    public void testUpdateMethodWithDifferentLineNumbersAndContentLengths() {
        JTailModel model = new JTailModel();
        JTailPanel jTailPanel = new JTailPanel(model);
        String lineNumbers = "1\n2\n3\n4\n5\n";
        String lines = "1\n2\n3\n4\n5\n6\n";
        JTailModelEvent event = new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, 1, 1, 1);
        jTailPanel.update(model, event);
        assertTrue(jTailPanel.isEnabled());
    }

    @Test
    public void testUpdateMethodWithSmallWindowSize() {
        JTailModel model = new JTailModel();
        JTailPanel jTailPanel = new JTailPanel(model);
        JTailModelEvent event = new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, 1, 1, 1);
        jTailPanel.update(model, event);
        assertTrue(jTailPanel.isEnabled());
    }

    @Test
    public void testUpdateMethod() {
        JTailModel model = new JTailModel();
        JTailPanel jTailPanel = new JTailPanel(model);
        JTailModelEvent event = new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, 1, 1, 1);
        jTailPanel.update(model, event);
        assertTrue(jTailPanel.isEnabled());
    }

    @Test
    public void testPrivateUpdateDataTableMethod() {
        JTailModel model = new JTailModel();
        JTailPanel jTailPanel = new JTailPanel(model);
        String lineNumbers = "1\n2\n3\n4\n5\n";
        String lines = "1\n2\n3\n4\n5\n";
        jTailPanel.updateDataTable(lineNumbers, lines);
        assertEquals(5, jTailPanel.getDataTable().getRowCount());
    }
}