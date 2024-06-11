package ipac.test;

import static org.junit.Assert.*;
import org.junit.Test;
import ipac.WhoIS;

public class WhoISTest {

    @Test
    public void testWhoIS() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois);
        assertEquals("APNIC", whois.WHOIS_NAME);
        assertEquals("whois.apnic.net", whois.WHOIS_ADDRESS);
        assertEquals(43, whois.WHOIS_PORT);
    }

    @Test
    public void testQueryPanel() {
        WhoIS whois = new WhoIS();
        assertEquals(3, whois.queryPanel().getComponentCount());
        assertTrue(whois.queryPanel().getComponents()[0] instanceof JLabel);
        assertTrue(whois.queryPanel().getComponents()[1] instanceof JTextField);
        assertTrue(whois.queryPanel().getComponents()[2] instanceof JButton);
    }

    @Test
    public void testAdditionalQueriesPanel() {
        WhoIS whois = new WhoIS();
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
        assertEquals(2, additionalQueriesPanel.getComponentCount());
        assertTrue(additionalQueriesPanel.getComponent(0) instanceof JPanel);
        assertTrue(additionalQueriesPanel.getComponent(1) instanceof JPanel);

        JPanel ipAddressLookups = (JPanel) additionalQueriesPanel.getComponent(0);
        assertEquals(7, ipAddressLookups.getComponentCount());
        assertTrue(ipAddressLookups.getComponent(0) instanceof JRadioButton);
        assertTrue(ipAddressLookups.getComponent(1) instanceof JRadioButton);
        assertTrue(ipAddressLookups.getComponent(2) instanceof JRadioButton);
        assertTrue(ipAddressLookups.getComponent(3) instanceof JRadioButton);
        assertTrue(ipAddressLookups.getComponent(4) instanceof JRadioButton);
        assertTrue(ipAddressLookups.getComponent(5) instanceof JCheckBox);
        assertTrue(ipAddressLookups.getComponent(6) instanceof JRadioButton);

        JPanel miscellaneousQueries = (JPanel) additionalQueriesPanel.getComponent(1);
        assertEquals(11, miscellaneousQueries.getComponentCount());
        assertTrue(miscellaneousQueries.getComponent(0) instanceof JLabel);
        assertTrue(miscellaneousQueries.getComponent(1) instanceof JComboBox);
        assertTrue(miscellaneousQueries.getComponent(2) instanceof JLabel);
        assertTrue(miscellaneousQueries.getComponent(3) instanceof JList);
        assertTrue(miscellaneousQueries.getComponent(4) instanceof JScrollPane);
    }

    @Test
    public void testCreatePopupMenu() {
        WhoIS whois = new WhoIS();
        JPopupMenu popupMenu = whois.popupMenu;
        assertEquals(5, popupMenu.getComponentCount());
        assertTrue(popupMenu.getComponent(0) instanceof JMenuItem);
        assertTrue(popupMenu.getComponent(1) instanceof JMenuItem);
        assertTrue(popupMenu.getComponent(2) instanceof JMenuItem);
        assertTrue(popupMenu.getComponent(3) instanceof JMenuItem);
        assertTrue(popupMenu.getComponent(4) instanceof JMenuItem);
    }

    @Test
    public void testQuery() {
        WhoIS whois = new WhoIS();
        whois.query("example.com");
        // Add assertions here to test the output of the WhoISOutput class
    }

    @Test
    public void testActionPerformed() {
        WhoIS whois = new WhoIS();
        ActionEvent actionEvent = new ActionEvent(new Object(), 0, "Query");
        whois.actionPerformed(actionEvent);
        whois.startQuery();
    }
}