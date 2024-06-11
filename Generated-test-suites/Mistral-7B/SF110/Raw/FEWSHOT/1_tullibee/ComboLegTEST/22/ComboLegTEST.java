package com.ib.client;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testConstructorEmpty() {
        ComboLeg comboLeg = new ComboLeg();
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_conId);
        assertNull(comboLeg.m_designatedLocation);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_ratio);
        assertEquals(0, comboLeg.m_shortSaleSlot);
    }

    @Test
    public void testConstructorWithArguments() {
        int conId = 1;
        int ratio = 2;
        String action = "BUY";
        String exchange = "IBKR";
        int openClose = ComboLeg.OPEN;
        int shortSaleSlot = 3;
        String designatedLocation = "LON";

        ComboLeg comboLeg = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);

        assertTrue(comboLeg.m_conId == conId);
        assertTrue(comboLeg.m_ratio == ratio);
        assertTrue(Util.StringCompareIgnCase(comboLeg.m_action, action) == 0);
        assertTrue(Util.StringCompareIgnCase(comboLeg.m_exchange, exchange) == 0);
        assertTrue(comboLeg.m_openClose == openClose);
        assertTrue(comboLeg.m_shortSaleSlot == shortSaleSlot);
        assertTrue(Util.StringCompareIgnCase(comboLeg.m_designatedLocation, designatedLocation) == 0);
    }

    @Test
    public void testEqualsNull() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, "LON");
        assertFalse(comboLeg.equals(null));
    }

    @Test
    public void testEqualsDifferentObjectType() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, "LON");
        assertFalse(comboLeg.equals(new Object()));
    }

    @Test
    public void testEqualsDifferentConId() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, "LON");
        ComboLeg comboLeg2 = new ComboLeg(2, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, "LON");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentRatio() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, "LON");
        ComboLeg comboLeg2 = new ComboLeg(1, 3, "BUY", "IBKR", ComboLeg.OPEN, 3, "LON");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentAction() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, "LON");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "SELL", "IBKR", ComboLeg.OPEN, 3, "LON");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentExchange() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, "LON");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "NASDAQ", ComboLeg.OPEN, 3, "LON");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentOpenClose() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, "LON");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.CLOSE, 3, "LON");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentShortSaleSlot() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, "LON");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 4, "LON");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentDesignatedLocation() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, "LON");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, "PAR");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsSameObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, "LON");
        assertTrue(comboLeg.equals(comboLeg));
    }

    @Test
    public void testHashCodeEquals() {
        int conId = 1;
        int ratio = 2;
        String action = "BUY";
        String exchange = "IBKR";
        int openClose = ComboLeg.OPEN;
        int shortSaleSlot = 3;
        String designatedLocation = "LON";

        ComboLeg comboLeg1 = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);

        ComboLeg comboLeg2 = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);

        assertTrue(comboLeg1.hashCode() == comboLeg2.hashCode());
        assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testHashCodeNotEquals() {
        int conId = 1;
        int ratio = 2;
        String action = "BUY";
        String exchange = "IBKR";
        int openClose = ComboLeg.OPEN;
        int shortSaleSlot = 3;
        String designatedLocation = "LON";

        ComboLeg comboLeg1 = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);

        ComboLeg comboLeg2 = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);

        ComboLeg comboLeg3 = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, "PAR");

        assertNotEquals(comboLeg1.hashCode(), comboLeg3.hashCode());
        assertFalse(comboLeg1.equals(comboLeg3));
    }
}