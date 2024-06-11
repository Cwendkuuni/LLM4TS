package ipac.test;

import static org.junit.Assert.*;
import ipac.WhoIS;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;

public class WhoISTest {

    @Test
    public void testWhoISInitialization() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois);
        assertEquals(Color.WHITE, whois.getBackground());
    }

    @Test
    public void testQueryPanelInitialization() {
        WhoIS whois = new WhoIS();
        JPanel queryPanel = whois.queryPanel();
        assertNotNull(queryPanel);
        assertEquals(FlowLayout.class, queryPanel.getLayoutClass());

        JTextField queryTextField = whois.queryTextField;
        assertNotNull(queryTextField);
        assertEquals(40, queryTextField.getColumns());
        assertEquals("Enter Whois database object you would like to query", queryTextField.getToolTipText());

        JButton queryButton = whois.queryButton;
        assertNotNull(queryButton);
        assertEquals("Query", queryButton.getText());
        queryButton.addActionListener(whois);

        JButton resetButton = whois.resetButton;
        assertNotNull(resetButton);
        assertEquals("Reset", resetButton.getText());
        resetButton.addActionListener(whois);
    }

    @Test
    public void testAdditionalQueriesPanelInitialization() {
        WhoIS whois = new WhoIS();
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
        assertNotNull(additionalQueriesPanel);
        assertEquals(FlowLayout.class, additionalQueriesPanel.getLayoutClass());

        JPanel LookupPanel = whois.IPAddressLookups();
        assertNotNull(LookupPanel);
        assertEquals(BoxLayout.class, LookupPanel.getLayoutClass());

        JRadioButton lowercaseL = whois.lowercaseL;
        assertTrue(lowercaseL.isSelected());

        JRadioButton uppercaseL = whois.uppercaseL;
        assertFalse(uppercaseL.isSelected());

        JRadioButton lowercaseM = whois.lowercaseM;
        assertFalse(lowercaseM.isSelected());

        JRadioButton uppercaseM = whois.uppercaseM;
        assertFalse(uppercaseM.isSelected());

        JRadioButton lowercaseX = whois.lowercaseX;
        assertFalse(lowercaseX.isSelected());

        JCheckBox lowercaseD = whois.lowercaseD;
        assertFalse(lowercaseD.isSelected());

        JRadioButton resetRadioButton = whois.resetRadioButton;
        assertTrue(resetRadioButton.isSelected());
    }

    @Test
    public void testMiscellaneousQueriesPanelInitialization() {
        WhoIS whois = new WhoIS();
        JPanel MiscPanel = whois.MiscellaneousQueries();
        assertNotNull(MiscPanel);
        assertEquals(BoxLayout.class, MiscPanel.getLayoutClass());

        JLabel inverseLabel = whois.inverseLabel;
        assertNotNull(inverseLabel);
        assertEquals("Inverse Queries", inverseLabel.getText());

        JComboBox inverseQueries = whois.inverseQueries;
        assertNotNull(inverseQueries);
        assertEquals(36, inverseQueries.getItemCount());
        assertEquals("None", inverseQueries.getSelectedItem());

        JLabel miscLabel = whois.miscLabel;
        assertNotNull(miscLabel);
        assertEquals("Object Types", miscLabel.getText());

        JList miscList = whois.miscList;
        assertNotNull(miscList);
        assertEquals("All", miscList.getSelectedValue());

        String[] items = whois.miscList.getModel().toArray(new String[0]);
        assertArrayEquals(new String[]{"All", "as-block", "as-set", "aut-num", "domain", "filter-set", "inet-rtr", "inet6num", "inetnum", "irt", "key-cert", "mntner", "peering-set", "person", "role", "route", "route-set", "rtr-set"}, items);
    }

    @Test
    public void testQueryHintsPanelInitialization() {
        WhoIS whois = new WhoIS();
        JPanel queryHintsPanel = whois.queryHintsPanel();
        assertNotNull(queryHintsPanel);
        assertEquals(FlowLayout.class, queryHintsPanel.getLayoutClass());

        JPanel innerPanel = whois.queryHintsInnerPanel();
        assertNotNull(innerPanel);
        assertEquals(BoxLayout.class, innerPanel.getLayoutClass());

        JLabel queryHintsText1 = whois.queryHintsText1;
        assertNotNull(queryHintsText1);
        assertEquals("Query hints", queryHintsText1.getText());

        JLabel queryHintsText2 = whois.queryHintsText2;
        assertNotNull(queryHintsText2);
        assertEquals("* Include \"AS\" in front of an AS number.", queryHintsText2.getText());

        JLabel queryHintsText3 = whois.queryHintsText3;
        assertNotNull(queryHintsText3);
        assertEquals("Example: AS4808", queryHintsText3.getText());

        JLabel queryHintsText4 = whois.queryHintsText4;
        assertNotNull(queryHintsText4);
        assertEquals("* Include \"-t\" (template only) or \"-v\" (template and description) in front of an object name to view the template.", queryHintsText4.getText());

        JLabel queryHintsText5 = whois.queryHintsText5;
        assertNotNull(queryHintsText5);
        assertEquals("Example: -t inetnum", queryHintsText5.getText());
    }

    @Test
    public void testCreatePopupMenu() {
        WhoIS whois = new WhoIS();
        JPopupMenu popupMenu = whois.popupMenu;
        assertNotNull(popupMenu);

        JMenuItem cut = whois.cutMenuItem;
        assertNotNull(cut);
        assertEquals("Cut", cut.getText());

        JMenuItem copy = whois.copyMenuItem;
        assertNotNull(copy);
        assertEquals("Copy", copy.getText());

        JMenuItem paste = whois.pasteMenuItem;
        assertNotNull(paste);
        assertEquals("Paste", paste.getText());

        JMenuItem selectAll = whois.selectAllMenuItem;
        assertNotNull(selectAll);
        assertEquals("Select All", selectAll.getText());

        ActionListener actionListener = whois.popupMenuActionListener;
        assertNotNull(actionListener);
    }

    @Test
    public void testActionPerformed() {
        WhoIS whois = new WhoIS();
        ActionEvent actionEvent = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "Query");
        whois.actionPerformed(actionEvent);
        whois.startQuery();
    }

    @Test
    public void testGetQuery() {
        WhoIS whois = new WhoIS();

        String query = "example.com";
        whois.queryTextField.setText(query);
        whois.inverseQueries.setSelectedIndex(1);
        whois.lowercaseL.setSelected(true);
        whois.lowercaseD.setSelected(true);

        String expectedQuery = "-i admin-c -l example.com -d";
        String actualQuery = whois.getQuery();
        assertEquals(expectedQuery, actualQuery);
    }
}