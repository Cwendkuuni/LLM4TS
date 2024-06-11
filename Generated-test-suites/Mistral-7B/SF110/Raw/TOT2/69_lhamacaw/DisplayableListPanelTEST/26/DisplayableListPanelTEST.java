import org.junit.Test;
import static org.junit.Assert.*;
import macaw.util.DisplayableListPanel;

public class DisplayableListPanelTest {

    @Test
    public void testGetPanel() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(null, null, "Test Panel", true, true);
        JPanel panel = displayableListPanel.getPanel();
        assertNotNull(panel);
    }
}