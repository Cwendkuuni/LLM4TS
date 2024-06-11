import org.junit.Test;
import static org.junit.Assert.*;
import ipac.WhoIS;

public class WhoISTest {

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
        assertEquals(4, queryPanel.getComponentCount());
    }

    @Test
    public void testStartQueryWithEmptyTextField() {
        WhoIS whois = new WhoIS();
        whois.startQuery();
        assertTrue(whois.displayErrorCalled); // Assuming displayError has a flag to check if it was called
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartQueryWithNullQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText(null);
        whois.startQuery();
    }

    @Test
    public void testStartQueryWithValidQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        whois.startQuery();
        assertFalse(whois.displayErrorCalled); // Assuming displayError has a flag to check if it was called
    }

    @Test
    public void testQueryWithInvalidWhoisAddress() {
        WhoIS whois = new WhoIS();
        whois.WHOIS_ADDRESS = "invalid.address";
        whois.startQuery();
        assertTrue(whois.displayErrorCalled); // Assuming displayError has a flag to check if it was called
    }

    @Test(expected = UnknownHostException.class)
    public void testQueryWithUnknownWhoisHost() {
        WhoIS whois = new WhoIS();
        whois.WHOIS_ADDRESS = "unknown.host";
        whois.startQuery();
    }

    @Test(expected = IOException.class)
    public void testQueryWithWhoisServerConnectionError() {
        WhoIS whois = new WhoIS();
        whois.startQuery();
    }

    // Test additional methods and their edge cases/exception handling scenarios as needed

}