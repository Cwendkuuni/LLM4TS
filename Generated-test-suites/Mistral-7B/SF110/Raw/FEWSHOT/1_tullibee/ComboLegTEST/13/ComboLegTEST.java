package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testConstructorNoArgs() {
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
    public void testConstructorSingleArg() {
        ComboLeg cl = new ComboLeg(1);
        assertEquals(1, cl.m_conId);
        assertEquals(0, cl.m_ratio);
        assertNull(cl.m_action);
        assertNull(cl.m_exchange);
        assertEquals(ComboLeg.UNKNOWN, cl.m_openClose);
        assertEquals(0, cl.m_shortSaleSlot);
        assertNull(cl.m_designatedLocation);
    }

    @Test
    public void testConstructorMultipleArgs() {
        ComboLeg cl = new ComboLeg(1, 2, "action", "exchange", ComboLeg.OPEN);
        assertEquals(1, cl.m_conId);
        assertEquals(2, cl.m_ratio);
        assertEquals("action", cl.m_action);
        assertEquals("exchange", cl.m_exchange);
        assertEquals(ComboLeg.OPEN, cl.m_openClose);
        assertEquals(0, cl.m_shortSaleSlot);
        assertNull(cl.m_designatedLocation);
    }

    @Test
    public void testConstructorAllArgs() {
        ComboLeg cl = new ComboLeg(1, 2, "action", "exchange", ComboLeg.OPEN, 3, "designatedLocation");
        assertEquals(1, cl.m_conId);
        assertEquals(2, cl.m_ratio);
        assertEquals("action", cl.m_action);
        assertEquals("exchange", cl.m_exchange);
        assertEquals(ComboLeg.OPEN, cl.m_openClose);
        assertEquals(3, cl.m_shortSaleSlot);
        assertEquals("designatedLocation", cl.m_designatedLocation);
    }

    @Test
    public void testEqualsNull() {
        ComboLeg cl = new ComboLeg();
        assertFalse(cl.equals(null));
    }

    @Test
    public void testEqualsSameInstance() {
        ComboLeg cl = new ComboLeg();
        assertTrue(cl.equals(cl));
    }

    @Test
    public void testEqualsDifferentInstanceWithSameValues() {
        ComboLeg cl1 = new ComboLeg(1, 2, "action", "exchange", ComboLeg.OPEN, 3, "designatedLocation");
        ComboLeg cl2 = new ComboLeg(1, 2, "action", "exchange", ComboLeg.OPEN, 3, "designatedLocation");
        assertTrue(cl1.equals(cl2));
    }

    @Test
    public void testEqualsDifferentInstanceWithDifferentValues() {
        ComboLeg cl1 = new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN, 3, "designatedLocation1");
        ComboLeg cl2 = new ComboLeg(1, 2, "action2", "exchange2", ComboLeg.CLOSE, 4, "designatedLocation2");
        assertFalse(cl1.equals(cl2));
    }

    @Test
    public void testEqualsDifferentType() {
        ComboLeg cl = new ComboLeg();
        assertFalse(cl.equals("test"));
    }
}