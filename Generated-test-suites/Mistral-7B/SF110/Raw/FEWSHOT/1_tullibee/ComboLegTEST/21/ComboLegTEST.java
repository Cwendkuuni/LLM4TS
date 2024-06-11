package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testConstructor0() {
        ComboLeg leg = new ComboLeg();
        assertEquals(0, leg.m_conId);
        assertEquals(0, leg.m_ratio);
        assertNull(leg.m_action);
        assertNull(leg.m_exchange);
        assertEquals(0, leg.m_openClose);
        assertEquals(0, leg.m_shortSaleSlot);
        assertNull(leg.m_designatedLocation);
    }

    @Test
    public void testConstructor1() {
        ComboLeg leg = new ComboLeg(1);
        assertEquals(1, leg.m_conId);
        assertEquals(0, leg.m_ratio);
        assertNull(leg.m_action);
        assertNull(leg.m_exchange);
        assertEquals(0, leg.m_openClose);
        assertEquals(0, leg.m_shortSaleSlot);
        assertNull(leg.m_designatedLocation);
    }

    @Test
    public void testConstructor2() {
        ComboLeg leg = new ComboLeg(1, 2, "action1", "exchange1", 3);
        assertEquals(1, leg.m_conId);
        assertEquals(2, leg.m_ratio);
        assertEquals("action1", leg.m_action);
        assertEquals("exchange1", leg.m_exchange);
        assertEquals(3, leg.m_openClose);
        assertEquals(0, leg.m_shortSaleSlot);
        assertNull(leg.m_designatedLocation);
    }

    @Test
    public void testConstructor3() {
        ComboLeg leg1 = new ComboLeg(1, 2, "action1", "exchange1", 3, 4, "designatedLocation1");
        ComboLeg leg2 = new ComboLeg(1, 2, "action1", "exchange1", 3, 5, "designatedLocation2");

        assertEquals(1, leg1.m_conId);
        assertEquals(2, leg1.m_ratio);
        assertEquals("action1", leg1.m_action);
        assertEquals("exchange1", leg1.m_exchange);
        assertEquals(3, leg1.m_openClose);
        assertEquals(4, leg1.m_shortSaleSlot);
        assertEquals("designatedLocation1", leg1.m_designatedLocation);

        assertNotSame(leg1, leg2);
        assertEquals(1, leg2.m_conId);
        assertEquals(2, leg2.m_ratio);
        assertEquals("action1", leg2.m_action);
        assertEquals("exchange1", leg2.m_exchange);
        assertEquals(3, leg2.m_openClose);
        assertEquals(5, leg2.m_shortSaleSlot);
        assertEquals("designatedLocation2", leg2.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg leg1 = new ComboLeg(1, 2, "action1", "exchange1", 3, 4, "designatedLocation1");
        ComboLeg leg2 = new ComboLeg(1, 2, "action1", "exchange1", 3, 4, "designatedLocation1");
        ComboLeg leg3 = new ComboLeg(1, 2, "action1", "exchange1", 3, 5, "designatedLocation1");
        ComboLeg leg4 = new ComboLeg(1, 2, "action2", "exchange2", 3, 4, "designatedLocation1");

        assertTrue(leg1.equals(leg2));
        assertFalse(leg1.equals(leg3));
        assertFalse(leg1.equals(leg4));
    }
}