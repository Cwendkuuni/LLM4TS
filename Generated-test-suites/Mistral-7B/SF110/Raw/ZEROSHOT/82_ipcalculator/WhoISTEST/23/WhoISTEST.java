package ipac.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;

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
    public void testInitialization() {
        assertNotNull(whois);
        assertEquals("APNIC", whois.WHOIS_NAME);
        assertEquals("whois.apnic.net", whois.WHOIS_ADDRESS);
        assertEquals(43, whois.WHOIS_PORT);
        assertNotNull(whois.queryTextField);
        assertNotNull(whois.queryButton);
        assertNotNull(whois.resetButton);
        assertNotNull(whois.lowercaseL);
        assertNotNull(whois.uppercaseL);
        assertNotNull(whois.lowercaseM);
        assertNotNull(whois.uppercaseM);
        assertNotNull(whois.lowercaseX);
        assertNotNull(whois.resetRadioButton);
        assertNotNull(whois.radioButtonGroup);
        assertNotNull(whois.popupMenu);
        assertNotNull(whois.inverseQueries);
        assertNotNull(whois.miscList);
    }

    @Test
    public void testQueryPanelCreation() {
        JPanel queryPanel = whois.queryPanel();
        assertNotNull(queryPanel);
        assertEquals(3, queryPanel.getComponentCount());
        assertTrue(queryPanel instanceof JPanel);
        assertTrue(queryLabel instanceof JLabel);
        assertTrue(queryTextField instanceof JTextField);
        assertTrue(queryButton instanceof JButton);
        assertTrue(resetButton instanceof JButton);
    }

    @Test
    public void testAdditionalQueriesPanelCreation() {
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
        assertNotNull(additionalQueriesPanel);
        assertEquals(3, additionalQueriesPanel.getComponentCount());
        assertTrue(additionalQueriesPanel instanceof JPanel);
        assertTrue(inverseLabel instanceof JLabel);
        assertTrue(inverseQueries instanceof JComboBox);
        assertTrue(MiscLabel instanceof JLabel);
    }

    @Test
    public void testQueryHintsPanelCreation() {
        JPanel queryHintsPanel = whois.queryHintsPanel();
        assertNotNull(queryHintsPanel);
        assertEquals(1, queryHintsPanel.getComponentCount());
        assertTrue(queryHintsPanel instanceof JPanel);
        assertTrue(innerPanel instanceof JPanel);
        assertTrue(queryHintsText1 instanceof JLabel);
        assertTrue(emptyLabel instanceof JLabel);
        assertTrue(queryHintsText2 instanceof JLabel);
        assertTrue(queryHintsText3 instanceof JLabel);
        assertTrue(queryHintsText4 instanceof JLabel);
        assertTrue(queryHintsText5 instanceof JLabel);
    }

    @Test
    public void testCreatePopupMenu() {
        assertNotNull(whois.popupMenu);
        assertEquals(4, whois.popupMenu.getComponentCount());
        assertTrue(cut instanceof JMenuItem);
        assertTrue(copy instanceof JMenuItem);
        assertTrue(paste instanceof JMenuItem);
        assertTrue(selectAll instanceof JMenuItem);
    }

    @Test
    public void testActionPerformed() throws IOException {
        String query = "example.com";
        whois.queryTextField.setText(query);
        whois.queryButton.doClick();

        String expectedOutput = getExpectedOutputForQuery(query);
        String actualOutput = "";

        InputStream inputStream = new ByteArrayInputStream(expectedOutput.getBytes());
        Socket socket = new Socket("localhost", 0);
        socket.getOutputStream().write(query.getBytes());
        InputStream serverInput = socket.getInputStream();
        byte[] buffer = new byte[1024];
        int bytesRead = -1;
        while ((bytesRead = serverInput.read(buffer)) != -1) {
            actualOutput += new String(Arrays.copyOfRange(buffer, 0, bytesRead));
        }
        WhoISOutput whoisOutput = new WhoISOutput(actualOutput);
        whoisOutput.display();

        assertEquals(expectedOutput, actualOutput);
    }

    private String getExpectedOutputForQuery(String query) {
        // Add your expected output for various queries here
        return "...";
    }

    @Test
    public void testReset() {
        whois.reset();
        assertTrue(whois.queryTextField.getText().isEmpty());
        assertTrue(whois.resetRadioButton.isSelected());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }
}