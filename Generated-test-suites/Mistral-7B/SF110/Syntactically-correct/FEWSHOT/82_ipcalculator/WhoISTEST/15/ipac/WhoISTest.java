package ipac;

import static org.junit.Assert.*;

import org.junit.Test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;

public class WhoISTest {

    @Test
    public void testWhoIS() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois);
        assertTrue(whois.isOpaque());
        assertEquals(Color.WHITE, whois.getBackground());

        JPanel queryPanel = whois.queryPanel();
        assertEquals(BoxLayout.X_AXIS, queryPanel.getLayoutOrient());
        JTextField queryTextField = whois.queryTextField;
        assertNotNull(queryTextField);
        assertEquals(40, queryTextField.getColumns());
        JButton queryButton = whois.queryButton;
        assertNotNull(queryButton);
        assertEquals("Query", queryButton.getText());
        JButton resetButton = whois.resetButton;
        assertNotNull(resetButton);
        assertEquals("Reset", resetButton.getText());

        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
        assertEquals(BoxLayout.Y_AXIS, additionalQueriesPanel.getLayoutOrient());
        JPanel ipAddressLookups = whois.IPAddressLookups();
        assertEquals(BoxLayout.X_AXIS, ipAddressLookups.getLayoutOrient());
        assertTrue(ipAddressLookups.isOpaque());
        JRadioButton lowercaseL = whois.lowercaseL;
        assertTrue(lowercaseL.isSelected());
        assertEquals("1st level less specific", lowercaseL.getText());
        JRadioButton uppercaseL = whois.uppercaseL;
        assertFalse(uppercaseL.isSelected());
        assertEquals("All less specific", uppercaseL.getText());
        JRadioButton lowercaseM = whois.lowercaseM;
        assertFalse(lowercaseM.isSelected());
        assertEquals("1st level more specific", lowercaseM.getText());
        JRadioButton uppercaseM = whois.uppercaseM;
        assertFalse(uppercaseM.isSelected());
        assertEquals("All more specific", uppercaseM.getText());
        JRadioButton lowercaseX = whois.lowercaseX;
        assertFalse(lowercaseX.isSelected());
        assertEquals("Exact match only", lowercaseX.getText());
        JCheckBox lowercaseD = whois.lowercaseD;
        assertFalse(lowercaseD.isSelected());
        assertEquals("Associated reverse domain", lowercaseD.getText());
        JRadioButton resetRadioButton = whois.resetRadioButton;
        assertTrue(resetRadioButton.isSelected());
        assertEquals("Reset Radio Button", resetRadioButton.getText());

        JPanel miscQueriesPanel = whois.MiscellaneousQueries();
        assertEquals(BoxLayout.Y_AXIS, miscQueriesPanel.getLayoutOrient());
        JLabel inverseLabel = whois.inverseLabel;
        assertNotNull(inverseLabel);
        assertEquals("Inverse Queries", inverseLabel.getText());
        JComboBox inverseQueries = whois.inverseQueries;
        assertNotNull(inverseQueries);
        assertEquals(23, inverseQueries.getItemCount());
        JLabel miscLabel = whois.miscLabel;
        assertNotNull(miscLabel);
        assertEquals("Object Types", miscLabel.getText());
        JList miscList = whois.miscList;
        assertEquals(16, miscList.getItemCount());

        JPanel queryHintsPanel = whois.queryHintsPanel();
        assertEquals(FlowLayout.LEFT, queryHintsPanel.getLayoutOrient());
        JPanel innerPanel = whois.innerPanel;
        assertEquals(BoxLayout.Y_AXIS, innerPanel.getLayoutOrient());
        JLabel queryHintsText1 = whois.queryHintsText1;
        assertNotNull(queryHintsText1);
        assertEquals("Query hints ", queryHintsText1.getText());
        JLabel emptyLabel = whois.emptyLabel;
        assertNotNull(emptyLabel);
        assertTrue(emptyLabel.isOpaque());
        JLabel queryHintsText2 = whois.queryHintsText2;
        assertNotNull(queryHintsText2);
        assertEquals("* Include \"AS\" in front of an AS number.", queryHintsText2.getText());
        JLabel queryHintsText3 = whois.queryHintsText3;
        assertNotNull(queryHintsText3);
        assertEquals("Example: AS4808", queryHintsText3.getText());
        JLabel queryHintsText4 = whois.queryHintsText4;
        assertNotNull(queryHintsText4);
        assertEquals("* Include \"-t\" (template only) or \"-v\" (template and description) in front of an object name to view the template.", queryHintsText4.getText());
        JLabel queryHintsText5 = whois.queryHintsText5;
        assertNotNull(queryHintsText5);
        assertEquals("Example: -t inetnum", queryHintsText5.getText());

        whois.createPopupMenu();
        JPopupMenu popupMenu = whois.popupMenu;
        assertNotNull(popupMenu);
        assertEquals(4, popupMenu.getComponentCount());
        JMenuItem cut = (JMenuItem) popupMenu.getComponent(0);
        assertEquals("Cut", cut.getText());
        JMenuItem copy = (JMenuItem) popupMenu.getComponent(1);
        assertEquals("Copy", copy.getText());
        JMenuItem paste = (JMenuItem) popupMenu.getComponent(2);
        assertEquals("Paste", paste.getText());
        JMenuItem selectAll = (JMenuItem) popupMenu.getComponent(3);
        assertEquals("Select All", selectAll.getText());

        ActionListener actionListener = (ActionEvent e) -> whois.queryTextField.cut();
        assertTrue(cut.getActionCommands()[0].equals("cut"));
        cut.addActionListener(actionListener);
        actionListener = (ActionEvent e) -> whois.queryTextField.copy();
        assertTrue(copy.getActionCommands()[0].equals("copy"));
        copy.addActionListener(actionListener);
        actionListener = (ActionEvent e) -> whois.queryTextField.paste();
        assertTrue(paste.getActionCommands()[0].equals("paste"));
        paste.addActionListener(actionListener);
        actionListener = (ActionEvent e) -> whois.queryTextField.selectAll();
        assertTrue(selectAll.getActionCommands()[0].equals("selectAll"));
        selectAll.addActionListener(actionListener);

        ActionListener actionListener1 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WhoIS whois = (WhoIS) e.getSource();
                whois.queryTextField.cut();
            }
        };
        cut.addActionListener(actionListener1);
        ActionListener actionListener2 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WhoIS whois = (WhoIS) e.getSource();
                whois.queryTextField.copy();
            }
        };
        copy.addActionListener(actionListener2);
        ActionListener actionListener3 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WhoIS whois = (WhoIS) e.getSource();
                whois.queryTextField.paste();
            }
        };
        paste.addActionListener(actionListener3);
        ActionListener actionListener4 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WhoIS whois = (WhoIS) e.getSource();
                whois.queryTextField.selectAll();
            }
        };
        selectAll.addActionListener(actionListener4);

        WhoIS spyWhoIS = new SpyWhoIS(whois);
        spyWhoIS.setVisible(true);

        WhoISSpy spy = spyWhoIS.getSpy();
        assertEquals("WhoIS", spy.getName());
        assertEquals(WhoisActionListener.class, spy.getActionListeners()[0].getClass());
        assertEquals("QueryButton", spy.getInvocationCount());

        WhoISSpy spy1 = spyWhoIS.getSpy();
        assertEquals(1, spy1.getInvocationCount());
        spyWhoIS.queryButton.doClick();
        assertEquals(2, spy1.getInvocationCount());
    }
}

class SpyWhoIS extends JFrame {
    private WhoIS whois;
    private WhoISSpy spy;

    public SpyWhoIS(WhoIS whois) {
        this.whois = whois;
        this.spy = new WhoISSpy();
        this.add(whois);
        this.setSize(800, 600);
        this.setVisible(true);
    }

    public WhoISSpy getSpy() {
        return this.spy;
    }
}

class WhoISSpy extends Proxy implements WhoisActionListener {
    private int invocationCount = 0;

    public WhoISSpy() {
        this(WhoisActionListener.class.getMethod("actionPerformed", ActionEvent.class));
    }

    public WhoISSpy(Method method) {
        super(WhoisActionListener.class);
        this.bindTo(method);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.invocationCount++;
    }

    public int getInvocationCount() {
        return this.invocationCount;
    }
}

interface WhoisActionListener extends ActionListener {}