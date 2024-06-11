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
    public void testWhoIS() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois);
        assertEquals("APNIC", whois.WHOIS_NAME);
        assertEquals("whois.apnic.net", whois.WHOIS_ADDRESS);
        assertEquals(43, whois.WHOIS_PORT);

        JTextField queryTextField = whois.queryTextField;
        JButton queryButton = whois.queryButton;
        JButton resetButton = whois.resetButton;

        // Test queryTextField
        assertEquals(40, queryTextField.getColumns());
        assertTrue(queryTextField.isFocusable());
        assertFalse(queryTextField.isEditable());

        // Test queryButton
        assertNotNull(queryButton);
        assertFalse(queryButton.isEnabled());

        // Test resetButton
        assertNotNull(resetButton);
        assertTrue(resetButton.isEnabled());

        // Test queryPanel()
        JPanel queryPanel = whois.queryPanel();
        assertNotNull(queryPanel);
        assertTrue(queryPanel.getComponentCount() > 0);

        // Test additionalQueriesPanel()
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
        assertNotNull(additionalQueriesPanel);
        assertTrue(additionalQueriesPanel.getComponentCount() > 0);

        // Test IPAddressLookups()
        JPanel lookupPanel = whois.IPAddressLookups();
        assertNotNull(lookupPanel);
        assertEquals(7, lookupPanel.getComponentCount());

        // Test MiscellaneousQueries()
        JPanel miscPanel = whois.MiscellaneousQueries();
        assertNotNull(miscPanel);
        assertEquals(2, miscPanel.getComponentCount());

        // Test queryHintsPanel()
        JPanel queryHintsPanel = whois.queryHintsPanel();
        assertNotNull(queryHintsPanel);
        assertEquals(5, queryHintsPanel.getComponentCount());

        // Test createPopupMenu()
        JPopupMenu popupMenu = whois.popupMenu;
        assertNotNull(popupMenu);
        assertEquals(4, popupMenu.getComponentCount());

        JMenuItem cut = (JMenuItem) popupMenu.getComponent(0);
        JMenuItem copy = (JMenuItem) popupMenu.getComponent(1);
        JMenuItem paste = (JMenuItem) popupMenu.getComponent(2);
        JMenuItem selectAll = (JMenuItem) popupMenu.getComponent(3);

        // Test actionPerformed()
        MockActionEvent event = new MockActionEvent(queryButton, ActionEvent.ACTION_PERFORMED);
        whois.actionPerformed(event);
        assertTrue(whois.isQueryStarted());

        // Test startQuery()
        String query = "example.com";
        whois.queryTextField.setText(query);
        whois.startQuery();
        assertTrue(whois.isQueryStarted());

        // Test getQuery()
        String expectedQuery = "-i None -l -m " + query;
        String actualQuery = whois.getQuery();
        assertEquals(expectedQuery, actualQuery);

        // Test query()
        try {
            Socket connection = new Socket("127.0.0.1", 80);
            PrintStream out = new PrintStream(connection.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            out.println(expectedQuery);

            StringBuilder output = new StringBuilder();
            while ((line = in.readLine()) != null) {
                output.append(line).append("\n");
            }
            in.close();
            out.close();
            connection.close();

            String expectedOutput = "WHOIS Server Version 2.2\r\n" +
                    "Domain Name: example.com\r\n" +
                    "..."; // More lines are expected, but not relevant for this test
            String actualOutput = whois.parseOutput(output.toString());
            assertEquals(expectedOutput, actualOutput);
        } catch (UnknownHostException | IOException e) {
            fail(e.getMessage());
        }

        // Test displayError()
        whois.displayError("Test Error");
        JOptionPane.showMessageDialog(new JFrame(), "Error: Test Error", "Error", JOptionPane.ERROR_MESSAGE);

        // Test reset()
        whois.reset();
        assertEquals("", whois.queryTextField.getText());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals("None", whois.inverseQueries.getSelectedItem().toString());
        assertEquals("All", whois.miscList.getSelectedValue());
    }

    private static class MockActionEvent extends ActionEvent {
        public MockActionEvent(Component source, int id) {
            super(source, id, System.currentTimeMillis(), 0, null);
        }
    }
}