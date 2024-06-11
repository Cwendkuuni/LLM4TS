import static org.junit.Assert.*;
import org.junit.Test;
import static ipac.WhoIS.*;

public class WhoISTest {

    @Test
    public void testWhoISConstructor() {
        WhoIS whois = new WhoIS();
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
    public void testQueryPanelMethod() {
        WhoIS whois = new WhoIS();
        JPanel queryPanel = whois.queryPanel();
        assertNotNull(queryPanel);
        assertEquals("Search for : ", queryPanel.getComponent(0).toString());
        assertTrue(queryPanel.getComponentCount() > 4);
        assertTrue(queryPanel.getComponent(1) instanceof JLabel);
        assertTrue(queryPanel.getComponent(2) instanceof JTextField);
        assertTrue(queryPanel.getComponent(3) instanceof JButton);
        assertTrue(queryPanel.getComponent(4) instanceof JButton);
    }

    @Test
    public void testAdditionalQueriesPanelMethod() {
        WhoIS whois = new WhoIS();
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
        assertNotNull(additionalQueriesPanel);
        assertEquals("IP Address Lookups", additionalQueriesPanel.getComponent(0).toString());
        assertTrue(additionalQueriesPanel.getComponentCount() > 2);
        assertTrue(additionalQueriesPanel.getComponent(1) instanceof JPanel);
        assertTrue(additionalQueriesPanel.getComponent(2) instanceof JPanel);
    }

    @Test
    public void testIPAddressLookupsMethod() {
        WhoIS whois = new WhoIS();
        JPanel lookupPanel = whois.IPAddressLookups();
        assertNotNull(lookupPanel);
        assertEquals("1st level less specific", lookupPanel.getComponent(0).toString());
        assertTrue(lookupPanel.getComponentCount() > 10);
        assertTrue(lookupPanel.getComponent(0) instanceof JRadioButton);
        assertTrue(lookupPanel.getComponent(1) instanceof JRadioButton);
        assertTrue(lookupPanel.getComponent(2) instanceof JRadioButton);
        assertTrue(lookupPanel.getComponent(3) instanceof JRadioButton);
        assertTrue(lookupPanel.getComponent(4) instanceof JRadioButton);
        assertTrue(lookupPanel.getComponent(11) instanceof JCheckBox);
        assertTrue(lookupPanel.getComponent(12) instanceof JRadioButton);
    }

    @Test
    public void testMiscellaneousQueriesMethod() {
        WhoIS whois = new WhoIS();
        JPanel miscPanel = whois.MiscellaneousQueries();
        assertNotNull(miscPanel);
        assertEquals("Object Types", miscPanel.getComponent(0).toString());
        assertTrue(miscPanel.getComponentCount() > 4);
        assertTrue(miscPanel.getComponent(1) instanceof JLabel);
        assertTrue(miscPanel.getComponent(2) instanceof JComboBox);
        assertTrue(miscPanel.getComponent(3) instanceof JLabel);
        assertTrue(miscPanel.getComponent(4) instanceof JList);
        assertTrue(miscPanel.getComponent(5) instanceof JScrollPane);
    }

    @Test
    public void testQueryHintsPanelMethod() {
        WhoIS whois = new WhoIS();
        JPanel queryHintsPanel = whois.queryHintsPanel();
        assertNotNull(queryHintsPanel);
        assertEquals("Query hints ", queryHintsPanel.getComponent(0).toString());
        assertTrue(queryHintsPanel.getComponentCount() > 5);
        assertTrue(queryHintsPanel.getComponent(1) instanceof JPanel);
        assertTrue(queryHintsPanel.getComponent(2) instanceof JLabel);
        assertTrue(queryHintsPanel.getComponent(3) instanceof JLabel);
        assertTrue(queryHintsPanel.getComponent(4) instanceof JLabel);
        assertTrue(queryHintsPanel.getComponent(5) instanceof JLabel);
        assertTrue(queryHintsPanel.getComponent(6) instanceof JLabel);
    }

    @Test(expected = NullPointerException.class)
    public void testCreatePopupMenuMethodWithNullPopupMenu() {
        WhoIS whois = new WhoIS();
        whois.createPopupMenu();
        assertNull(whois.popupMenu);
    }

    @Test
    public void testCreatePopupMenuMethod() {
        WhoIS whois = new WhoIS();
        JPopupMenu popupMenu = whois.popupMenu;
        assertNotNull(popupMenu);
        assertTrue(popupMenu.getComponentCount() > 4);
        assertTrue(popupMenu.getComponent(0) instanceof JMenuItem);
        assertTrue(popupMenu.getComponent(1) instanceof JMenuItem);
        assertTrue(popupMenu.getComponent(2) instanceof JMenuItem);
        assertTrue(popupMenu.getComponent(3) instanceof JMenuItem);
    }

    @Test
    public void testActionPerformedMethodWhenQueryButtonClicked() {
        WhoIS whois = new WhoIS();
        mockStatic(System.out.class);
        when(System.out.println(anyString())).thenReturn(true);
        WhoISOutput output = spy(new WhoISOutput(""));
        doReturn(output).when(whois).new WhoISOutput(anyString());
        whois.actionPerformed(mock(ActionEvent.class));
        verify(output).printOutput();
    }

    @Test(expected = UnknownHostException.class)
    public void testQueryMethodWithUnknownHostException() {
        WhoIS whois = new WhoIS();
        doThrow(new UnknownHostException("Unknown Host")).when(whois).query(anyString());
        whois.startQuery();
    }

    @Test(expected = IOException.class)
    public void testQueryMethodWithIOException() {
        WhoIS whois = new WhoIS();
        doThrow(new IOException("IO Exception")).when(whois).query(anyString());
        whois.startQuery();
    }

    @Test
    public void testGetQueryMethodWithEmptyQuery() {
        WhoIS whois = new WhoIS();
        assertEquals("", whois.getQuery());
    }

    @Test
    public void testGetQueryMethodWithNonEmptyQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        assertEquals("example.com", whois.getQuery());
    }
}