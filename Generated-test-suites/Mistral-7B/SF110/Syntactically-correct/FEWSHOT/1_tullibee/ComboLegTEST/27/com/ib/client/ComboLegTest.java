package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testConstructorZeroArgs() {
        ComboLeg cl = new ComboLeg();
        assertEquals(0, cl.m_conId);
        assertEquals(0, cl.m_ratio);
        assertNull(cl.m_action);
        assertNull(cl.m_exchange);
        assertEquals(0, cl.m_openClose);
        assertEquals(0, cl.m_shortSaleSlot);
        assertNull(cl.m_designatedLocation);
    }

    @Test
    public void testConstructorArgs() {
        int conId = 1;
        int ratio = 2;
        String action = "BUY";
        String exchange = "IBKR";
        int openClose = ComboLeg.OPEN;
        int shortSaleSlot = 3;
        String designatedLocation = "NY";

        ComboLeg cl = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);

        assertEquals(conId, cl.m_conId);
        assertEquals(ratio, cl.m_ratio);
        assertEquals(action, cl.m_action);
        assertEquals(exchange, cl.m_exchange);
        assertEquals(openClose, cl.m_openClose);
        assertEquals(shortSaleSlot, cl.m_shortSaleSlot);
        assertEquals(designatedLocation, cl.m_designatedLocation);
    }

    @Test
    public void testConstructorArgsWithNullValues() {
        ComboLeg cl = new ComboLeg(0, 0, null, null, 0, 0, null);
        assertEquals(0, cl.m_conId);
        assertEquals(0, cl.m_ratio);
        assertNull(cl.m_action);
        assertNull(cl.m_exchange);
        assertEquals(0, cl.m_openClose);
        assertEquals(0, cl.m_shortSaleSlot);
        assertNull(cl.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg cl1 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, "NY");
        ComboLeg cl2 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, "NY");
        ComboLeg cl3 = new ComboLeg(2, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, "NY");

        assertTrue(cl1.equals(cl2));
        assertNotSame(cl1, cl3);
        assertFalse(cl1.equals(null));
    }
}