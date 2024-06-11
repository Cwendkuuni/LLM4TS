package ipac;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class WhoISTest {

    private WhoIS whois;

    @Before
    public void setup() {
        whois = new WhoIS();
    }

    @Test
    public void testWhoISConstructor() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois);
    }

    @Test
    public void testQueryPanelCreation() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois.queryPanel());
    }

    @Test
    public void testAdditionalQueriesPanelCreation() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois.additionalQueriesPanel());
    }

    @Test
    public void testIPAddressLookupsPanelCreation() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois.IPAddressLookups());
    }

    @Test
    public void testMiscellaneousQueriesPanelCreation() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois.MiscellaneousQueries());
    }

    @Test
    public void testQueryHintsPanelCreation() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois.queryHintsPanel());
    }

    @Test(expected = NullPointerException.class)
    public void testQueryNullQuery() {
        WhoIS whois = new WhoIS();
        whois.startQuery(null);
    }

    @Test
    public void testStartQueryWithValidQuery() {
        WhoIS whois = new WhoIS();
        String query = "example.com";
        whois.queryTextField.setText(query);
        whois.startQuery();
        // Verify that query method is called with the correct query
        String expectedQuery = "-i None -l " + query;
        assertEquals(expectedQuery, whois.getQuery());
        // Add more assertions here to verify the behavior of the WhoIS class after a successful query
    }

    @Test(expected = UnknownHostException.class)
    public void testUnknownHostException() {
        WhoIS whois = new WhoIS();
        String query = "unknown.host";
        whois.queryTextField.setText(query);
        whois.startQuery();
    }

    @Test(expected = IOException.class)
    public void testIOException() {
        WhoIS whois = new WhoIS();
        String query = "example.com";
        whois.queryTextField.setText(query);
        when(mockSocket.getInputStream()).thenThrow(new IOException("Exception occurred"));
        whois.startQuery();
    }
}