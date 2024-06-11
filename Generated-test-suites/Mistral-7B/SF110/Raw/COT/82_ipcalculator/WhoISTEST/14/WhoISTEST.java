package ipac.test;

import static org.junit.Assert.*;

import org.junit.Test;

import ipac.WhoIS;

public class WhoISTest {

    @Test
    public void testWhoISConstructor() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois);
        assertEquals("APNIC", whois.WHOIS_NAME);
        assertEquals("whois.apnic.net", whois.WHOIS_ADDRESS);
        assertEquals(43, whois.WHOIS_PORT);
    }

    @Test
    public void testQueryPanelCreation() {
        WhoIS whois = new WhoIS();
        JPanel queryPanel = whois.queryPanel();

        assertNotNull(queryPanel);
        assertTrue(queryPanel.getComponentCount() > 0);
        assertTrue(queryPanel.getBackground() != null);
    }

    // Add test cases for other methods, such as startQuery(), reset(), etc.

    @Test(expected = IllegalArgumentException.class)
    public void testQueryWithEmptyQuery() {
        WhoIS whois = new WhoIS();
        whois.startQuery("");
    }

    @Test
    public void testQueryWithValidQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        whois.startQuery();
        // Assert that the WhoISOutput is displayed correctly
    }

    @Test(expected = UnknownHostException.class)
    public void testUnknownWhoisHost() {
        WhoIS whois = new WhoIS();
        whois.WHOIS_ADDRESS = "unknown.host";
        whois.startQuery("example.com");
    }

    @Test(expected = IOException.class)
    public void testWhoisServerNotReachable() {
        WhoIS whois = new WhoIS();
        whois.WHOIS_ADDRESS = "whois.example.com";
        whois.startQuery("example.com");
    }

    @Test
    public void testGetQueryWithNoInverseQueries() {
        WhoIS whois = new WhoIS();
        String query = whois.getQuery();
        assertTrue(query.startsWith("example.com"));
        assertEquals(0, query.split("\\s+").length);
    }

    @Test
    public void testGetQueryWithInverseQueries() {
        WhoIS whois = new WhoIS();
        whois.inverseQueries.setSelectedIndex(1);
        String query = whois.getQuery();
        assertTrue(query.startsWith("-i tech-c example.com"));
    }

    // Add more test cases for other combinations of selected radio buttons and checkboxes

    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        whois.startQuery();
        whois.reset();
        assertTrue(whois.queryTextField.getText().isEmpty());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }
}