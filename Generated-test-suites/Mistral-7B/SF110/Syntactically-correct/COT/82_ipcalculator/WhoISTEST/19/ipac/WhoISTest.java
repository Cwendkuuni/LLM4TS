package ipac.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ipac.WhoIS;
import ipac.WhoISOutput;

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
    public void testWhoISConstructor() {
        WhoIS whois = new WhoIS();
        assertNull(whois.getQueryTextField());
        assertNull(whois.getQueryButton());
        assertNull(whois.getResetButton());
        assertNull(whois.getLowercaseL());
        assertNull(whois.getUppercaseL());
        assertNull(whois.getLowercaseM());
        assertNull(whois.getUppercaseM());
        assertNull(whois.getLowercaseX());
        assertNull(whois.getResetRadioButton());
        assertNull(whois.getRadioButtonGroup());
        assertNull(whois.getPopupMenu());
        assertNull(whois.getInverseQueries());
        assertNull(whois.getMiscList());
        assertEquals(Color.WHITE, whois.getBackground());
        assertEquals("APNIC", whois.getWHOIS_NAME());
        assertEquals("whois.apnic.net", whois.getWHOIS_ADDRESS());
        assertEquals(43, whois.getWHOIS_PORT());
    }

    @Test
    public void testQueryPanelCreation() {
        WhoIS whois = new WhoIS();
        JPanel queryPanel = whois.queryPanel();
        assertNotNull(queryPanel);
        assertNotNull(queryPanel.getLayout());
        assertEquals(FlowLayout.class, queryPanel.getLayout().getClass());
        assertEquals(3, queryPanel.getComponentCount());
        assertTrue(queryPanel.getComponent(0) instanceof JLabel);
        assertTrue(queryPanel.getComponent(1) instanceof JTextField);
        assertTrue(queryPanel.getComponent(2) instanceof JButton);
    }

    @Test
    public void testStartQueryWithEmptyTextField() {
        WhoIS whois = new WhoIS();
        whois.startQuery();
        assertFalse(whois.displayErrorCalled);
        assertTrue(whois.queryTextField.getText().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartQueryWithNullQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText(null);
        whois.startQuery();
    }

    @Test(expected = IOException.class)
    public void testQueryWithInvalidAddress() {
        WhoIS whois = new WhoIS();
        whois.WHOIS_ADDRESS = "invalid-address";
        whois.startQuery();
    }

    @Test(expected = UnknownHostException.class)
    public void testQueryWithUnknownHost() {
        WhoIS whois = new WhoIS();
        whois.WHOIS_ADDRESS = "unknown-host";
        whois.startQuery();
    }

    @Test
    public void testQueryWithValidQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        whois.startQuery();
        assertFalse(whois.displayErrorCalled);
        assertNotNull(whois.whoisOutput);
        assertTrue(whois.whoisOutput instanceof WhoISOutput);
        assertFalse(whois.queryTextField.getText().isEmpty());
    }

    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.startQuery();
        whois.reset();
        assertTrue(whois.queryTextField.getText().isEmpty());
        assertFalse(whois.displayErrorCalled);
    }
}