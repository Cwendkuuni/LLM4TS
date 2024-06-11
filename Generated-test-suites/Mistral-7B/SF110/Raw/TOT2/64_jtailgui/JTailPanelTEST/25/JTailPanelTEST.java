// JTailPanelTest.java

import static org.junit.Assert.*;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.view.JTailPanel;

public class JTailPanelTest {

    @Test
    public void testJTailPanelConstructor() {
        JTailModel model = new JTailModel();
        JTailPanel jTailPanel = new JTailPanel(model);
        assertNotNull(jTailPanel);
    }

    @Test
    public void testGetCharacterHeight() {
        JTailPanel jTailPanel = new JTailPanel(new JTailModel());
        int characterHeight = jTailPanel.getCharacterHeight();
        assertNotNull(characterHeight);
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
    public void testUpdateMethod() {
        JTailModel model = new JTailModel();
        JTailPanel jTailPanel = new JTailPanel(model);
        jTailPanel.update(model, new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, null));
    }

    @Test
    public void testUpdateDataTableMethod() {
        JTailModel model = new JTailModel();
        JTailPanel jTailPanel = new JTailPanel(model);
        String lineNumbers = "1\n2\n3\n4";
        String lines = "Hello\nWorld\nJava\nTesting";
        jTailPanel.updateDataTable(lineNumbers, lines);
        assertEquals(4, jTailPanel.getTableModel().getRowCount());
    }
}