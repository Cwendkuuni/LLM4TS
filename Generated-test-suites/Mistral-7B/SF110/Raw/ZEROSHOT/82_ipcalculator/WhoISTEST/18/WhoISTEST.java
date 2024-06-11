package ipac.whois;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

import ipac.WhoIS;
import ipac.WhoISOutput;

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
        assertTrue(whois.queryPanel() instanceof JPanel);
        assertNotNull(whois.queryTextField);
        assertNotNull(whois.queryButton);
        assertNotNull(whois.resetButton);
    }

    @Test
    public void testAdditionalQueriesPanel() {
        assertTrue(whois.additionalQueriesPanel() instanceof JPanel);
        assertNotNull(whois.IPAddressLookups());
        assertNotNull(whois.MiscellaneousQueries());
    }

    @Test
    public void testIPAddressLookups() {
        assertTrue(whois.IPAddressLookups() instanceof JPanel);
        assertNotNull(whois.lowercaseL);
        assertNotNull(whois.uppercaseL);
        assertNotNull(whois.lowercaseM);
        assertNotNull(whois.uppercaseM);
        assertNotNull(whois.lowercaseX);
        assertNotNull(whois.lowercaseD);
        assertNotNull(whois.resetRadioButton);
    }

    @Test
    public void testMiscellaneousQueries() {
        assertTrue(whois.MiscellaneousQueries() instanceof JPanel);
        assertNotNull(whois.inverseQueries);
        assertNotNull(whois.miscList);
    }

    @Test
    public void testQueryHintsPanel() {
        assertTrue(whois.queryHintsPanel() instanceof JPanel);
    }

    @Test
    public void testCreatePopupMenu() {
        assertNotNull(whois.popupMenu);
        assertTrue(whois.popupMenu.getComponentCount() > 0);
    }

    @Test
    public void testActionPerformed() {
        WhoISOutput outputMock = new WhoISOutput("Test output");
        System.setIn(new ByteArrayInputStream("Test output".getBytes()));
        InputStream in = System.in;
        whois.setInput(in);
        whois.setOutput(outputMock);

        whois.actionPerformed(new ActionEvent(whois.queryButton, ActionEvent.ACTION_PERFORMED, ""));

        assertEquals("Test output", outputMock.getOutput());
    }

    @Test
    public void testStartQuery() throws UnknownHostException, IOException {
        Socket socketMock = mock(Socket.class);
        when(socketMock.getOutputStream()).thenReturn(mock(PrintStream.class));
        when(socketMock.getInputStream()).thenReturn(mock(BufferedReader.class));
        when(socketMock.isClosed()).thenReturn(false);

        WhoISOutput outputMock = mock(WhoISOutput.class);

        whois.setSocket(socketMock);
        whois.setOutput(outputMock);

        whois.startQuery();

        verify(socketMock).connect(new InetSocketAddress("whois.apnic.net", 43));
        verify(socketMock).getOutputStream().println("TestQuery");
        verify(socketMock).getInputStream().close();
        verifyZeroInteractions(outputMock);
    }

    @Test(expected = UnknownHostException.class)
    public void testStartQueryUnknownHostException() throws UnknownHostException, IOException {
        WhoISOutput outputMock = mock(WhoISOutput.class);
        whois.setOutput(outputMock);

        whois.startQuery();

        verifyZeroInteractions(outputMock);
        throw new UnknownHostException();
    }

    @Test(expected = IOException.class)
    public void testStartQueryIOException() throws UnknownHostException, IOException {
        Socket socketMock = mock(Socket.class);
        when(socketMock.getOutputStream()).thenReturn(mock(PrintStream.class));
        when(socketMock.getInputStream()).thenThrow(new IOException());

        WhoISOutput outputMock = mock(WhoISOutput.class);

        whois.setSocket(socketMock);
        whois.setOutput(outputMock);

        whois.startQuery();

        verify(socketMock).connect(new InetSocketAddress("whois.apnic.net", 43));
        verify(socketMock).getInputStream().close();
        throw new IOException();
    }

    @Test
    public void testGetQuery() {
        String query = "TestQuery";
        whois.queryTextField.setText(query);
        whois.inverseQueries.setSelectedIndex(1);
        whois.lowercaseL.setSelected(true);
        whois.lowercaseM.setSelected(true);
        whois.lowercaseX.setSelected(true);
        whois.lowercaseD.setSelected(true);
        Object[] selected = {"All", "as-block"};
        whois.miscList.setSelectedValues(selected);

        String expected = "-i admin-c -l TestQuery -m 1st level more specific -M All -x TestQuery -d TestQuery -T all as-block";
        assertEquals(expected, whois.getQuery());
    }

    @Test
    public void testReset() {
        whois.queryTextField.setText("TestQuery");

        whois.reset();

        assertEquals("", whois.queryTextField.getText());
    }
}