package ipac.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.EventObject;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.junit.Before;
import org.junit.Test;

import ipac.WhoIS;
import ipac.WhoIS.WhoISOutput;

public class WhoISTest {

    private WhoIS whois;

    @Before
    public void setUp() {
        whois = new WhoIS();
        JFrame frame = new JFrame();
        frame.add(whois);
        frame.setSize(800, 600);
        frame.setVisible(true);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                whois.queryTextField.requestFocus();
            }
        });
    }

    @Test
    public void testInitialization() {
        assertEquals("APNIC", whois.WHOIS_NAME);
        assertEquals("whois.apnic.net", whois.WHOIS_ADDRESS);
        assertEquals(43, whois.WHOIS_PORT);
        assertSame(whois.radioButtonGroup, whois.queryPanel.getComponents()[4]);
        assertEquals(Color.WHITE, whois.getBackground());
    }

    @Test
    public void testCreatePopupMenu() {
        assertNotNull(whois.popupMenu);
        assertEquals(4, whois.popupMenu.getComponentCount());
    }

    @Test
    public void testActionPerformedQueryButton() throws UnknownHostException, IOException {
        whois.queryTextField.setText("example.com");
        whois.actionPerformed(new ActionEvent(whois.queryButton, ActionEvent.ACTION_PERFORMED, null));
        String expectedOutput = "VRSN\r\n"
                + "TTL: 321\r\n"
                + "Domain Name: EXAMPLE.COM\r\n"
                + "Origin: AS15167\r\n"
                + "Description: Example.com\r\n"
                + "Admin-C: AS20964\r\n"
                + "Name-Server: NS1.EXAMPLE.COM\r\n"
                + "Name-Server: NS2.EXAMPLE.COM\r\n"
                + "Name-Server: NS3.EXAMPLE.COM\r\n"
                + "Name-Server: NS4.EXAMPLE.COM\r\n"
                + "Name-Server: NS5.EXAMPLE.COM\r\n";

        String output = "";
        InputStream inputStream = new ByteArrayInputStream(expectedOutput.getBytes());
        Socket socket = new Socket(whois.WHOIS_ADDRESS, whois.WHOIS_PORT);
        PrintStream out = new PrintStream(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line;
        out.println(whois.getQuery());

        while ((line = in.readLine()) != null) {
            output += line + "\n";
        }

        new WhoISOutput(output);
        assertEquals(expectedOutput, whois.output.getText());
    }

    @Test
    public void testActionPerformedResetButton() {
        whois.actionPerformed(new ActionEvent(whois.resetButton, ActionEvent.ACTION_PERFORMED, null));
        assertEquals("", whois.queryTextField.getText());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals("None", whois.inverseQueries.getSelectedItem().toString());
        assertEquals("All", whois.miscList.getSelectedValue());
    }

    @Test
    public void testStartQueryEmptyQuery() {
        whois.queryTextField.setText("");
        whois.startQuery();
        assertEquals("No Query Detected", whois.output.getText());
    }

    @Test
    public void testStartQueryNonEmptyQuery() throws UnknownHostException, IOException {
        whois.queryTextField.setText("example.com");
        whois.startQuery();
        String expectedOutput = "VRSN\r\n"
                + "TTL: 321\r\n"
                + "Domain Name: EXAMPLE.COM\r\n"
                + "Origin: AS15167\r\n"
                + "Description: Example.com\r\n"
                + "Admin-C: AS20964\r\n"
                + "Name-Server: NS1.EXAMPLE.COM\r\n"
                + "Name-Server: NS2.EXAMPLE.COM\r\n"
                + "Name-Server: NS3.EXAMPLE.COM\r\n"
                + "Name-Server: NS4.EXAMPLE.COM\r\n"
                + "Name-Server: NS5.EXAMPLE.COM\r\n";

        String output = "";
        Socket socket = new Socket(whois.WHOIS_ADDRESS, whois.WHOIS_PORT);
        PrintStream out = new PrintStream(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line;
        out.println(whois.getQuery());

        while ((line = in.readLine()) != null) {
            output += line + "\n";
        }

        new WhoISOutput(output);
        assertEquals(expectedOutput, whois.output.getText());
    }

    @Test
    public void testGetQuery() {
        whois.queryTextField.setText("example.com");
        whois.inverseQueries.setSelectedIndex(1);
        whois.lowercaseL.setSelected(true);
        whois.lowercaseM.setSelected(true);
        whois.lowercaseX.setSelected(true);
        whois.lowercaseD.setSelected(true);
        Object[] selected = {"as-set"};
        whois.miscList.setSelectedValues(selected);

        String query = whois.getQuery();
        String expectedQuery = "-i admin-c -l example.com -m 1st level more specific -m 1st level more specific -x exact match only -d example.com -T as-set example.com";
        assertEquals(expectedQuery, query);
    }

    @Test
    public void testReset() {
        whois.queryTextField.setText("example.com");
        whois.inverseQueries.setSelectedIndex(1);
        whois.lowercaseL.setSelected(true);
        whois.lowercaseM.setSelected(true);
        whois.lowercaseX.setSelected(true);
        whois.lowercaseD.setSelected(true);
        Object[] selected = {"as-set"};
        whois.miscList.setSelectedValues(selected);

        whois.reset();
        assertEquals("", whois.queryTextField.getText());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals("None", whois.inverseQueries.getSelectedItem().toString());
        assertEquals("All", whois.miscList.getSelectedValue());
    }

    @Test
    public void testIPAddressLookups() {
        JPanel panel = whois.IPAddressLookups();
        assertEquals(11, panel.getComponentCount());
        assertTrue(panel.getComponent(0) instanceof JLabel);
        assertTrue(panel.getComponent(1) instanceof JRadioButton);
        assertTrue(panel.getComponent(2) instanceof JRadioButton);
        assertTrue(panel.getComponent(3) instanceof JRadioButton);
        assertTrue(panel.getComponent(4) instanceof JRadioButton);
        assertTrue(panel.getComponent(5) instanceof JRadioButton);
        assertTrue(panel.getComponent(6) instanceof JCheckBox);
        assertTrue(panel.getComponent(7) instanceof AbstractButton);
        assertTrue(panel.getComponent(8) instanceof JLabel);
        assertTrue(panel.getComponent(9) instanceof JLabel);
        assertTrue(panel.getComponent(10) instanceof JLabel);
    }

    @Test
    public void testMiscellaneousQueries() {
        JPanel panel = whois.MiscellaneousQueries();
        assertEquals(3, panel.getComponentCount());
        assertTrue(panel.getComponent(0) instanceof JLabel);
        assertTrue(panel.getComponent(1) instanceof JComboBox);
        assertTrue(panel.getComponent(2) instanceof JScrollPane);
    }

    @Test
    public void testQueryHintsPanel() {
        JPanel panel = whois.queryHintsPanel();
        assertEquals(6, panel.getComponentCount());
        assertTrue(panel.getComponent(0) instanceof JPanel);
        assertTrue(panel.getComponent(1) instanceof JLabel);
        assertTrue(panel.getComponent(2) instanceof JLabel);
        assertTrue(panel.getComponent(3) instanceof JLabel);
        assertTrue(panel.getComponent(4) instanceof JLabel);
        assertTrue(panel.getComponent(5) instanceof JLabel);
    }

    @Test
    public void testQueryPanel() {
        JPanel panel = whois.queryPanel();
        assertEquals(6, panel.getComponentCount());
        assertTrue(panel.getComponent(0) instanceof JLabel);
        assertTrue(panel.getComponent(1) instanceof JTextField);
        assertTrue(panel.getComponent(2) instanceof JButton);
        assertTrue(panel.getComponent(3) instanceof JButton);
        assertTrue(panel.getComponent(4) instanceof ButtonGroup);
        assertTrue(panel.getComponent(5) instanceof JPopupMenu);
    }

    @Test
    public void testAdditionalQueriesPanel() {
        JPanel panel = whois.additionalQueriesPanel();
        assertEquals(2, panel.getComponentCount());
        assertTrue(panel.getComponent(0) instanceof JPanel);
        assertTrue(panel.getComponent(1) instanceof JPanel);
    }

    @Test
    public void testWhoISPanel() {
        JPanel panel = whois.WhoISPanel;
        assertEquals(3, panel.getComponentCount());
        assertTrue(panel.getComponent(0) instanceof JPanel);
        assertTrue(panel.getComponent(1) instanceof JPanel);
        assertTrue(panel.getComponent(2) instanceof JPanel);
    }

    @Test
    public void testCreatePopupMenuMenuItemActionPerformed() {
        whois.popupMenu.getComponent(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WhoIS.this.queryTextField.cut();
            }
        });
        whois.popupMenu.getComponent(1).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WhoIS.this.queryTextField.copy();
            }
        });
        whois.popupMenu.getComponent(2).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WhoIS.this.queryTextField.paste();
            }
        });
        whois.popupMenu.getComponent(3).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WhoIS.this.queryTextField.selectAll();
            }
        });

        whois.queryTextField.setText("example.com");
        whois.popupMenu.show(whois, 10, 10);
        assertEquals("example.com", whois.queryTextField.getText());
        whois.popupMenu.getComponent(0).actionPerformed(new ActionEvent(whois.popupMenu.getComponent(0), ActionEvent.ACTION_PERFORMED, null));
        assertEquals("example.com", whois.queryTextField.getText().substring(0, whois.queryTextField.getCaretPosition() - 1));

        whois.queryTextField.setText("example.com");
        whois.popupMenu.getComponent(1).actionPerformed(new ActionEvent(whois.popupMenu.getComponent(1), ActionEvent.ACTION_PERFORMED, null));
        assertEquals("example.comexample.com", whois.queryTextField.getText());

        whois.queryTextField.setText("example.com");
        whois.popupMenu.getComponent(2).actionPerformed(new ActionEvent(whois.popupMenu.getComponent(2), ActionEvent.ACTION_PERFORMED, null));
        assertEquals("example.comexample.com", whois.queryTextField.getText());

        whois.queryTextField.setText("example.com");
        whois.popupMenu.getComponent(3).actionPerformed(new ActionEvent(whois.popupMenu.getComponent(3), ActionEvent.ACTION_PERFORMED, null));
        assertEquals("example.comexample.com", whois.queryTextField.getText());
    }

    @Test
    public void testInverseQueriesComboBoxModel() {
        ComboBoxModel<String> model = whois.inverseQueries.getModel();
        Object[] items = model.getElements();
        assertEquals(14, items.length);
        Arrays.sort(items);
        String[] expectedItems = {"admin-c", "author", "cross-nfy", "local-as", "mnt-by", "mnt-lower", "mnt-nfy", "mnt-routes", "notify", "origin", "person", "referral-by", "rev-srv", "zone-c"};
        assertArrayEquals(expectedItems, items);
    }

    @Test
    public void testMiscListModel() {
        ComboBoxModel<String> model = (ComboBoxModel<String>) whois.miscList.getModel();
        Object[] items = model.getElements();
        assertEquals(15, items.length);
        String[] expectedItems = {"All", "as-block", "as-set", "aut-num", "domain", "filter-set", "inet-rtr", "inet6num", "inetnum", "irt", "key-cert", "mntner", "peering-set", "person", "role", "route", "route-set", "rtr-set"};
        assertArrayEquals(expectedItems, items);
    }
}

class WhoISOutput extends JTextArea {

    private static final long serialVersionUID = 1L;

    public WhoISOutput(String text) {
        super(text);
    }

}