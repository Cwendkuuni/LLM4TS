package ipac.test;

import static org.junit.Assert.*;
import ipac.WhoIS;

import java.awt.*;
import java.util.Arrays;

import org.junit.Test;

public class WhoISTest {

    @Test
    public void testWhoISConstruction() {
        WhoIS whoIS = new WhoIS();
        assertNotNull(whoIS);
        assertEquals("APNIC", whoIS.WHOIS_NAME);
        assertEquals("whois.apnic.net", whoIS.WHOIS_ADDRESS);
        assertEquals(43, whoIS.WHOIS_PORT);
    }

    @Test
    public void testQueryPanelCreation() {
        WhoIS whoIS = new WhoIS();
        JPanel queryPanel = whoIS.queryPanel();
        assertNotNull(queryPanel);
        assertTrue(queryPanel.getBackground() instanceof Color);
        assertTrue(queryPanel.getLayout() instanceof FlowLayout);
        assertEquals(3, queryPanel.getComponentCount());
        assertTrue(queryPanel.getComponent(0) instanceof JLabel);
        assertTrue(queryPanel.getComponent(1) instanceof JTextField);
        assertTrue(queryPanel.getComponent(2) instanceof JButton);
    }

    @Test
    public void testAdditionalQueriesPanelCreation() {
        WhoIS whoIS = new WhoIS();
        JPanel additionalQueriesPanel = whoIS.additionalQueriesPanel();
        assertNotNull(additionalQueriesPanel);
        assertTrue(additionalQueriesPanel.getBackground() instanceof Color);
        assertTrue(additionalQueriesPanel.getLayout() instanceof BoxLayout);
        assertEquals(2, additionalQueriesPanel.getComponentCount());
        JPanel lookupPanel = (JPanel) additionalQueriesPanel.getComponent(0);
        assertEquals(7, lookupPanel.getComponentCount());
        assertTrue(lookupPanel.getComponent(0) instanceof JRadioButton);
        assertTrue(lookupPanel.getComponent(1) instanceof JRadioButton);
        assertTrue(lookupPanel.getComponent(2) instanceof JRadioButton);
        assertTrue(lookupPanel.getComponent(3) instanceof JRadioButton);
        assertTrue(lookupPanel.getComponent(4) instanceof JRadioButton);
        assertTrue(lookupPanel.getComponent(5) instanceof JCheckBox);
        assertTrue(lookupPanel.getComponent(6) instanceof JRadioButton);
        JPanel miscPanel = (JPanel) additionalQueriesPanel.getComponent(1);
        assertEquals(3, miscPanel.getComponentCount());
        assertTrue(miscPanel.getComponent(0) instanceof JLabel);
        assertTrue(miscPanel.getComponent(1) instanceof JComboBox);
        assertTrue(miscPanel.getComponent(2) instanceof JScrollPane);
    }

    @Test
    public void testQueryHintsPanelCreation() {
        WhoIS whoIS = new WhoIS();
        JPanel queryHintsPanel = whoIS.queryHintsPanel();
        assertNotNull(queryHintsPanel);
        assertTrue(queryHintsPanel.getBackground() instanceof Color);
        assertTrue(queryHintsPanel.getLayout() instanceof FlowLayout);
        assertEquals(1, queryHintsPanel.getComponentCount());
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
        WhoIS whoIS = new WhoIS();
        JPopupMenu popupMenu = whoIS.popupMenu;
        assertNotNull(popupMenu);
        assertEquals(4, popupMenu.getComponentCount());
        assertTrue(popupMenu.getComponent(0) instanceof JMenuItem);
        assertTrue(popupMenu.getComponent(1) instanceof JMenuItem);
        assertTrue(popupMenu.getComponent(2) instanceof JMenuItem);
        assertTrue(popupMenu.getComponent(3) instanceof JMenuItem);
    }

    @Test
    public void testActionPerformedQueryButton() {
        WhoIS whoIS = new WhoIS();
        WhoISMock queryMock = new WhoISMock();
        WhoISOutput outputMock = new WhoISOutput(queryMock);
        WhoIS.setWhoisOutput(outputMock);
        whoIS.addActionListener(e -> whoIS.startQuery());
        whoIS.queryButton.doClick();
        assertEquals("Start Query", whoIS.getLastActionEvent().getActionCommand());
        verify(queryMock).query(whoIS.getQuery());
    }

    @Test
    public void testActionPerformedResetButton() {
        WhoIS whoIS = new WhoIS();
        whoIS.addActionListener(e -> whoIS.reset());
        whoIS.resetButton.doClick();
        assertEquals("", whoIS.queryTextField.getText());
        assertFalse(whoIS.lowercaseD.isSelected());
        assertEquals("None", whoIS.inverseQueries.getSelectedItem().toString());
        assertEquals("All", this.miscList.getSelectedItem().toString());
    }

    @Test
    public void testInverseQueries() {
        WhoIS whoIS = new WhoIS();
        WhoISMock queryMock = new WhoISMock();
        WhoISOutput outputMock = new WhoISOutput(queryMock);
        WhoIS.setWhoisOutput(outputMock);
        whoIS.addActionListener(e -> whoIS.startQuery());
        whoIS.inverseQueries.setSelectedIndex(1);
        whoIS.queryTextField.setText("test");
        whoIS.queryButton.doClick();
        assertEquals("test admin-c", whoIS.getQuery());
        verify(queryMock).query("test admin-c");
    }

