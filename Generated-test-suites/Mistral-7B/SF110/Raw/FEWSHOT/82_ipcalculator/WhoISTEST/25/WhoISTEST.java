package ipac.test;

import static org.junit.Assert.*;
import ipac.WhoIS;

import java.util.Arrays;

import org.junit.Test;

public class WhoISTest {

    @Test
    public void testWhoISConstruction() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois);
    }

    @Test
    public void testQueryPanelCreation() {
        WhoIS whois = new WhoIS();
        JPanel queryPanel = whois.queryPanel();

        assertNotNull(queryPanel);
        assertEquals(3, queryPanel.getComponentCount());

        JTextField queryTextField = (JTextField) queryPanel.getComponent(1);
        assertNotNull(queryTextField);
        assertEquals(40, queryTextField.getColumns());

        JButton queryButton = (JButton) queryPanel.getComponent(2);
        assertNotNull(queryButton);
        assertEquals("Query", queryButton.getText());

        JButton resetButton = (JButton) queryPanel.getComponent(2);
        assertNotNull(resetButton);
        assertEquals("Reset", resetButton.getText());
    }

    @Test
    public void testAdditionalQueriesPanelCreation() {
        WhoIS whois = new WhoIS();
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();

        assertNotNull(additionalQueriesPanel);
        assertEquals(2, additionalQueriesPanel.getComponentCount());

        JPanel lookupPanel = (JPanel) additionalQueriesPanel.getComponent(0);
        assertNotNull(lookupPanel);
        assertEquals(7, lookupPanel.getComponentCount());

        JRadioButton lowercaseL = (JRadioButton) lookupPanel.getComponent(0);
        assertNotNull(lowercaseL);
        assertEquals("1st level less specific", lowercaseL.getText());

        JRadioButton uppercaseL = (JRadioButton) lookupPanel.getComponent(1);
        assertNotNull(uppercaseL);
        assertEquals("All less specific", uppercaseL.getText());

        JRadioButton lowercaseM = (JRadioButton) lookupPanel.getComponent(2);
        assertNotNull(lowercaseM);
        assertEquals("1st level more specific", lowercaseM.getText());

        JRadioButton uppercaseM = (JRadioButton) lookupPanel.getComponent(3);
        assertNotNull(uppercaseM);
        assertEquals("All more specific", uppercaseM.getText());

        JRadioButton lowercaseX = (JRadioButton) lookupPanel.getComponent(4);
        assertNotNull(lowercaseX);
        assertEquals("Exact match only", lowercaseX.getText());

        JRadioButton resetRadioButton = (JRadioButton) lookupPanel.getComponent(5);
        assertNotNull(resetRadioButton);
        assertEquals("Reset Radio Button", resetRadioButton.getText());

        JCheckBox lowercaseD = (JCheckBox) lookupPanel.getComponent(6);
        assertNotNull(lowercaseD);
        assertEquals("Associated reverse domain", lowercaseD.getText());

        JPanel miscPanel = (JPanel) additionalQueriesPanel.getComponent(1);
        assertNotNull(miscPanel);
        assertEquals(2, miscPanel.getComponentCount());

        JLabel inverseLabel = (JLabel) miscPanel.getComponent(0);
        assertNotNull(inverseLabel);
        assertEquals("Inverse Queries", inverseLabel.getText());

        JComboBox inverseQueries = (JComboBox) miscPanel.getComponent(1);
        assertNotNull(inverseQueries);
        assertEquals(16, inverseQueries.getItemCount());
        assertTrue(Arrays.asList(inverseQueries.getItemNames()).containsAll(Arrays.asList("None", "admin-c", "admin-c,tech-c,zone-c", "author", "cross-mnt", "cross-nfy", "local-as", "mbrs-by-ref", "member-of", "mnt-by", "mnt-lower", "mnt-nfy", "mnt-routes", "notify", "nserver", "origin", "person", "referral-by", "rev-srv", "sub-dom", "tech-c", "upd-to", "zone-c")));
    }

    @Test
    public void testQueryHintsPanelCreation() {
        WhoIS whois = new WhoIS();
        JPanel queryHintsPanel = whois.queryHintsPanel();

        assertNotNull(queryHintsPanel);
        assertEquals(1, queryHintsPanel.getComponentCount());

        JPanel innerPanel = (JPanel) queryHintsPanel.getComponent(0);
        assertNotNull(innerPanel);
        assertEquals(6, innerPanel.getComponentCount());

        JLabel queryHintsText1 = (JLabel) innerPanel.getComponent(0);
        assertNotNull(queryHintsText1);
        assertEquals("Query hints", queryHintsText1.getText());

        JLabel emptyLabel = (JLabel) innerPanel.getComponent(1);
        assertNotNull(emptyLabel);
        assertEquals("EMPTY LABEL", emptyLabel.getText());

        JLabel queryHintsText2 = (JLabel) innerPanel.getComponent(2);
        assertNotNull(queryHintsText2);
        assertEquals("* Include \"AS\" in front of an AS number.", queryHintsText2.getText());

        JLabel queryHintsText3 = (JLabel) innerPanel.getComponent(3);
        assertNotNull(queryHintsText3);
        assertEquals("Example: AS4808", queryHintsText3.getText());

        JLabel queryHintsText4 = (JLabel) innerPanel.getComponent(4);
        assertNotNull(queryHintsText4);
        assertEquals("* Include \"-t\" (template only) or \"-v\" (template and description) in front of an object name to view the template.", queryHintsText4.getText());

        JLabel queryHintsText5 = (JLabel) innerPanel.getComponent(5);
        assertNotNull(queryHintsText5);
        assertEquals("Example: -t inetnum", queryHintsText5.getText());
    }

    @Test
    public void testCreatePopupMenu() {
        WhoIS whois = new WhoIS();
        JPopupMenu popupMenu = whois.popupMenu;

        assertNotNull(popupMenu);
        assertEquals(4, popupMenu.getComponentCount());

        JMenuItem cut = (JMenuItem) popupMenu.getComponent(0);
        assertNotNull(cut);
        assertEquals("Cut", cut.getText());

        JMenuItem copy = (JMenuItem) popupMenu.getComponent(1);
        assertNotNull(copy);
        assertEquals("Copy", copy.getText());

        JMenuItem paste = (JMenuItem) popupMenu.getComponent(2);
        assertNotNull(paste);
        assertEquals("Paste", paste.getText());

        JMenuItem selectAll = (JMenuItem) popupMenu.getComponent(3);
        assertNotNull(selectAll);
        assertEquals("Select All", selectAll.getText());
    }
}