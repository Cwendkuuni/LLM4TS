package ipac;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class WhoISTest {

    private WhoIS whois;

    @Before
    public void setUp() {
        whois = new WhoIS();
    }

    @Test
    public void testConstructor() {
        assertNotNull(whois);
        assertEquals("APNIC", whois.WHOIS_NAME);
        assertEquals("whois.apnic.net", whois.WHOIS_ADDRESS);
        assertEquals(43, whois.WHOIS_PORT);
    }

    @Test
    public void testQueryPanel() {
        assertNotNull(whois.queryPanel());
        assertTrue(whois.queryButton != null);
        assertTrue(whois.resetButton != null);
    }

    @Test
    public void testAdditionalQueriesPanel() {
        assertNotNull(whois.additionalQueriesPanel());
    }

    @Test
    public void testIPAddressLookups() {
        assertNotNull(whois.IPAddressLookups());
        assertTrue(whois.lowercaseL != null);
        assertTrue(whois.uppercaseL != null);
        // Test other components and their combinations
    }

    @Test
    public void testMiscellaneousQueries() {
        assertNotNull(whois.MiscellaneousQueries());
        // Test all components and their combinations
    }

    @Test(expected = NullPointerException.class)
    public void testGetQueryWithNullQuery() {
        whois.queryTextField.setText(null);
        String query = whois.getQuery();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetQueryWithEmptyQuery() {
        whois.queryTextField.setText("");
        String query = whois.getQuery();
    }

    @Test
    public void testStartQueryWithNullQuery() {
        whois.queryTextField.setText(null);
        whois.startQuery();
        assertNull(whois.getOutput());
    }

    @Test
    public void testStartQueryWithValidQuery() {
        whois.queryTextField.setText("example.com");
        whois.startQuery();
        assertNotNull(whois.getOutput());
    }

    @Test(expected = UnknownHostException.class)
    public void testQueryWithInvalidHost() {
        whois.queryTextField.setText("invalid.host");
        whois.startQuery();
    }

    @Test(expected = IOException.class)
    public void testQueryWithServerNotReachable() {
        whois.queryTextField.setText("whois.apnic.net");
        whois.startQuery();
    }

    @Test
    public void testReset() {
        whois.reset();
        assertEquals("", whois.queryTextField.getText());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals("None", whois.inverseQueries.getSelectedItem().toString());
        assertEquals("All", whois.miscList.getSelectedValue().toString());
    }
}