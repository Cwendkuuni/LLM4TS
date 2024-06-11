package ipac.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.event.ItemEvent;
import java.util.Arrays;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ipac.WhoIS;
import ipac.WhoISOutput;

public class WhoISTest {
    private JFrame frame;
    private WhoIS whois;

    @Before
    public void setUp() {
        whois = new WhoIS();
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(whois);
        frame.pack();
        frame.setVisible(true);
    }

    @After
    public void tearDown() {
        frame.dispose();
    }

    @Test
    public void testInitialization() {
        assertEquals("APNIC", whois.WHOIS_NAME);
        assertEquals("whois.apnic.net", whois.WHOIS_ADDRESS);
        assertEquals(43, whois.WHOIS_PORT);
        assertSame(new ButtonGroup(), whois.radioButtonGroup);
        assertTrue(whois.queryTextField != null);
        assertTrue(whois.queryButton != null);
        assertTrue(whois.resetButton != null);
        assertFalse(whois.lowercaseD.isSelected());
        assertSame(JCheckBox.unselected, whois.lowercaseD.getState());
        assertNull(whois.popupMenu);
        assertNull(whois.inverseQueries);
        assertNull(whois.miscList);
    }

    @Test
    public void testQueryPanel() {
        assertTrue(whois.queryPanel != null);
        assertTrue(whois.queryLabel != null);
        assertTrue(whois.queryTextField != null);
        assertTrue(whois.queryButton != null);
        assertTrue(whois.resetButton != null);
        assertSame(BoxLayout.X_AXIS, whois.queryPanel.getLayout());
    }

    @Test
    public void testAdditionalQueriesPanel() {
        assertTrue(whois.additionalQueriesPanel != null);
        assertTrue(whois.IPAddressLookups() != null);
        assertTrue(whois.MiscellaneousQueries() != null);
        assertSame(FlowLayout.X_AXIS, whois.additionalQueriesPanel.getLayout());
    }

    @Test
    public void testIPAddressLookups() {
        assertTrue(whois.lowercaseL != null);
        assertTrue(whois.uppercaseL != null);
        assertTrue(whois.lowercaseM != null);
        assertTrue(whois.uppercaseM != null);
        assertTrue(whois.lowercaseX != null);
        assertTrue(whois.resetRadioButton != null);
        assertTrue(whois.radioButtonGroup != null);
        assertTrue(whois.lowercaseL.getBackground() == Color.WHITE);
        assertTrue(whois.uppercaseL.getBackground() == Color.WHITE);
        assertTrue(whois.lowercaseM.getBackground() == Color.WHITE);
        assertTrue(whois.uppercaseM.getBackground() == Color.WHITE);
        assertTrue(whois.lowercaseX.getBackground() == Color.WHITE);
        assertTrue(whois.lowercaseD != null);
        assertTrue(whois.lowercaseD.getBackground() == Color.WHITE);
    }

    @Test
    public void testMiscellaneousQueries() {
        assertTrue(whois.inverseQueries != null);
        assertTrue(whois.miscList != null);
        assertEquals(29, whois.inverseQueries.getItemCount());
        assertSame(BoxLayout.X_AXIS, whois.MiscellaneousQueries().getLayout());
    }

    @Test
    public void testQueryHintsPanel() {
        assertTrue(whois.queryHintsPanel != null);
        assertTrue(whois.queryHintsText1 != null);
        assertTrue(whois.queryHintsText2 != null);
        assertTrue(whois.queryHintsText3 != null);
        assertTrue(whois.queryHintsText4 != null);
        assertTrue(whois.queryHintsText5 != null);
        assertTrue(whois.queryHintsText1.getDisplayedMnemonic() == 'Q');
        assertTrue(whois.queryHintsText1.getLabelForAt(0) == queryHintsText1);
    }

