package ipac.whois; // make sure to include the package name in your test class

import static org.junit.Assert.*;
import ipac.WhoIS;
import org.junit.Test;

public class WhoISTest {

    @Test
    public void testInitialization() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois);
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
    }

    @Test
    public void testActionPerformed() {
        WhoIS whois = new WhoIS();
        MockActionEvent event = new MockActionEvent();
        whois.actionPerformed(event);
        verify(event).setSource(whois.queryButton);
    }

    @Test
    public void testStartQuery() {
        WhoIS whois = new WhoIS();
        MockActionEvent queryButtonEvent = new MockActionEvent();
        MockActionEvent resetButtonEvent = new MockActionEvent();
        whois.actionPerformed(queryButtonEvent);
        assertFalse(whois.isQueryInProgress());
        whois.queryTextField.setText("example.com");
        whois.actionPerformed(queryButtonEvent);
        assertTrue(whois.isQueryInProgress());
        whois.actionPerformed(resetButtonEvent);
        assertFalse(whois.isQueryInProgress());
    }

    @Test
    public void testGetQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        WhoIS.InverseQuery inverseQuery = WhoIS.InverseQuery.PERSON;
        whois.inverseQueries.setSelectedIndex(WhoIS.InverseQuery.PERSON.ordinal());
        whois.lowercaseL.setSelected(true);
        whois.lowercaseD.setSelected(true);
        WhoIS.MiscQuery[] miscQueries = WhoIS.MiscQuery.values();
        for (WhoIS.MiscQuery miscQuery : miscQueries) {
            if (!miscQuery.toString().equals("All")) {
                whois.miscList.setSelectedValue(miscQuery.toString(), true);
                break;
            }
        }
        String expectedQuery = "-i person -l example.com -d example.com -T as-block,as-set,aut-num,domain,filter-set,inet-rtr,inet6num,inetnum,irt,key-cert,mntner,peering-set,person,role,route,route-set,rtr-set example.com";
        assertEquals(expectedQuery, whois.getQuery());
    }

    @Test
    public void testQuery() {
        // This test case is out of scope as it requires network connectivity and handling of network responses.
    }
}