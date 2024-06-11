package ipac;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.UnknownHostException;

public class WhoISTest {

    private WhoIS whois;

    @Before
    public void setUp() {
        whois = new WhoIS();
    }

    @Test
    public void testWhoISConstructor() {
        assertNotNull(whois);
        assertEquals("APNIC", whois.WHOIS_NAME);
        assertEquals("whois.apnic.net", whois.WHOIS_ADDRESS);
        assertEquals(43, whois.WHOIS_PORT);
    }

    @Test
    public void testQueryPanelCreation() {
        assertNotNull(whois.queryPanel());
        assertTrue(whois.queryPanel().getComponentCount() > 0);
    }

    @Test
    public void testQueryTextField() {
        assertNotNull(whois.queryTextField);
        assertEquals(40, whois.queryTextField.getColumns());
    }

    @Test
    public void testQueryButton() {
        assertNotNull(whois.queryButton);
        assertTrue(whois.queryButton.getModel().isEnabled());
    }

    @Test
    public void testResetButton() {
        assertNotNull(whois.resetButton);
        assertTrue(whois.resetButton.getModel().isEnabled());
    }

    @Test(expected = NullPointerException.class)
    public void testQueryWithNullQuery() {
        whois.startQuery(null);
    }

    @Test
    public void testQueryWithEmptyQuery() {
        whois.queryTextField.setText("");
        whois.startQuery();
        String expectedError = "No Query Detected";
        String actualError = whois.getErrorMessage();
        assertEquals(expectedError, actualError);
    }

    @Test
    public void testQueryWithValidQuery() throws IOException, UnknownHostException {
        whois.queryTextField.setText("example.com");
        whois.startQuery();
        // Add assertions here to check the output of the WhoISOutput class
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInverseQuery() {
        whois.inverseQueries.setSelectedItem("InvalidQuery");
        whois.startQuery();
    }

    @Test
    public void testMultipleSelectedMiscQueries() {
        String[] items = {"All", "as-block", "as-set", "aut-num", "domain", "filter-set", "inet-rtr", "inet6num", "inetnum", "irt", "key-cert", "mntner", "peering-set", "person", "role", "route", "route-set", "rtr-set"};
        for (int i = 0; i < items.length; i++) {
            whois.miscList.setSelectedValue(items[i], true);
        }
        whois.startQuery();
        // Add assertions here to check the query string
    }
}