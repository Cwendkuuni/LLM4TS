package ipac.test;

import static org.junit.Assert.*;
import ipac.WhoIS;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import org.junit.Test;

public class WhoISTest {

    @Test
    public void testWhoISInitialization() {
        WhoIS whois = new WhoIS();
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
        assertEquals(Color.WHITE, whois.getBackground());
        assertEquals(40, whois.queryTextField.getColumns());
        assertEquals(200, whois.queryTextField.getPreferredSize().width);
        assertEquals(200, whois.queryTextField.getPreferredSize().height);
    }

    @Test
    public void testQueryPanelCreation() {
        WhoIS whois = new WhoIS();
        JPanel queryPanel = whois.queryPanel();
        assertNotNull(queryPanel);
        assertNotNull(queryPanel.getLayout());
        assertEquals(BoxLayout.X_AXIS, queryPanel.getLayout().getType());
        assertEquals(3, queryPanel.getComponentCount());
        assertTrue(queryPanel.getComponent(0) instanceof JLabel);
        assertTrue(queryPanel.getComponent(1) instanceof JTextField);
        assertTrue(queryPanel.getComponent(2) instanceof JButton);
    }

    @Test
    public void testAdditionalQueriesPanelCreation() {
        WhoIS whois = new WhoIS();
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
        assertNotNull(additionalQueriesPanel);
        assertNotNull(additionalQueriesPanel.getLayout());
        assertEquals(BoxLayout.X_AXIS, additionalQueriesPanel.getLayout().getType());
        assertEquals(3, additionalQueriesPanel.getComponentCount());
        assertTrue(additionalQueriesPanel.getComponent(0) instanceof JPanel);
        assertTrue(additionalQueriesPanel.getComponent(1) instanceof JPanel);
        assertTrue(additionalQueriesPanel.getComponent(2) instanceof JPanel);
    }

    @Test
    public void testIPAddressLookupsPanelCreation() {
        WhoIS whois = new WhoIS();
        JPanel ipAddressLookupsPanel = whois.IPAddressLookups();
        assertNotNull(ipAddressLookupsPanel);
        assertNotNull(ipAddressLookupsPanel.getLayout());
        assertEquals(BoxLayout.X_AXIS, ipAddressLookupsPanel.getLayout().getType());
        assertEquals(11, ipAddressLookupsPanel.getComponentCount());
        assertTrue(ipAddressLookupsPanel.getComponent(0) instanceof JRadioButton);
        assertTrue(ipAddressLookupsPanel.getComponent(1) instanceof JRadioButton);
        assertTrue(ipAddressLookupsPanel.getComponent(2) instanceof JRadioButton);
        assertTrue(ipAddressLookupsPanel.getComponent(3) instanceof JRadioButton);
        assertTrue(ipAddressLookupsPanel.getComponent(4) instanceof JRadioButton);
        assertTrue(ipAddressLookupsPanel.getComponent(5) instanceof JCheckBox);
        assertTrue(ipAddressLookupsPanel.getComponent(6) instanceof JRadioButton);
        assertTrue(ipAddressLookupsPanel.getComponent(7) instanceof JRadioButton);
        assertTrue(ipAddressLookupsPanel.getComponent(8) instanceof JRadioButton);
        assertTrue(ipAddressLookupsPanel.getComponent(9) instanceof JRadioButton);
        assertTrue(ipAddressLookupsPanel.getComponent(10) instanceof JRadioButton);
    }

    @Test
    public void testMiscellaneousQueriesPanelCreation() {
        WhoIS whois = new WhoIS();
        JPanel miscQueriesPanel = whois.MiscellaneousQueries();
        assertNotNull(miscQueriesPanel);
        assertNotNull(miscQueriesPanel.getLayout());
        assertEquals(BoxLayout.X_AXIS, miscQueriesPanel.getLayout().getType());
        assertEquals(4, miscQueriesPanel.getComponentCount());
        assertTrue(miscQueriesPanel.getComponent(0) instanceof JLabel);
        assertTrue(miscQueriesPanel.getComponent(1) instanceof JComboBox);
        assertTrue(miscQueriesPanel.getComponent(2) instanceof JLabel);
        assertTrue(miscQueriesPanel.getComponent(3) instanceof JList);
    }

