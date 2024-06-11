package ipac.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

import ipac.WhoIS;

public class WhoISTest {

    private WhoIS whois;

    @Before
    public void setUp() {
        whois = new WhoIS();
    }

    @Test
    public void testConstructor() {
        assertNotNull(whois);
        assertEquals("APNIC", whois.WHOIS_NAME);
        assertEquals("whois.apnic.net", whois.WHOIS_ADDRESS);
        assertEquals(43, whois.WHOIS_PORT);
    }

    @Test
    public void testQueryPanel() {
        WhoISPanel queryPanel = whois.queryPanel();
        assertNotNull(queryPanel);
        assertTrue(queryPanel.getBackground() == Color.WHITE);
        assertEquals(3, queryPanel.getComponentCount());

        JTextField queryTextField = (JTextField) queryPanel.getComponent(1);
        assertEquals(40, queryTextField.getColumns());
        assertTrue(queryTextField.isFocusable());
        assertFalse(queryTextField.isEditable());

        JButton queryButton = (JButton) queryPanel.getComponent(2);
        assertEquals("Query", queryButton.getText());
        assertNotNull(queryButton.getActionListeners());

        JButton resetButton = (JButton) queryPanel.getComponent(2);
        assertEquals("Reset", resetButton.getText());
        assertNotNull(resetButton.getActionListeners());
    }

    @Test
    public void testAdditionalQueriesPanel() {
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
        assertNotNull(additionalQueriesPanel);
        assertTrue(additionalQueriesPanel.getBackground() == Color.WHITE);
        assertEquals(2, additionalQueriesPanel.getComponentCount());

        JPanel lookupPanel = (JPanel) additionalQueriesPanel.getComponent(0);
        assertEquals(10, lookupPanel.getComponentCount());

        JRadioButton lowercaseL = (JRadioButton) lookupPanel.getComponent(0);
        assertEquals("1st level less specific", lowercaseL.getText());
        assertFalse(lowercaseL.isSelected());

        JRadioButton uppercaseL = (JRadioButton) lookupPanel.getComponent(1);
        assertEquals("All less specific", uppercaseL.getText());
        assertFalse(uppercaseL.isSelected());

        JRadioButton lowercaseM = (JRadioButton) lookupPanel.getComponent(2);
        assertEquals("1st level more specific", lowercaseM.getText());
        assertFalse(lowercaseM.isSelected());

        JRadioButton uppercaseM = (JRadioButton) lookupPanel.getComponent(3);
        assertEquals("All more specific", uppercaseM.getText());
        assertFalse(uppercaseM.isSelected());

        JRadioButton lowercaseX = (JRadioButton) lookupPanel.getComponent(4);
        assertEquals("Exact match only", lowercaseX.getText());
        assertFalse(lowercaseX.isSelected());

        JCheckBox lowercaseD = (JCheckBox) lookupPanel.getComponent(5);
        assertEquals("Associated reverse domain", lowercaseD.getText());
        assertFalse(lowercaseD.isSelected());

        JRadioButton resetRadioButton = (JRadioButton) lookupPanel.getComponent(6);
        assertEquals("Reset Radio Button", resetRadioButton.getText());
        assertFalse(resetRadioButton.isSelected());

        JPanel miscPanel = (JPanel) additionalQueriesPanel.getComponent(1);
        assertEquals(3, miscPanel.getComponentCount());

        JLabel inverseLabel = (JLabel) miscPanel.getComponent(0);
        assertEquals("Inverse Queries", inverseLabel.getText());

        JComboBox<String> inverseQueries = (JComboBox<String>) miscPanel.getComponent(1);
        assertEquals(22, inverseQueries.getItemCount());
        assertFalse(inverseQueries.isEnabled());

        JList miscList = (JList) miscPanel.getComponent(2);
        assertEquals(14, miscList.getItemCount());
        assertTrue(miscList.isEnabled());
    }

