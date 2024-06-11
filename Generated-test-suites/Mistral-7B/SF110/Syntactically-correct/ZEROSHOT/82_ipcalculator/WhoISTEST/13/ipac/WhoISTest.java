package ipac.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;

public class WhoISTest {

    private WhoIS whois;

    @Before
    public void setUp() {
        whois = new WhoIS();
    }

    @Test
    public void testWhoISConstruction() {
        assertNotNull(whois);
        assertEquals("APNIC", whois.WHOIS_NAME);
        assertEquals("whois.apnic.net", whois.WHOIS_ADDRESS);
        assertEquals(43, whois.WHOIS_PORT);
    }

    @Test
    public void testQueryPanelCreation() {
        assertNotNull(whois.queryPanel());
        assertTrue(whois.queryTextField instanceof JTextField);
        assertTrue(whois.queryButton instanceof JButton);
        assertTrue(whois.resetButton instanceof JButton);
    }

    @Test
    public void testAdditionalQueriesPanelCreation() {
        assertNotNull(whois.additionalQueriesPanel());
        assertTrue(whois.IPAddressLookups() instanceof JPanel);
        assertTrue(whois.MiscellaneousQueries() instanceof JPanel);
    }

    @Test
    public void testIPAddressLookupsCreation() {
        assertNotNull(whois.IPAddressLookups());
        assertTrue(whois.lowercaseL instanceof JRadioButton);
        assertTrue(whois.uppercaseL instanceof JRadioButton);
        assertTrue(whois.lowercaseM instanceof JRadioButton);
        assertTrue(whois.uppercaseM instanceof JRadioButton);
        assertTrue(whois.lowercaseX instanceof JRadioButton);
        assertTrue(whois.resetRadioButton instanceof JRadioButton);
        assertTrue(whois.radioButtonGroup instanceof ButtonGroup);
    }

    @Test
    public void testMiscellaneousQueriesCreation() {
        assertNotNull(whois.MiscellaneousQueries());
        assertTrue(whois.inverseQueries instanceof JComboBox);
        assertTrue(whois.miscList instanceof JList);
    }

    @Test
    public void testQueryHintsPanelCreation() {
        assertNotNull(whois.queryHintsPanel());
    }

    @Test
    public void testCreatePopupMenu() {
        assertNotNull(whois.popupMenu);
        assertTrue(whois.popupMenu instanceof JPopupMenu);
    }

    @Test
    public void testActionPerformedQueryButton() throws IOException, UnknownHostException {
        String query = "example.com";
        whois.queryTextField.setText(query);
        MockEvent mEvent = new MockEvent(MouseEvent.MOUSE_CLICKED, 1, 1, 1, 1, queryTextField, null);
        whois.queryButton.dispatchEvent(mEvent);

        String expectedOutput = "..."; // Replace this with the expected output from the whois query
        String actualOutput = "";

        try (Socket connection = new Socket("whois.apnic.net", 43);
             PrintStream out = new PrintStream(connection.getOutputStream());
             BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {

            out.println(whois.getQuery());
            String line = "";
            while ((line = in.readLine()) != null) {
                actualOutput += line + "\n";
            }
        }

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testActionPerformedResetButton() {
        whois.reset();
        assertTrue(whois.queryTextField.getText().isEmpty());
        assertTrue(whois.resetRadioButton.isSelected());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }
}