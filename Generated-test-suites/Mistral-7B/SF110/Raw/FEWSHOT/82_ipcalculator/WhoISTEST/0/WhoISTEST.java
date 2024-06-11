package ipac.test;

import static org.junit.Assert.*;
import ipac.WhoIS;

import java.awt.event.ActionEvent;
import java.util.Arrays;

import org.junit.Test;

public class WhoISTest {

    @Test
    public void testWhoIS() {
        WhoIS whois = new WhoIS();
        assertTrue(whois instanceof JPanel);
        assertEquals(Color.WHITE, whois.getBackground());
    }

    @Test
    public void testQueryPanel() {
        WhoIS whois = new WhoIS();
        JPanel queryPanel = whois.queryPanel();
        assertEquals(FlowLayout.class, queryPanel.getLayoutClass());
        assertTrue(queryPanel.getBackground() instanceof Color);
        assertTrue(queryPanel.getBackground().equals(Color.WHITE));
        JTextField queryTextField = (JTextField) queryPanel.getComponent(1);
        assertEquals(40, queryTextField.getColumns());
        JButton queryButton = (JButton) queryPanel.getComponent(3);
        assertEquals("Query", queryButton.getText());
        JButton resetButton = (JButton) queryPanel.getComponent(4);
        assertEquals("Reset", resetButton.getText());
    }

    @Test
    public void testAdditionalQueriesPanel() {
        WhoIS whois = new WhoIS();
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
        assertEquals(FlowLayout.class, additionalQueriesPanel.getLayoutClass());
        assertTrue(additionalQueriesPanel.getBackground() instanceof Color);
        assertTrue(additionalQueriesPanel.getBackground().equals(Color.WHITE));
        JComponent ipAddressLookups = additionalQueriesPanel.getComponent(0);
        assertTrue(ipAddressLookups instanceof JPanel);
        JComboBox inverseQueries = (JComboBox) additionalQueriesPanel.getComponent(1);
        assertEquals(27, inverseQueries.getItemCount());
        JList miscList = (JList) additionalQueriesPanel.getComponent(2);
        assertEquals(Arrays.asList(new String[]{"All", "as-block", "as-set", "aut-num", "domain", "filter-set", "inet-rtr", "inet6num", "inetnum", "irt", "key-cert", "mntner", "peering-set", "person", "role", "route", "route-set", "rtr-set"}), miscList.getModel().getElementsList());
    }

    @Test
    public void testIPAddressLookups() {
        WhoIS whois = new WhoIS();
        JPanel ipAddressLookups = whois.IPAddressLookups();
        assertEquals(BoxLayout.class, ipAddressLookups.getLayoutClass());
        assertTrue(ipAddressLookups.getBackground() instanceof Color);
        assertTrue(ipAddressLookups.getBackground().equals(Color.WHITE));
        JRadioButton lowercaseL = (JRadioButton) ipAddressLookups.getComponent(2);
        assertEquals("1st level less specific", lowercaseL.getText());
        JRadioButton uppercaseL = (JRadioButton) ipAddressLookups.getComponent(3);
        assertEquals("All less specific", uppercaseL.getText());
        JRadioButton lowercaseM = (JRadioButton) ipAddressLookups.getComponent(4);
        assertEquals("1st level more specific", lowercaseM.getText());
        JRadioButton uppercaseM = (JRadioButton) ipAddressLookups.getComponent(5);
        assertEquals("All more specific", uppercaseM.getText());
        JRadioButton lowercaseX = (JRadioButton) ipAddressLookups.getComponent(6);
        assertEquals("Exact match only", lowercaseX.getText());
        JCheckBox lowercaseD = (JCheckBox) ipAddressLookups.getComponent(7);
        assertEquals("Associated reverse domain", lowercaseD.getText());
        JRadioButton resetRadioButton = (JRadioButton) ipAddressLookups.getComponent(8);
        assertEquals("Reset Radio Button", resetRadioButton.getText());
    }