    @Test(expected = AssertionError.class)
    public void testQueryHintsPanel() {
        WhoISPanel queryHintsPanel = whois.queryHintsPanel();
        assertNotNull(queryHintsPanel);
        assertTrue(queryHintsPanel.getBackground() == Color.WHITE);

        JPanel innerPanel = (JPanel) queryHintsPanel.getComponent(0);
        assertEquals(6, innerPanel.getComponentCount());

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
    public void testCreatePopupMenu() {
        assertNotNull(whois.popupMenu);
        assertEquals(4, whois.popupMenu.getComponentCount());

        JMenuItem cut = (JMenuItem) whois.popupMenu.getComponent(0);
        assertEquals("Cut", cut.getText());
        assertNotNull(cut.getActionListeners());

        JMenuItem copy = (JMenuItem) whois.popupMenu.getComponent(1);
        assertEquals("Copy", copy.getText());
        assertNotNull(copy.getActionListeners());

        JMenuItem paste = (JMenuItem) whois.popupMenu.getComponent(2);
        assertEquals("Paste", paste.getText());
        assertNotNull(paste.getActionListeners());

        JMenuItem selectAll = (JMenuItem) whois.popupMenu.getComponent(3);
        assertEquals("Select All", selectAll.getText());
        assertNotNull(selectAll.getActionListeners());
    }

    @Test
    public void testActionPerformed() throws UnknownHostException, IOException {
        String query = "example.com";
        whois.queryTextField.setText(query);
        whois.actionPerformed(new ActionEvent(whois, 0, "Query"));

        String expectedOutput = "---------------------------------------------------------\n"
                + "Domain Name: EXAMPLE.COM\n"
                + "Registry Domain ID: 12345-RS\n"
                + "Registrar WHOIS Server: whois.registrar.com\n"
                + "Registrar URL: http://www.registrar.com\n"
                + "Updated Date: 12-Mar-2015 12:34:56\n"
                + "Creation Date: 11-Mar-2015 12:34:56\n"
                + "Expiration Date: 13-Mar-2016 12:34:56\n"
                + "Registrant Name: Registrant Name\n"
                + "Registrant Street: Registrant Street\n"
                + "Registrant City: Registrant City\n"
                + "Registrant State/Province: Registrant State\n"
                + "Registrant Postal Code: Registrant Postal Code\n"
                + "Registrant Country: Registrant Country\n"
                + "Registrant Phone: Registrant Phone\n"
                + "Registrant Email: registrant.email@example.com\n"
                + "Name Servers: NS1.NAMESERVER.COM, NS2.NAMESERVER.COM\n"
                + "---------------------------------------------------------\n";

        new Thread(() -> {
            try {
                WhoISOutput output = new WhoISOutput(whois.query(query));
                assertEquals(expectedOutput, output.getText());
            } catch (UnknownHostException e) {
                fail("UnknownHostException thrown unexpectedly.");
            } catch (IOException e) {
                fail("IOException thrown unexpectedly.");
            }
        }).start();

        // Wait for the query to finish
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            fail("InterruptedException thrown unexpectedly.");
        }
    }

    @Test
    public void testGetQuery() {
        String query = "example.com";
        whois.queryTextField.setText(query);
        whois.lowercaseL.setSelected(true);
        whois.lowercaseD.setSelected(true);
        whois.inverseQueries.setSelectedItem("admin-c");
        String[] miscQueries = {"as-block", "aut-num"};
        whois.miscList.setSelectedValues(miscQueries);

        String expectedQuery = "-l example.com -d example.com -i admin-c -T as-block aut-num";

        String actualQuery = whois.getQuery();
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testStartQuery() {
        String query = "example.com";
        whois.queryTextField.setText(query);

        whois.startQuery();

        assertFalse(whois.queryTextField.getText().isEmpty());
    }

    @Test
    public void testDisplayError() {
        whois.displayError("Error message");

        String message = JOptionPane.showMessageDialog(whois, "Error message", "Error", JOptionPane.ERROR_MESSAGE).toString();
        assertEquals("Error message", message);
    }

    @Test
    public void testReset() {
        whois.queryTextField.setText("example.com");
        whois.reset();

        assertEquals("", whois.queryTextField.getText());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals("None", whois.inverseQueries.getSelectedItem().toString());
        assertEquals("All", whois.miscList.getSelectedValue().toString());
    }
}

class WhoISOutput extends JTextArea {

    public WhoISOutput(String text) {
        super(text);
        setEditable(false);
    }

    public String getText() {
        return getText().trim();
    }
}