package ipac.test;

import static org.junit.Assert.*;
import ipac.WhoIS;

import java.awt.event.ActionEvent;
import java.util.Arrays;

import org.junit.Test;

public class WhoISTest {

    @Test
    public void testConstructor() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois);
        assertEquals("APNIC", whois.WHOIS_NAME);
        assertEquals("whois.apnic.net", whois.WHOIS_ADDRESS);
        assertEquals(43, whois.WHOIS_PORT);
    }

    @Test
    public void testQueryPanel() {
        WhoIS whois = new WhoIS();
        JPanel queryPanel = whois.queryPanel();
        assertNotNull(queryPanel);
        assertEquals(3, queryPanel.getComponentCount());

        JTextField queryTextField = (JTextField) queryPanel.getComponent(1);
        assertNotNull(queryTextField);
        assertEquals(40, queryTextField.getColumns());

        JButton queryButton = (JButton) queryPanel.getComponent(2);
        assertNotNull(queryButton);
        assertEquals("Query", queryButton.getText());

        JButton resetButton = (JButton) queryPanel.getComponent(2);
        assertNotNull(resetButton);
        assertEquals("Reset", resetButton.getText());
    }

    @Test
    public void testAdditionalQueriesPanel() {
        WhoIS whois = new WhoIS();
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
        assertNotNull(additionalQueriesPanel);
        assertEquals(2, additionalQueriesPanel.getComponentCount());

        JPanel lookupPanel = (JPanel) additionalQueriesPanel.getComponent(0);
        assertNotNull(lookupPanel);
        assertEquals(8, lookupPanel.getComponentCount());

        JRadioButton lowercaseL = (JRadioButton) lookupPanel.getComponent(0);
        assertNotNull(lowercaseL);
        assertEquals("1st level less specific", lowercaseL.getText());

        JRadioButton uppercaseL = (JRadioButton) lookupPanel.getComponent(1);
        assertNotNull(uppercaseL);
        assertEquals("All less specific", uppercaseL.getText());

        JRadioButton lowercaseM = (JRadioButton) lookupPanel.getComponent(2);
        assertNotNull(lowercaseM);
        assertEquals("1st level more specific", lowercaseM.getText());

        JRadioButton uppercaseM = (JRadioButton) lookupPanel.getComponent(3);
        assertNotNull(uppercaseM);
        assertEquals("All more specific", uppercaseM.getText());

        JRadioButton lowercaseX = (JRadioButton) lookupPanel.getComponent(4);
        assertNotNull(lowercaseX);
        assertEquals("Exact match only", lowercaseX.getText());

        JRadioButton resetRadioButton = (JRadioButton) lookupPanel.getComponent(5);
        assertNotNull(resetRadioButton);
        assertEquals("Reset Radio Button", resetRadioButton.getText());

        JCheckBox lowercaseD = (JCheckBox) lookupPanel.getComponent(6);
        assertNotNull(lowercaseD);
        assertEquals("Associated reverse domain", lowercaseD.getText());

        JPanel miscPanel = (JPanel) additionalQueriesPanel.getComponent(1);
        assertNotNull(miscPanel);
        assertEquals(3, miscPanel.getComponentCount());

        JLabel inverseLabel = (JLabel) miscPanel.getComponent(0);
        assertNotNull(inverseLabel);
        assertEquals("Inverse Queries", inverseLabel.getText());

        JComboBox inverseQueries = (JComboBox) miscPanel.getComponent(1);
        assertNotNull(inverseQueries);
        assertEquals(20, inverseQueries.getItemCount());
        assertEquals("None", inverseQueries.getSelectedItem().toString());

        JLabel miscLabel = (JLabel) miscPanel.getComponent(2);
        assertNotNull(miscLabel);
        assertEquals("Object Types", miscLabel.getText());

        JList miscList = (JList) miscPanel.getComponent(3);
        assertNotNull(miscList);
        assertEquals(15, miscList.getItemCount());
        assertArrayEquals(new String[]{"All", "as-block", "as-set", "aut-num", "domain", "filter-set", "inet-rtr", "inet6num", "inetnum", "irt", "key-cert", "mntner", "peering-set", "person", "role", "route", "route-set", "rtr-set"}, miscList.getModel().toArray(new String[0]));
    }

    @Test
    public void testQueryHintsPanel() {
        WhoIS whois = new WhoIS();
        JPanel queryHintsPanel = whois.queryHintsPanel();
        assertNotNull(queryHintsPanel);

        JPanel innerPanel = (JPanel) queryHintsPanel.getComponent(0);
        assertNotNull(innerPanel);
        assertEquals(6, innerPanel.getComponentCount());

        JLabel queryHintsText1 = (JLabel) innerPanel.getComponent(0);
        assertNotNull(queryHintsText1);
        assertEquals("Query hints ", queryHintsText1.getText());

        JLabel emptyLabel = (JLabel) innerPanel.getComponent(1);
        assertNotNull(emptyLabel);
        assertEquals("EMPTY LABEL", emptyLabel.getText());

        JLabel queryHintsText2 = (JLabel) innerPanel.getComponent(2);
        assertNotNull(queryHintsText2);
        assertEquals("* Include \"AS\" in front of an AS number.", queryHintsText2.getText());

        JLabel queryHintsText3 = (JLabel) innerPanel.getComponent(3);
        assertNotNull(queryHintsText3);
        assertEquals("Example: AS4808", queryHintsText3.getText());

        JLabel queryHintsText4 = (JLabel) innerPanel.getComponent(4);
        assertNotNull(queryHintsText4);
        assertEquals("* Include \"-t\" (template only) or \"-v\" (template and description) in front of an object name to view the template.", queryHintsText4.getText());

        JLabel queryHintsText5 = (JLabel) innerPanel.getComponent(5);
        assertNotNull(queryHintsText5);
        assertEquals("Example: -t inetnum", queryHintsText5.getText());
    }

    @Test
    public void testCreatePopupMenu() {
        WhoIS whois = new WhoIS();
        JPopupMenu popupMenu = whois.popupMenu;
        assertNotNull(popupMenu);

        JMenuItem cut = popupMenu.getComponent(0);
        assertNotNull(cut);
        assertEquals("Cut", cut.getText());

        JMenuItem copy = popupMenu.getComponent(1);
        assertNotNull(copy);
        assertEquals("Copy", copy.getText());

        JMenuItem paste = popupMenu.getComponent(2);
        assertNotNull(paste);
        assertEquals("Paste", paste.getText());

        JMenuItem selectAll = popupMenu.getComponent(3);
        assertNotNull(selectAll);
        assertEquals("Select All", selectAll.getText());
    }

    @Test
    public void testActionPerformed() {
        WhoIS whois = new WhoIS();
        ActionEvent actionEvent = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "");
        whois.actionPerformed(actionEvent);
        fail("Method should not be called directly");
    }

    @Test
    public void testStartQuery() {
        WhoIS whois = new WhoIS();
        String query = "example";
        whois.queryTextField.setText(query);
        whois.startQuery();

        try {
            whois.query(query);
        } catch (UnknownHostException | IOException e) {
            fail("Exception should not be thrown");
        }

        String expectedOutput = "WhoIS Output:\n"; // Replace this with the expected output from the query
        String output = whois.getQueryOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testDisplayError() {
        WhoIS whois = new WhoIS();
        whois.displayError("Test Error");
        JOptionPane.showMessageDialog(whois, "Test Error", "Error", 0);
    }

    @Test
    public void testQuery() {
        WhoIS whois = new WhoIS();
        String query = "example";

        try {
            whois.query(query);
            fail("Expected IOException or UnknownHostException");
        } catch (UnknownHostException | IOException e) {
            // Expected
        }
    }

    @Test
    public void testGetQuery() {
        WhoIS whois = new WhoIS();
        String query = "example";
        whois.queryTextField.setText(query);
        whois.inverseQueries.setSelectedIndex(1);
        whois.lowercaseL.setSelected(true);
        whois.lowercaseD.setSelected(true);
        String[] miscQueries = {"as-set", "mntner"};
        for (String miscQuery : miscQueries) {
            WhoIS.JList miscList = whois.miscList;
            miscList.setSelectedValue(miscQuery, true);
        }

        String expectedQuery = "-i admin-c,tech-c -l example -d example -T as-set,mntner";
        String actualQuery = whois.getQuery();
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.reset();

        assertTrue("queryTextField should be empty", whois.queryTextField.getText().isEmpty());
        assertFalse("lowercaseD should be unselected", whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }
}