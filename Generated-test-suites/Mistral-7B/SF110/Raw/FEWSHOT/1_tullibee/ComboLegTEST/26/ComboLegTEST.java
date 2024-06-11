package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testConstructor() {
        ComboLeg comboLeg1 = new ComboLeg();
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN);
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 1, "NY");

        assertEquals(0, comboLeg1.m_conId);
        assertEquals(0, comboLeg1.m_ratio);
        assertNull(comboLeg1.m_action);
        assertNull(comboLeg1.m_exchange);
        assertEquals(ComboLeg.SAME, comboLeg1.m_openClose);
        assertEquals(0, comboLeg1.m_shortSaleSlot);
        assertNull(comboLeg1.m_designatedLocation);

        assertEquals(1, comboLeg2.m_conId);
        assertEquals(2, comboLeg2.m_ratio);
        assertEquals("BUY", comboLeg2.m_action);
        assertEquals("IBKR", comboLeg2.m_exchange);
        assertEquals(ComboLeg.OPEN, comboLeg2.m_openClose);
        assertEquals(0, comboLeg2.m_shortSaleSlot);
        assertNull(comboLeg2.m_designatedLocation);

        assertEquals(1, comboLeg3.m_conId);
        assertEquals(2, comboLeg3.m_ratio);
        assertEquals("BUY", comboLeg3.m_action);
        assertEquals("IBKR", comboLeg3.m_exchange);
        assertEquals(ComboLeg.OPEN, comboLeg3.m_openClose);
        assertEquals(1, comboLeg3.m_shortSaleSlot);
        assertEquals("NY", comboLeg3.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg();
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN);
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN);
        ComboLeg comboLeg4 = new ComboLeg(1, 3, "BUY", "IBKR", ComboLeg.OPEN);
        ComboLeg comboLeg5 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.CLOSE);

        assertTrue(comboLeg2.equals(comboLeg3));
        assertNotEquals(comboLeg2, comboLeg4);
        assertNotEquals(comboLeg2, comboLeg5);
        assertNotEquals(comboLeg1, comboLeg2);
        assertTrue(comboLeg1.equals(comboLeg1));
    }

    @Test
    public void testGetStringCompareIgnCase() {
        assertEquals(0, Util.StringCompareIgnCase(null, null));
        assertEquals(0, Util.StringCompareIgnCase("", ""));
        assertEquals(0, Util.StringCompareIgnCase("foo", "FOO"));
        assertEquals(0, Util.StringCompareIgnCase("foo", "foo"));
        assertEquals(1, Util.StringCompareIgnCase("foo", "bar"));
        assertEquals(-1, Util.StringCompareIgnCase("bar", "foo"));
    }
}