package ipac.test;

import static org.junit.Assert.*;
import ipac.WhoIS;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Arrays;

import org.junit.Test;

public class WhoISTest {

    @Test
    public void testWhoISInitialization() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois);
        assertEquals(Color.WHITE, whois.getBackground());
    }

    @Test
    public void testQueryPanelInitialization() {
        WhoIS whois = new WhoIS();
        JPanel queryPanel = whois.queryPanel();
        assertNotNull(queryPanel);
        assertEquals(FlowLayout.class, queryPanel.getLayoutClass());
        assertEquals(3, queryPanel.getComponentCount());
        assertEquals(JLabel.class, queryPanel.getComponent(0).getClass());
        assertEquals(JTextField.class, queryPanel.getComponent(1).getClass());
        assertEquals(JButton.class, queryPanel.getComponent(2).getClass());
    }

    @Test
    public void testAdditionalQueriesPanelInitialization() {
        WhoIS whois = new WhoIS();
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
        assertNotNull(additionalQueriesPanel);
        assertEquals(FlowLayout.class, additionalQueriesPanel.getLayoutClass());
        assertEquals(2, additionalQueriesPanel.getComponentCount());
        assertEquals(JPanel.class, additionalQueriesPanel.getComponent(0).getClass());
        assertEquals(JPanel.class, additionalQueriesPanel.getComponent(1).getClass());
    }

    @Test
    public void testIPAddressLookupsInitialization() {
        WhoIS whois = new WhoIS();
        JPanel ipAddressLookupsPanel = whois.IPAddressLookups();
        assertNotNull(ipAddressLookupsPanel);
        assertEquals(BoxLayout.class, ipAddressLookupsPanel.getLayoutClass());
        assertEquals(11, ipAddressLookupsPanel.getComponentCount());
        assertArrayEquals(new String[]{"1st level less specific", "All less specific", "1st level more specific", "All more specific", "Exact match only", "Reset Radio Button", "Associated reverse domain"}, getRadioButtonLabels(ipAddressLookupsPanel));
    }

    @Test
    public void testMiscellaneousQueriesInitialization() {
        WhoIS whois = new WhoIS();
        JPanel miscellaneousQueriesPanel = whois.MiscellaneousQueries();
        assertNotNull(miscellaneousQueriesPanel);
        assertEquals(BoxLayout.class, miscellaneousQueriesPanel.getLayoutClass());
        assertEquals(3, miscellaneousQueriesPanel.getComponentCount());
        assertEquals(JLabel.class, miscellaneousQueriesPanel.getComponent(0).getClass());
        assertEquals(JComboBox.class, miscellaneousQueriesPanel.getComponent(1).getClass());
        assertEquals(JList.class, miscellaneousQueriesPanel.getComponent(2).getClass());
    }

    @Test
    public void testQueryHintsPanelInitialization() {
        WhoIS whois = new WhoIS();
        JPanel queryHintsPanel = whois.queryHintsPanel();
        assertNotNull(queryHintsPanel);
        assertEquals(FlowLayout.class, queryHintsPanel.getLayoutClass());
        assertEquals(5, queryHintsPanel.getComponentCount());
        assertEquals(JLabel.class, queryHintsPanel.getComponent(0).getClass());
        assertEquals(JLabel.class, queryHintsPanel.getComponent(1).getClass());
        assertEquals(JLabel.class, queryHintsPanel.getComponent(2).getClass());
        assertEquals(JLabel.class, queryHintsPanel.getComponent(3).getClass());
        assertEquals(JLabel.class, queryHintsPanel.getComponent(4).getClass());
    }

    @Test
    public void testPopupMenuInitialization() {
        WhoIS whois = new WhoIS();
        JPopupMenu popupMenu = whois.popupMenu;
        assertNotNull(popupMenu);
        assertEquals(4, popupMenu.getComponentCount());
        assertEquals(JMenuItem.class, popupMenu.getComponent(0).getClass());
        assertEquals(JMenuItem.class, popupMenu.getComponent(1).getClass());
        assertEquals(JMenuItem.class, popupMenu.getComponent(2).getClass());
        assertEquals(JMenuItem.class, popupMenu.getComponent(3).getClass());
    }

    @Test
    public void testActionPerformedQueryButton() {
        WhoIS whois = new WhoIS();
        JButton queryButton = whois.queryButton;
        ActionEvent actionEvent = new ActionEvent(queryButton, 0, "Query");
        whois.actionPerformed(actionEvent);
        whois.startQuery();
    }

    @Test
    public void testActionPerformedResetButton() {
        WhoIS whois = new WhoIS();
        JButton resetButton = whois.resetButton;
        ActionEvent actionEvent = new ActionEvent(resetButton, 0, "Reset");
        whois.actionPerformed(actionEvent);
        whois.reset();
    }

    private String[] getRadioButtonLabels(JPanel panel) {
        JComponent[] components = panel.getComponents();
        int index = 0;
        for (JComponent component : components) {
            if (component.getClass() == JRadioButton.class) {
                return new String[]{(String) component.getText()};
            }
            index++;
        }
        return new String[0];
    }
}