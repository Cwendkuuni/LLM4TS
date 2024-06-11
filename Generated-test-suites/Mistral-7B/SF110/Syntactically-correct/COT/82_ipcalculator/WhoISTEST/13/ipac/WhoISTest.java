import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.List;

public class WhoISTest {

    private WhoIS whois;

    @Before
    public void setUp() {
        whois = new WhoIS();
    }

    @After
    public void tearDown() {
        whois = null;
    }

    @Test
    public void testConstructor() {
        assertNotNull(whois);
    }

    @Test
    public void testQueryPanel() {
        JPanel queryPanel = whois.queryPanel();
        assertNotNull(queryPanel);
        assertEquals(3, queryPanel.getComponentCount());
    }

    @Test
    public void testAdditionalQueriesPanel() {
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
        assertNotNull(additionalQueriesPanel);
        assertEquals(2, additionalQueriesPanel.getComponentCount());
    }

    @Test
    public void testIPAddressLookups() {
        JPanel ipAddressLookups = whois.IPAddressLookups();
        assertNotNull(ipAddressLookups);
        assertEquals(7, ipAddressLookups.getComponentCount());
        assertSame(whois, ipAddressLookups.getParent());
    }

    @Test
    public void testMiscellaneousQueries() {
        JPanel miscellaneousQueries = whois.MiscellaneousQueries();
        assertNotNull(miscellaneousQueries);
        assertEquals(3, miscellaneousQueries.getComponentCount());
    }

    @Test
    public void testQueryHintsPanel() {
        JPanel queryHintsPanel = whois.queryHintsPanel();
        assertNotNull(queryHintsPanel);
        assertEquals(3, queryHintsPanel.getComponentCount());
    }

    @Test(expected = NullPointerException.class)
    public void testCreatePopupMenuNullComponent() {
        whois.popupMenu.add(null);
    }

    @Test
    public void testCreatePopupMenuComponentsCount() {
        assertEquals(9, whois.popupMenu.getComponentCount());
    }

    @Test
    public void testActionPerformedQueryButton() throws IOException {
        String query = "example.com";
        whois.queryTextField.setText(query);
        whois.actionPerformed(new ActionEvent(whois.queryButton, 0, "Query"));
        whois.startQuery();
        // Add assertions for the expected behavior of the query method here
    }

    @Test
    public void testActionPerformedResetButton() {
        whois.actionPerformed(new ActionEvent(whois.resetButton, 0, "Reset"));
        assertEquals("", whois.queryTextField.getText());
        assertFalse(whois.booleanD);
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }

    @Test
    public void testGetQueryEmptyQuery() {
        assertEquals("", whois.getQuery());
    }

    @Test
    public void testGetQueryValidQuery() {
        String query = "example.com";
        whois.queryTextField.setText(query);
        whois.lowercaseL.setSelected(true);
        whois.lowercaseD.setSelected(true);
        whois.inverseQueries.setSelectedIndex(1);
        List<String> miscQueries = Arrays.asList("as-block", "aut-num", "filter-set");
        whois.miscList.setSelectedValues(miscQueries.toArray());
        String expectedQuery = "-i admin-c,tech-c,zone-c -l example.com -d";
        assertEquals(expectedQuery, whois.getQuery());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQueryInvalidQuery() {
        whois.query("invalid query");
    }

    @Test(expected = UnknownHostException.class)
    public void testQueryUnknownHost() throws IOException {
        whois.query("unknown.host");
    }

    @Test(expected = SocketException.class)
    public void testQuerySocketException() throws IOException {
        whois.query("whois.server:9999");
    }

    @Test
    public void testDisplayError() {
        whois.displayError("Test Error");
        String message = JOptionPane.showMessageDialog(whois, "Error Message", "Error", 0).getMessage();
        assertEquals("Test Error", message);
    }
}