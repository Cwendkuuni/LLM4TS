package ipac.test;

import static org.junit.Assert.*;
import ipac.WhoIS;

import org.junit.Test;

public class WhoISTest {

    @Test
    public void testWhoIS() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois);
        assertEquals("APNIC", whois.WHOIS_NAME);
        assertEquals("whois.apnic.net", whois.WHOIS_ADDRESS);
        assertEquals(43, whois.WHOIS_PORT);

        //Test queryPanel()
        JPanel queryPanel = whois.queryPanel();
        assertNotNull(queryPanel);
        assertEquals(3, queryPanel.getComponentCount());
        assertTrue(queryPanel.getComponent(0) instanceof JLabel);
        assertTrue(queryPanel.getComponent(1) instanceof JTextField);
        assertTrue(queryPanel.getComponent(2) instanceof JButton);

        //Test additionalQueriesPanel()
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
        assertNotNull(additionalQueriesPanel);
        assertEquals(0, additionalQueriesPanel.getComponentCount());

        //Test IPAddressLookups()
        JPanel ipAddressLookups = whois.IPAddressLookups();
        assertNotNull(ipAddressLookups);
        assertEquals(8, ipAddressLookups.getComponentCount());
        assertTrue(ipAddressLookups.getComponent(0) instanceof JRadioButton);
        assertTrue(ipAddressLookups.getComponent(1) instanceof JRadioButton);
        assertTrue(ipAddressLookups.getComponent(2) instanceof JRadioButton);
        assertTrue(ipAddressLookups.getComponent(3) instanceof JRadioButton);
        assertTrue(ipAddressLookups.getComponent(4) instanceof JRadioButton);
        assertTrue(ipAddressLookups.getComponent(5) instanceof JCheckBox);
        assertTrue(ipAddressLookups.getComponent(6) instanceof JRadioButton);
        assertTrue(ipAddressLookups.getComponent(7) instanceof JRadioButton);

        //Test MiscellaneousQueries()
        JPanel miscQueries = whois.MiscellaneousQueries();
        assertNotNull(miscQueries);
        assertEquals(3, miscQueries.getComponentCount());
        assertTrue(miscQueries.getComponent(0) instanceof JLabel);
        assertTrue(miscQueries.getComponent(1) instanceof JComboBox);
        assertTrue(miscQueries.getComponent(2) instanceof JLabel);

        //Test queryHintsPanel()
        JPanel queryHintsPanel = whois.queryHintsPanel();
        assertNotNull(queryHintsPanel);
        assertEquals(3, queryHintsPanel.getComponentCount());

        //Test createPopupMenu()
        JPopupMenu popupMenu = whois.popupMenu;
        assertNotNull(popupMenu);
        assertEquals(4, popupMenu.getComponentCount());
        assertTrue(popupMenu.getComponent(0) instanceof JMenuItem);
        assertTrue(popupMenu.getComponent(1) instanceof JMenuItem);
        assertTrue(popupMenu.getComponent(2) instanceof JMenuItem);
        assertTrue(popupMenu.getComponent(3) instanceof JMenuItem);
    }
}