    @Test
    public void testCreatePopupMenu() {
        assertTrue(whois.popupMenu != null);
        assertTrue(whois.popupMenu.isPopupMenu());
        assertEquals(4, whois.popupMenu.getComponentCount());
        assertTrue(whois.popupMenu.getComponent(0) instanceof JMenuItem);
        assertTrue(whois.popupMenu.getComponent(1) instanceof JMenuItem);
        assertTrue(whois.popupMenu.getComponent(2) instanceof JMenuItem);
        assertTrue(whois.popupMenu.getComponent(3) instanceof JMenuItem);

        final JMenuItem cut = (JMenuItem) whois.popupMenu.getComponent(0);
        cut.doClick();
        assertTrue(whois.queryTextField.getText().isEmpty());

        final JMenuItem copy = (JMenuItem) whois.popupMenu.getComponent(1);
        whois.queryTextField.setText("test");
        copy.doClick();
        assertEquals("test", whois.queryTextField.getText());

        final JMenuItem paste = (JMenuItem) whois.popupMenu.getComponent(2);
        whois.queryTextField.setText("");
        whois.queryTextField.paste();
        assertEquals("test", whois.queryTextField.getText());

        final JMenuItem selectAll = (JMenuItem) whois.popupMenu.getComponent(3);
        whois.queryTextField.setText("test1 test2");
        selectAll.doClick();
        assertEquals("test1 test2", whois.queryTextField.getText());
        assertEquals(whois.queryTextField.getCaretPosition(), whois.queryTextField.getDocument().getLength());
    }

    @Test
    public void testActionPerformed() {
        final String query = "test";
        whois.queryTextField.setText(query);
        whois.queryTextField.setCaretPosition(query.length());

        final ItemEvent itemEvent = new ItemEvent(whois.lowercaseL, ItemEvent.SELECTED, 0);
        whois.lowercaseL.itemStateChanged(itemEvent);
        assertEquals("-l " + query, whois.getQuery());

        final ItemEvent itemEvent2 = new ItemEvent(whois.uppercaseL, ItemEvent.SELECTED, 0);
        whois.uppercaseL.itemStateChanged(itemEvent2);
        assertEquals("-L " + query, whois.getQuery());

        final ItemEvent itemEvent3 = new ItemEvent(whois.lowercaseM, ItemEvent.SELECTED, 0);
        whois.lowercaseM.itemStateChanged(itemEvent3);
        assertEquals("-m " + query, whois.getQuery());

        final ItemEvent itemEvent4 = new ItemEvent(whois.uppercaseM, ItemEvent.SELECTED, 0);
        whois.uppercaseM.itemStateChanged(itemEvent4);
        assertEquals("-M " + query, whois.getQuery());

        final ItemEvent itemEvent5 = new ItemEvent(whois.lowercaseX, ItemEvent.SELECTED, 0);
        whois.lowercaseX.itemStateChanged(itemEvent5);
        assertEquals("-x " + query, whois.getQuery());
    }

    @Test
    public void testInverseQueries() {
        JComboBox inverseQueries = whois.inverseQueries;
        String[] inverseQueriesItems = {"None", "admin-c", "admin-c,tech-c,zone-c", "author", "cross-mnt", "cross-nfy", "local-as", "mbrs-by-ref", "member-of", "mnt-by", "mnt-lower", "mnt-nfy", "mnt-routes", "notify", "nserver", "origin", "person", "referral-by", "rev-srv", "sub-dom", "tech-c", "upd-to", "zone-c"};
        assertArrayEquals(inverseQueriesItems, inverseQueries.getItemIds());
    }

    @Test
    public void testMiscQueries() {
        JList miscList = whois.miscList;
        String[] miscListItems = {"All", "as-block", "as-set", "aut-num", "domain", "filter-set", "inet-rtr", "inet6num", "inetnum", "irt", "key-cert", "mntner", "peering-set", "person", "role", "route", "route-set", "rtr-set"};
        Arrays.sort(miscListItems);

        Object[] selectedValues = miscList.getSelectedValues();
        assertArrayEquals(miscListItems, miscList.getModel().toArray(new String[0]));
        Arrays.sort(selectedValues);
        assertArrayEquals(miscListItems, selectedValues);
    }

    @Test
    public void testGetQuery() {
        String query = "test";
        whois.queryTextField.setText(query);
        whois.inverseQueries.setSelectedItem("author");
        whois.lowercaseL.setSelected(true);

        assertEquals("-l author test", whois.getQuery());
    }

    @Test
    public void testReset() {
        whois.queryTextField.setText("test");
        whois.reset();
        assertEquals("", whois.queryTextField.getText());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }
}