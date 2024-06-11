package ipac;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WhoISTest {
    private WhoIS whois;

    @Before
    public void setUp() {
        whois = new WhoIS();
    }

    

    @Test
    public void testWhoISConstructor() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois.WHOIS_NAME);
        assertNotNull(whois.WHOIS_ADDRESS);
        assertEquals("APNIC", whois.WHOIS_NAME);
        assertEquals("whois.apnic.net", whois.WHOIS_ADDRESS);
        assertEquals(43, whois.WHOIS_PORT);
    }

    @Test
    public void testQueryPanelCreation() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois.queryPanel());
    }

    @Test
    public void testAdditionalQueriesPanelCreation() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois.additionalQueriesPanel());
    }

    @Test
    public void testIPAddressLookupsCreation() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois.IPAddressLookups());
    }

    @Test
    public void testMiscellaneousQueriesCreation() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois.MiscellaneousQueries());
    }

    @Test
    public void testQueryHintsPanelCreation() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois.queryHintsPanel());
    }

    @Test
    public void testCreatePopupMenu() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois.popupMenu);
        assertEquals(4, whois.popupMenu.getComponentCount());
    }

    @Test(expected = NullPointerException.class)
    public void testStartQueryWithEmptyQuery() {
        WhoIS whois = new WhoIS();
        whois.startQuery();
    }

    @Test
    public void testStartQueryWithValidQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        whois.startQuery();
        // Add assertions to check the expected behavior of the WhoIS class when a query is executed
    }

    @Test(expected = UnknownHostException.class)
    public void testQueryWithInvalidHost() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("invalid.host.com");
        whois.startQuery();
    }

    @Test(expected = IOException.class)
    public void testQueryWithServerConnectionError() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("google.com");
        when(mockSocketFactory.createSocket(eq("google.com"), eq(43))).thenThrow(new IOException("Server unreachable"));
        whois.setSocketFactory(mockSocketFactory);
        whois.startQuery();
    }

    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.startQuery();
        whois.reset();
        // Add assertions to check the expected behavior of the WhoIS class after a reset
    }

    
}