    @Test
    public void testQueryHintsPanelCreation() {
        WhoIS whois = new WhoIS();
        JPanel queryHintsPanel = whois.queryHintsPanel();
        assertNotNull(queryHintsPanel);
        assertNotNull(queryHintsPanel.getLayout());
        assertEquals(BoxLayout.Y_AXIS, queryHintsPanel.getLayout().getType());
        assertEquals(6, queryHintsPanel.getComponentCount());
        assertTrue(queryHintsPanel.getComponent(0) instanceof JLabel);
        assertTrue(queryHintsPanel.getComponent(1) instanceof JLabel);
        assertTrue(queryHintsPanel.getComponent(2) instanceof JLabel);
        assertTrue(queryHintsPanel.getComponent(3) instanceof JLabel);
        assertTrue(queryHintsPanel.getComponent(4) instanceof JLabel);
        assertTrue(queryHintsPanel.getComponent(5) instanceof JLabel);
    }

    @Test
    public void testPopupMenuCreation() {
        WhoIS whois = new WhoIS();
        JPopupMenu popupMenu = whois.popupMenu;
        assertNotNull(popupMenu);
        assertEquals(4, popupMenu.getComponentCount());
        assertTrue(popupMenu.getComponent(0) instanceof JMenuItem);
        assertTrue(popupMenu.getComponent(1) instanceof JMenuItem);
        assertTrue(popupMenu.getComponent(2) instanceof JMenuItem);
        assertTrue(popupMenu.getComponent(3) instanceof JMenuItem);
    }

    @Test
    public void testActionPerformedQueryButton() {
        WhoIS whois = new WhoIS();
        mockQueryTextField("test");
        when(whois.queryButton.getActionCommand()).thenReturn("Query");
        whois.actionPerformed(mockActionEvent());
        verify(whois).startQuery();
    }

    @Test
    public void testActionPerformedResetButton() {
        WhoIS whois = new WhoIS();
        when(whois.resetButton.getActionCommand()).thenReturn("Reset");
        whois.actionPerformed(mockActionEvent());
        verify(whois).reset();
    }

    @Test
    public void testGetQueryWithNoOptions() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("test");
        assertEquals("test", whois.getQuery());
    }

    @Test
    public void testGetQueryWithInverseQueries() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("test");
        whois.inverseQueries.setSelectedIndex(1);
        assertEquals("-i admin-c test", whois.getQuery());
    }

    @Test
    public void testGetQueryWithIPAddressLookups() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("test");
        whois.lowercaseL.setSelected(true);
        assertEquals("-l test", whois.getQuery());
    }

    @Test
    public void testGetQueryWithMiscellaneousQueries() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("test");
        Object[] miscQueries = { "as-block", "mntner" };
        whois.miscList.setSelectedValues(miscQueries);
        assertEquals("-T as-block,mntner test", whois.getQuery());
    }

    @Test
    public void testGetQueryWithBooleanD() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("test");
        whois.lowercaseD.setSelected(true);
        assertEquals("-d test", whois.getQuery());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetQueryWithEmptyQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("");
        whois.getQuery();
    }

    private void mockQueryTextField(String text) {
        when(whois.queryTextField.getText()).thenReturn(text);
    }

    private ActionEvent mockActionEvent() {
        return argumentCaptor(ActionEvent.class).getValue();
    }

    private ArgumentCaptor<ActionEvent> argumentCaptor(Class<?> type) {
        return ArgumentCaptor.forClass(type);
    }

    private WhoIS whois = mock(WhoIS.class);
}