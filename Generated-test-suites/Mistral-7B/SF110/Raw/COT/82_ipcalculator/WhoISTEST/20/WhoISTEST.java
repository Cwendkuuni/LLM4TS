package ipac;

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
    public void setup() {
        whois = new WhoIS();
    }

    @Test
    public void testWhoISConstructor() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois.WHOIS_NAME);
        assertNotNull(whois.WHOIS_ADDRESS);
        assertEquals(whois.WHOIS_PORT, 43);
        assertNotNull(whois.queryTextField);
        assertNotNull(whois.queryButton);
        assertNotNull(whois.resetButton);
    }

    @Test
    public void testQueryPanelCreation() {
        JPanel queryPanel = whois.queryPanel();
        assertNotNull(queryPanel);
        assertTrue(queryPanel.getComponentCount() > 0);
        assertTrue(queryPanel.getComponent(0) instanceof JLabel);
        assertTrue(queryPanel.getComponent(1) instanceof JTextField);
        assertTrue(queryPanel.getComponent(2) instanceof JButton);
        assertTrue(queryPanel.getComponent(3) instanceof JButton);
    }

    @Test(expected = NullPointerException.class)
    public void testStartQueryWithEmptyQuery() {
        whois.startQuery();
    }

    @Test
    public void testStartQueryWithValidQuery() {
        whois.queryTextField.setText("example.com");
        whois.startQuery();
        // Add assertions to verify the query output and behavior here
    }

    @Test(expected = UnknownHostException.class)
    public void testQueryWithInvalidHost() {
        whois.queryTextField.setText("invalid-host.com");
        whois.startQuery();
    }

    @Test(expected = IOException.class)
    public void testQueryWithServerConnectionError() {
        whois.queryTextField.setText("who.is");
        whois.startQuery();
    }

    @Test
    public void testGetQueryWithDefaultSettings() {
        whois.queryTextField.setText("example.com");
        String query = whois.getQuery();
        assertEquals(query, "example.com");
    }

    @Test
    public void testGetQueryWithInverseQueries() {
        whois.inverseQueries.setSelectedIndex(1);
        whois.queryTextField.setText("example.com");
        String query = whois.getQuery();
        assertEquals(query, "-i admin-c example.com");
    }

    @Test
    public void testGetQueryWithLowerCaseOption() {
        whois.lowercaseL.setSelected(true);
        whois.queryTextField.setText("example.com");
        String query = whois.getQuery();
        assertEquals(query, "-l example.com");
    }

    // Add more test cases for other query options and exceptions as needed

    @Test
    public void testReset() {
        whois.queryTextField.setText("example.com");
        whois.startQuery();
        whois.reset();
        assertTrue(whois.queryTextField.getText().isEmpty());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(whois.inverseQueries.getSelectedIndex(), 0);
        assertEquals(whois.miscList.getSelectedIndex(), 0);
    }
}