    @Test
    public void testMiscellaneousQueries() {
        WhoIS whois = new WhoIS();
        JPanel miscQueries = whois.MiscellaneousQueries();
        assertEquals(BoxLayout.class, miscQueries.getLayoutClass());
        assertTrue(miscQueries.getBackground() instanceof Color);
        assertTrue(miscQueries.getBackground().equals(Color.WHITE));
        JLabel inverseLabel = (JLabel) miscQueries.getComponent(0);
        assertEquals("Inverse Queries", inverseLabel.getText());
        JComboBox inverseQueries = (JComboBox) miscQueries.getComponent(1);
        assertEquals(28, inverseQueries.getItemCount());
        JLabel miscLabel = (JLabel) miscQueries.getComponent(2);
        assertEquals("Object Types", miscLabel.getText());
        JList miscList = (JList) miscQueries.getComponent(3);
        assertEquals(Arrays.asList(new String[]{"All", "as-block", "as-set", "aut-num", "domain", "filter-set", "inet-rtr", "inet6num", "inetnum", "irt", "key-cert", "mntner", "peering-set", "person", "role", "route", "route-set", "rtr-set"}), miscList.getModel().getElementsList());
    }

    @Test
    public void testQueryHintsPanel() {
        WhoIS whois = new WhoIS();
        JPanel queryHintsPanel = whois.queryHintsPanel();
        assertEquals(FlowLayout.class, queryHintsPanel.getLayoutClass());
        assertTrue(queryHintsPanel.getBackground() instanceof Color);
        assertTrue(queryHintsPanel.getBackground().equals(Color.WHITE));
        JPanel innerPanel = (JPanel) queryHintsPanel.getComponent(0);
        assertEquals(BoxLayout.class, innerPanel.getLayoutClass());
        assertTrue(innerPanel.getBackground() instanceof Color);
        assertTrue(innerPanel.getBackground().equals(Color.WHITE));
        JLabel queryHintsText1 = (JLabel) innerPanel.getComponent(0);
        assertEquals("Query hints ", queryHintsText1.getText());
        JLabel emptyLabel = (JLabel) innerPanel.getComponent(1);
        assertEquals("EMPTY LABEL", emptyLabel.getText());
        JLabel queryHintsText2 = (JLabel) innerPanel.getComponent(2);
        assertEquals("* Include \"AS\" in front of an AS number.", queryHintsText2.getText());
        JLabel queryHintsText3 = (JLabel) innerPanel.getComponent(3);
        assertEquals("Example: AS4808", queryHintsText3.getText());
        JLabel queryHintsText4 = (JLabel) innerPanel.getComponent(4);
        assertEquals("* Include \"-t\" (template only) or \"-v\" (template and description) in front of an object name to view the template.", queryHintsText4.getText());
        JLabel queryHintsText5 = (JLabel) innerPanel.getComponent(5);
        assertEquals("Example: -t inetnum", queryHintsText5.getText());
    }

    @Test
    public void testActionPerformed() {
        WhoIS whois = new WhoIS();
        final AtomicBoolean queryButtonCalled = new AtomicBoolean(false);
        final AtomicBoolean resetButtonCalled = new AtomicBoolean(false);
        whois.addActionListener(e -> {
            if (e.getSource() instanceof JButton && e.getSource().equals(whois.queryButton)) {
                queryButtonCalled.set(true);
            } else if (e.getSource() instanceof JButton && e.getSource().equals(whois.resetButton)) {
                resetButtonCalled.set(true);
            }
        });
        whois.queryTextField.setText("192.0.2.1");
        whois.actionPerformed(new ActionEvent(whois.queryButton, new ActionEvent.ActionEvent(new Object(), 0, "Query", new ActionEvent.ActionEvent(new Object(), 0, "Query", new ActionEvent.ActionEvent(new Object(), 0, "Query")))));
        assertTrue(queryButtonCalled.get());
        assertTrue(resetButtonCalled.equals(false));
        whois.reset();
        whois.actionPerformed(new ActionEvent(whois.resetButton, new ActionEvent.ActionEvent(new Object(), 0, "Reset", new ActionEvent.ActionEvent(new Object(), 0, "Reset"))));
        assertTrue(queryButtonCalled.equals(false));
        assertTrue(resetButtonCalled.get());
    }
}