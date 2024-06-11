import org.junit.Test;
import static org.junit.Assert.*;

public class DisplayableListPanelTest {

    @Test
    public void testDisplayableListPanelWithSessionPropertiesAndParentForm() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = Mockito.mock(DisplayableListParentForm.class);
        String panelTitleText = "Test Panel Title";
        boolean allowWriteAccess = true;

        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);

        assertEquals(panelTitleText, displayableListPanel.getPanelTitleText());
        assertTrue(displayableListPanel.isWritable());
    }
}