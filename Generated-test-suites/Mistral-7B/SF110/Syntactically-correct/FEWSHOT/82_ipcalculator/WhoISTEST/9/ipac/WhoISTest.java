package ipac.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import ipac.WhoIS;
import ipac.WhoIS.JRadioButton;

public class WhoISTest {

    @Test
    public void testWhoISObjectCreation() {
        WhoIS whois = new WhoIS();
        assertNull(whois.getQuery());
        assertTrue(whois.getBackground().equals(Color.WHITE));
    }

    @Test
    public void testQueryPanelCreation() {
        WhoIS whois = new WhoIS();
        JPanel queryPanel = whois.queryPanel();
        JTextField queryTextField = (JTextField) queryPanel.getComponent(1);
        JButton queryButton = (JButton) queryPanel.getComponent(2);
        JButton resetButton = (JButton) queryPanel.getComponent(3);

        assertEquals(JComponent.WhisperFocusPolicy.TRAILING, queryTextField.getFocusPolicy());
        assertEquals(40, queryTextField.getColumns());
        assertEquals("Query", queryButton.getText());
        assertEquals("Reset", resetButton.getText());
    }

    @Test
    public void testAdditionalQueriesPanelCreation() {
        WhoIS whois = new WhoIS();
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();

        JPanel lookupsPanel = (JPanel) additionalQueriesPanel.getComponent(0);
        JRadioButton lowercaseL = (JRadioButton) lookupsPanel.getComponent(0);
        JRadioButton uppercaseL = (JRadioButton) lookupsPanel.getComponent(1);
        JRadioButton lowercaseM = (JRadioButton) lookupsPanel.getComponent(2);
        JRadioButton uppercaseM = (JRadioButton) lookupsPanel.getComponent(3);
        JRadioButton lowercaseX = (JRadioButton) lookupsPanel.getComponent(4);
        JRadioButton resetRadioButton = (JRadioButton) lookupsPanel.getComponent(5);

        assertEquals("1st level less specific", lowercaseL.getText());
        assertEquals("All less specific", uppercaseL.getText());
        assertEquals("1st level more specific", lowercaseM.getText());
        assertEquals("All more specific", uppercaseM.getText());
        assertEquals("Exact match only", lowercaseX.getText());
        assertEquals("Reset Radio Button", resetRadioButton.getText());
        assertFalse(lowercaseL.isSelected());
        assertFalse(uppercaseL.isSelected());
        assertFalse(lowercaseM.isSelected());
        assertFalse(uppercaseM.isSelected());
        assertFalse(lowercaseX.isSelected());
        assertFalse(resetRadioButton.isSelected());

        JPanel inverseQueriesPanel = (JPanel) additionalQueriesPanel.getComponent(1);
        JComboBox inverseQueries = (JComboBox) inverseQueriesPanel.getComponent(0);

        String[] expectedItems = {"None", "admin-c", "admin-c,tech-c,zone-c", "author", "cross-mnt", "cross-nfy", "local-as", "mbrs-by-ref", "member-of", "mnt-by", "mnt-lower", "mnt-nfy", "mnt-routes", "notify", "nserver", "origin", "person", "referral-by", "rev-srv", "sub-dom", "tech-c", "upd-to", "zone-c"};
        assertArrayEquals(expectedItems, inverseQueries.getItemCount() > 0 ? inverseQueries.getItemValues() : new String[0]);
        assertEquals("None", inverseQueries.getSelectedItem());
    }

    @Test
    public void testQueryHintsPanelCreation() {
        WhoIS whois = new WhoIS();
        JPanel queryHintsPanel = whois.queryHintsPanel();

        JPanel innerPanel = (JPanel) queryHintsPanel.getComponent(0);
        JLabel queryHintsText1 = (JLabel) innerPanel.getComponent(0);
        JLabel emptyLabel = (JLabel) innerPanel.getComponent(1);
        JLabel queryHintsText2 = (JLabel) innerPanel.getComponent(2);
        JLabel queryHintsText3 = (JLabel) innerPanel.getComponent(3);
        JLabel queryHintsText4 = (JLabel) innerPanel.getComponent(4);
        JLabel queryHintsText5 = (JLabel) innerPanel.getComponent(5);

        assertEquals("Query hints ", queryHintsText1.getText());
        assertEquals("EMPTY LABEL", emptyLabel.getText());
        assertEquals("* Include \"AS\" in front of an AS number.", queryHintsText2.getText());
        assertEquals("Example: AS4808", queryHintsText3.getText());
        assertEquals("* Include \"-t\" (template only) or \"-v\" (template and description) in front of an object name to view the template.", queryHintsText4.getText());
        assertEquals("Example: -t inetnum", queryHintsText5.getText());
    }
}