    @Test
    public void testLowercaseRadioButtons() {
        WhoIS whoIS = new WhoIS();
        WhoISMock queryMock = new WhoISMock();
        WhoISOutput outputMock = new WhoISOutput(queryMock);
        WhoIS.setWhoisOutput(outputMock);
        whoIS.addActionListener(e -> whoIS.startQuery());
        whoIS.lowercaseL.doClick();
        whoIS.queryTextField.setText("test");
        whoIS.queryButton.doClick();
        assertEquals("-l test", whoIS.getQuery());
        verify(queryMock).query("-l test");

        whoIS.lowercaseL.doClick();
        whoIS.queryTextField.setText("test");
        whoIS.queryButton.doClick();
        assertEquals("-L test", whoIS.getQuery());
        verify(queryMock).query("-L test");

        whoIS.lowercaseM.doClick();
        whoIS.queryTextField.setText("test");
        whoIS.queryButton.doClick();
        assertEquals("-m test", whoIS.getQuery());
        verify(queryMock).query("-m test");

        whoIS.lowercaseM.doClick();
        whoIS.queryTextField.setText("test");
        whoIS.queryButton.doClick();
        assertEquals("-M test", whoIS.getQuery());
        verify(queryMock).query("-M test");
    }

    @Test
    public void testUppercaseRadioButtons() {
        WhoIS whoIS = new WhoIS();
        WhoISMock queryMock = new WhoISMock();
        WhoISOutput outputMock = new WhoISOutput(queryMock);
        WhoIS.setWhoisOutput(outputMock);
        whoIS.addActionListener(e -> whoIS.startQuery());
        whoIS.uppercaseL.doClick();
        whoIS.queryTextField.setText("test");
        whoIS.queryButton.doClick();
        assertEquals("-L test", whoIS.getQuery());
        verify(queryMock).query("-L test");

        whoIS.uppercaseL.doClick();
        whoIS.queryTextField.setText("test");
        whoIS.queryButton.doClick();
        assertEquals("-L test", whoIS.getQuery());
        verify(queryMock).query("-L test");

        whoIS.uppercaseM.doClick();
        whoIS.queryTextField.setText("test");
        whoIS.queryButton.doClick();
        assertEquals("-M test", whoIS.getQuery());
        verify(queryMock).query("-M test");

        whoIS.uppercaseM.doClick();
        whoIS.queryTextField.setText("test");
        whoIS.queryButton.doClick();
        assertEquals("-M test", whoIS.getQuery());
        verify(queryMock).query("-M test");
    }

    @Test
    public void testBooleanCheckbox() {
        WhoIS whoIS = new WhoIS();
        WhoISMock queryMock = new WhoISMock();
        WhoISOutput outputMock = new WhoISOutput(queryMock);
        WhoIS.setWhoisOutput(outputMock);
        whoIS.addActionListener(e -> whoIS.startQuery());
        whoIS.lowercaseD.doClick();
        whoIS.queryTextField.setText("test");
        whoIS.queryButton.doClick();
        assertEquals("-d test", whoIS.getQuery());
        verify(queryMock).query("-d test");

        whoIS.lowercaseD.doClick();
        whoIS.queryTextField.setText("test");
        whoIS.queryButton.doClick();
        assertEquals("-d test", whoIS.getQuery());
        verify(queryMock).query("-d test");
    }

    @Test
    public void testMiscQueries() {
        WhoIS whoIS = new WhoIS();
        WhoISMock queryMock = new WhoISMock();
        WhoISOutput outputMock = new WhoISOutput(queryMock);
        WhoIS.setWhoisOutput(outputMock);
        whoIS.addActionListener(e -> whoIS.startQuery());
        String[] items = {"All", "as-block", "as-set", "aut-num", "domain", "filter-set", "inet-rtr", "inet6num", "inetnum", "irt", "key-cert", "mntner", "peering-set", "person", "role", "route", "route-set", "rtr-set"};
        whoIS.miscList.setSelectedValues(Arrays.asList(items[2], items[5], items[8], items[11]));
        whoIS.queryTextField.setText("test");
        whoIS.queryButton.doClick();
        assertEquals("-T aut-num,filter-set,inetnum,mntner test", whoIS.getQuery());
        verify(queryMock).query("-T aut-num,filter-set,inetnum,mntner test");
    }

    private ActionEvent getLastActionEvent() {
        SwingUtilities swingUtilities = SwingUtilities.getAncestorOfClass(JFrame.class, WhoIS.this);
        JFrame frame = (JFrame) swingUtilities;
        return frame.getActionMap().getActionForKey("queryButtonAction");
    }

    private static class WhoISMock implements WhoIS {

        private String query;

        @Override
        public void query(String query) {
            this.query = query;
        }

        String getQuery() {
            return this.query;
        }

        @Override
        public void displayError(String error) {
            // Not used in test
        }

        @Override
        public void reset() {
            // Not used in test
        }

        @Override
        public void startQuery() {
            // Not used in test
        }
    }

    private static class WhoISOutput {

        WhoISOutput(WhoISMock whoISMock) {
            this.whoISMock = whoISMock;
        }

        void displayOutput(String output) {
            // Not used in test
        }

        WhoISMock whoISMock;
    }
}