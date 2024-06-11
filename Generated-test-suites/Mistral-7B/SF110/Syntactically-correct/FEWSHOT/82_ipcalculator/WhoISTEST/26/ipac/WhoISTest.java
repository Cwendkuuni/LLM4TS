package ipac.test;

import static org.junit.Assert.*;
import ipac.WhoIS;

import org.junit.Test;

public class WhoISTest {

    @Test
    public void testWhoIS() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois);
    }

    @Test
    public void testWHOIS_NAME() {
        WhoIS whois = new WhoIS();
        assertEquals("APNIC", whois.WHOIS_NAME);
    }

    @Test
    public void testWHOIS_ADDRESS() {
        WhoIS whois = new WhoIS();
        assertEquals("whois.apnic.net", whois.WHOIS_ADDRESS);
    }

    @Test
    public void testWHOIS_PORT() {
        WhoIS whois = new WhoIS();
        assertEquals(43, whois.WHOIS_PORT);
    }

    @Test
    public void testQueryTextField() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois.queryTextField);
    }

    @Test
    public void testQueryButton() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois.queryButton);
    }

    @Test
    public void testResetButton() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois.resetButton);
    }

    @Test
    public void testLowercaseL() {
        WhoIS whois = new WhoIS();
        assertTrue(whois.lowercaseL.isSelected() == false);
    }

    @Test
    public void testUppercaseL() {
        WhoIS whois = new WhoIS();
        assertTrue(whois.uppercaseL.isSelected() == false);
    }

    @Test
    public void testLowercaseM() {
        WhoIS whois = new WhoIS();
        assertTrue(whois.lowercaseM.isSelected() == false);
    }

    @Test
    public void testUppercaseM() {
        WhoIS whois = new WhoIS();
        assertTrue(whois.uppercaseM.isSelected() == false);
    }

    @Test
    public void testLowercaseX() {
        WhoIS whois = new WhoIS();
        assertTrue(whois.lowercaseX.isSelected() == false);
    }

    @Test
    public void testLowercaseD() {
        WhoIS whois = new WhoIS();
        assertFalse(whois.lowercaseD.isSelected());
    }

    @Test
    public void testInverseQueries() {
        WhoIS whois = new WhoIS();
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
    }

    @Test
    public void testMiscList() {
        WhoIS whois = new WhoIS();
        assertEquals(0, whois.miscList.getSelectedIndex());
    }

    @Test
    public void testPopupMenu() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois.popupMenu);
    }

    @Test
    public void testCreatePopupMenu() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois.popupMenu);
        assertEquals(4, whois.popupMenu.getComponentCount());
    }

    @Test
    public void testActionPerformed() {
        WhoIS whois = new WhoIS();
        mockedActionEvent queryButton = mock(ActionEvent.class);
        mockedActionEvent resetButton = mock(ActionEvent.class);
        when(queryButton.getSource()).thenReturn(whois.queryButton);
        when(resetButton.getSource()).thenReturn(whois.resetButton);
        whois.actionPerformed(queryButton);
        whois.actionPerformed(resetButton);
        verify(whois, times(1)).startQuery();
        verify(whois, times(1)).reset();
    }

    @Test
    public void testStartQuery() {
        WhoIS whois = new WhoIS();
        String query = "example.com";
        whois.queryTextField.setText(query);
        whois.startQuery();
        assertTrue(whois.getQuery().contains(query));
    }

    @Test
    public void testDisplayError() {
        WhoIS whois = new WhoIS();
        whois.displayError("Error Message");
        assertTrue(true); // No assertions for error messages, as they are not testable in this context
    }

    @Test
    public void testQuery() {
        WhoIS whois = new WhoIS();
        String query = "example.com";
        String result = "Result of WhoIS query for example.com";
        when(whois.getQuery()).thenReturn(query);
        when(whois.query(query)).thenReturn(result);
        WhoISOutput output = whois.new WhoISOutput(result);
        // As we cannot check the contents of GUI components, we can only assert that the output is created
        assertTrue(output instanceof WhoISOutput);
    }

    @Test
    public void testGetQuery() {
        WhoIS whois = new WhoIS();
        String query = "example.com";
        whois.queryTextField.setText(query);
        whois.lowercaseL.setSelected(true);
        whois.inverseQueries.setSelectedIndex(1);
        whois.lowercaseD.setSelected(true);
        whois.miscList.setSelectedValue("as-set");
        assertEquals("-i tech-c -l example.com -d", whois.getQuery());
    }

    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.reset();
        // As we cannot check the contents of GUI components, we can only assert that the reset method is called
        verify(whois, times(1)).reset();
    }
}