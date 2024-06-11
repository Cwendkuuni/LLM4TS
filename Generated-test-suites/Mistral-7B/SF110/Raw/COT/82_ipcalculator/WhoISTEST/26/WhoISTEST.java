package ipac;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WhoISTest {

    private WhoIS whois;

    @Before
    public void setUp() {
        whois = new WhoIS();
    }

    @Test
    public void testWhoISConstructor() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois);
    }

    @Test
    public void testQueryPanelCreation() {
        WhoIS whois = new WhoIS();
        JPanel queryPanel = whois.queryPanel();
        assertNotNull(queryPanel);
        assertTrue(queryPanel.getComponentCount() > 0);
    }

    @Test
    public void testCreatePopupMenu() {
        WhoIS whois = new WhoIS();
        JPopupMenu popupMenu = whois.popupMenu;
        assertNotNull(popupMenu);
        assertTrue(popupMenu.getComponentCount() > 0);
    }

    @Test
    public void testActionPerformedQueryButton() {
        WhoIS whois = new WhoIS();
        String query = "example.com";
        whois.queryTextField.setText(query);
        whois.actionPerformed(new ActionEvent(whois.queryButton, 0, "Query Button Clicked"));
        verify(whois).startQuery();
    }

    @Test
    public void testActionPerformedResetButton() {
        WhoIS whois = new WhoIS();
        whois.actionPerformed(new ActionEvent(whois.resetButton, 0, "Reset Button Clicked"));
        assertEquals("", whois.queryTextField.getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartQueryEmptyQuery() {
        WhoIS whois = new WhoIS();
        whois.startQuery();
        fail("No Query Detected exception expected");
    }

    @Test
    public void testQueryUnreachableHost() {
        WhoIS whois = new WhoIS();
        whois.WHOIS_ADDRESS = "unknown.host";
        whois.startQuery();
        String errorMessage = "Unknown Whois host: unknown.host";
        assertEquals(errorMessage, whois.displayErrorMessage());
    }

    @Test
    public void testQuerySuccess() {
        WhoIS whois = new WhoIS();
        String query = "google.com";
        whois.queryTextField.setText(query);
        String expectedOutput = "..."; // Replace this with the expected output from the WhoIS server
        whois.actionPerformed(new ActionEvent(whois.queryButton, 0, "Query Button Clicked"));
        String actualOutput = ((WhoISOutput)whois.getOutput()).getOutput();
        assertEquals(expectedOutput, actualOutput);
    }

    // Add test cases for other methods and edge cases as needed
}