package ipac.test;

import static org.junit.Assert.*;
import ipac.WhoIS;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class WhoISTest {

    @Test
    public void testWhoIS() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois);
        assertEquals(40, whois.queryTextField.getColumns());
    }

    @Test
    public void testQueryButtonActionPerformed() {
        WhoIS whois = new WhoIS();
        String query = "example.com";
        whois.queryTextField.setText(query);
        whois.queryButton.doClick();

        // Add assertions for the expected behavior of the Whois application
        // after the query is executed, such as displaying the result or an error message.
    }

    @Test
    public void testResetButtonActionPerformed() {
        WhoIS whois = new WhoIS();
        whois.reset();

        // Add assertions to verify that the fields and options have been reset.
        assertEquals("", whois.queryTextField.getText());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals("None", whois.inverseQueries.getSelectedItem().toString());
        assertEquals(0, whois.miscList.getSelectedIndices().length);
    }

    @Test
    public void testPopupMenuItems() {
        WhoIS whois = new WhoIS();

        List<JMenuItem> items = new ArrayList<>();
        for (Component component : whois.popupMenu.getComponents()) {
            if (component instanceof JMenuItem) {
                items.add((JMenuItem) component);
            }
        }

        assertEquals(4, items.size());
        assertTrue(items.get(0).getText().equals("Cut"));
        // Add assertions for the remaining items.
    }

    @Test
    public void testActionPerformedWithEmptyQuery() {
        WhoIS whois = new WhoIS();
        whois.actionPerformed(new ActionEvent(whois.queryButton, ActionEvent.ACTION_PERFORMED, null));

        // Add assertions for the expected behavior when an empty query is submitted.
        // For example, an error message could be displayed.
    }

    @Test
    public void testInverseQueries() {
        WhoIS whois = new WhoIS();

        whois.inverseQueries.setSelectedItem("author");
        String query = whois.getQuery();

        assertTrue(query.startsWith("-i author "));
    }

    @Test
    public void testLowercaseOptions() {
        WhoIS whois = new WhoIS();

        whois.lowercaseL.doClick();
        String query = whois.getQuery();

        assertTrue(query.startsWith("-l "));

        whois.lowercaseM.doClick();
        query = whois.getQuery();

        assertTrue(query.startsWith("-m "));

        whois.lowercaseX.doClick();
        query = whois.getQuery();

        assertTrue(query.startsWith("-x "));
    }

    @Test
    public void testBooleanDOption() {
        WhoIS whois = new WhoIS();

        whois.lowercaseD.doClick();
        String query = whois.getQuery();

        assertTrue(query.startsWith("-d "));
    }

    @Test
    public void testMiscQueries() {
        WhoIS whois = new WhoIS();

        String[] selectedValues = {"as-block", "aut-num"};
        whois.miscList.setSelectedValues(selectedValues, false);
        String query = whois.getQuery();

        StringBuilder expectedQuery = new StringBuilder();
        expectedQuery.append("-T as-block,aut-num ");
        assertTrue(query.startsWith(expectedQuery.toString()));
    }
}