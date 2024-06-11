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
        assertTrue(whois.lowercaseL.isSelected());
    }

    @Test
    public void testUppercaseL() {
        WhoIS whois = new WhoIS();
        assertFalse(whois.uppercaseL.isSelected());
    }

    @Test
    public void testLowercaseM() {
        WhoIS whois = new WhoIS();
        assertFalse(whois.lowercaseM.isSelected());
    }

    @Test
    public void testUppercaseM() {
        WhoIS whois = new WhoIS();
        assertFalse(whois.uppercaseM.isSelected());
    }

    @Test
    public void testLowercaseX() {
        WhoIS whois = new WhoIS();
        assertFalse(whois.lowercaseX.isSelected());
    }

    @Test
    public void testResetRadioButton() {
        WhoIS whois = new WhoIS();
        assertTrue(whois.resetRadioButton.isSelected());
    }

    @Test
    public void testInverseQueries() {
        WhoIS whois = new WhoIS();
        assertEquals(15, whois.inverseQueries.getItemCount());
    }

    @Test
    public void testMiscList() {
        WhoIS whois = new WhoIS();
        assertEquals(16, whois.miscList.getModel().getSize());
    }
}