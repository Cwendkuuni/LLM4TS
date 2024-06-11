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
        assertNotNull(whois.getQueryTextField());
        assertNotNull(whois.getQueryButton());
        assertNotNull(whois.getResetButton());
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
    public void testActionPerformedQueryButton() {
        WhoIS whois = new WhoIS();
        mockQueryTextField("testquery");
        whois.actionPerformed(new ActionEvent(whois.queryButton, 0, "Query"));
        assertEquals("testquery", whois.getQuery());
    }

    @Test
    public void testActionPerformedResetButton() {
        WhoIS whois = new WhoIS();
        whois.actionPerformed(new ActionEvent(whois.resetButton, 0, "Reset"));
        assertEquals("", whois.queryTextField.getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartQueryEmptyQuery() {
        WhoIS whois = new WhoIS();
        whois.startQuery();
    }

    @Test
    public void testStartQueryValidQuery() {
        WhoIS whois = new WhoIS();
        mockQueryTextField("testquery");
        whois.startQuery();
        // Add assertions to check the behavior of the query method here
    }

    @Test(expected = UnknownHostException.class)
    public void testQueryUnknownHost() {
        WhoIS whois = new WhoIS();
        whois.startQuery();
    }

    @Test(expected = IOException.class)
    public void testQueryIOException() {
        WhoIS whois = new WhoIS();
        whois.startQuery();
    }

    

    private void mockQueryTextField(String query) {
        WhoIS whois = new WhoIS();
        WhoIS.JTextField queryTextFieldSpy = whois.queryTextField;
        queryTextFieldSpy.setText(query);
    }
}