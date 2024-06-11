package ipac.test;

import static org.junit.Assert.*;
import ipac.WhoIS;
import java.util.Arrays;

public class WhoISTest {

    @Test
    public void testWhoIS() {
        WhoIS whoIS = new WhoIS();
        assertEquals("APNIC", whoIS.WHOIS_NAME);
        assertEquals("whois.apnic.net", whoIS.WHOIS_ADDRESS);
        assertEquals(43, whoIS.WHOIS_PORT);
        
        // Test queryPanel method
        JPanel queryPanel = whoIS.queryPanel();
        assertEquals(3, queryPanel.getComponentCount());
        assertTrue(queryPanel.getComponent(0) instanceof JLabel);
        assertTrue(queryPanel.getComponent(1) instanceof JTextField);
        assertTrue(queryPanel.getComponent(2) instanceof JButton);

        // Test additionalQueriesPanel method
        JPanel additionalQueriesPanel = whoIS.additionalQueriesPanel();
        assertEquals(2, additionalQueriesPanel.getComponentCount());
        assertTrue(additionalQueriesPanel.getComponent(0) instanceof JPanel);
        assertTrue(additionalQueriesPanel.getComponent(1) instanceof JPanel);

        // Test IPAddressLookups method
        JPanel ipAddressLookups = whoIS.IPAddressLookups();
        assertEquals(9, ipAddressLookups.getComponentCount());
        assertTrue(ipAddressLookups.getComponent(0) instanceof JRadioButton);
        assertTrue(ipAddressLookups.getComponent(1) instanceof JRadioButton);
        assertTrue(ipAddressLookups.getComponent(2) instanceof JRadioButton);
        assertTrue(ipAddressLookups.getComponent(3) instanceof JRadioButton);
        assertTrue(ipAddressLookups.getComponent(4) instanceof JRadioButton);
        assertTrue(ipAddressLookups.getComponent(5) instanceof JCheckBox);
        assertTrue(ipAddressLookups.getComponent(6) instanceof JRadioButton);
        assertTrue(ipAddressLookups.getComponent(7) instanceof JRadioButton);
        assertTrue(ipAddressLookups.getComponent(8) instanceof JRadioButton);

        // Test MiscellaneousQueries method
        JPanel miscQueries = whoIS.MiscellaneousQueries();
        assertEquals(3, miscQueries.getComponentCount());
        assertTrue(miscQueries.getComponent(0) instanceof JLabel);
        assertTrue(miscQueries.getComponent(1) instanceof JComboBox);
        assertTrue(miscQueries.getComponent(2) instanceof JScrollPane);

        // Test queryHintsPanel method
        JPanel queryHintsPanel = whoIS.queryHintsPanel();
        assertEquals(3, queryHintsPanel.getComponentCount());
        assertTrue(queryHintsPanel.getComponent(0) instanceof JPanel);
        assertTrue(queryHintsPanel.getComponent(1) instanceof JLabel);
        assertTrue(queryHintsPanel.getComponent(2) instanceof JLabel);

        // Test createPopupMenu method
        WhoIS whoISInstance = new WhoIS();
        JPopupMenu popupMenu = whoISInstance.popupMenu;
        assertEquals(4, popupMenu.getComponentCount());
        assertTrue(popupMenu.getComponent(0) instanceof JMenuItem);
        assertTrue(popupMenu.getComponent(1) instanceof JMenuItem);
        assertTrue(popupMenu.getComponent(2) instanceof JMenuItem);
        assertTrue(popupMenu.getComponent(3) instanceof JMenuItem);
    }

    @Test
    public void testStartQuery() {
        WhoIS whoIS = new WhoIS();
        String query = "example.com";
        whoIS.queryTextField.setText(query);
        whoIS.startQuery();
        // Add assertions here to test the behavior of the query() method
    }

    @Test
    public void testGetQuery() {
        WhoIS whoIS = new WhoIS();
        String query = "example.com";
        whoIS.queryTextField.setText(query);
        WhoIS.JComboBox inverseQueries = new WhoIS().inverseQueries;
        inverseQueries.setSelectedIndex(1);
        WhoIS.JRadioButton lowercaseL = new WhoIS().lowercaseL;
        lowercaseL.setSelected(true);
        WhoIS.JRadioButton uppercaseM = new WhoIS().uppercaseM;
        uppercaseM.setSelected(true);
        WhoIS.JCheckBox lowercaseD = new WhoIS().lowercaseD;
        lowercaseD.setSelected(true);
        WhoIS.JList miscList = new WhoIS().miscList;
        Object[] selectedValues = {"as-set"};
        miscList.setSelectedValues(selectedValues);

        String expectedQuery = "-i tech-c -l example.com -L -m all -T as-set -d";
        String actualQuery = whoIS.getQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testReset() {
        WhoIS whoIS = new WhoIS();
        whoIS.reset();
        String expectedQuery = "";
        String actualQuery = whoIs.getQuery();
        assertEquals(expectedQuery, actualQuery);
    }
}