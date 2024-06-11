package ipac.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ipac.WhoIS;
import ipac.WhoISOutput;

public class WhoISTest {

    private WhoIS whoIS;
    private WhoISOutput whoISOutput;

    @Before
    public void setUp() {
        whoIS = new WhoIS();
        whoISOutput = new WhoISOutput();
    }

    @Test
    public void testConstructor() {
        WhoIS whoIS = new WhoIS();
        assertNotNull(whoIS);
        assertEquals("APNIC", whoIS.WHOIS_NAME);
        assertEquals("whois.apnic.net", whoIS.WHOIS_ADDRESS);
        assertEquals(43, whoIS.WHOIS_PORT);
    }

    @Test
    public void testQueryPanel() {
        JPanel queryPanel = whoIS.queryPanel();
        assertNotNull(queryPanel);
        assertTrue(queryPanel.getComponentCount() > 0);
        assertTrue(queryPanel.getBackground() == Color.WHITE);
        assertEquals(4, queryTextField().getColumns());
    }

    @Test
    public void testAdditionalQueriesPanel() {
        JPanel additionalQueriesPanel = whoIS.additionalQueriesPanel();
        assertNotNull(additionalQueriesPanel);
        assertTrue(additionalQueriesPanel.getComponentCount() > 0);
        assertTrue(additionalQueriesPanel.getBackground() == Color.WHITE);
    }

    @Test
    public void testIPAddressLookups() {
        JPanel lookupPanel = whoIS.IPAddressLookups();
        assertNotNull(lookupPanel);
        int numRadioButtons = 6;
        assertEquals(numRadioButtons, lookupPanel.getComponentCount());
        for (int i = 0; i < numRadioButtons; i++) {
            assertTrue(lookupPanel.getComponent(i) instanceof JRadioButton);
        }
        assertTrue(lookupPanel.getBackground() == Color.WHITE);
    }

    @Test
    public void testMiscellaneousQueries() {
        JPanel miscPanel = whoIS.MiscellaneousQueries();
        assertNotNull(miscPanel);
        int numComponents = 4;
        assertEquals(numComponents, miscPanel.getComponentCount());
        assertTrue(miscPanel.getBackground() == Color.WHITE);
        assertEquals("Inverse Queries", miscPanel.getComponent(0).getText());
        assertNotNull(miscPanel.getComponent(1));
        assertTrue(miscPanel.getComponent(2).getClass().isInstance(whoIS.miscList));
    }

    @Test
    public void testQueryHintsPanel() {
        JPanel queryHintsPanel = whoIS.queryHintsPanel();
        assertNotNull(queryHintsPanel);
        assertTrue(queryHintsPanel.getComponentCount() > 0);
        assertTrue(queryHintsPanel.getBackground() == Color.WHITE);
    }

    @Test
    public void testCreatePopupMenu() {
        JPopupMenu popupMenu = whoIS.popupMenu;
        assertNotNull(popupMenu);
        assertEquals(4, popupMenu.getComponentCount());
        for (int i = 0; i < 4; i++) {
            assertTrue(popupMenu.getComponent(i) instanceof JMenuItem);
        }
    }

    @Test
    public void testActionPerformed() throws IOException {
        String expectedQuery = "example.com";
        String queryText = "example.com";
        whoIS.queryTextField.setText(queryText);
        quiestMockSocket(expectedQuery);
        whoIS.actionPerformed(new ActionEvent(whoIS.queryButton, 0, "Query"));
        assertEquals(expectedQuery, whoIS.getQuery());
    }

    @Test
    public void testGetQuery() {
        String queryText = "example.com";
        String expectedQuery = "-i admin-c -l 1st level less specific example.com";
        whoIS.queryTextField.setText(queryText);
        whoIS.lowercaseL.setSelected(true);
        whoIS.inverseQueries.setSelectedIndex(0);
        assertEquals(expectedQuery, whoIS.getQuery());
    }

    @Test
    public void testReset() {
        whoIS.reset();
        assertTrue(whoIS.queryTextField.getText().isEmpty());
        assertFalse(whoIS.lowercaseD.isSelected());
        assertEquals(0, whoIS.inverseQueries.getSelectedIndex());
        assertEquals(0, whoIS.miscList.getSelectedIndex());
    }

    private void quiestMockSocket(String expectedQuery) throws IOException {
        Socket socket = mock(Socket.class);
        when(socket.getOutputStream()).then(invocation -> {
            PrintStream out = new PrintStream(socket.getOutputStream());
            out.println(expectedQuery);
            return out;
        });
        when(socket.getInputStream()).thenReturn(new ByteArrayInputStream(getExpectedOutput().getBytes()));
        WhoISOutput whoISOutput = new WhoISOutput();
        whoIS.query(whoIS.getQuery());
        assertEquals(expectedQuery, whoISOutput.getQuery());
        verify(socket).close();
    }

    private List<String> getExpectedOutput() {
        return Arrays.asList(
                "Domain Name: EXAMPLE.NET",
                "Registry Domain ID: ORG2-DEMO.ORG",
                "Registrar WHOIS Server: whois.verisign-grs.com",
                "Registrar URL: http://www.verisign-grs.com",
                "Registrar Name: Verisign Global Registry Services",
                "Registrar Phone: +1.7035473837",
                "Registrar Fax: +1.7035473839",
                "Registrar Email: reg-support@verisign-grs.com",
                "Name Servers: NS1.EXAMPLE.NET.",
                "Name Servers: NS2.EXAMPLE.NET.",
                "DNSSEC: unsigned.",
                "");
    }
}