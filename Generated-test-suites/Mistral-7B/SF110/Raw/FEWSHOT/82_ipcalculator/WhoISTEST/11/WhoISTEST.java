package ipac.whois; // Assuming a new package ipac.whois is created for testing

import ipac.WhoIS;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testQueryPanel() {
        WhoIS whois = new WhoIS();
        assertEquals("Query", whois.queryButton.getText());
        assertEquals("Reset", whois.resetButton.getText());
        assertTrue(whois.queryTextField instanceof JTextField);
        assertTrue(whois.queryButton instanceof JButton);
        assertTrue(whois.resetButton instanceof JButton);
    }

    @Test
    public void testAdditionalQueriesPanel() {
        WhoIS whois = new WhoIS();
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
        assertEquals(WhoIS.JUST_ONE_LINE, additionalQueriesPanel.getLayout().getLayoutDimensions().height, 0);

        JPanel lookupsPanel = (JPanel) additionalQueriesPanel.getComponent(0);
        assertEquals(7, lookupsPanel.getComponentCount());
        assertTrue(lookupsPanel.getComponent(0) instanceof JRadioButton);
        assertTrue(lookupsPanel.getComponent(1) instanceof JRadioButton);
        assertTrue(lookupsPanel.getComponent(2) instanceof JRadioButton);
        assertTrue(lookupsPanel.getComponent(3) instanceof JRadioButton);
        assertTrue(lookupsPanel.getComponent(4) instanceof JRadioButton);
        assertTrue(lookupsPanel.getComponent(5) instanceof JRadioButton);
        assertTrue(lookupsPanel.getComponent(6) instanceof JCheckBox);

        JPanel miscQueriesPanel = (JPanel) additionalQueriesPanel.getComponent(1);
        assertEquals(WhoIS.JUST_ONE_LINE, miscQueriesPanel.getLayout().getLayoutDimensions().height, 0);
        assertTrue(miscQueriesPanel.getComponent(0) instanceof JLabel);
        assertTrue(miscQueriesPanel.getComponent(1) instanceof JComboBox);
        assertTrue(miscQueriesPanel.getComponent(2) instanceof JLabel);
        assertTrue(miscQueriesPanel.getComponent(3) instanceof JList);
    }

    @Test
    public void testQueryHintsPanel() {
        WhoIS whois = new WhoIS();
        JPanel queryHintsPanel = whois.queryHintsPanel();
        assertEquals(WhoIS.TWO_LINES, queryHintsPanel.getLayout().getLayoutDimensions().height, 0);

        JPanel innerPanel = (JPanel) queryHintsPanel.getComponent(0);
        assertEquals(6, innerPanel.getComponentCount());
        assertTrue(innerPanel.getComponent(0) instanceof JLabel);
        assertTrue(innerPanel.getComponent(1) instanceof JLabel);
        assertTrue(innerPanel.getComponent(2) instanceof JLabel);
        assertTrue(innerPanel.getComponent(3) instanceof JLabel);
        assertTrue(innerPanel.getComponent(4) instanceof JLabel);
        assertTrue(innerPanel.getComponent(5) instanceof JLabel);
    }

    @Test
    public void testCreatePopupMenu() {
        WhoIS whois = new WhoIS();
        JPopupMenu popupMenu = whois.popupMenu;
        assertEquals(4, popupMenu.getComponentCount());
        assertTrue(popupMenu.getComponent(0) instanceof JMenuItem);
        assertTrue(popupMenu.getComponent(1) instanceof JMenuItem);
        assertTrue(popupMenu.getComponent(2) instanceof JMenuItem);
        assertTrue(popupMenu.getComponent(3) instanceof JMenuItem);
    }

    @Test
    public void testActionPerformed() {
        WhoIS whois = new WhoIS();
        ActionEvent actionEvent = new ActionEvent(whois, 0, ActionEvent.ACTION_PERFORMED);
        whois.actionPerformed(actionEvent);
        assertFalse(whois.queryButton.isEnabled());
        assertFalse(whois.resetButton.isEnabled());

        actionEvent = new ActionEvent(whois.queryButton, 0, ActionEvent.ACTION_PERFORMED);
        whois.actionPerformed(actionEvent);
        assertTrue(whois.queryButton.isEnabled());
        assertFalse(whois.resetButton.isEnabled());

        actionEvent = new ActionEvent(whois.resetButton, 0, ActionEvent.ACTION_PERFORMED);
        whois.actionPerformed(actionEvent);
        assertTrue(whois.queryButton.isEnabled());
        assertTrue(whois.resetButton.isEnabled());
    }

    @Test
    public void testGetQuery() {
        WhoIS whois = new WhoIS();
        String query = "example.com";
        whois.queryTextField.setText(query);
        whois.inverseQueries.setSelectedIndex(1);
        whois.lowercaseL.setSelected(true);
        whois.lowercaseM.setSelected(true);
        whois.lowercaseX.setSelected(true);
        whois.lowercaseD.setSelected(true);
        Object[] miscQueries = {"as-block", "inetnum"};
        whois.miscList.setSelectedValues(miscQueries);

        String expectedQuery = "-i admin-c,tech-c -l example.com -m 1st level more specific -m 1st level more specific -x exact match only -d 1";
        assertEquals(expectedQuery, whois.getQuery());
    }

    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.reset();
        assertEquals("", whois.queryTextField.getText());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
        assertTrue(whois.resetRadioButton.isSelected());
    }

}