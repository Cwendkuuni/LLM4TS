package ipac.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ipac.WhoIS;
import ipac.WhoISOutput;

public class WhoISTest {

    private WhoIS whois;

    @Before
    public void setUp() {
        whois = new WhoIS();
    }

    @Test
    public void testConstructor() {
        assertNull(whois.WHOIS_NAME);
        assertEquals("whois.apnic.net", whois.WHOIS_ADDRESS);
        assertEquals(43, whois.WHOIS_PORT);
        assertNotNull(whois.radioButtonGroup);
        assertNull(whois.popupMenu);
        assertNull(whois.inverseQueries);
        assertNull(whois.miscList);
    }

    @Test
    public void testQueryPanel() {
        JPanel queryPanel = whois.queryPanel();
        assertNotNull(queryPanel);
        assertInstanceOf(JPanel.class, queryPanel);
        assertInstanceOf(BoxLayout.class, queryPanel.getLayout());
        assertInstanceOf(JLabel.class, queryPanel.getComponent(0));
        assertInstanceOf(JTextField.class, queryPanel.getComponent(2));
        assertInstanceOf(JButton.class, queryPanel.getComponent(3));
        assertInstanceOf(JButton.class, queryPanel.getComponent(4));
    }

    @Test
    public void testAdditionalQueriesPanel() {
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
        assertNotNull(additionalQueriesPanel);
        assertInstanceOf(JPanel.class, additionalQueriesPanel);
        assertInstanceOf(FlowLayout.class, additionalQueriesPanel.getLayout());
        assertNotNull(whois.IPAddressLookups());
        assertNotNull(whois.MiscellaneousQueries());
        assertInstanceOf(JPanel.class, additionalQueriesPanel.getComponent(0));
        assertInstanceOf(JPanel.class, additionalQueriesPanel.getComponent(1));
    }

    @Test
    public void testIPAddressLookups() {
        JPanel lookups = whois.IPAddressLookups();
        assertNotNull(lookups);
        assertInstanceOf(BoxLayout.class, lookups.getLayout());
        assertInstanceOf(JRadioButton.class, lookups.getComponent(0));
        assertInstanceOf(JRadioButton.class, lookups.getComponent(1));
        assertInstanceOf(JRadioButton.class, lookups.getComponent(2));
        assertInstanceOf(JRadioButton.class, lookups.getComponent(3));
        assertInstanceOf(JRadioButton.class, lookups.getComponent(4));
        assertInstanceOf(JCheckBox.class, lookups.getComponent(5));
        assertInstanceOf(JRadioButton.class, lookups.getComponent(6));
        assertInstanceOf(JRadioButton.class, lookups.getComponent(7));
        assertInstanceOf(JRadioButton.class, lookups.getComponent(8));
        assertInstanceOf(JRadioButton.class, lookups.getComponent(9));
        assertInstanceOf(JRadioButton.class, lookups.getComponent(10));
        assertInstanceOf(JButton.class, lookups.getComponent(11));
        assertSame(Color.WHITE, lookups.getBackground());
    }

    @Test
    public void testMiscellaneousQueries() {
        JPanel miscPanel = whois.MiscellaneousQueries();
        assertNotNull(miscPanel);
        assertInstanceOf(BoxLayout.class, miscPanel.getLayout());
        assertInstanceOf(JLabel.class, miscPanel.getComponent(0));
        assertInstanceOf(JComboBox.class, miscPanel.getComponent(1));
        assertInstanceOf(JLabel.class, miscPanel.getComponent(2));
        assertInstanceOf(JList.class, miscPanel.getComponent(3));
        assertInstanceOf(JScrollPane.class, miscPanel.getComponent(4));
    }

    @Test
    public void testQueryHintsPanel() {
        JPanel queryHintsPanel = whois.queryHintsPanel();
        assertNotNull(queryHintsPanel);
        assertInstanceOf(JPanel.class, queryHintsPanel);
        assertInstanceOf(FlowLayout.class, queryHintsPanel.getLayout());
        assertInstanceOf(JPanel.class, queryHintsPanel.getComponent(0));
        assertInstanceOf(JLabel.class, queryHintsPanel.getComponent(1));
        assertInstanceOf(JLabel.class, queryHintsPanel.getComponent(2));
        assertInstanceOf(JLabel.class, queryHintsPanel.getComponent(3));
        assertInstanceOf(JLabel.class, queryHintsPanel.getComponent(4));
        assertInstanceOf(JLabel.class, queryHintsPanel.getComponent(5));
        assertInstanceOf(JLabel.class, queryHintsPanel.getComponent(6));
        assertInstanceOf(Color.WHITE, queryHintsPanel.getBackground());
    }

