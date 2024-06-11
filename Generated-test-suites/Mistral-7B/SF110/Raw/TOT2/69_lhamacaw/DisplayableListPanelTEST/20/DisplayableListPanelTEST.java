import org.junit.Test;
import static org.junit.Assert.*;

public class DisplayableListPanelTest {

    @Test
    public void testDisplayableListPanelWithTwoArguments() {
        SessionProperties sessionProperties = Mockito.mock(SessionProperties.class);
        DisplayableListParentForm parentForm = Mockito.mock(DisplayableListParentForm.class);
        String panelTitleText = "Test Panel Title";
        boolean allowWriteAccess = true;

        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);

        assertNotNull(displayableListPanel.getPanel());
    }
}