package ipac;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.UnknownHostException;

public class WhoISTest {
    private WhoIS whoIS;

    @Before
    public void setUp() {
        whoIS = new WhoIS();
    }

    @Test
    public void testConstructor() {
        assertNotNull(whoIS);
        assertEquals(new String("APNIC"), whoIS.WHOIS_NAME);
        assertEquals(new String("whois.apnic.net"), whoIS.WHOIS_ADDRESS);
        assertEquals(43, whoIS.WHOIS_PORT);
    }

    @Test
    public void testQueryPanelCreation() {
        assertNotNull(whoIS.queryPanel());
    }

    @Test
    public void testAdditionalQueriesPanelCreation() {
        assertNotNull(whoIS.additionalQueriesPanel());
    }

    @Test
    public void testIPAddressLookupsCreation() {
        assertNotNull(whoIS.IPAddressLookups());
    }

    @Test
    public void testMiscellaneousQueriesCreation() {
        assertNotNull(whoIS.MiscellaneousQueries());
    }

    @Test
    public void testQueryHintsPanelCreation() {
        assertNotNull(whoIS.queryHintsPanel());
    }

    @Test(expected = NullPointerException.class)
    public void testEmptyQueryTextField() {
        whoIS.startQuery();
    }

    @Test
    public void testValidQueryWithDefaultOptions() throws IOException, UnknownHostException {
        whoIS.queryTextField.setText("example.com");
        whoIS.startQuery();
        // Add assertions to verify the WhoISOutput here
    }

    @Test
    public void testValidQueryWithInverseQueries() throws IOException, UnknownHostException {
        whoIS.queryTextField.setText("example.com");
        whoIS.inverseQueries.setSelectedIndex(1); // Set to "admin-c"
        whoIS.startQuery();
        // Add assertions to verify the WhoISOutput here
    }

    @Test
    public void testValidQueryWithLowercaseOptions() throws IOException, UnknownHostException {
        whoIS.queryTextField.setText("example.com");
        whoIS.lowercaseL.setSelected(true);
        whoIS.startQuery();
        // Add assertions to verify the WhoISOutput here
    }

    @Test
    public void testValidQueryWithUppercaseOptions() throws IOException, UnknownHostException {
        whoIS.queryTextField.setText("example.com");
        whoIS.uppercaseL.setSelected(true);
        whoIS.startQuery();
        // Add assertions to verify the WhoISOutput here
    }

    @Test
    public void testValidQueryWithMiscOptions() throws IOException, UnknownHostException {
        whoIS.queryTextField.setText("example.com");
        Object[] selected = {"as-block"};
        whoIS.miscList.setSelectedValues(selected);
        whoIS.startQuery();
        // Add assertions to verify the WhoISOutput here
    }

    @Test
    public void testValidQueryWithBooleanD() throws IOException, UnknownHostException {
        whoIS.queryTextField.setText("example.com");
        whoIS.lowercaseD.setSelected(true);
        whoIS.startQuery();
        // Add assertions to verify the WhoISOutput here
    }

    @Test(expected = IOException.class)
    public void testInvalidQueryWithEmptyAddress() throws IOException {
        whoIS.queryTextField.setText("");
        whoIS.startQuery();
    }

    @Test(expected = UnknownHostException.class)
    public void testInvalidQueryWithUnknownHost() throws IOException, UnknownHostException {
        whoIS.queryTextField.setText("invalid-host.com");
        whoIS.startQuery();
    }

    @Test
    public void testReset() {
        whoIS.queryTextField.setText("example.com");
        whoIS.startQuery();
        whoIS.reset();
        assertEquals("", whoIS.queryTextField.getText());
        assertFalse(whoIS.lowercaseD.isSelected());
        assertEquals("None", whoIS.inverseQueries.getSelectedItem().toString());
        assertEquals("All", whoIS.miscList.getSelectedItem().toString());
    }
}