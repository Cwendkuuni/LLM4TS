import static org.junit.Assert.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

import org.junit.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

import ipac.WhoIS;
import ipac.WhoISOutput;

public class WhoISTest {

    WhoIS whois;
    WhoISOutput output;

    @Before
    public void setUp() {
        whois = new WhoIS();
        output = mock(WhoISOutput.class);
    }

    @Test
    public void testWhoISConstructor() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois);
    }

    @Test
    public void testQueryPanelCreation() {
        WhoIS whois = new WhoIS();
        JPanel queryPanel = whois.queryPanel();
        assertNotNull(queryPanel);

        JTextField queryTextField = whois.queryTextField;
        assertNotNull(queryTextField);

        JButton queryButton = whois.queryButton;
        assertNotNull(queryButton);

        JButton resetButton = whois.resetButton;
        assertNotNull(resetButton);
    }

    @Test
    public void testAdditionalQueriesPanelCreation() {
        WhoIS whois = new WhoIS();
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
        assertNotNull(additionalQueriesPanel);

        JPanel lookupPanel = whois.IPAddressLookups();
        assertNotNull(lookupPanel);

        JPanel miscPanel = whois.MiscellaneousQueries();
        assertNotNull(miscPanel);
    }

    @Test
    public void testQueryHintsPanelCreation() {
        WhoIS whois = new WhoIS();
        JPanel queryHintsPanel = whois.queryHintsPanel();
        assertNotNull(queryHintsPanel);

        JPanel innerPanel = whois.queryHintsPanel().getComponent(0);
        assertNotNull(innerPanel);
    }

    @Test
    public void testCreatePopupMenu() {
        WhoIS whois = new WhoIS();
        JPopupMenu popupMenu = whois.popupMenu;
        assertNotNull(popupMenu);

        JMenuItem cut = popupMenu.getComponent(0);
        assertNotNull(cut);

        JMenuItem copy = popupMenu.getComponent(1);
        assertNotNull(copy);

        JMenuItem paste = popupMenu.getComponent(2);
        assertNotNull(paste);

        JMenuItem selectAll = popupMenu.getComponent(3);
        assertNotNull(selectAll);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartQueryWithEmptyQuery() {
        WhoIS whois = new WhoIS();
        whois.startQuery();
    }

    @Test
    public void testStartQueryWithValidQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        whois.startQuery();
        verify(output).displayOutput("example.com\n");
    }

    @Test(expected = UnknownHostException.class)
    public void testQueryWithUnknownHost() {
        WhoIS whois = new WhoIS();
        whois.WHOIS_ADDRESS = "unknown.host";
        whois.startQuery();
    }

    @Test(expected = IOException.class)
    public void testQueryWithIOException() {
        WhoIS whois = new WhoIS();
        doThrow(IOException.class).when(output).displayOutput("");
        whois.startQuery();
    }

    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.startQuery();
        whois.reset();
        assertEquals("", whois.queryTextField.getText());
    }

    @Test
    public void testGetQueryWithNoInverseQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        String query = whois.getQuery();
        assertEquals("example.com", query);
    }

    @Test
    public void testGetQueryWithInverseQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        whois.inverseQueries.setSelectedIndex(1);
        String query = whois.getQuery();
        assertEquals("-i admin-c example.com", query);
    }

    @Test
    public void testGetQueryWithIPv6Address() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("2001:db8:0:1234:0:5678:0:1:1");
        String query = whois.getQuery();
        assertEquals("-T inet6num 2001:db8:0:1234:0:5678:0:1:1", query);
    }

    @Test
    public void testGetQueryWithMultipleSelectedMiscQueries() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        String[] miscQueries = {"as-block", "peering-set"};
        Arrays.stream(miscQueries).forEach(type -> whois.miscList.setSelectedItem(type));
        String query = whois.getQuery();
        String expectedQuery = "-T as-block,peering-set example.com";
        assertEquals(expectedQuery, query);
    }

    @After
    public void tearDown() {
        whois = null;
        output = null;
    }
}