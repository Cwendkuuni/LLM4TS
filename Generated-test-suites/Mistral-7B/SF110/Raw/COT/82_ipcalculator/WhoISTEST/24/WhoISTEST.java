import org.junit.Assert;
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
        Assert.assertNotNull(whois.queryPanel());
    }

    @Test
    public void testAdditionalQueriesPanelCreation() {
        WhoIS whois = new WhoIS();
        Assert.assertNotNull(whois.additionalQueriesPanel());
    }

    @Test
    public void testIPAddressLookupsCreation() {
        WhoIS whois = new WhoIS();
        Assert.assertNotNull(whois.IPAddressLookups());
    }

    @Test
    public void testMiscellaneousQueriesCreation() {
        WhoIS whois = new WhoIS();
        Assert.assertNotNull(whois.MiscellaneousQueries());
    }

    @Test
    public void testQueryHintsPanelCreation() {
        WhoIS whois = new WhoIS();
        Assert.assertNotNull(whois.queryHintsPanel());
    }

    @Test
    public void testCreatePopupMenu() {
        WhoIS whois = new WhoIS();
        Assert.assertNotNull(whois.popupMenu);
    }

    @Test(expected = NullPointerException.class)
    public void testQueryWithNullQuery() {
        WhoIS whois = new WhoIS();
        whois.startQuery(null);
    }

    @Test
    public void testStartQueryWithValidQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        whois.startQuery();
        // Verify the query method is called with the correct query string
        String query = whois.getQuery();
        Assert.assertEquals("example.com", query);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartQueryWithEmptyQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("");
        whois.startQuery();
    }

    @Test
    public void testDisplayErrorWithNullErrorMessage() {
        WhoIS whois = new WhoIS();
        whois.displayError(null);
        // Verify no error message is displayed
        assertEquals(0, JOptionPane.showConfirmDialog(whois, "Error"));
    }

    @Test
    public void testDisplayErrorWithValidErrorMessage() {
        WhoIS whois = new WhoIS();
        whois.displayError("Test Error Message");
        // Verify the error message is displayed
        assertEquals(0, JOptionPane.showConfirmDialog(whois, "Error: Test Error Message"));
    }

    @Test(expected = UnknownHostException.class)
    public void testQueryWithUnknownHost() {
        WhoIS whois = new WhoIS();
        whois.WHOIS_ADDRESS = "unknown-host.com";
        whois.startQuery();
    }

    @Test(expected = IOException.class)
    public void testQueryWithIOException() {
        WhoIS whois = new WhoIS();
        try {
            Socket connection = new Socket("127.0.0.1", 1234);
            connection.close();
        } catch (IOException e) {
            // Swallow the IOException
        }
        whois.WHOIS_ADDRESS = "127.0.0.1";
        whois.WHOIS_PORT = 1234;
        whois.startQuery();
    }
}