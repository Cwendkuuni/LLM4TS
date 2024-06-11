import static org.junit.Assert.*;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.view.JTailPanel;

public class JTailPanelTest {

    @Test
    public void testConstructor() {
        JTailModel model = new JTailModel();
        JTailPanel jTailPanel = new JTailPanel(model);
        assertNotNull(jTailPanel);
    }

    @Test
    public void testGetCharacterHeight() {
        JTailModel model = new JTailModel();
        JTailPanel jTailPanel = new JTailPanel(model);
        int characterHeight = jTailPanel.getCharacterHeight();
        assertNotNull(characterHeight);
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
        Observable observable = new Observable() {
            @Override
            public void notifyObservers(Object arg) {
                // Do nothing
            }
        };
        jTailPanel.update(observable, new JTailModelEvent(observable, JTailModelEvent.Type.RESIZED));
    }
}