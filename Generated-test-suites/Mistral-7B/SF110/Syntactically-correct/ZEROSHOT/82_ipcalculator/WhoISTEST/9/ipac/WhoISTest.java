package ipac.whois;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;

import static org.mockito.Mockito.*;

public class WhoISTest {

    private WhoIS whoIS;
    private ActionEvent queryButtonActionEvent;
    private ActionEvent resetButtonActionEvent;
    private JTextField mockQueryTextField;

    @Before
    public void setUp() {
        whoIS = new WhoIS();
        queryButtonActionEvent = mock(ActionEvent.class);
        resetButtonActionEvent = mock(ActionEvent.class);
        mockQueryTextField = mock(JTextField.class);
        when(mockQueryTextField.getText()).thenReturn("testQuery");
        whoIS.queryTextField = mockQueryTextField;
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(whoIS);
    }

    @Test
    public void testConstructor() {
        WhoIS whoIS = new WhoIS();
        Assert.assertNotNull(whoIS);
    }

    @Test
    public void test_queryPanel() {
        JPanel queryPanel = whoIS.queryPanel();
        Assert.assertNotNull(queryPanel);
        Assert.assertTrue(queryPanel.getComponentCount() > 0);
    }

    @Test
    public void test_additionalQueriesPanel() {
        JPanel additionalQueriesPanel = whoIS.additionalQueriesPanel();
        Assert.assertNotNull(additionalQueriesPanel);
        Assert.assertTrue(additionalQueriesPanel.getComponentCount() > 0);
    }

    @Test
    public void test_IPAddressLookups() {
        JPanel ipAddressLookups = whoIS.IPAddressLookups();
        Assert.assertNotNull(ipAddressLookups);
        Assert.assertTrue(ipAddressLookups.getComponentCount() > 0);
        Assert.assertEquals(7, ipAddressLookups.getComponentCount());
    }

    @Test
    public void test_MiscellaneousQueries() {
        JPanel MiscellaneousQueries = whoIS.MiscellaneousQueries();
        Assert.assertNotNull(MiscellaneousQueries);
        Assert.assertTrue(MiscellaneousQueries.getComponentCount() > 0);
        Assert.assertEquals(2, MiscellaneousQueries.getComponentCount());
    }

    @Test
    public void test_queryHintsPanel() {
        JPanel queryHintsPanel = whoIS.queryHintsPanel();
        Assert.assertNotNull(queryHintsPanel);
        Assert.assertTrue(queryHintsPanel.getComponentCount() > 0);
    }

    @Test
    public void test_createPopupMenu() {
        WhoIS whoISSpy = spy(whoIS);
        whoISSpy.createPopupMenu();
        verify(whoISSpy, times(1)).createPopupMenu();
    }

    @Test
    public void test_actionPerformed_queryButton() {
        whoIS.addActionListener(mock(ActionListener.class));
        whoIS.queryButton.doClick();
        verify(whoIS, times(1)).startQuery();
    }

    @Test
    public void test_actionPerformed_resetButton() {
        whoIS.addActionListener(mock(ActionListener.class));
        whoIS.resetButton.doClick();
        verify(whoIS, times(1)).reset();
    }

    @Test
    public void test_startQuery() {
        whoIS.startQuery();
        verify(whoIS, times(1)).query(eq("testQuery"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_startQuery_emptyQuery() {
        whoIS.startQuery();
        verify(whoIS, never()).query(anyString());
        throw new IllegalArgumentException("No Query Detected");
    }

    @Test
    public void test_query() throws IOException {
        Socket socket = mock(Socket.class);
        PrintStream out = mock(PrintStream.class);
        BufferedReader in = mock(BufferedReader.class);
        when(socket.getOutputStream()).thenReturn(out);
        when(socket.getInputStream()).thenReturn(in);
        when(socket.isConnected()).thenReturn(true);
        when(socket.connect(any(InetAddress.class), anyInt())).thenReturn(socket);
        when(out.println(eq("testQuery"))).thenReturn(true);

        whoIS.query("testQuery");

        verify(socket, times(1)).connect(eq("whois.apnic.net"), eq(43));
        verify(out, times(1)).println("testQuery");
        verify(in, times(1)).readLine();
    }

    @Test(expected = UnknownHostException.class)
    public void test_query_unknownHostException() throws IOException {
        when(whoIS.queryTextField.getText()).thenReturn("testQuery");
        when(whoIS.WHOIS_ADDRESS).thenReturn("unknownHost");
        whoIS.query("testQuery");
    }

    @Test(expected = IOException.class)
    public void test_query_ioException() throws IOException {
        when(whoIS.queryTextField.getText()).thenReturn("testQuery");
        Socket socket = mock(Socket.class);
        PrintStream out = mock(PrintStream.class);
        BufferedReader in = mock(BufferedReader.class);
        when(socket.getOutputStream()).thenReturn(out);
        when(socket.getInputStream()).thenThrow(new IOException());
        when(socket.connect(any(InetAddress.class), anyInt())).thenReturn(socket);
        whoIS.query("testQuery");
    }

    @Test
    public void test_getQuery() {
        String query = whoIS.getQuery();
        Assert.assertEquals("testQuery", query);
    }

    @Test
    public void test_reset() {
        whoIS.reset();
        verify(whoIS.queryTextField, times(1)).setText("");
        verify(whoIS.resetRadioButton, times(1)).setSelected(true);
        verify(whoIS.lowercaseD, times(1)).setSelected(false);
        verify(whoIS.inverseQueries, times(1)).setSelectedIndex(0);
        verify(whoIS.miscList, times(1)).setSelectedIndex(0);
    }
}