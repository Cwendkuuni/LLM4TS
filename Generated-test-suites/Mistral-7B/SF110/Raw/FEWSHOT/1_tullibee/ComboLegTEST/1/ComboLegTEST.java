package com.ib.client;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testConstructors() {
        ComboLeg comboLeg1 = new ComboLeg();
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN, 4, "DesignatedLocation1");

        assertEquals(0, comboLeg1.m_conId);
        assertEquals(0, comboLeg1.m_ratio);
        assertNull(comboLeg1.m_action);
        assertNull(comboLeg1.m_exchange);
        assertEquals(0, comboLeg1.m_openClose);
        assertEquals(0, comboLeg1.m_shortSaleSlot);
        assertNull(comboLeg1.m_designatedLocation);

        assertEquals(1, comboLeg2.m_conId);
        assertEquals(2, comboLeg2.m_ratio);
        assertEquals("Action1", comboLeg2.m_action);
        assertEquals("Exchange1", comboLeg2.m_exchange);
        assertEquals(ComboLeg.OPEN, comboLeg2.m_openClose);

        assertEquals(1, comboLeg3.m_conId);
        assertEquals(2, comboLeg3.m_ratio);
        assertEquals("Action1", comboLeg3.m_action);
        assertEquals("Exchange1", comboLeg3.m_exchange);
        assertEquals(ComboLeg.OPEN, comboLeg3.m_openClose);
        assertEquals(4, comboLeg3.m_shortSaleSlot);
        assertEquals("DesignatedLocation1", comboLeg3.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action2", "Exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg4 = new ComboLeg(1, 2, "Action1", "Exchange2", ComboLeg.OPEN);

        assertTrue(comboLeg1.equals(comboLeg2));
        assertFalse(comboLeg1.equals(comboLeg3));
        assertFalse(comboLeg1.equals(comboLeg4));
    }

    @Test
    public void testHashCode() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action2", "Exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg4 = new ComboLeg(1, 2, "Action1", "Exchange2", ComboLeg.OPEN);

        int hashCode1 = comboLeg1.hashCode();
        int hashCode2 = comboLeg2.hashCode();
        int hashCode3 = comboLeg3.hashCode();
        int hashCode4 = comboLeg4.hashCode();

        assertTrue(hashCode1 == hashCode2);
        assertFalse(hashCode1 == hashCode3);
        assertFalse(hashCode1 == hashCode4);
    }
}