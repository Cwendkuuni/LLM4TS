package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testComboLegConstructor() {
        ComboLeg cl1 = new ComboLeg();
        ComboLeg cl2 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN);
        ComboLeg cl3 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 1, "NY");

        assertEquals(0, cl1.m_conId);
        assertEquals(0, cl1.m_ratio);
        assertNull(cl1.m_action);
        assertNull(cl1.m_exchange);
        assertEquals(ComboLeg.SAME, cl1.m_openClose);
        assertEquals(0, cl1.m_shortSaleSlot);
        assertNull(cl1.m_designatedLocation);

        assertEquals(1, cl2.m_conId);
        assertEquals(2, cl2.m_ratio);
        assertEquals("BUY", cl2.m_action);
        assertEquals("IBKR", cl2.m_exchange);
        assertEquals(ComboLeg.OPEN, cl2.m_openClose);
        assertEquals(0, cl2.m_shortSaleSlot);
        assertNull(cl2.m_designatedLocation);

        assertEquals(1, cl3.m_conId);
        assertEquals(2, cl3.m_ratio);
        assertEquals("BUY", cl3.m_action);
        assertEquals("IBKR", cl3.m_exchange);
        assertEquals(ComboLeg.OPEN, cl3.m_openClose);
        assertEquals(1, cl3.m_shortSaleSlot);
        assertEquals("NY", cl3.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg cl1 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN);
        ComboLeg cl2 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN);
        ComboLeg cl3 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.CLOSE);

        assertTrue(cl1.equals(cl2));
        assertNotSame(cl1, cl3);
    }

    @Test
    public void testEmptyConstructor() {
        ComboLeg cl1 = new ComboLeg();
        ComboLeg cl2 = new ComboLeg();

        assertTrue(cl1.equals(cl2));
    }

    @Test(expected = NullPointerException.class)
    public void testNullExchange() {
        new ComboLeg(1, 2, null, null, ComboLeg.OPEN);
    }

    @Test(expected = NullPointerException.class)
    public void testNullAction() {
        new ComboLeg(1, 2, null, "IBKR", ComboLeg.OPEN);
    }

    @Test
    public void testStringCompareIgnCase() {
        String str1 = "buy";
        String str2 = "BUY";
        int result = Util.StringCompareIgnCase(str1, str2);
        assertEquals(0, result);
    }
}