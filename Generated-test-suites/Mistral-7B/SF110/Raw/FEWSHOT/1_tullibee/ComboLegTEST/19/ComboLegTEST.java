package com.ib.client;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testConstructor() {
        ComboLeg cl1 = new ComboLeg();
        ComboLeg cl2 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
        ComboLeg cl3 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN, 1, "Location1");

        assertEquals(0, cl1.m_conId);
        assertEquals(0, cl1.m_ratio);
        assertNull(cl1.m_action);
        assertNull(cl1.m_exchange);
        assertEquals(ComboLeg.SAME, cl1.m_openClose);
        assertEquals(0, cl1.m_shortSaleSlot);
        assertNull(cl1.m_designatedLocation);

        assertEquals(1, cl2.m_conId);
        assertEquals(2, cl2.m_ratio);
        assertEquals("Action1", cl2.m_action);
        assertEquals("Exchange1", cl2.m_exchange);
        assertEquals(ComboLeg.OPEN, cl2.m_openClose);

        assertEquals(1, cl3.m_conId);
        assertEquals(2, cl3.m_ratio);
        assertEquals("Action1", cl3.m_action);
        assertEquals("Exchange1", cl3.m_exchange);
        assertEquals(ComboLeg.OPEN, cl3.m_openClose);
        assertEquals(1, cl3.m_shortSaleSlot);
        assertEquals("Location1", cl3.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg cl1 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
        ComboLeg cl2 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
        ComboLeg cl3 = new ComboLeg(1, 2, "Action1", "Exchange2", ComboLeg.OPEN);
        ComboLeg cl4 = new ComboLeg(2, 2, "Action1", "Exchange1", ComboLeg.OPEN);

        assertTrue(cl1.equals(cl2));
        assertFalse(cl1.equals(cl3));
        assertFalse(cl1.equals(cl4));
    }

    @Test
    public void testHashCode() {
        ComboLeg cl1 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
        ComboLeg cl2 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
        ComboLeg cl3 = new ComboLeg(1, 2, "Action1", "Exchange2", ComboLeg.OPEN);
        ComboLeg cl4 = new ComboLeg(2, 2, "Action1", "Exchange1", ComboLeg.OPEN);

        int hc1 = cl1.hashCode();
        int hc2 = cl2.hashCode();
        int hc3 = cl3.hashCode();
        int hc4 = cl4.hashCode();

        assertEquals(hc1, hc2);
        assertNotEquals(hc1, hc3);
        assertNotEquals(hc1, hc4);
    }
}