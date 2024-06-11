package com.ib.client;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_ratio);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);

        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN);
        assertEquals(1, comboLeg1.m_conId);
        assertEquals(2, comboLeg1.m_ratio);
        assertEquals("action1", comboLeg1.m_action);
        assertEquals("exchange1", comboLeg1.m_exchange);
        assertEquals(ComboLeg.OPEN, comboLeg1.m_openClose);
        assertEquals(0, comboLeg1.m_shortSaleSlot);
        assertNull(comboLeg1.m_designatedLocation);

        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN, 3, "designatedLocation1");
        assertEquals(1, comboLeg2.m_conId);
        assertEquals(2, comboLeg2.m_ratio);
        assertEquals("action1", comboLeg2.m_action);
        assertEquals("exchange1", comboLeg2.m_exchange);
        assertEquals(ComboLeg.OPEN, comboLeg2.m_openClose);
        assertEquals(3, comboLeg2.m_shortSaleSlot);
        assertEquals("designatedLocation1", comboLeg2.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "action1", "exchange2", ComboLeg.OPEN);
        ComboLeg comboLeg4 = new ComboLeg(2, 2, "action1", "exchange1", ComboLeg.OPEN);

        assertTrue(comboLeg1.equals(comboLeg2));
        assertFalse(comboLeg1.equals(comboLeg3));
        assertFalse(comboLeg1.equals(comboLeg4));
    }

    @Test
    public void testHashCode() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "action1", "exchange2", ComboLeg.OPEN);
        ComboLeg comboLeg4 = new ComboLeg(2, 2, "action1", "exchange1", ComboLeg.OPEN);

        int hashCode1 = comboLeg1.hashCode();
        int hashCode2 = comboLeg2.hashCode();
        int hashCode3 = comboLeg3.hashCode();
        int hashCode4 = comboLeg4.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCode3);
        assertNotSame(hashCode1, hashCode4);
    }
}