package ipac.test;

import static org.junit.Assert.*;
import ipac.WhoIS;

import java.awt.*;
import java.util.Arrays;

import org.junit.Test;

public class WhoISTest {

    @Test
    public void testConstructor() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois);
    }

    @Test
    public void testQueryPanel() {
        WhoIS whois = new WhoIS();
        JPanel queryPanel = whois.queryPanel();

        // Check the number of components in the panel
        assertEquals(6, queryPanel.getComponentCount());

        // Check the types of components in the panel
        assertTrue(queryPanel.getComponent(0) instanceof JLabel);
        assertTrue(queryPanel.getComponent(1) instanceof JTextField);
        assertTrue(queryPanel.getComponent(2) instanceof JButton);
        assertTrue(queryPanel.getComponent(3) instanceof JButton);
        assertTrue(queryPanel.getComponent(4) instanceof JButton);
        assertTrue(queryPanel.getComponent(5) instanceof JScrollPane);
    }

    @Test
    public void testAdditionalQueriesPanel() {
        WhoIS whois = new WhoIS();
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();

        // Check the number of components in the panel
        assertEquals(2, additionalQueriesPanel.getComponentCount());

        // Check the types of components in the panel
        assertTrue(additionalQueriesPanel.getComponent(0) instanceof JPanel);
        assertTrue(additionalQueriesPanel.getComponent(1) instanceof JPanel);

        // Test the IPAddressLookups() method
        JPanel ipLookupsPanel = (JPanel) additionalQueriesPanel.getComponent(1);
        assertEquals(7, ipLookupsPanel.getComponentCount());

        // Check the types of components in the IPAddressLookups() panel
        for (int i = 0; i < 7; i++) {
            assertTrue(ipLookupsPanel.getComponent(i) instanceof JRadioButton);
        }
    }

    @Test
    public void testMiscellaneousQueries() {
        WhoIS whois = new WhoIS();
        JPanel miscQueriesPanel = whois.MiscellaneousQueries();

        // Check the number of components in the panel
        assertEquals(3, miscQueriesPanel.getComponentCount());

        // Check the types of components in the panel
        assertTrue(miscQueriesPanel.getComponent(0) instanceof JLabel);
        assertTrue(miscQueriesPanel.getComponent(1) instanceof JComboBox);
        assertTrue(miscQueriesPanel.getComponent(2) instanceof JScrollPane);

        // Test the inverseQueries property
        JComboBox inverseQueries = (JComboBox) miscQueriesPanel.getComponent(1);
        String[] expectedItems = {"None", "admin-c", "admin-c,tech-c,zone-c", "author", "cross-mnt", "cross-nfy", "local-as", "mbrs-by-ref", "member-of", "mnt-by", "mnt-lower", "mnt-nfy", "mnt-routes", "notify", "nserver", "origin", "person", "referral-by", "rev-srv", "sub-dom", "tech-c", "upd-to", "zone-c"};
        assertArrayEquals(expectedItems, (Object[]) inverseQueries.getItems());

        // Test the miscList property
        JList miscList = (JList) miscQueriesPanel.getComponent(2);
        String[] expectedMiscItems = {"All", "as-block", "as-set", "aut-num", "domain", "filter-set", "inet-rtr", "inet6num", "inetnum", "irt", "key-cert", "mntner", "peering-set", "person", "role", "route", "route-set", "rtr-set"};
        assertArrayEquals(expectedMiscItems, (Object[]) miscList.getModel().toArray());
    }

    @Test
    public void testQueryHintsPanel() {
        WhoIS whois = new WhoIS();
        JPanel queryHintsPanel = whois.queryHintsPanel();

        // Check the number of components in the panel
        assertEquals(3, queryHintsPanel.getComponentCount());

        // Check the types of components in the panel
        assertTrue(queryHintsPanel.getComponent(0) instanceof JPanel);
        assertTrue(queryHintsPanel.getComponent(1) instanceof JLabel);
        assertTrue(queryHintsPanel.getComponent(2) instanceof JLabel);
    }

    @Test
    public void testCreatePopupMenu() {
        WhoIS whois = new WhoIS();
        JPopupMenu popupMenu = whois.popupMenu;

        // Check the number of components in the popup menu
        assertEquals(5, popupMenu.getComponentCount());

        // Check the types of components in the popup menu
        for (int i = 0; i < 5; i++) {
            assertTrue(popupMenu.getComponent(i) instanceof JMenuItem);
        }
    }
}