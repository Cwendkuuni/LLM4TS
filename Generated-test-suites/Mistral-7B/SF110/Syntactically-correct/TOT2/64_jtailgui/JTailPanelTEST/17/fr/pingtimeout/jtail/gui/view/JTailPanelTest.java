import static org.junit.Assert.*;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import org.junit.Before;
import org.junit.Test;

public class JTailPanelTest {

    private JTailPanel jTailPanel;
    private JTailModel jTailModel;

    @Before
    public void setUp() {
        jTailModel = new JTailModel();
        jTailPanel = new JTailPanel(jTailModel);
    }

    @Test
    public void testJTailPanelConstruction() {
        assertNotNull(jTailPanel);
        assertEquals(jTailModel, jTailPanel.getModel());
    }
}