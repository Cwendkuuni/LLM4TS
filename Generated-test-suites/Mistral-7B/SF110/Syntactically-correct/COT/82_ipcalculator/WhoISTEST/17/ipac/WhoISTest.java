package ipac.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ipac.WhoIS;
import ipac.WhoISOutput;

public class WhoISTest {
    // Test instance of WhoIS class
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
        WhoISPanel queryPanel = whois.queryPanel();

        // Check if queryPanel is not null
        assertNotNull(queryPanel);

        // Check for components' existence and type
        assertTrue(queryPanel instanceof JPanel);
        assertTrue(queryLabel instanceof JLabel);
        assertTrue(queryTextField instanceof JTextField);
        assertTrue(queryButton instanceof JButton);
        assertTrue(resetButton instanceof JButton);
    }

    @Test
    public void testAdditionalQueriesPanelCreation() {
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();

        // Check if additionalQueriesPanel is not null
        assertNotNull(additionalQueriesPanel);

        // Check for components' existence and type
        assertTrue(additionalQueriesPanel instanceof JPanel);
        assertTrue(IPAddressLookups instanceof JPanel);
        assertTrue(MiscellaneousQueries instanceof JPanel);
    }

    @Test
    public void testQueryHintsPanelCreation() {
        JPanel queryHintsPanel = whois.queryHintsPanel();

        // Check if queryHintsPanel is not null
        assertNotNull(queryHintsPanel);

        // Check for innerPanel's existence and type
        assertTrue(innerPanel instanceof JPanel);

        // Check for labels' existence and type
        assertTrue(queryHintsText1 instanceof JLabel);
        assertTrue(queryHintsText2 instanceof JLabel);
        assertTrue(queryHintsText3 instanceof JLabel);
        assertTrue(queryHintsText4 instanceof JLabel);
        assertTrue(queryHintsText5 instanceof JLabel);
    }

    @Test(expected = NullPointerException.class)
    public void testCreatePopupMenuWhenNullComponent() {
        // Create a null component for the popup menu
        WhoIS.JPopupMenu popupMenu = new WhoIS.JPopupMenu(null);
    }

    @Test
    public void testCreatePopupMenu() {
        WhoIS.JPopupMenu popupMenu = whois.createPopupMenu();

        // Check if popupMenu is not null
        assertNotNull(popupMenu);

        // Check for items' existence and type
        assertTrue(popupMenu.getComponentCount() >= 5);
        assertTrue(cut instanceof JMenuItem);
        assertTrue(copy instanceof JMenuItem);
        assertTrue(paste instanceof JMenuItem);
        assertTrue(selectAll instanceof JMenuItem);
    }

    @Test
    public void testActionPerformedWhenQueryButton() throws IOException, UnknownHostException {
        String query = "example.com";
        whois.queryTextField.setText(query);

        // Simulate action performed event for queryButton
        whois.actionPerformed(new ActionEvent(whois.queryButton, 0, "Query"));

        // Check if query method is called
        assertTrue(whois.query(query) > 0);
    }

    @Test
    public void testActionPerformedWhenResetButton() {
        // Simulate action performed event for resetButton
        whois.actionPerformed(new ActionEvent(whois.resetButton, 0, "Reset"));

        // Check if reset method is called
        assertTrue(whois.queryTextField.getText().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQueryWithEmptyQuery() {
        whois.query("");
    }

    @Test
    public void testQueryWithValidQuery() throws IOException, UnknownHostException {
        String query = "example.com";
        whois.query(query);

        // Check if WhoISOutput is not null
        assertNotNull(whois.whoisOutput);

        // Check if WhoISOutput contains valid output
        assertTrue(whois.whoisOutput.getOutput().contains("Domain Name: EXAMPLE.COM"));
    }

    @Test(expected = UnknownHostException.class)
    public void testQueryWithInvalidHost() {
        String query = "invalid.host";
        whois.query(query);
    }

    @Test(expected = IOException.class)
    public void testQueryWithInvalidPort() {
        String query = "example.com";
        WhoISOutput whoisOutput = new WhoISOutput("Invalid Output");
        whois.whoisOutput = whoisOutput;

        whois.query(query);
    }

    @After
    public void tearDown() {
        whois = null;
    }
}