package ipac;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class WhoISTest {

    private WhoIS whois;

    @Before
    public void setUp() {
        whois = new WhoIS();
    }

    @Test
    public void testConstructor() {
        assertNotNull(whois);
    }

    @Test
    public void testQueryPanelMethodCreation() {
        assertNotNull(whois.queryPanel());
    }

    @Test
    public void testAdditionalQueriesPanelMethodCreation() {
        assertNotNull(whois.additionalQueriesPanel());
    }

    @Test
    public void testIPAddressLookupsMethodCreation() {
        assertNotNull(whois.IPAddressLookups());
    }

    @Test
    public void testMiscellaneousQueriesMethodCreation() {
        assertNotNull(whois.MiscellaneousQueries());
    }

    @Test
    public void testQueryHintsPanelMethodCreation() {
        assertNotNull(whois.queryHintsPanel());
    }

    @Test
    public void testCreatePopupMenuMethod() {
        assertNotNull(whois.popupMenu);
    }

    @Test
    public void testActionPerformedQueryButton() throws IOException {
        String query = "example.com";
        whois.queryTextField.setText(query);
        whois.actionPerformed(new ActionEvent(whois.queryButton, 0, "Query"));
        assertTrue(whois.getQuery().startsWith("-i none " + query));
        assertTrue(whois.getQuery().endsWith("."));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testActionPerformedQueryButtonEmptyString() {
        whois.actionPerformed(new ActionEvent(whois.queryButton, 0, "Query"));
    }

    @Test
    public void testActionPerformedResetButton() {
        whois.actionPerformed(new ActionEvent(whois.resetButton, 0, "Reset"));
        assertTrue(whois.queryTextField.getText().isEmpty());
    }

    @Test
    public void testStartQueryNullQuery() {
        whois.startQuery();
        assertEquals("No Query Detected", whois.getErrorMessage());
    }

    @Test
    public void testStartQueryValidQuery() throws IOException, UnknownHostException {
        String query = "example.com";
        whois.queryTextField.setText(query);
        whois.startQuery();
        assertNotNull(whois.getQueryOutput());
        assertTrue(whois.getQueryOutput().contains(query));
    }

    @Test(expected = IOException.class)
    public void testQueryUnknownHostException() {
        whois.query("unknownhost.com");
    }

    @Test(expected = IOException.class)
    public void testQueryIOException() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("Error message".getBytes());
        Socket socket = new Socket("localhost", 0) {
            @Override
            public InputStream getInputStream() {
                return inputStream;
            }

            @Override
            public OutputStream getOutputStream() {
                return outputStream;
            }
        };
        WhoIS whoisSpy = new WhoIS() {
            @Override
            public String getErrorMessage() {
                return "Error message";
            }

            @Override
            public void query(String query) throws IOException {
                super.query(query);
            }

            @Override
            public String getQueryOutput() {
                return outputStream.toString();
            }
        };
        whoisSpy.query("example.com");
    }
}

class WhoISOutput {

    private String output;

    WhoISOutput(String output) {
        this.output = output;
    }

    String getOutput() {
        return output;
    }
}