package ipac.test;

import ipac.WhoIS;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WhoISTest {

    @Test
    public void testWhoISInstance() {
        WhoIS whois = new WhoIS();
        assertNull(whois.WHOIS_NAME);
        assertEquals("whois.apnic.net", whois.WHOIS_ADDRESS);
        assertEquals(43, whois.WHOIS_PORT);
    }

    @Test
    public void testQueryPanel() {
        WhoIS whois = new WhoIS();
        JPanel queryPanel = whois.queryPanel();
        assertNotNull(queryPanel);
        assertEquals(3, queryPanel.getComponentCount());
    }

    @Test
    public void testAdditionalQueriesPanel() {
        WhoIS whois = new WhoIS();
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
        assertNotNull(additionalQueriesPanel);
        assertEquals(2, additionalQueriesPanel.getComponentCount());
    }

    @Test
    public void testIPAddressLookups() {
        WhoIS whois = new WhoIS();
        JPanel lookupsPanel = whois.IPAddressLookups();
        assertNotNull(lookupsPanel);
        assertEquals(7, lookupsPanel.getComponentCount());
        assertTrue(((JRadioButton)lookupsPanel.getComponent(0)).isSelected());
        assertEquals(Arrays.asList("1st level less specific", "All less specific", "1st level more specific", "All more specific", "Exact match only"), Arrays.asList(lookupsPanel.getComponentNames()));
    }

    @Test
    public void testMiscellaneousQueries() {
        WhoIS whois = new WhoIS();
        JPanel miscQueriesPanel = whois.MiscellaneousQueries();
        assertNotNull(miscQueriesPanel);
        assertEquals(3, miscQueriesPanel.getComponentCount());
        assertEquals("Inverse Queries", miscQueriesPanel.getComponent(0).getName());
        assertEquals("Object Types", miscQueriesPanel.getComponent(1).getName());
        assertEquals("JList", miscQueriesPanel.getComponent(2).getClass().getSimpleName());
    }

    @Test
    public void testQueryHintsPanel() {
        WhoIS whois = new WhoIS();
        JPanel queryHintsPanel = whois.queryHintsPanel();
        assertNotNull(queryHintsPanel);
        assertEquals(6, queryHintsPanel.getComponentCount());
        assertTrue(queryHintsPanel.getComponent(0) instanceof JPanel);
        assertEquals("Query hints", ((JPanel)queryHintsPanel.getComponent(0)).getName());
        assertEquals(6, ((JPanel)queryHintsPanel.getComponent(0)).getComponentCount());
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

    @Test
    public void testActionPerformed() {
        WhoIS whois = new WhoIS();
        MockActionEvent event = new MockActionEvent("queryButton");
        whois.actionPerformed(event);
        whois.startQuery();
    }

    @Test
    public void testStartQueryEmptyQuery() {
        WhoIS whois = new WhoIS();
        whois.startQuery();
        assertTrue(whois.displayErrorCalled);
    }

    @Test
    public void testStartQueryValidQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        whois.startQuery();
        assertFalse(whois.displayErrorCalled);
    }

    private static class MockActionEvent extends ActionEvent {
        public MockActionEvent(String name) {
            super(new Object(), new ActionEvent.ActionListener() {}, name, 0, false);
        }
    }

}

class WhoIS {
    boolean displayErrorCalled = false;
    
    public void displayError(String error) {
        displayErrorCalled = true;
    }
}