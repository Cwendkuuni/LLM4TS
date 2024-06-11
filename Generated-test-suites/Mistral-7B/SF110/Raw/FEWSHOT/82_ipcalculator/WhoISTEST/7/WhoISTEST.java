package ipac.test;

import static org.junit.Assert.*;
import ipac.WhoIS;

import org.junit.Test;

public class WhoISTest {

    @Test
    public void testWhoISConstructor() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois);
        assertEquals("APNIC", whois.WHOIS_NAME);
        assertEquals("whois.apnic.net", whois.WHOIS_ADDRESS);
        assertEquals(43, whois.WHOIS_PORT);
    }

    @Test
    public void testQueryPanelCreation() {
        WhoIS whois = new WhoIS();
        JPanel queryPanel = whois.queryPanel();
        assertNotNull(queryPanel);
        assertTrue(queryPanel.getBackground() instanceof Color);
        assertTrue(queryPanel.getLayout() instanceof BoxLayout);
    }

    @Test
    public void testAdditionalQueriesPanelCreation() {
        WhoIS whois = new WhoIS();
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
        assertNotNull(additionalQueriesPanel);
        assertTrue(additionalQueriesPanel.getBackground() instanceof Color);
        assertTrue(additionalQueriesPanel.getLayout() instanceof BoxLayout);
    }

    @Test
    public void testIPAddressLookupsCreation() {
        WhoIS whois = new WhoIS();
        JPanel lookupPanel = whois.IPAddressLookups();
        assertNotNull(lookupPanel);
        assertTrue(lookupPanel.getBackground() instanceof Color);
        assertTrue(lookupPanel.getLayout() instanceof BoxLayout);
        assertEquals(6, lookupPanel.getComponentCount());
        assertTrue(lookupPanel.getComponent(0) instanceof JRadioButton);
        assertTrue(lookupPanel.getComponent(1) instanceof JRadioButton);
        assertTrue(lookupPanel.getComponent(2) instanceof JRadioButton);
        assertTrue(lookupPanel.getComponent(3) instanceof JRadioButton);
        assertTrue(lookupPanel.getComponent(4) instanceof JRadioButton);
        assertTrue(lookupPanel.getComponent(5) instanceof JCheckBox);
    }

    @Test
    public void testMiscellaneousQueriesCreation() {
        WhoIS whois = new WhoIS();
        JPanel miscPanel = whois.MiscellaneousQueries();
        assertNotNull(miscPanel);
        assertTrue(miscPanel.getBackground() instanceof Color);
        assertTrue(miscPanel.getLayout() instanceof BoxLayout);
        assertEquals(3, miscPanel.getComponentCount());
        assertTrue(miscPanel.getComponent(0) instanceof JLabel);
        JComboBox inverseQueries = (JComboBox) miscPanel.getComponent(1);
        assertEquals(17, inverseQueries.getItemCount());
        assertTrue(miscPanel.getComponent(2) instanceof JScrollPane);
        JList miscList = ((JScrollPane) miscPanel.getComponent(2)).getViewport().getComponent(0);
        assertEquals(16, miscList.getItemCount());
    }

    @Test
    public void testQueryHintsPanelCreation() {
        WhoIS whois = new WhoIS();
        JPanel queryHintsPanel = whois.queryHintsPanel();
        assertNotNull(queryHintsPanel);
        assertTrue(queryHintsPanel.getBackground() instanceof Color);
        assertTrue(queryHintsPanel.getLayout() instanceof FlowLayout);
        assertTrue(queryHintsPanel.getComponentCount() > 0);
    }

    @Test
    public void testCreatePopupMenu() {
        WhoIS whois = new WhoIS();
        JPopupMenu popupMenu = whois.popupMenu;
        assertNotNull(popupMenu);
        assertEquals(4, popupMenu.getComponentCount());
        assertTrue(popupMenu.getComponent(0) instanceof JMenuItem);
        assertTrue(popupMenu.getComponent(1) instanceof JMenuItem);
        assertTrue(popupMenu.getComponent(2) instanceof JMenuItem);
        assertTrue(popupMenu.getComponent(3) instanceof JMenuItem);
    }
}