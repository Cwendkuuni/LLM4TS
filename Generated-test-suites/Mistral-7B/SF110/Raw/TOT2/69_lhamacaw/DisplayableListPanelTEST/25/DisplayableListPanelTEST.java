import org.junit.Test;
import static org.junit.Assert.*;

public class DisplayableListPanelTest {

    @Test
    public void testDisplayableListPanelWithTwoArguments() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Test Panel Title";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
        assertTrue(displayableListPanel instanceof DisplayableListPanel);
    }
}