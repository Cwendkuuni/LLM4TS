package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testConstructorWithDefaultValues() {
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
    public void testConstructorWithValues() {
        String action = "Sell";
        int conId = 123;
        String exchange = "SMART";
        int openClose = ComboLeg.OPEN;
        int ratio = 2;
        int shortSaleSlot = 5;
        String designatedLocation = "NY";

        ComboLeg comboLeg = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);

        assertEquals(conId, comboLeg.m_conId);
        assertEquals(ratio, comboLeg.m_ratio);
        assertEquals(action, comboLeg.m_action);
        assertEquals(exchange, comboLeg.m_exchange);
        assertEquals(openClose, comboLeg.m_openClose);
        assertEquals(shortSaleSlot, comboLeg.m_shortSaleSlot);
        assertEquals(designatedLocation, comboLeg.m_designatedLocation);
    }

    @Test
    public void testEqualsWithSameInstance() {
        ComboLeg comboLeg = new ComboLeg(1, 1, "Buy", "IBKR", ComboLeg.SAME, 0, "LIMA");
        assertTrue(comboLeg.equals(comboLeg));
    }

    @Test
    public void testEqualsWithNullObject() {
        ComboLeg comboLeg = new ComboLeg(1, 1, "Buy", "IBKR", ComboLeg.SAME, 0, "LIMA");
        assertFalse(comboLeg.equals(null));
    }

    @Test
    public void testEqualsWithDifferentType() {
        ComboLeg comboLeg = new ComboLeg(1, 1, "Buy", "IBKR", ComboLeg.SAME, 0, "LIMA");
        assertFalse(comboLeg.equals("test"));
    }

    @Test
    public void testEqualsWithDifferentValues() {
        ComboLeg comboLeg1 = new ComboLeg(1, 1, "Buy", "IBKR", ComboLeg.SAME, 0, "LIMA");
        ComboLeg comboLeg2 = new ComboLeg(2, 2, "Sell", "NASDAQ", ComboLeg.OPEN, 1, "TOKYO");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithSameValuesButDifferentInstance() {
        ComboLeg comboLeg1 = new ComboLeg(1, 1, "Buy", "IBKR", ComboLeg.SAME, 0, "LIMA");
        ComboLeg comboLeg2 = new ComboLeg(1, 1, "Buy", "IBKR", ComboLeg.SAME, 0, "LIMA");
        assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testStringCompareIgnCase() {
        String str1 = "BUY";
        String str2 = "buy";
        assertEquals(0, Util.StringCompareIgnCase(str1, str2));
    }

    @Test
    public void testStringCompareIgnCaseWithDifferentStrings() {
        String str1 = "BUY";
        String str2 = "SELL";
        assertEquals(-1, Util.StringCompareIgnCase(str1, str2));
    }

    @Test
    public void testStringCompareIgnCaseWithSameString() {
        String str1 = "BUY";
        String str2 = "BUY";
        assertEquals(0, Util.StringCompareIgnCase(str1, str2));
    }

    @Test
    public void testHashCodeWithSameValuesButDifferentInstance() {
        ComboLeg comboLeg1 = new ComboLeg(1, 1, "Buy", "IBKR", ComboLeg.SAME, 0, "LIMA");
        ComboLeg comboLeg2 = new ComboLeg(1, 1, "Buy", "IBKR", ComboLeg.SAME, 0, "LIMA");
        assertEquals(comboLeg1.hashCode(), comboLeg2.hashCode());
    }

    @Test
    public void testHashCodeWithDifferentValues() {
        ComboLeg comboLeg1 = new ComboLeg(1, 1, "Buy", "IBKR", ComboLeg.SAME, 0, "LIMA");
        ComboLeg comboLeg2 = new ComboLeg(2, 2, "Sell", "NASDAQ", ComboLeg.OPEN, 1, "TOKYO");
        assertNotSame(comboLeg1.hashCode(), comboLeg2.hashCode());
    }
}