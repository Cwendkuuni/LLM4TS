import org.junit.Test;
import static org.junit.Assert.*;
import macaw.util.DisplayableListPanel;

public class DisplayableListPanelTest {
    // ...
    
    @Test
    public void testGetPanel() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test Panel Title", true);
        JPanel panel = displayableListPanel.getPanel();
        assertNotNull(panel);
    }
}