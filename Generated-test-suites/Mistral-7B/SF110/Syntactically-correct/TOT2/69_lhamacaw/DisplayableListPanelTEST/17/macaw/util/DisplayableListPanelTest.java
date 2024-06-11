import org.junit.Test;
import static org.junit.Assert.*;

public class DisplayableListPanelTest {

    @Test
    public void testDisplayableListPanelWithTwoArguments() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Title", true);
        assertNotNull(displayableListPanel);
    }
}