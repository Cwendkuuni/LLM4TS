package com.ib.client;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
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

        ComboLeg comboLeg2 = new ComboLeg(3, 4, "action2", "exchange2", ComboLeg.CLOSE, 5, "designatedLocation1");
        assertEquals(3, comboLeg2.m_conId);
        assertEquals(4, comboLeg2.m_ratio);
        assertEquals("action2", comboLeg2.m_action);
        assertEquals("exchange2", comboLeg2.m_exchange);
        assertEquals(ComboLeg.CLOSE, comboLeg2.m_openClose);
        assertEquals(5, comboLeg2.m_shortSaleSlot);
        assertEquals("designatedLocation1", comboLeg2.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN);
        assertTrue(comboLeg1.equals(comboLeg2));

        ComboLeg comboLeg3 = new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN);
        comboLeg3.m_designatedLocation = "designatedLocation1";
        assertFalse(comboLeg1.equals(comboLeg3));

        ComboLeg comboLeg4 = new ComboLeg(1, 3, "action1", "exchange1", ComboLeg.OPEN);
        assertFalse(comboLeg1.equals(comboLeg4));

        ComboLeg comboLeg5 = new ComboLeg(2, 2, "action1", "exchange1", ComboLeg.OPEN);
        assertFalse(comboLeg1.equals(comboLeg5));
    }

    @Test
    public void testHashCode() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN);
        int hashCode1 = comboLeg1.hashCode();

        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN);
        int hashCode2 = comboLeg2.hashCode();

        assertEquals(hashCode1, hashCode2);

        ComboLeg comboLeg3 = new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN);
        comboLeg3.m_designatedLocation = "designatedLocation1";
        int hashCode3 = comboLeg3.hashCode();

        assertNotEquals(hashCode1, hashCode3);

        ComboLeg comboLeg4 = new ComboLeg(1, 3, "action1", "exchange1", ComboLeg.OPEN);
        int hashCode4 = comboLeg4.hashCode();

        assertNotEquals(hashCode1, hashCode4);

        ComboLeg comboLeg5 = new ComboLeg(2, 2, "action1", "exchange1", ComboLeg.OPEN);
        int hashCode5 = comboLeg5.hashCode();

        assertNotEquals(hashCode1, hashCode5);
    }
}