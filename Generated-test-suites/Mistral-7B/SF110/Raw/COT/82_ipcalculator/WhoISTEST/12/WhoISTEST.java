package ipac.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ipac.WhoIS;

public class WhoISTest {

    private WhoIS whois;

    @Before
    public void setup() {
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
        WhoIS.JPanel queryPanel = whois.queryPanel();
        assertNotNull(queryPanel);
    }

    @Test
    public void testAdditionalQueriesPanelCreation() {
        WhoIS whois = new WhoIS();
        WhoIS.JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
        assertNotNull(additionalQueriesPanel);
    }

    @Test
    public void testIPAddressLookupsCreation() {
        WhoIS whois = new WhoIS();
        WhoIS.JPanel lookupPanel = whois.IPAddressLookups();
        assertNotNull(lookupPanel);
    }

    @Test
    public void testMiscellaneousQueriesCreation() {
        WhoIS whois = new WhoIS();
        WhoIS.JPanel miscPanel = whois.MiscellaneousQueries();
        assertNotNull(miscPanel);
    }

    @Test
    public void testQueryHintsPanelCreation() {
        WhoIS whois = new WhoIS();
        WhoIS.JPanel queryHintsPanel = whois.queryHintsPanel();
        assertNotNull(queryHintsPanel);
    }

    @Test(expected = NullPointerException.class)
    public void testStartQueryWithEmptyTextField() {
        WhoIS whois = new WhoIS();
        whois.startQuery();
    }

    @Test
    public void testStartQueryWithValidQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        whois.startQuery();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartQueryWithInvalidQueryFormat() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("invalidquery");
        whois.startQuery();
    }

    @Test(expected = UnknownHostException.class)
    public void testQueryWithInvalidHost() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("invalidhost.com");
        whois.startQuery();
    }

    @Test(expected = IOException.class)
    public void testQueryWithUnreachableServer() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("whoishost.com");
        whois.startQuery();
    }

    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.startQuery();
        whois.reset();
        assertEquals("", whois.queryTextField.getText());
    }
}