package com.ib.client;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testComboLegConstructor() {
        ComboLeg cl1 = new ComboLeg();
        ComboLeg cl2 = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.OPEN);
        ComboLeg cl3 = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.OPEN, 1, "NYSE");

        assertTrue(cl1 instanceof ComboLeg);
        assertTrue(cl2 instanceof ComboLeg);
        assertTrue(cl3 instanceof ComboLeg);

        assertEquals(0, cl1.m_conId);
        assertEquals(0, cl1.m_ratio);
        assertNull(cl1.m_action);
        assertNull(cl1.m_exchange);
        assertEquals(0, cl1.m_openClose);
        assertEquals(0, cl1.m_shortSaleSlot);
        assertNull(cl1.m_designatedLocation);

        assertEquals(1, cl2.m_conId);
        assertEquals(2, cl2.m_ratio);
        assertEquals("BUY", cl2.m_action);
        assertEquals("SMART", cl2.m_exchange);
        assertEquals(ComboLeg.OPEN, cl2.m_openClose);
        assertEquals(0, cl2.m_shortSaleSlot);
        assertNull(cl2.m_designatedLocation);

        assertEquals(1, cl3.m_conId);
        assertEquals(2, cl3.m_ratio);
        assertEquals("BUY", cl3.m_action);
        assertEquals("SMART", cl3.m_exchange);
        assertEquals(ComboLeg.OPEN, cl3.m_openClose);
        assertEquals(1, cl3.m_shortSaleSlot);
        assertEquals("NYSE", cl3.m_designatedLocation);
    }

    @Test
    public void testComboLegEquals() {
        ComboLeg cl1 = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.OPEN);
        ComboLeg cl2 = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.OPEN);
        ComboLeg cl3 = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.OPEN, 1, "NYSE");
        ComboLeg cl4 = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.OPEN, 1, "NASDAQ");

        assertTrue(cl1.equals(cl2));
        assertFalse(cl1.equals(cl3));
        assertFalse(cl1.equals(cl4));
    }

    @Test
    public void testComboLegStringCompareIgnCase() {
        assertTrue(Util.StringCompareIgnCase("BUY", "bUy") == 0);
        assertTrue(Util.StringCompareIgnCase("SMART", "sMART") == 0);
        assertFalse(Util.StringCompareIgnCase("BUY", "SELL") != 0);
        assertFalse(Util.StringCompareIgnCase("NYSE", "NASDAQ") != 0);
    }

    private void assertEquals(int expected, int actual) {
        assertEquals((Object)expected, (Object)actual);
    }

    private void assertNull(Object object) {
        if (object != null) {
            fail("Expected null, but was: " + object);
        }
    }
}