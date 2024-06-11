package ipac.test;

import static org.junit.Assert.*;
import ipac.WhoIS;

import org.junit.Test;

public class WhoISTest {

    @Test
    public void testConstructor() {
        WhoIS whoIS = new WhoIS();
        assertNotNull(whoIS);
        assertEquals("APNIC", whoIS.WHOIS_NAME);
        assertEquals("whois.apnic.net", whoIS.WHOIS_ADDRESS);
        assertEquals(43, whoIS.WHOIS_PORT);
    }

    @Test
    public void testQueryPanel() {
        WhoIS whoIS = new WhoIS();
        JPanel queryPanel = whoIS.queryPanel();

        assertNotNull(queryPanel);
        assertNotNull(whoIS.queryTextField);
        assertNotNull(whoIS.queryButton);
        assertNotNull(whoIS.resetButton);

        assertEquals(40, whoIS.queryTextField.getColumns());
        assertEquals("Query", whoIS.queryButton.getText());
        assertEquals("Reset", whoIS.resetButton.getText());

        // Add more assertions for the components in the queryPanel
    }

    @Test
    public void testAdditionalQueriesPanel() {
        WhoIS whoIS = new WhoIS();
        JPanel additionalQueriesPanel = whoIS.additionalQueriesPanel();

        assertNotNull(additionalQueriesPanel);

        // Add assertions for the components in the additionalQueriesPanel
        // For example, you can check if the labels and radio buttons exist
    }

    @Test
    public void testIPAddressLookups() {
        WhoIS whoIS = new WhoIS();
        JPanel lookupPanel = whoIS.IPAddressLookups();

        assertNotNull(lookupPanel);
        assertTrue(whoIS.lowercaseL.isSelected()); // Default value

        // Add more assertions for the components in the lookupPanel
    }

    @Test
    public void testMiscellaneousQueries() {
        WhoIS whoIS = new WhoIS();
        JPanel miscPanel = whoIS.MiscellaneousQueries();

        assertNotNull(miscPanel);

        // Add assertions for the components in the miscPanel
        // For example, you can check if the JLabel and JList exist
    }

    @Test
    public void testQueryHintsPanel() {
        WhoIS whoIS = new WhoIS();
        JPanel queryHintsPanel = whoIS.queryHintsPanel();

        assertNotNull(queryHintsPanel);

        // Add assertions for the components in the queryHintsPanel
        // For example, you can check if the labels and JScrollPane exist
    }

    @Test
    public void testCreatePopupMenu() {
        WhoIS whoIS = new WhoIS();
        JPopupMenu popupMenu = whoIS.popupMenu;

        assertNotNull(popupMenu);

        // Add assertions for the components in the popupMenu
        // For example, you can check if the JMenuItem exists
    }

    @Test
    public void testActionPerformed() {
        WhoIS whoIS = new WhoIS();
        MockComponentEvent mockEvent = new MockComponentEvent();

        whoIS.actionPerformed(mockEvent);

        // Add assertions based on the source of the ActionEvent
    }

    @Test
    public void testStartQuery() {
        WhoIS whoIS = new WhoIS();
        String query = "example.com";

        whoIS.queryTextField.setText(query);
        whoIS.actionPerformed(new ActionEvent(whoIS.queryButton, 0, "Query"));

        // Add assertions for the query that is sent
    }

    @Test
    public void testDisplayError() {
        WhoIS whoIS = new WhoIS();
        String error = "Error message";

        whoIS.displayError(error);

        // Add assertions for the error message displayed
    }

    @Test
    public void testQuery() {
        WhoIS whoIS = new WhoIS();
        String query = "example.com";

        // Mock the Socket, PrintStream, and BufferedReader
        // Set up the expected response from the Whois server

        whoIS.query(query);

        // Add assertions for the response received from the Whois server
    }

    @Test
    public void testGetQuery() {
        WhoIS whoIS = new WhoIS();

        // Set up the JTextField, JComboBox, and radio buttons
        // Call the getQuery() method with different selections

        String expectedQuery = "";
        String query = whoIS.getQuery();

        assertEquals(expectedQuery, query);

        // Repeat the test with different selections and expected queries
    }

    @Test
    public void testReset() {
        WhoIS whoIS = new WhoIS();

        whoIS.reset();

        // Add assertions for the components after resetting
    }
}