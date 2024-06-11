package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComboLegTest {
    @Test
    public void testConstructor_NoArgs() {
        ComboLeg comboLeg = new ComboLeg();
        assertNull(comboLeg.m_conId);
        assertNull(comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(ComboLeg.SAME, comboLeg.m_openClose);
        assertNull(comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);

        // Test equals with null object
        assertFalse(comboLeg.equals(null));
    }

    @Test
    public void testConstructor_OneArg() {
        ComboLeg comboLeg = new ComboLeg(1);
        assertEquals(1, comboLeg.m_conId);
        assertNull(comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(ComboLeg.SAME, comboLeg.m_openClose);
        assertNull(comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testConstructor_AllArgs() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.OPEN, 3, "NYSE");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("SMART", comboLeg.m_exchange);
        assertEquals(ComboLeg.OPEN, comboLeg.m_openClose);
        assertEquals(3, comboLeg.m_shortSaleSlot);
        assertEquals("NYSE", comboLeg.m_designatedLocation);

        // Test equals with same object
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.OPEN, 3, "NYSE");
        assertTrue(comboLeg.equals(comboLeg2));
    }

    @Test
    public void testEquals_DifferentObjects() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.OPEN, 3, "NYSE");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "SELL", "SMART", ComboLeg.CLOSE, 3, "NYSE");
        assertFalse(comboLeg1.equals(comboLeg2));

        ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.OPEN, 4, "NYSE");
        assertFalse(comboLeg1.equals(comboLeg3));

        ComboLeg comboLeg4 = new ComboLeg(2, 2, "BUY", "SMART", ComboLeg.OPEN, 3, "NYSE");
        assertFalse(comboLeg1.equals(comboLeg4));

        ComboLeg comboLeg5 = new ComboLeg(1, 2, "BUY", null, ComboLeg.OPEN, 3, "NYSE");
        assertFalse(comboLeg1.equals(comboLeg5));

        ComboLeg comboLeg6 = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.OPEN, 3, null);
        assertFalse(comboLeg1.equals(comboLeg6));
    }

    @Test
    public void testEquals_SameValues_DifferentInstances() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.OPEN, 3, "NYSE");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.OPEN, 3, "NYSE");
        assertTrue(comboLeg1.equals(comboLeg2));
    }
}