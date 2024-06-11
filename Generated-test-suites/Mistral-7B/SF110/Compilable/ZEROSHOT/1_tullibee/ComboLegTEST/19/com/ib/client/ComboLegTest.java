package com.ib.client;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ComboLegTest {

    ComboLeg comboLeg1;
    ComboLeg comboLeg2;

    @Before
    public void setup() {
        comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", 0, 0, "NY");
        comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", 0, 0, "NY");
    }

    @Test
    public void testComboLegConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        ComboLeg comboLegWithArgs = new ComboLeg(1, 2, "BUY", "IBKR", 0);
        ComboLeg comboLegWithAllArgs = new ComboLeg(1, 2, "BUY", "IBKR", 0, 0, "NY");

        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);

        assertEquals(1, comboLegWithArgs.m_conId);
        assertEquals(2, comboLegWithArgs.m_ratio);
        assertEquals("BUY", comboLegWithArgs.m_action);
        assertEquals("IBKR", comboLegWithArgs.m_exchange);
        assertEquals(0, comboLegWithArgs.m_openClose);

        assertEquals(1, comboLegWithAllArgs.m_conId);
        assertEquals(2, comboLegWithAllArgs.m_ratio);
        assertEquals("BUY", comboLegWithAllArgs.m_action);
        assertEquals("IBKR", comboLegWithAllArgs.m_exchange);
        assertEquals(0, comboLegWithAllArgs.m_openClose);
        assertEquals(0, comboLegWithAllArgs.m_shortSaleSlot);
        assertEquals("NY", comboLegWithAllArgs.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testNotEquals() {
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "IBKR", 0, 1, "NY");
        assertFalse(comboLeg1.equals(comboLeg3));
    }

    @Test(expected = NullPointerException.class)
    public void testNullObject() {
        assertFalse(comboLeg1.equals(null));
    }

    @Test(expected = ClassCastException.class)
    public void testDifferentClassObject() {
        assertFalse(comboLeg1.equals(new Object()));
    }
}