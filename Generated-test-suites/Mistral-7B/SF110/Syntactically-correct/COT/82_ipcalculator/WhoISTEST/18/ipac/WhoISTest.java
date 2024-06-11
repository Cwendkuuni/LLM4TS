import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.After;
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
        // Basic functionality test
        assertNotNull(whois);
        assertEquals("APNIC", whois.WHOIS_NAME);
        assertEquals("whois.apnic.net", whois.WHOIS_ADDRESS);
        assertEquals(43, whois.WHOIS_PORT);
    }

    @Test
    public void testQueryPanelCreation() {
        // Basic functionality test
        assertNotNull(whois.queryPanel());

        // Test queryTextField behavior
        assertSame(whois.queryTextField, whois.queryPanel().getComponent(1));
        assertEquals(40, whois.queryTextField.getColumns());

        // Test queryButton behavior
        assertSame(whois.queryButton, whois.queryPanel().getComponent(3));

        // Test resetButton behavior
        assertSame(whois.resetButton, whois.queryPanel().getComponent(4));
    }

    @Test
    public void testAdditionalQueriesPanelCreation() {
        // Basic functionality test
        assertNotNull(whois.additionalQueriesPanel());

        // Test IPAddressLookups behavior
        assertNotNull(whois.IPAddressLookups());
        assertTrue(whois.IPAddressLookups().getComponentCount() > 0);

        // Test MiscellaneousQueries behavior
        assertNotNull(whois.MiscellaneousQueries());
        assertTrue(whois.MiscellaneousQueries().getComponentCount() > 0);
    }

    @Test
    public void testQueryHintsPanelCreation() {
        // Basic functionality test
        assertNotNull(whois.queryHintsPanel());

        // Test innerPanel behavior
        assertNotNull(whois.queryHintsPanel().getComponent(0).getComponent(0));
        assertNotNull(whois.queryHintsPanel().getComponent(0).getComponent(1));
        assertNotNull(whois.queryHintsPanel().getComponent(0).getComponent(2));
        assertNotNull(whois.queryHintsPanel().getComponent(0).getComponent(3));
        assertNotNull(whois.queryHintsPanel().getComponent(0).getComponent(4));
        assertNotNull(whois.queryHintsPanel().getComponent(0).getComponent(5));
    }

    @Test(expected = NullPointerException.class)
    public void testStartQueryWhenNoQuery() {
        // Edge case test
        whois.startQuery();
    }

    @Test
    public void testStartQueryWithValidQuery() {
        // Test query behavior
        whois.queryTextField.setText("example.com");
        whois.startQuery();

        // Verify displayError was not called
        assertFalse(whois.hasDisplayedErrorMessage());

        // Verify query method was called
        assertTrue(whois.queryWasCalled());
    }

    @Test(expected = UnknownHostException.class)
    public void testQueryWithInvalidHost() {
        // Exception handling test
        whois.queryTextField.setText("invalid.host");
        whois.startQuery();
    }

    @Test(expected = IOException.class)
    public void testQueryWithInvalidServerResponse() {
        // Exception handling test
        whois.queryTextField.setText("example.com");
        WhoISOutput spyOutput = mock(WhoISOutput.class);
        when(spyOutput.display).thenThrow(new IOException());
        whois.setWhoISOutput(spyOutput);
        whois.startQuery();
    }

    @Test
    public void testReset() {
        // Basic functionality test
        whois.reset();

        // Test queryTextField behavior
        assertNull(whois.queryTextField.getText());

        // Test resetRadioButton behavior
        assertTrue(whois.resetRadioButton.isSelected());

        // Test lowercaseD behavior
        assertFalse(whois.lowercaseD.isSelected());

        // Test inverseQueries behavior
        assertEquals("None", whois.inverseQueries.getSelectedItem().toString());

        // Test miscList behavior
        assertEquals("All", whois.miscList.getSelectedValue().toString());
    }

    @After
    public void tearDown() {
        whois = null;
    }
}