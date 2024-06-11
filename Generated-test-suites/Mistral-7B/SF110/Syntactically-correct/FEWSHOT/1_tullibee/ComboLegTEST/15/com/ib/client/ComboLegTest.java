package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testConstructor1() {
        ComboLeg cl = new ComboLeg();
        assertNotNull(cl);
        assertEquals(0, cl.m_conId);
        assertEquals(0, cl.m_ratio);
        assertNull(cl.m_action);
        assertNull(cl.m_exchange);
        assertEquals(0, cl.m_openClose);
        assertEquals(0, cl.m_shortSaleSlot);
        assertNull(cl.m_designatedLocation);
    }

    @Test
    public void testConstructor2() {
        ComboLeg cl = new ComboLeg(1, 2, "action1", "exchange1", 1);
        assertNotNull(cl);
        assertEquals(1, cl.m_conId);
        assertEquals(2, cl.m_ratio);
        assertEquals("action1", cl.m_action);
        assertEquals("exchange1", cl.m_exchange);
        assertEquals(1, cl.m_openClose);
        assertEquals(0, cl.m_shortSaleSlot);
        assertNull(cl.m_designatedLocation);
    }

    @Test
    public void testConstructor3() {
        ComboLeg cl = new ComboLeg(1, 2, "action1", "exchange1", 1, 3, "designatedLocation1");
        assertNotNull(cl);
        assertEquals(1, cl.m_conId);
        assertEquals(2, cl.m_ratio);
        assertEquals("action1", cl.m_action);
        assertEquals("exchange1", cl.m_exchange);
        assertEquals(1, cl.m_openClose);
        assertEquals(3, cl.m_shortSaleSlot);
        assertEquals("designatedLocation1", cl.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg cl1 = new ComboLeg(1, 2, "action1", "exchange1", 1);
        ComboLeg cl2 = new ComboLeg(1, 2, "action1", "exchange1", 1);
        assertTrue(cl1.equals(cl2));

        cl2.m_ratio = 3;
        assertFalse(cl1.equals(cl2));

        cl1.m_openClose = 2;
        assertTrue(cl1.equals(cl2));

        cl2.m_shortSaleSlot = 4;
        assertFalse(cl1.equals(cl2));

        cl1.m_designatedLocation = "designatedLocation2";
        assertTrue(cl1.equals(cl2));

        cl2.m_designatedLocation = "designatedLocation3";
        assertFalse(cl1.equals(cl2));

        cl1 = null;
        assertFalse(cl2.equals(null));

        cl2 = new ComboLeg(2, 2, "action1", "exchange1", 1);
        assertFalse(cl1.equals(cl2));
    }

    @Test
    public void testHashCode() {
        ComboLeg cl1 = new ComboLeg(1, 2, "action1", "exchange1", 1);
        ComboLeg cl2 = new ComboLeg(1, 2, "action1", "exchange1", 1);
        assertEquals(cl1.hashCode(), cl2.hashCode());

        cl2.m_ratio = 3;
        assertNotEquals(cl1.hashCode(), cl2.hashCode());

        cl1.m_openClose = 2;
        assertEquals(cl1.hashCode(), cl2.hashCode());

        cl2.m_shortSaleSlot = 4;
        assertNotEquals(cl1.hashCode(), cl2.hashCode());

        cl1.m_designatedLocation = "designatedLocation2";
        assertNotEquals(cl1.hashCode(), cl2.hashCode());

        cl2.m_designatedLocation = "designatedLocation3";
        assertNotEquals(cl1.hashCode(), cl2.hashCode());
    }
}