    @Test
    public void testCreatePopupMenu() {
        JPopupMenu popupMenu = whois.popupMenu;
        assertNotNull(popupMenu);
        assertInstanceOf(JPopupMenu.class, popupMenu);
        assertEquals(4, popupMenu.getComponentCount());
        assertInstanceOf(JMenuItem.class, popupMenu.getComponent(0));
        assertInstanceOf(JMenuItem.class, popupMenu.getComponent(1));
        assertInstanceOf(JMenuItem.class, popupMenu.getComponent(2));
        assertInstanceOf(JMenuItem.class, popupMenu.getComponent(3));
    }

    @Test
    public void testActionPerformedWithQueryButton() {
        final String query = "example.com";
        SwingUtilities.invokeLater(() -> {
            whois.queryTextField.setText(query);
        });
        SwingUtilities.invokeLater(() -> {
            WhoISEvent event = new WhoISEvent("queryButton");
            whois.actionPerformed(event);
        });
        SwingUtilities.invokeLater(() -> {
            assertTrue(whois.isQuerying());
        });
    }

    @Test
    public void testActionPerformedWithResetButton() {
        SwingUtilities.invokeLater(() -> {
            WhoISEvent event = new WhoISEvent("resetButton");
            whois.actionPerformed(event);
        });
        SwingUtilities.invokeLater(() -> {
            assertEquals("", whois.queryTextField.getText());
        });
    }

    @Test
    public void testGetQueryWithDefaults() {
        SwingUtilities.invokeLater(() -> {
            whois.queryTextField.setText("example.com");
        });
        SwingUtilities.invokeLater(() -> {
            WhoISEvent event = new WhoISEvent("queryButton");
            whois.actionPerformed(event);
        });
        String query = whois.getQuery();
        assertEquals("example.com", query);
    }

    @Test
    public void testGetQueryWithAllOptions() {
        SwingUtilities.invokeLater(() -> {
            whois.reset();
            whois.lowercaseL.doClick();
            whois.uppercaseL.doClick();
            whois.lowercaseM.doClick();
            whois.uppercaseM.doClick();
            whois.lowercaseX.doClick();
            whois.lowercaseD.doClick();
            WhoISEvent inverseEvent = new WhoISEvent("inverseQueries");
            whois.inverseQueries.setSelectedIndex(7);
            whois.inverseQueries.fireEvent(inverseEvent);
            JList miscList = whois.miscList;
            List<String> selectedValues = Arrays.asList("as-block", "aut-num", "filter-set");
            for (String value : selectedValues) {
                SwingUtilities.invokeLater(() -> {
                    miscList.setSelectedValue(value, true);
                });
            }
        });
        String query = whois.getQuery();
        assertEquals("-l example.com -L example.com -m example.com -M example.com -x example.com -d example.com -T as-block,aut-num,filter-set example.com", query);
    }

    @Test
    public void testDisplayErrorMessage() {
        SwingUtilities.invokeLater(() -> {
            WhoISEvent event = new WhoISEvent("queryButton");
            whois.actionPerformed(event);
            whois.displayError("Error occurred while querying.");
        });
        SwingUtilities.invokeLater(() -> {
            JOptionPane optionPane = SwingUtilities.windowForComponent(whois.queryTextField);
            assertTrue(optionPane.isDisplayable());
            JOptionPane messageDialog = (JOptionPane) optionPane;
            assertEquals("Error", messageDialog.getTitle());
            assertEquals("Error occurred while querying.", messageDialog.getMessage());
            assertEquals(0, messageDialog.getValue());
        });
    }

    @Test
    public void testSocketConnection() {
        SwingUtilities.invokeLater(() -> {
            WhoISEvent event = new WhoISEvent("queryButton");
            whois.actionPerformed(event);
        });
        SwingUtilities.invokeLater(() -> {
            try {
                WhoISOutput whoisOutput = new WhoISOutput("Successful query output.");
                SwingUtilities.invokeLater(() -> {
                    whois.queryOutput(whoisOutput);
                });
                assertTrue(whois.isQuerySuccess());
            } catch (IOException e) {
                whois.displayError("Whois server could not be contacted.\nPlease check your internet connection.");
                assertFalse(whois.isQuerySuccess());
            }
        });
    }

    @Test
    public void testQueryOutput() {
        SwingUtilities.invokeLater(() -> {
            WhoISEvent event = new WhoISEvent("queryButton");
            whois.actionPerformed(event);
            WhoISOutput whoisOutput = new WhoISOutput("Successful query output.");
            SwingUtilities.invokeLater(() -> {
                whois.queryOutput(whoisOutput);
                JFrame outputFrame = SwingUtilities.windowForComponent(whois.queryOutputPanel);
                assertTrue(outputFrame.isDisplayable());
                assertEquals("WhoIS Output", outputFrame.getTitle());
                assertEquals("Successful query output.", whois.queryOutputPanel.getText());
            });
        });
    }

    @After
    public void tearDown() {
        if (whois != null) {
            SwingUtilities.invokeLater(() -> {
                WhoISFrame frame = (WhoISFrame) SwingUtilities.getWindowAncestor(whois);
                frame.dispose();
            });
        }
    }
}